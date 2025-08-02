package net.thenextlvl.perworlds.statistics;

import net.kyori.adventure.key.Keyed;

import java.util.function.BiConsumer;

/**
 * Represents a specialized type of {@link Stat} related to a specific keyed type.
 *
 * @param <T> the type of key that this statistic is associated with.
 * @since 1.0.0
 */
public interface TypedStat<T extends Keyed> extends Stat<T> {
    void forEachValue(BiConsumer<T, Integer> action);
}
