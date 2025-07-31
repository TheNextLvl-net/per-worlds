package net.thenextlvl.perworlds.statistics;

import core.nbt.serialization.TagSerializable;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

import java.util.Map;

/**
 * @since 0.1.0
 */
@NullMarked
@ApiStatus.NonExtendable
public interface Stat<T> extends TagSerializable {
    @Unmodifiable
    @Contract(pure = true)
    Map<T, Integer> getValues();

    @ApiStatus.Internal
    boolean shouldSerialize();

    @Contract(pure = true)
    int getValue(T type);

    @Contract(mutates = "this")
    void setValue(T type, int value);

    @ApiStatus.Internal
    @Contract(mutates = "param2")
    void apply(Statistic statistic, Player player);
}
