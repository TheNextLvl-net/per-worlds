package net.thenextlvl.perworlds.data;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

/**
 * This interface is designed to manage the spawning mechanics of the Warden for a player.
 *
 * @since 0.1.0
 */
@NullMarked
@ApiStatus.NonExtendable
public interface WardenSpawnTracker {
    /**
     * Returns the player's cooldown duration in ticks until the next Warden warning can occur.
     *
     * @return the number of ticks remaining until the next Warden warning
     */
    @Contract(pure = true)
    int cooldownTicks();

    /**
     * Sets the cooldown duration in ticks for the Warden before it can issue another warning.
     *
     * @param cooldownTicks the cooldown duration in ticks
     * @return a new instance of {@code WardenSpawnTracker} with the updated cooldown duration
     */
    @Contract(value = "_ -> new", pure = true)
    WardenSpawnTracker cooldownTicks(int cooldownTicks);

    /**
     * Returns the number of ticks since the last Warden warning occurred.
     *
     * @return the number of ticks since the last Warden warning
     */
    @Contract(pure = true)
    int ticksSinceLastWarning();

    /**
     * Sets the number of ticks since the last Warden warning occurred.
     *
     * @param ticksSinceLastWarning the number of ticks since the last Warden warning
     * @return a new instance of {@code WardenSpawnTracker} with the updated ticks since the last warning
     */
    @Contract(value = "_ -> new", pure = true)
    WardenSpawnTracker ticksSinceLastWarning(int ticksSinceLastWarning);

    /**
     * Returns the current Warden warning level.
     *
     * @return the current Warden warning level
     */
    @Contract(pure = true)
    int warningLevel();

    /**
     * Sets the warning level for the Warden.
     *
     * @param warningLevel the new warning level to be set
     * @return a new instance of {@code WardenSpawnTracker} with the updated warning level
     */
    @Contract(value = "_ -> new", pure = true)
    WardenSpawnTracker warningLevel(int warningLevel);

    /**
     * Creates a new instance of the {@link WardenSpawnTracker} for the specified player.
     *
     * @param player the player to create the WardenSpawnTracker for
     * @return a new instance of {@link WardenSpawnTracker} initialized with the player's current Warden spawn tracking data
     * @since 1.0.0
     */
    @Contract(value = "_ -> new", pure = true)
    static WardenSpawnTracker of(final Player player) {
        return new WardenSpawnTrackerImpl(player);
    }

    /**
     * Creates a new instance of {@link WardenSpawnTracker} with default values.
     *
     * @return a new instance of {@link WardenSpawnTracker} initialized with default values
     * @since 1.0.0
     */
    @Contract(value = " -> new", pure = true)
    static WardenSpawnTracker create() {
        return new WardenSpawnTrackerImpl(0, 0, 0);
    }

    /**
     * Creates a new instance of {@link WardenSpawnTracker} with the specified parameters.
     *
     * @param cooldownTicks         the cooldown duration in ticks before the Warden will be warned again
     * @param ticksSinceLastWarning the number of ticks since the last Warden warning occurred
     * @param warningLevel          the current warden warning level
     * @return a new instance of {@link WardenSpawnTracker} initialized with the specified parameters
     * @since 1.0.0
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    static WardenSpawnTracker create(final int cooldownTicks, final int ticksSinceLastWarning, final int warningLevel) {
        return new WardenSpawnTrackerImpl(cooldownTicks, ticksSinceLastWarning, warningLevel);
    }
}
