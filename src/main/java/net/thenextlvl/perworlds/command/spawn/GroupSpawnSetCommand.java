package net.thenextlvl.perworlds.command.spawn;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
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
import net.thenextlvl.perworlds.command.brigadier.SimpleCommand;
import net.thenextlvl.perworlds.command.suggestion.GroupMemberSuggestionProvider;
import org.bukkit.World;

import static net.thenextlvl.perworlds.command.GroupCommand.groupArgument;

final class GroupSpawnSetCommand extends SimpleCommand {
    private GroupSpawnSetCommand(PerWorldsPlugin plugin) {
        super(plugin, "set", "perworlds.command.group.spawn.set");
    }

    public static ArgumentBuilder<CommandSourceStack, ?> create(PerWorldsPlugin plugin) {
        var command = new GroupSpawnSetCommand(plugin);
        return command.create()
                .then(command.targetArgument())
                .executes(command);
    }

    private ArgumentBuilder<CommandSourceStack, ?> targetArgument() {
        return groupArgument(plugin, true).then(worldArgument().then(positionArgument()
                .then(rotationArgument().executes(this))
                .executes(this)));
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

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var positionResolver = tryGetArgument(context, "position", FinePositionResolver.class).orElse(null);
        var rotationResolver = tryGetArgument(context, "rotation", RotationResolver.class).orElse(null);

        var world = tryGetArgument(context, "world", World.class).orElseGet(() -> context.getSource().getLocation().getWorld());
        var group = tryGetArgument(context, "group", WorldGroup.class).orElseGet(() -> {
            return plugin.groupProvider().getGroup(world).orElse(plugin.groupProvider().getUnownedWorldGroup());
        });

        var position = positionResolver != null ? positionResolver.resolve(context.getSource()) : context.getSource().getLocation();
        var rotation = rotationResolver != null ? rotationResolver.resolve(context.getSource()) : Rotation.rotation(0, 0);

        var location = position.toLocation(world).setRotation(rotation);

        group.getGroupData().setSpawnLocation(location);
        plugin.bundle().sendMessage(context.getSource().getSender(), "group.spawn.set",
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
