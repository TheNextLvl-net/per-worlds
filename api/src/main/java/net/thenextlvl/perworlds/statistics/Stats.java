package net.thenextlvl.perworlds.statistics;

import org.bukkit.Statistic;
import org.bukkit.block.BlockType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemType;
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
public interface Stats {
    @Unmodifiable
    @Contract(pure = true)
    Map<Statistic, Stat<?>> getStatistics();

    @Contract(pure = true)
    int getStatistic(Statistic statistic);

    @Contract(pure = true)
    int getStatistic(Statistic statistic, BlockType type);

    @Contract(pure = true)
    int getStatistic(Statistic statistic, EntityType type);

    @Contract(pure = true)
    int getStatistic(Statistic statistic, ItemType type);

    @Contract(mutates = "this")
    void setStatistic(Statistic statistic, BlockType type, int value);

    @Contract(mutates = "this")
    void setStatistic(Statistic statistic, EntityType type, int value);

    @Contract(mutates = "this")
    void setStatistic(Statistic statistic, ItemType type, int value);

    @Contract(mutates = "this")
    void setStatistic(Statistic statistic, int value);

    @ApiStatus.Internal
    @Contract(mutates = "param1")
    void apply(Player player);

    @ApiStatus.Internal
    @Contract(mutates = "param1")
    void clear(Player player);
}
