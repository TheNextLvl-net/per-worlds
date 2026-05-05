package net.thenextlvl.perworlds.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.command.brigadier.SimpleCommand;
import org.jspecify.annotations.NullMarked;

import java.util.Comparator;

@NullMarked
final class GroupListCommand extends SimpleCommand {
    private GroupListCommand(final PerWorldsPlugin plugin) {
        super(plugin, "list", "perworlds.command.group.list");
    }

    public static ArgumentBuilder<CommandSourceStack, ?> create(final PerWorldsPlugin plugin) {
        final var command = new GroupListCommand(plugin);
        return command.create().executes(command);
    }

    @Override
    public int run(final CommandContext<CommandSourceStack> context) {
        final var sender = context.getSource().getSender();
        final var groups = plugin.groupProvider().getAllGroups().stream()
                .sorted(Comparator.comparing(group -> group.getName().toLowerCase()))
                .toList();
        if (groups.isEmpty()) {
            plugin.bundle().sendMessage(sender, "group.list.empty");
            return SINGLE_SUCCESS;
        }
        plugin.bundle().sendMessage(sender, "group.list.header",
                Formatter.booleanChoice("single", groups.size() == 1),
                Formatter.number("amount", groups.size()));
        for (var i = 0; i < groups.size(); i++) {
            final var group = groups.get(i);
            final var worlds = group.getPersistedWorlds().size();
            plugin.bundle().sendMessage(sender, "group.list.entry",
                    Placeholder.parsed("tree", i + 1 == groups.size() ? "└" : "├"),
                    Formatter.booleanChoice("single", worlds == 1),
                    Formatter.number("amount", worlds),
                    Placeholder.parsed("group", group.getName()));
        }
        return SINGLE_SUCCESS;
    }
}
