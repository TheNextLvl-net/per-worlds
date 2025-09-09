package net.thenextlvl.perworlds.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.command.brigadier.SimpleCommand;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class GroupListCommand extends SimpleCommand {
    private GroupListCommand(PerWorldsPlugin plugin) {
        super(plugin, "list", "perworlds.command.group.list");
    }

    public static ArgumentBuilder<CommandSourceStack, ?> create(PerWorldsPlugin plugin) {
        var command = new GroupListCommand(plugin);
        return command.create().executes(command);
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
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
