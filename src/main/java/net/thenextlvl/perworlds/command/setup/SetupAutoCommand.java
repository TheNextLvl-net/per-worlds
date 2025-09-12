package net.thenextlvl.perworlds.command.setup;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.key.Key;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.WorldGroup;
import net.thenextlvl.perworlds.command.brigadier.SimpleCommand;
import net.thenextlvl.perworlds.group.PaperWorldGroup;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

final class SetupAutoCommand extends SimpleCommand {
    private SetupAutoCommand(PerWorldsPlugin plugin) {
        super(plugin, "setup", "perworlds.command.setup");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(PerWorldsPlugin plugin) {
        var command = new SetupAutoCommand(plugin);
        return command.create()
                .then(Commands.literal("auto"))
                .then(Commands.literal("finish"));
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        return 0;
    }

    private void createDefaultGroup() {
        var defaultGroupName = "default";

        var defaultGroup = plugin.groupProvider().getGroup(defaultGroupName)
                .orElseGet(() -> plugin.groupProvider().createGroup(defaultGroupName));

        Optional.ofNullable(plugin.getServer().getWorld(Key.key(Key.MINECRAFT_NAMESPACE, "overworld")))
                .ifPresent(defaultGroup::addWorld);
        Optional.ofNullable(plugin.getServer().getWorld(Key.key(Key.MINECRAFT_NAMESPACE, "the_nether")))
                .ifPresent(defaultGroup::addWorld);
        Optional.ofNullable(plugin.getServer().getWorld(Key.key(Key.MINECRAFT_NAMESPACE, "the_end")))
                .ifPresent(defaultGroup::addWorld);
    }
}
