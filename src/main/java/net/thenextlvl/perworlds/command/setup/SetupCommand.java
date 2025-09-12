package net.thenextlvl.perworlds.command.setup;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.command.brigadier.BrigadierCommand;

public final class SetupCommand extends BrigadierCommand {
    private SetupCommand(PerWorldsPlugin plugin) {
        super(plugin, "setup", "perworlds.command.setup");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(PerWorldsPlugin plugin) {
        return new SetupCommand(plugin).create()
                .then(SetupAutoCommand.create(plugin))
                .then(SetupFinishCommand.create(plugin));
    }
}
