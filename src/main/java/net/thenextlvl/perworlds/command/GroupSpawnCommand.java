package net.thenextlvl.perworlds.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.FinePositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.RotationResolver;
import io.papermc.paper.math.Rotation;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.WorldGroup;
import net.thenextlvl.perworlds.command.suggestion.GroupMemberSuggestionProvider;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

import static net.thenextlvl.perworlds.command.GroupCommand.groupArgument;

@NullMarked
class GroupSpawnCommand {
    private final PerWorldsPlugin plugin;

    private GroupSpawnCommand(PerWorldsPlugin plugin) {
        this.plugin = plugin;
    }

    public static ArgumentBuilder<CommandSourceStack, ?> create(PerWorldsPlugin plugin) {
        var command = new GroupSpawnCommand(plugin);
        return Commands.literal("spawn")
                .requires(source -> source.getSender().hasPermission("perworlds.command.group.spawn"))
                .then(command.setSpawn())
                .then(command.unsetSpawn())
                .executes(command::spawn);
    }

    private int spawn(CommandContext<CommandSourceStack> context) {
        var sender = context.getSource().getSender();
        if (!(sender instanceof Player player)) {
            plugin.bundle().sendMessage(sender, "command.sender");
            return 0;
        }
        var group = plugin.groupProvider().getGroup(player.getWorld())
                .orElse(plugin.groupProvider().getUnownedWorldGroup());
        group.getSpawnLocation().map(player::teleportAsync)
                .orElseGet(() -> CompletableFuture.completedFuture(false))
                .thenAccept(success -> {
                    var message = success ? "group.teleport.self" : "group.teleport.failed";
                    plugin.bundle().sendMessage(player, message, Placeholder.parsed("group", group.getName()));
                });
        return Command.SINGLE_SUCCESS;
    }

    private LiteralArgumentBuilder<CommandSourceStack> setSpawn() {
        return Commands.literal("set")
                .requires(source -> source.getSender().hasPermission("perworlds.command.group.spawn.set"))
                .executes(this::setSpawn)
                .then(targetArgument());
    }

    private LiteralArgumentBuilder<CommandSourceStack> unsetSpawn() {
        return Commands.literal("unset")
                .requires(source -> source.getSender().hasPermission("perworlds.command.group.spawn.unset"))
                .then(groupArgument(plugin, true).executes(context -> {
                    var group = context.getArgument("group", WorldGroup.class);
                    return unsetSpawn(context, group);
                })).executes(context -> {
                    var world = context.getSource().getLocation().getWorld();
                    var group = plugin.groupProvider().getGroup(world)
                            .orElse(plugin.groupProvider().getUnownedWorldGroup());
                    return unsetSpawn(context, group);
                });
    }

    private int unsetSpawn(CommandContext<CommandSourceStack> context, WorldGroup group) {
        var sender = context.getSource().getSender();
        group.getGroupData().setSpawnLocation(null);
        plugin.bundle().sendMessage(sender, "group.spawn.unset",
                Placeholder.parsed("group", group.getName()));
        return Command.SINGLE_SUCCESS;
    }

    private ArgumentBuilder<CommandSourceStack, ?> targetArgument() {
        return groupArgument(plugin, true).then(worldArgument()
                .then(positionArgument().then(rotationArgument().executes(context -> {
                    var rotation = context.getArgument("rotation", RotationResolver.class);
                    return setTargetSpawn(context, rotation.resolve(context.getSource()));
                })).executes(context -> setTargetSpawn(context, Rotation.rotation(0, 0)))));
    }

    private ArgumentBuilder<CommandSourceStack, ?> positionArgument() {
        return Commands.argument("position", ArgumentTypes.finePosition());
    }

    private ArgumentBuilder<CommandSourceStack, ?> rotationArgument() {
        return Commands.argument("rotation", ArgumentTypes.rotation());
    }

    private ArgumentBuilder<CommandSourceStack, ?> worldArgument() {
        return Commands.argument("world", ArgumentTypes.world())
                .suggests(new GroupMemberSuggestionProvider<>());
    }

    private int setTargetSpawn(CommandContext<CommandSourceStack> context, Rotation rotation) throws CommandSyntaxException {
        var position = context.getArgument("position", FinePositionResolver.class).resolve(context.getSource());
        var group = context.getArgument("group", WorldGroup.class);
        var world = context.getArgument("world", World.class);
        return setSpawn(context, group, position.toLocation(world).setRotation(rotation));
    }

    private int setSpawn(CommandContext<CommandSourceStack> context) {
        var location = context.getSource().getLocation();
        var group = plugin.groupProvider().getGroup(location.getWorld())
                .orElse(plugin.groupProvider().getUnownedWorldGroup());
        return setSpawn(context, group, location);
    }

    private int setSpawn(CommandContext<CommandSourceStack> context, WorldGroup group, Location location) {
        var sender = context.getSource().getSender();
        group.getGroupData().setSpawnLocation(location);
        plugin.bundle().sendMessage(sender, "group.spawn.set",
                Placeholder.parsed("group", group.getName()),
                Placeholder.parsed("world", location.getWorld().getName()),
                Formatter.number("x", location.x()),
                Formatter.number("y", location.y()),
                Formatter.number("z", location.z()),
                Formatter.number("yaw", location.getYaw()),
                Formatter.number("pitch", location.getPitch()));
        return Command.SINGLE_SUCCESS;
    }
}
