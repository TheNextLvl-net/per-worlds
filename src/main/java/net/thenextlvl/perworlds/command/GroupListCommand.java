package net.thenextlvl.perworlds.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import org.jspecify.annotations.NullMarked;

@NullMarked
class GroupListCommand {
    private final PerWorldsPlugin plugin;

    private GroupListCommand(PerWorldsPlugin plugin) {
        this.plugin = plugin;
    }

    public static ArgumentBuilder<CommandSourceStack, ?> create(PerWorldsPlugin plugin) {
        var command = new GroupListCommand(plugin);
        return Commands.literal("list")
                .requires(source -> source.getSender().hasPermission("perworlds.command.group.list"))
                .executes(command::list);
    }

    private int list(CommandContext<CommandSourceStack> context) {
        var sender = context.getSource().getSender();
        var groups = plugin.groupProvider().getAllGroups().stream().map(group ->
                plugin.bundle().component("group.list.component", sender,
                        Placeholder.parsed("group", group.getName()))
        ).toList();
        plugin.bundle().sendMessage(sender, "group.list",
                Formatter.number("amount", groups.size()),
                Formatter.joining("groups", groups));
        return Command.SINGLE_SUCCESS;
    }
}
