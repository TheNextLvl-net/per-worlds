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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.List;

import static net.thenextlvl.perworlds.command.GroupCommand.groupArgument;

@NullMarked
class GroupTeleportCommand {
    private final PerWorldsPlugin plugin;

    private GroupTeleportCommand(PerWorldsPlugin plugin) {
        this.plugin = plugin;
    }

    public static ArgumentBuilder<CommandSourceStack, ?> create(PerWorldsPlugin plugin) {
        var command = new GroupTeleportCommand(plugin);
        return Commands.literal("teleport")
                .requires(source -> source.getSender().hasPermission("perworlds.command.group.teleport"))
                .then(groupArgument(plugin, true)
                        .then(Commands.argument("players", ArgumentTypes.players())
                                .executes(command::teleportPlayers))
                        .executes(command::teleport));
    }

    private int teleportPlayers(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var players = context.getArgument("players", PlayerSelectorArgumentResolver.class);
        var resolved = players.resolve(context.getSource());
        return teleport(context, resolved);
    }

    private int teleport(CommandContext<CommandSourceStack> context) {
        if (context.getSource().getSender() instanceof Player player)
            return teleport(context, List.of(player));
        plugin.bundle().sendMessage(context.getSource().getSender(), "command.sender");
        return 0;
    }

    private int teleport(CommandContext<CommandSourceStack> context, List<Player> players) {
        var group = context.getArgument("group", WorldGroup.class);
        return teleport(context.getSource().getSender(), group, players.stream()
                .filter(player -> !group.containsWorld(player.getWorld()))
                .toList());
    }

    private int teleport(CommandSender sender, WorldGroup group, List<Player> players) {
        if (!group.getSettings().enabled()) {
            plugin.bundle().sendMessage(sender, "group.teleport.disabled",
                    Placeholder.parsed("group", group.getName()));
            return 0;
        }
        var message = group.getWorlds().findAny().isEmpty() ? "group.teleport.empty"
                : players.size() == 1 ? "group.teleport.other"
                : players.isEmpty() ? "group.teleport.none" : "group.teleport.others";
        players.forEach(player -> group.loadPlayerData(player, true).thenAccept(success ->
                plugin.bundle().sendMessage(player, success ? "group.teleport.self" : "group.teleport.failed",
                        Placeholder.parsed("group", group.getName()))));
        if (players.size() == 1 && players.getFirst().equals(sender)) return Command.SINGLE_SUCCESS;
        plugin.bundle().sendMessage(sender, message,
                Placeholder.component("player", players.isEmpty() ? Component.empty() : players.getFirst().name()),
                Formatter.number("players", players.size()),
                Placeholder.parsed("group", group.getName()));
        return players.isEmpty() ? 0 : Command.SINGLE_SUCCESS;
    }
}
