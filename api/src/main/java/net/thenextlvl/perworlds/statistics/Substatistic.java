package net.thenextlvl.perworlds.statistics;

import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Represents a substatistic related to a specific keyed type.
 *
 * @param <T> the type of key that this statistic is associated with.
 * @since 1.0.0
 */
public interface Substatistic<T extends Keyed> extends Stat {
    @Unmodifiable
    @Contract(pure = true)
    Map<T, Integer> getValues();

    @Contract(pure = true)
    int getValue(T type);

    @Contract(mutates = "this")
    void setValue(T type, int value);

    void forEachValue(BiConsumer<T, Integer> action);
}
