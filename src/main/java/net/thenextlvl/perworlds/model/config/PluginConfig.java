package net.thenextlvl.perworlds.model.config;

import net.thenextlvl.perworlds.GroupProvider;
import net.thenextlvl.perworlds.WorldGroup;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

@NullMarked
public final class PluginConfig {
    public @Nullable String migrateToGroup = null;

    // todo: reload all player data?
    public boolean migrateToGroup(WorldGroup group) {
        if (group.getName().equals(migrateToGroup)) return false;
        migrateToGroup = group.getName();
        return true;
    }

    public Optional<WorldGroup> getMigrateToGroup(GroupProvider provider) {
        return Optional.ofNullable(migrateToGroup).flatMap(provider::getGroup);
    }
}
