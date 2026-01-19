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

    public boolean migrateToGroup(PerWorldsPlugin plugin, WorldGroup group) {
        if (group.getName().equals(migrateToGroup)) return false;
        migrateToGroup = group.getName();
        var provider = plugin.groupProvider();
        plugin.getServer().getOnlinePlayers().forEach(player -> {
            if (group.hasPlayerData(player)) return;
            var current = provider.getGroup(player.getWorld()).orElse(provider.getUnownedWorldGroup());
            if (current.equals(group) || current.hasPlayerData(player)) return;
            current.loadPlayerData(player, false);
        });
        plugin.configFile().save(plugin);
        return true;
    }

    public Optional<WorldGroup> getMigrateToGroup(GroupProvider provider) {
        return Optional.ofNullable(migrateToGroup).flatMap(provider::getGroup);
    }
}
