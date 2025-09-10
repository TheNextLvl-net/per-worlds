package net.thenextlvl.perworlds.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.command.brigadier.SimpleCommand;

import static net.thenextlvl.perworlds.PerWorldsPlugin.DOCS_URL;

final class GroupHelpCommand extends SimpleCommand {
    private GroupHelpCommand(PerWorldsPlugin plugin) {
        super(plugin, "help", null);
    }

    public static ArgumentBuilder<CommandSourceStack, ?> create(PerWorldsPlugin plugin) {
        var command = new GroupHelpCommand(plugin);
        return command.create().executes(command);
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        var sender = context.getSource().getSender();
        plugin.bundle().sendMessage(sender, "perworlds.help", Placeholder.parsed("documentation", DOCS_URL));
        return Command.SINGLE_SUCCESS;
    }
}
