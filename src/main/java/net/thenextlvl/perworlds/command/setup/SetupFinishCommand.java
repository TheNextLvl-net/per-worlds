package net.thenextlvl.perworlds.command.setup;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.command.brigadier.SimpleCommand;

import java.io.IOException;
import java.nio.file.Files;

final class SetupFinishCommand extends SimpleCommand {
    private SetupFinishCommand(PerWorldsPlugin plugin) {
        super(plugin, "finish", "perworlds.command.setup.finish");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(PerWorldsPlugin plugin) {
        var command = new SetupFinishCommand(plugin);
        return command.create();
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        var sender = context.getSource().getSender();
        if (Files.isRegularFile(plugin.setupSuccess)) {
            plugin.bundle().sendMessage(sender, "setup.finish.already");
            return 0;
        } else try {
            Files.createFile(plugin.setupSuccess);
            plugin.bundle().sendMessage(sender, "setup.finish.success");
            return SINGLE_SUCCESS;
        } catch (IOException e) {
            plugin.bundle().sendMessage(sender, "setup.finish.failed");
            plugin.getComponentLogger().error("Failed to create setup success file", e);
            return 0;
        }
    }
}
