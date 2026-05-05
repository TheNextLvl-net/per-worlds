package net.thenextlvl.perworlds.command.spawn;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.WorldGroup;
import net.thenextlvl.perworlds.command.brigadier.SimpleCommand;

import static net.thenextlvl.perworlds.command.WorldCommand.groupArgument;

final class GroupSpawnUnsetCommand extends SimpleCommand {

    private GroupSpawnUnsetCommand(final PerWorldsPlugin plugin) {
        super(plugin, "unset", "perworlds.command.group.spawn.unset");
    }

    public static ArgumentBuilder<CommandSourceStack, ?> create(final PerWorldsPlugin plugin) {
        final var command = new GroupSpawnUnsetCommand(plugin);
        return command.create()
                .then(groupArgument(plugin, true).executes(command))
                .executes(command);
    }

    @Override
    public int run(final CommandContext<CommandSourceStack> context) {
        final var group = tryGetArgument(context, "group", WorldGroup.class).orElseGet(() -> {
            return plugin.groupProvider().getGroup(context.getSource().getLocation().getWorld())
                    .orElse(plugin.groupProvider().getUnownedWorldGroup());
        });
        group.getGroupData().setSpawnLocation(null);
        plugin.bundle().sendMessage(context.getSource().getSender(), "group.spawn.unset",
                Placeholder.parsed("group", group.getName()));
        return Command.SINGLE_SUCCESS;
    }
}
