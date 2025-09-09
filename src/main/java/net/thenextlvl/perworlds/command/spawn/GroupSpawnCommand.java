package net.thenextlvl.perworlds.command.spawn;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.command.brigadier.SimpleCommand;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
public final class GroupSpawnCommand extends SimpleCommand {
    private GroupSpawnCommand(PerWorldsPlugin plugin) {
        super(plugin, "spawn", "perworlds.command.group.spawn");
    }

    public static ArgumentBuilder<CommandSourceStack, ?> create(PerWorldsPlugin plugin) {
        var command = new GroupSpawnCommand(plugin);
        return command.create()
                .then(GroupSpawnSetCommand.create(plugin))
                .then(GroupSpawnUnsetCommand.create(plugin))
                .executes(command);
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
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
}
