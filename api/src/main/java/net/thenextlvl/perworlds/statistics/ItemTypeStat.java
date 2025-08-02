package net.thenextlvl.perworlds.statistics;

import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.function.BiConsumer;

/**
 * @since 0.1.0
 */
@NullMarked
@ApiStatus.NonExtendable
public interface ItemTypeStat extends Stat<ItemType> {
    void forEachValue(BiConsumer<ItemType, Integer> action);
}
