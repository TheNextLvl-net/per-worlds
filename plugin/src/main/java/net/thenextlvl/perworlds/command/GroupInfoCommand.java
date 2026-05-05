package net.thenextlvl.perworlds.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.WorldGroup;
import net.thenextlvl.perworlds.command.brigadier.SimpleCommand;
import org.jspecify.annotations.NullMarked;

import static net.thenextlvl.perworlds.command.WorldCommand.groupArgument;

@NullMarked
final class GroupInfoCommand extends SimpleCommand {
    private GroupInfoCommand(final PerWorldsPlugin plugin) {
        super(plugin, "info", "perworlds.command.group.info");
    }

    public static ArgumentBuilder<CommandSourceStack, ?> create(final PerWorldsPlugin plugin) {
        final var command = new GroupInfoCommand(plugin);
        return command.create()
                .then(groupArgument(plugin, true).executes(command))
                .executes(command);
    }

    @Override
    public int run(final CommandContext<CommandSourceStack> context) {
        final var group = tryGetArgument(context, "group", WorldGroup.class).orElseGet(() -> {
            return plugin.groupProvider().getGroup(context.getSource().getLocation().getWorld())
                    .orElse(plugin.groupProvider().getUnownedWorldGroup());
        });
        final var worlds = group.getPersistedWorlds().stream().map(key -> {
            final var world = plugin.getServer().getWorld(key);
            return world != null ? world.getName() : key.asString();
        }).map(Component::text).toList();
        plugin.bundle().sendMessage(context.getSource().getSender(), "group.info",
                Formatter.booleanChoice("single", worlds.size() == 1),
                Formatter.joining("worlds", worlds),
                Formatter.number("amount", worlds.size()),
                Placeholder.unparsed("group", group.getName()));
        return Command.SINGLE_SUCCESS;
    }
}
