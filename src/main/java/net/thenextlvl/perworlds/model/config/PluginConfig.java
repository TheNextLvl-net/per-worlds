package net.thenextlvl.perworlds.model.config;

import net.thenextlvl.perworlds.GroupProvider;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.WorldGroup;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

@NullMarked
public final class PluginConfig {
    public @Nullable String migrateToGroup = null;
    public boolean handleAdvancementMessages = true;
    public boolean handleDeathMessages = true;
    public boolean handleJoinMessages = true;
    public boolean handleQuitMessages = true;

    public boolean migrateToGroup(final PerWorldsPlugin plugin, final WorldGroup group) {
        if (group.getName().equals(migrateToGroup)) return false;
        migrateToGroup = group.getName();
        final var provider = plugin.groupProvider();
        plugin.getServer().getOnlinePlayers().forEach(player -> {
            if (group.hasPlayerData(player)) return;
            final var current = provider.getGroup(player.getWorld()).orElse(provider.getUnownedWorldGroup());
            if (current.equals(group) || current.hasPlayerData(player)) return;
            current.loadPlayerData(player, false);
        });
        plugin.configFile().save(plugin);
        return true;
    }

    public Optional<WorldGroup> getMigrateToGroup(final GroupProvider provider) {
        return Optional.ofNullable(migrateToGroup).flatMap(provider::getGroup);
    }
}
