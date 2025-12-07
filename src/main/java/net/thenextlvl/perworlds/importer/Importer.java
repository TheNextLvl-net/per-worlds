package net.thenextlvl.perworlds.importer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.WorldGroup;
import net.thenextlvl.perworlds.data.PlayerData;
import org.bukkit.command.CommandSender;
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
        this.dataPath = plugin.getServer().getPluginsFolder().toPath().resolve(name);
        this.name = name;
        this.plugin = plugin;
    }

    public boolean isAvailable() {
        return Files.isDirectory(dataPath);
    }

    public Path getDataPath() {
        return dataPath;
    }

    public String getName() {
        return name;
    }

    public boolean load(CommandSender sender) {
        try {
            plugin.bundle().sendMessage(sender, "group.data.import.start", Placeholder.parsed("provider", name));
            var groups = loadGroups(sender);
            loadPlayers(groups, sender);
            return true;
        } catch (IOException e) {
            plugin.getComponentLogger().error("Failed to import {}", name, e);
            return false;
        }
    }

    public Set<WorldGroup> loadGroups(CommandSender sender) throws IOException {
        var read = readGroups();
        var groups = new HashSet<WorldGroup>(read.size());
        read.forEach((group, worlds) -> {
            var worldGroup = plugin.groupProvider().getGroup(group).orElseGet(() ->
                    plugin.groupProvider().createGroup(group));
            worlds.stream().map(plugin.getServer()::getWorld)
                    .filter(Objects::nonNull)
                    .forEach(worldGroup::addWorld);
            groups.add(worldGroup);
            plugin.bundle().sendMessage(sender, "group.data.import.group.success",
                    Placeholder.parsed("group", group),
                    Formatter.joining("worlds", worlds.stream()
                            .map(Component::text)
                            .toList()));
        });
        return groups;
    }

    public void loadPlayers(Set<WorldGroup> groups, CommandSender sender) throws IOException {
        readPlayers().forEach((uuid, name) -> groups.forEach(group -> {
            var offlinePlayer = plugin.getServer().getOfflinePlayer(uuid);
            group.persistPlayerData(offlinePlayer, playerData -> {
                try {
                    readPlayer(uuid, name, group, playerData);
                    plugin.bundle().sendMessage(sender, "group.data.import.player.success",
                            Placeholder.parsed("group", group.getName()),
                            Placeholder.parsed("player", name));
                } catch (IOException e) {
                    plugin.bundle().sendMessage(sender, "group.data.import.player.failed",
                            Placeholder.parsed("group", group.getName()),
                            Placeholder.parsed("player", name));
                    plugin.getComponentLogger().error("Failed to import player data for {} ({}) in group {}",
                            name, uuid, group.getName(), e);
                }
            });
        }));
    }

    public abstract Map<String, Set<String>> readGroups() throws IOException;

    public abstract Map<UUID, String> readPlayers() throws IOException;

    public abstract void readPlayer(UUID uuid, String name, WorldGroup group, PlayerData data) throws IOException;
}
