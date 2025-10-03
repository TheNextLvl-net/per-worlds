package net.thenextlvl.perworlds.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.command.brigadier.SimpleCommand;
import org.bukkit.World;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

final class GroupAutoCommand extends SimpleCommand {
    private GroupAutoCommand(PerWorldsPlugin plugin) {
        super(plugin, "auto", "perworlds.command.group.auto");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> create(PerWorldsPlugin plugin) {
        var command = new GroupAutoCommand(plugin);
        return command.create().executes(command);
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        var sender = context.getSource().getSender();
        var groupCount = new AtomicInteger();
        var worldCount = new AtomicInteger();
        autoGroup().forEach((worlds, name) -> {
            if (worlds.size() == 1 || worlds.stream().allMatch(plugin.groupProvider()::hasGroup)) {
                worlds.forEach(world -> plugin.bundle().sendMessage(sender, "group.auto.skipped",
                        Placeholder.parsed("world", world.getName())));
                return;
            }

            var group = plugin.groupProvider().createGroup(plugin.groupProvider().findFreeName(name));
            plugin.bundle().sendMessage(sender, "group.auto.created",
                    Placeholder.parsed("group", group.getName()));
            groupCount.incrementAndGet();

            worlds.forEach(world -> {
                var added = group.addWorld(world);
                if (added) worldCount.incrementAndGet();
                var message = added ? "group.auto.added" : "group.auto.failed";
                plugin.bundle().sendMessage(sender, message,
                        Placeholder.parsed("world", world.getName()),
                        Placeholder.parsed("group", group.getName()));
            });
        });
        plugin.bundle().sendMessage(sender, "group.auto.done",
                Formatter.number("groups", groupCount.get()),
                Formatter.number("worlds", worldCount.get()));
        return 0;
    }

    @Contract(pure = true)
    private Map<List<World>, String> autoGroup() {
        var worlds = plugin.getServer().getWorlds();
        var groups = worlds.stream().collect(Collectors.groupingBy(world -> world.key().namespace()));

        var result = new HashMap<List<World>, String>();
        for (var entry : groups.entrySet()) {
            var nameGroups = new ArrayList<List<World>>();
            for (var world : entry.getValue()) {
                var added = false;
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
                result.put(group, name);
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
