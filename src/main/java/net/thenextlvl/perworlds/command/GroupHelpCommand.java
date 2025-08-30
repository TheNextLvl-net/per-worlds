package net.thenextlvl.perworlds.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.perworlds.PerWorldsPlugin;

final class GroupHelpCommand {
    private static final String DOCS_URL = "https://thenextlvl.net/docs/perworlds";
    
    public static ArgumentBuilder<CommandSourceStack, ?> create(PerWorldsPlugin plugin) {
        return Commands.literal("help").executes(context -> {
            var sender = context.getSource().getSender();
            plugin.bundle().sendMessage(sender, "perworlds.help", Placeholder.parsed("documentation", DOCS_URL));
            return Command.SINGLE_SUCCESS;
        });
    }
}
