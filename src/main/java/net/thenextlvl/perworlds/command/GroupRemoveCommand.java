package net.thenextlvl.perworlds.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.WorldGroup;
import net.thenextlvl.perworlds.command.suggestion.GroupMemberSuggestionProvider;
import org.jspecify.annotations.NullMarked;

import static net.thenextlvl.perworlds.command.GroupCommand.groupArgument;

@NullMarked
class GroupRemoveCommand {
    private final PerWorldsPlugin plugin;

    private GroupRemoveCommand(PerWorldsPlugin plugin) {
        this.plugin = plugin;
    }

    public static ArgumentBuilder<CommandSourceStack, ?> create(PerWorldsPlugin plugin) {
        var command = new GroupRemoveCommand(plugin);
        return Commands.literal("remove")
                .requires(source -> source.getSender().hasPermission("perworlds.command.group.remove"))
                .then(command.remove());
    }

    private ArgumentBuilder<CommandSourceStack, ?> remove() {
        return groupArgument(plugin).then(Commands.argument("world", ArgumentTypes.key())
                .suggests(new GroupMemberSuggestionProvider<>())
                .executes(this::remove));
    }

    private int remove(CommandContext<CommandSourceStack> context) {
        var group = context.getArgument("group", WorldGroup.class);
        var key = context.getArgument("world", Key.class);
        var world = plugin.getServer().getWorld(key);
        var success = world != null ? group.removeWorld(world) : group.removeWorld(key);
        var message = success ? "group.world.removed" : "group.world.remove.failed";
        plugin.bundle().sendMessage(context.getSource().getSender(), message,
                Placeholder.unparsed("group", group.getName()),
                Placeholder.unparsed("world", world != null ? world.getName() : key.asString()));
        return success ? Command.SINGLE_SUCCESS : 0;
    }
}
