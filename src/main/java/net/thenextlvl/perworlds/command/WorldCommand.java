package net.thenextlvl.perworlds.command;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.WorldGroup;
import net.thenextlvl.perworlds.command.argument.GroupArgument;
import net.thenextlvl.perworlds.command.brigadier.BrigadierCommand;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class WorldCommand extends BrigadierCommand {
    private WorldCommand(PerWorldsPlugin plugin) {
        super(plugin, "world", "perworlds.command");
    }

    public static LiteralCommandNode<CommandSourceStack> create(PerWorldsPlugin plugin) {
        var command = new WorldCommand(plugin);
        return command.create()
                .then(GroupCommand.create(plugin))
                .build();
    }

    public static RequiredArgumentBuilder<CommandSourceStack, WorldGroup> groupArgument(PerWorldsPlugin plugin, boolean listAll) {
        return Commands.argument("group", new GroupArgument(plugin, listAll));
    }
}
