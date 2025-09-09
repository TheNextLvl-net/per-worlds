package net.thenextlvl.perworlds.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.command.brigadier.SimpleCommand;

final class GroupHelpCommand extends SimpleCommand {
    private static final String DOCS_URL = "https://thenextlvl.net/docs/perworlds";

    GroupHelpCommand(PerWorldsPlugin plugin) {
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
