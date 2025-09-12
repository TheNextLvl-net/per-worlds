package net.thenextlvl.perworlds.command.setup;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.command.brigadier.SimpleCommand;

final class SetupFinishCommand extends SimpleCommand {
    private SetupFinishCommand(PerWorldsPlugin plugin) {
        super(plugin, "setup", "perworlds.command.setup");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(PerWorldsPlugin plugin) {
        var command = new SetupFinishCommand(plugin);
        return command.create()
                .then(Commands.literal("auto"))
                .then(Commands.literal("finish"));
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        return 0;
    }
}
