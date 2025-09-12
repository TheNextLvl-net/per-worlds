package net.thenextlvl.perworlds.command.setup;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.WorldGroup;
import net.thenextlvl.perworlds.command.brigadier.SimpleCommand;
import net.thenextlvl.perworlds.group.PaperGroupData;
import net.thenextlvl.perworlds.group.PaperGroupSettings;
import net.thenextlvl.perworlds.group.PaperWorldGroup;
import org.bukkit.World;

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
        var group = group();
        group.forEach(g -> {
            System.out.println(g.getName() + ": " + g.getWorlds().map(World::getName).collect(Collectors.joining(", "))); // todo: remove
        });
        return 0;
    }

    // todo: cleanup
    private List<WorldGroup> group() {
        var worlds = plugin.getServer().getWorlds();
        var groups = worlds.stream().collect(Collectors.groupingBy(w -> w.key().namespace()));
        System.out.println(groups); // todo: remove

        var result = new ArrayList<WorldGroup>();
        for (var entry : groups.entrySet()) {
            var nameGroups = new ArrayList<List<World>>();
            for (var world : entry.getValue()) {
                boolean added = false;
                var worldParts = splitParts(world.getName());
                for (var group : nameGroups) {
                    var groupParts = splitParts(group.getFirst().getName());
                    var matches = countMatches(worldParts, groupParts);
                    System.out.println(matches + " matches between " + group.getFirst().getName() + " and " + world.getName()); // todo: remove
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
                if (group.size() <= 1) continue;
                var name = mostCommonPart(group).orElse(entry.getKey());
                result.add(new PaperWorldGroup(plugin.groupProvider(), name, new PaperGroupData(), new PaperGroupSettings(), group));
            }
        }
        return result;
    }

    private long countMatches(List<String> a, List<String> b) {
        return a.stream().filter(b::contains).count();
    }

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

    private List<String> splitParts(String name) {
        return List.of(name.toLowerCase().split("[\\-_\\s]+"));
    }
}
