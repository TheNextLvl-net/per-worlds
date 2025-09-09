package net.thenextlvl.perworlds.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.command.brigadier.SimpleCommand;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class GroupCreateCommand extends SimpleCommand {
    private GroupCreateCommand(PerWorldsPlugin plugin) {
        super(plugin, "create", "perworlds.command.group.create");
    }

    public static ArgumentBuilder<CommandSourceStack, ?> create(PerWorldsPlugin plugin) {
        var command = new GroupCreateCommand(plugin);
        return command.create().then(Commands.argument("name", StringArgumentType.string()).executes(command));
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        var sender = context.getSource().getSender();
        var name = context.getArgument("name", String.class);
        var success = !plugin.groupProvider().hasGroup(name);
        if (success) plugin.groupProvider().createGroup(name);
        var message = success ? "group.created" : "group.exists";
        plugin.bundle().sendMessage(sender, message, Placeholder.unparsed("name", name));
        return success ? Command.SINGLE_SUCCESS : 0;
    }
}
