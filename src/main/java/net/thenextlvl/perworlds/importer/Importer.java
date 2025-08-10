package net.thenextlvl.perworlds.importer;

import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.WorldGroup;
import net.thenextlvl.perworlds.data.PlayerData;
import org.bukkit.generator.WorldInfo;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@NullMarked
public abstract class Importer {
    protected final PerWorldsPlugin plugin;

    private final Path dataPath;
    private final String name;

    protected Importer(PerWorldsPlugin plugin, String name) {
        this.dataPath = Path.of("plugins", name);
        this.name = name;
        this.plugin = plugin;
    }

    public Path getDataPath() {
        return dataPath;
    }

    public String getName() {
        return name;
    }

    public void load() {
        try {
            var groups = loadGroups();
            loadPlayers(groups);
        } catch (IOException e) {
            plugin.getComponentLogger().error("Failed to import {}", name, e);
        }
    }

    public Set<WorldGroup> loadGroups() throws IOException {
        var read = readGroups();
        var groups = new HashSet<WorldGroup>(read.size());
        read.forEach((group, worlds) -> {
            var worldGroup = plugin.groupProvider().getGroup(group).orElseGet(() ->
                    plugin.groupProvider().createGroup(group));
            worlds.stream().map(plugin.getServer()::getWorld)
                    .filter(Objects::nonNull)
                    .forEach(worldGroup::addWorld);
            groups.add(worldGroup);
        });
        return groups;
    }

    public void loadPlayers(Set<WorldGroup> groups) throws IOException {
        // todo: extract to readPlayer
        readPlayers().forEach((uuid, name) -> {
            groups.stream().forEach(group -> group.getWorlds().map(WorldInfo::getName)
                    .map(this.dataPath.resolve("worlds")::resolve)
                    .map(path -> path.resolve(name + ".json"))
                    .filter(Files::isRegularFile)
                    .findAny().ifPresent(path -> {
                        // todo: load player data from path into the current group
                    })
            );
        });
    }

    public abstract Map<String, Set<String>> readGroups() throws IOException;

    public abstract Map<UUID, String> readPlayers() throws IOException;

    public abstract PlayerData readPlayer(UUID uuid, String name, WorldGroup group) throws IOException;
}
