package net.thenextlvl.perworlds.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.WorldGroup;
import org.jspecify.annotations.NullMarked;

import static net.thenextlvl.perworlds.command.GroupCommand.groupArgument;

@NullMarked
class GroupDeleteCommand {
    private final PerWorldsPlugin plugin;

    private GroupDeleteCommand(PerWorldsPlugin plugin) {
        this.plugin = plugin;
    }

    public static ArgumentBuilder<CommandSourceStack, ?> create(PerWorldsPlugin plugin) {
        var command = new GroupDeleteCommand(plugin);
        return Commands.literal("delete")
                .requires(source -> source.getSender().hasPermission("perworlds.command.group.delete"))
                .then(groupArgument(plugin).executes(command::delete));
    }

    private int delete(CommandContext<CommandSourceStack> context) {
        var sender = context.getSource().getSender();
        var group = context.getArgument("group", WorldGroup.class);
        var success = group.delete();
        var message = success ? "group.deleted" : "group.delete.failed";
        plugin.bundle().sendMessage(sender, message, Placeholder.unparsed("group", group.getName()));
        return success ? Command.SINGLE_SUCCESS : 0;
    }
}
