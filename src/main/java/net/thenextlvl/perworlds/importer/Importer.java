package net.thenextlvl.perworlds.importer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.WorldGroup;
import net.thenextlvl.perworlds.data.PlayerData;
import org.bukkit.World;
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
import java.util.concurrent.CompletableFuture;

@NullMarked
public abstract class Importer {
    protected final PerWorldsPlugin plugin;

    private final Path dataPath;
    private final String name;

    protected Importer(final PerWorldsPlugin plugin, final String name) {
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

    public CompletableFuture<Boolean> load(final CommandSender sender) {
        return CompletableFuture.supplyAsync(() -> {
            if (!isAvailable()) return false;
            try {
                plugin.bundle().sendMessage(sender, "group.data.import.start", Placeholder.parsed("provider", name));
                final var groups = loadGroups(sender);
                loadPlayers(groups, sender);
                groups.forEach(WorldGroup::persist);
                return true;
            } catch (final IOException e) {
                plugin.getComponentLogger().error("Failed to import {}", name, e);
                return false;
            }
        });
    }

    public Set<WorldGroup> loadGroups(final CommandSender sender) throws IOException {
        final var read = readGroups();
        final var groups = new HashSet<WorldGroup>(read.size());
        read.forEach((group, worlds) -> {
            final var worldGroup = plugin.groupProvider().getGroup(group).orElseGet(() ->
                    plugin.groupProvider().createGroup(group));
            final var importedWorlds = worlds.stream().map(plugin.getServer()::getWorld)
                    .filter(Objects::nonNull)
                    .peek(worldGroup::addWorld)
                    .map(World::getName)
                    .map(Component::text)
                    .toList();
            groups.add(worldGroup);
            final var message = importedWorlds.isEmpty()
                    ? "group.data.import.group.success.empty"
                    : "group.data.import.group.success";
            plugin.bundle().sendMessage(sender, message,
                    Placeholder.parsed("group", group),
                    Formatter.joining("worlds", importedWorlds));
        });
        return groups;
    }

    public void loadPlayers(final Set<WorldGroup> groups, final CommandSender sender) throws IOException {
        readPlayers().forEach((uuid, name) -> groups.forEach(group -> {
            final var offlinePlayer = plugin.getServer().getOfflinePlayer(uuid);
            group.persistPlayerData(offlinePlayer, playerData -> {
                try {
                    readPlayer(uuid, name, group, playerData);
                    plugin.bundle().sendMessage(sender, "group.data.import.player.success",
                            Placeholder.parsed("group", group.getName()),
                            Placeholder.parsed("player", name));
                } catch (final IOException e) {
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
