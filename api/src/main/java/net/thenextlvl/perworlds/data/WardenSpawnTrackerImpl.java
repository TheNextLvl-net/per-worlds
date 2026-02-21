package net.thenextlvl.perworlds.data;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
record WardenSpawnTrackerImpl(
        int cooldownTicks,
        int ticksSinceLastWarning,
        int warningLevel
) implements WardenSpawnTracker {
    WardenSpawnTrackerImpl(final Player player) {
        this(player.getWardenWarningCooldown(), player.getWardenTimeSinceLastWarning(), player.getWardenWarningLevel());
    }

    @Override
    public WardenSpawnTrackerImpl cooldownTicks(final int cooldownTicks) {
        return new WardenSpawnTrackerImpl(cooldownTicks, ticksSinceLastWarning, warningLevel);
    }

    @Override
    public WardenSpawnTrackerImpl ticksSinceLastWarning(final int ticksSinceLastWarning) {
        return new WardenSpawnTrackerImpl(cooldownTicks, ticksSinceLastWarning, warningLevel);
    }

    @Override
    public WardenSpawnTrackerImpl warningLevel(final int warningLevel) {
        return new WardenSpawnTrackerImpl(cooldownTicks, ticksSinceLastWarning, warningLevel);
    }
}
