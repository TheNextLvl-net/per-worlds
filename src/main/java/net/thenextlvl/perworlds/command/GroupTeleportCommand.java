package net.thenextlvl.perworlds.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.WorldGroup;
import net.thenextlvl.perworlds.command.brigadier.SimpleCommand;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.List;

import static net.thenextlvl.perworlds.command.WorldCommand.groupArgument;

@NullMarked
final class GroupTeleportCommand extends SimpleCommand {
    private GroupTeleportCommand(PerWorldsPlugin plugin) {
        super(plugin, "teleport", "perworlds.command.group.teleport");
    }

    public static ArgumentBuilder<CommandSourceStack, ?> create(PerWorldsPlugin plugin) {
        var command = new GroupTeleportCommand(plugin);
        return command.create().then(groupArgument(plugin, true)
                .then(Commands.argument("players", ArgumentTypes.players()).executes(command))
                .executes(command));
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var sender = context.getSource().getSender();
        var group = context.getArgument("group", WorldGroup.class);

        if (!group.getSettings().enabled()) {
            plugin.bundle().sendMessage(sender, "group.teleport.disabled",
                    Placeholder.parsed("group", group.getName()));
            return 0;
        }

        var resolver = tryGetArgument(context, "players", PlayerSelectorArgumentResolver.class).orElse(null);
        var players = resolver != null ? resolver.resolve(context.getSource()) : sender instanceof Player player ? List.of(player) : null;

        if (players == null) {
            plugin.bundle().sendMessage(sender, "command.sender");
            return 0;
        }

        var filtered = players.stream()
                .filter(player -> !group.containsWorld(player.getWorld()))
                .toList();

        filtered.forEach(player -> group.loadPlayerData(player, true).thenAccept(success -> {
            plugin.bundle().sendMessage(player, success ? "group.teleport.self" : "group.teleport.failed",
                    Placeholder.parsed("group", group.getName()));
        }));

        if (filtered.size() == 1 && filtered.getFirst().equals(sender)) return Command.SINGLE_SUCCESS;

        var message = group.getWorlds().findAny().isEmpty() ? "group.teleport.empty"
                : filtered.size() == 1 ? "group.teleport.other"
                : filtered.isEmpty() ? "group.teleport.none" : "group.teleport.others";
        plugin.bundle().sendMessage(sender, message,
                Placeholder.component("player", filtered.isEmpty() ? Component.empty() : filtered.getFirst().name()),
                Formatter.number("players", filtered.size()),
                Placeholder.parsed("group", group.getName()));
        return filtered.isEmpty() ? 0 : Command.SINGLE_SUCCESS;

    }
}
