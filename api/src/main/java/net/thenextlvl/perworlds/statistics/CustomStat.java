package net.thenextlvl.perworlds.statistics;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ApiStatus.NonExtendable
public interface CustomStat extends Stat<Void> {
    int getValue();

    void setValue(int value);
}
