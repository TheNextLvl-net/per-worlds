package net.thenextlvl.perworlds.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.WorldGroup;
import net.thenextlvl.perworlds.command.brigadier.SimpleCommand;
import org.jspecify.annotations.NullMarked;

import static net.thenextlvl.perworlds.command.WorldCommand.groupArgument;

@NullMarked
final class GroupCloneCommand extends SimpleCommand {
    private GroupCloneCommand(final PerWorldsPlugin plugin) {
        super(plugin, "clone", "perworlds.command.group.clone");
    }

    public static ArgumentBuilder<CommandSourceStack, ?> create(final PerWorldsPlugin plugin) {
        final var command = new GroupCloneCommand(plugin);
        final var name = Commands.argument("name", StringArgumentType.string());
        return command.create().then(groupArgument(plugin, true).then(name.executes(command)));
    }

    @Override
    public int run(final CommandContext<CommandSourceStack> context) {
        final var sender = context.getSource().getSender();
        final var group = context.getArgument("group", WorldGroup.class);
        final var name = context.getArgument("name", String.class);
        final var success = !plugin.groupProvider().hasGroup(name);
        if (success) plugin.groupProvider().createGroup(name, data -> {
            data.copyFrom(group.getGroupData());
        }, groupSettings -> {
            groupSettings.copyFrom(group.getSettings());
        });
        final var message = success ? "group.cloned" : "group.exists";
        plugin.bundle().sendMessage(sender, message,
                Placeholder.unparsed("group", group.getName()),
                Placeholder.unparsed("name", name));
        return success ? Command.SINGLE_SUCCESS : 0;
    }
}
