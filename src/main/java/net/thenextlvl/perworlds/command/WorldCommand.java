package net.thenextlvl.perworlds.command;

import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.WorldGroup;
import net.thenextlvl.perworlds.command.argument.GroupArgument;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class WorldCommand {
    public static LiteralCommandNode<CommandSourceStack> create(PerWorldsPlugin plugin) {
        return Commands.literal("world")
                .requires(source -> source.getSender().hasPermission("perworlds.command"))
                .then(GroupCommand.create(plugin))
                .build();
    }

    public static RequiredArgumentBuilder<CommandSourceStack, WorldGroup> groupArgument(PerWorldsPlugin plugin, boolean listAll) {
        return Commands.argument("group", new GroupArgument(plugin, listAll));
    }
}
