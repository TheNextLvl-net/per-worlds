package net.thenextlvl.perworlds.group;

import net.kyori.adventure.key.Key;
import org.bukkit.World;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NullMarked
public final class PaperUnownedWorldGroup extends PaperWorldGroup {
    public PaperUnownedWorldGroup(final PaperGroupProvider provider) {
        super(provider, "unowned", new PaperGroupData(), new PaperGroupSettings(), Set.of());
    }

    @Override
    public boolean delete() {
        final var deleted = delete(getConfigFile()) | delete(getConfigFileBackup()) | delete(getDataFolder());
        if (deleted) getPlayers().forEach(this::loadPlayerData);
        return deleted;
    }

    @Override
    public Stream<World> getWorlds() {
        return provider.getServer().getWorlds().stream()
                .filter(this::containsWorld);
    }

    @Override
    public @Unmodifiable Set<Key> getPersistedWorlds() {
        return getWorlds().map(World::key)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public boolean addWorld(final World world) {
        return false;
    }

    @Override
    public boolean removeWorld(final World world) {
        return false;
    }

    @Override
    public boolean containsWorld(final World world) {
        return !provider.hasGroup(world);
    }
}
