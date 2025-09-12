package net.thenextlvl.perworlds.command.setup;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.command.brigadier.SimpleCommand;
import org.bukkit.World;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

final class SetupAutoCommand extends SimpleCommand {
    private SetupAutoCommand(PerWorldsPlugin plugin) {
        super(plugin, "auto", "perworlds.command.setup.auto");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(PerWorldsPlugin plugin) {
        var command = new SetupAutoCommand(plugin);
        return command.create().executes(command);
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        var sender = context.getSource().getSender();
        group().forEach((name, worlds) -> {
            if (worlds.size() == 1) {
                sender.sendMessage("Skipping world '" + worlds.getFirst().key() + "'");
                return;
            }
            var group = plugin.groupProvider().getGroup(name).orElseGet(() -> {
                sender.sendMessage("Creating new group '" + name + "'");
                return plugin.groupProvider().createGroup(name);
            });
            worlds.forEach(world -> {
                if (group.addWorld(world)) {
                    sender.sendMessage("Added world '" + world.key() + "' to group '" + group.getName() + "'");
                } else {
                    sender.sendMessage("Failed to add world '" + world.key() + "' to group '" + group.getName() + "'");
                }
            });
        });
        sender.sendMessage("Skipped worlds will remain in the 'unowned' group.");
        return 0;
    }

    // todo: cleanup
    @Contract(pure = true)
    private Map<String, List<World>> group() {
        var worlds = plugin.getServer().getWorlds();
        var groups = worlds.stream().collect(Collectors.groupingBy(w -> w.key().namespace()));

        var result = new HashMap<String, List<World>>();
        for (var entry : groups.entrySet()) {
            var nameGroups = new ArrayList<List<World>>();
            for (var world : entry.getValue()) {
                boolean added = false;
                var worldParts = splitParts(world.getName());
                for (var group : nameGroups) {
                    var groupParts = splitParts(group.getFirst().getName());
                    var matches = countMatches(worldParts, groupParts);
                    if (matches > 0) {
                        group.add(world);
                        added = true;
                        break;
                    }
                }
                if (!added) {
                    var newGroup = new ArrayList<World>();
                    newGroup.add(world);
                    nameGroups.add(newGroup);
                }
            }
            for (var group : nameGroups) {
                var name = mostCommonPart(group).orElse(entry.getKey());
                result.put(name, group);
            }
        }
        return result;
    }

    @Contract(pure = true)
    private long countMatches(List<String> a, List<String> b) {
        return a.stream().filter(b::contains).count();
    }

    @Contract(pure = true)
    private Optional<String> mostCommonPart(List<World> worlds) {
        var partCount = new HashMap<String, Integer>();
        for (var world : worlds) {
            for (var part : splitParts(world.getName())) {
                partCount.put(part, partCount.getOrDefault(part, 0) + 1);
            }
        }
        return partCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }

    @Contract(pure = true)
    private List<String> splitParts(String name) {
        return List.of(name.toLowerCase().split("[\\-_\\s]+"));
    }
}
