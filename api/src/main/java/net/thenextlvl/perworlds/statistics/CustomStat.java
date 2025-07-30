package net.thenextlvl.perworlds.statistics;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

/**
 * @since 0.1.0
 */
@NullMarked
@ApiStatus.NonExtendable
public interface CustomStat extends Stat<Void> {
    int getValue();

    @Contract(mutates = "this")
    void setValue(int value);
}
