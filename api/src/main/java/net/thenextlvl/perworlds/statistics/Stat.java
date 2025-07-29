package net.thenextlvl.perworlds.statistics;

import core.nbt.serialization.TagSerializable;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

import java.util.Map;

@NullMarked
@ApiStatus.NonExtendable
public interface Stat<T> extends TagSerializable {
    @Unmodifiable
    Map<T, Integer> getValues();

    boolean shouldSerialize();

    int getValue(T type);

    void setValue(T type, int value);

    void apply(Statistic statistic, Player player);
}
