package net.thenextlvl.perworlds.statistics;

import org.bukkit.Statistic;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

/**
 * @since 0.1.0
 */
@NullMarked
@ApiStatus.NonExtendable
public interface Stat {
    /**
     * Determines if the statistic contains any data.
     *
     * @return {@code true} if the statistic has data, otherwise {@code false}.
     * @since 1.0.0
     */
    @Contract(pure = true)
    boolean hasData();

    /**
     * Retrieves the type of the statistic.
     *
     * @return the type of the statistic.
     * @since 1.0.0
     */
    @Contract(pure = true)
    Statistic.Type getType();
}
