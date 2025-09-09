package net.thenextlvl.perworlds.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.WorldGroup;
import net.thenextlvl.perworlds.command.argument.GroupArgument;
import net.thenextlvl.perworlds.command.brigadier.BrigadierCommand;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class GroupCommand extends BrigadierCommand {
    private GroupCommand(PerWorldsPlugin plugin) {
        super(plugin, "group", "perworlds.command.group");
    }

    public static ArgumentBuilder<CommandSourceStack, ?> create(PerWorldsPlugin plugin) {
        var command = new GroupCommand(plugin);
        return command.create()
                .then(GroupAddCommand.create(plugin))
                .then(GroupCreateCommand.create(plugin))
                .then(GroupDeleteCommand.create(plugin))
                .then(GroupHelpCommand.create(plugin))
                .then(GroupInfoCommand.create(plugin))
                .then(GroupListCommand.create(plugin))
                .then(GroupOptionCommand.create(plugin))
                .then(GroupRemoveCommand.create(plugin))
                .then(GroupSpawnCommand.create(plugin))
                .then(GroupTeleportCommand.create(plugin));
    }

    static RequiredArgumentBuilder<CommandSourceStack, WorldGroup> groupArgument(PerWorldsPlugin plugin, boolean listAll) {
        return Commands.argument("group", new GroupArgument(plugin, listAll));
    }
}
