package net.thenextlvl.perworlds.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.WorldGroup;
import net.thenextlvl.perworlds.command.brigadier.SimpleCommand;
import org.jspecify.annotations.NullMarked;

import static net.thenextlvl.perworlds.command.WorldCommand.groupArgument;

@NullMarked
final class GroupDeleteCommand extends SimpleCommand {
    private GroupDeleteCommand(final PerWorldsPlugin plugin) {
        super(plugin, "delete", "perworlds.command.group.delete");
    }

    public static ArgumentBuilder<CommandSourceStack, ?> create(final PerWorldsPlugin plugin) {
        final var command = new GroupDeleteCommand(plugin);
        return command.create().then(groupArgument(plugin, true).executes(command));
    }

    @Override
    public int run(final CommandContext<CommandSourceStack> context) {
        final var sender = context.getSource().getSender();
        final var group = context.getArgument("group", WorldGroup.class);
        final var success = group.delete();
        final var message = success ? "group.deleted" : "group.delete.failed";
        plugin.bundle().sendMessage(sender, message, Placeholder.unparsed("group", group.getName()));
        return success ? Command.SINGLE_SUCCESS : 0;
    }
}
