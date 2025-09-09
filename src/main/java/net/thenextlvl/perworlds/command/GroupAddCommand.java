package net.thenextlvl.perworlds.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.WorldGroup;
import net.thenextlvl.perworlds.command.brigadier.SimpleCommand;
import net.thenextlvl.perworlds.command.suggestion.UnassignedWorldsSuggestionProvider;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;

import static net.thenextlvl.perworlds.command.GroupCommand.groupArgument;

@NullMarked
final class GroupAddCommand extends SimpleCommand {
    private GroupAddCommand(PerWorldsPlugin plugin) {
        super(plugin, "add", "perworlds.command.group.add");
    }

    public static ArgumentBuilder<CommandSourceStack, ?> create(PerWorldsPlugin plugin) {
        var command = new GroupAddCommand(plugin);
        return command.create().then(command.add());
    }

    private ArgumentBuilder<CommandSourceStack, ?> add() {
        return Commands.argument("world", ArgumentTypes.world())
                .suggests(new UnassignedWorldsSuggestionProvider<>(plugin))
                .then(groupArgument(plugin, false).executes(this));
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        var group = context.getArgument("group", WorldGroup.class);
        var world = context.getArgument("world", World.class);
        var success = group.addWorld(world);
        var message = success ? "group.world.added" : "group.world.add.failed";
        plugin.bundle().sendMessage(context.getSource().getSender(), message,
                Placeholder.unparsed("group", group.getName()),
                Placeholder.unparsed("world", world.getName()));
        return success ? Command.SINGLE_SUCCESS : 0;
    }
}
