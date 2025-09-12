package net.thenextlvl.perworlds.command;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.command.brigadier.BrigadierCommand;
import net.thenextlvl.perworlds.command.setup.SetupCommand;

public class PerWorldsCommand extends BrigadierCommand {
    private PerWorldsCommand(PerWorldsPlugin plugin) {
        super(plugin, "perworlds", "perworlds.command");
    }
    
    public static LiteralCommandNode<CommandSourceStack> create(PerWorldsPlugin plugin) {
        var command = new PerWorldsCommand(plugin);
        return command.create()
                .then(SetupCommand.create(plugin))
                .build();
    }
}
