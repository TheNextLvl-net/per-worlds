package net.thenextlvl.perworlds.data;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
record WardenSpawnTrackerImpl(
        int cooldownTicks,
        int ticksSinceLastWarning,
        int warningLevel
) implements WardenSpawnTracker {
    WardenSpawnTrackerImpl(Player player) {
        this(player.getWardenWarningCooldown(), player.getWardenTimeSinceLastWarning(), player.getWardenWarningLevel());
    }

    @Override
    public WardenSpawnTrackerImpl cooldownTicks(int cooldownTicks) {
        return new WardenSpawnTrackerImpl(cooldownTicks, ticksSinceLastWarning, warningLevel);
    }

    @Override
    public WardenSpawnTrackerImpl ticksSinceLastWarning(int ticksSinceLastWarning) {
        return new WardenSpawnTrackerImpl(cooldownTicks, ticksSinceLastWarning, warningLevel);
    }

    @Override
    public WardenSpawnTrackerImpl warningLevel(int warningLevel) {
        return new WardenSpawnTrackerImpl(cooldownTicks, ticksSinceLastWarning, warningLevel);
    }
}
