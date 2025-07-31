package net.thenextlvl.perworlds.statistics;

import org.bukkit.block.BlockType;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.function.BiConsumer;

/**
 * @since 0.1.0
 */
@NullMarked
@ApiStatus.NonExtendable
public interface BlockTypeStat extends Stat<BlockType> {
    void forEachValue(BiConsumer<BlockType, Integer> action);
}
