package net.thenextlvl.perworlds.model;

import net.thenextlvl.perworlds.statistics.BlockTypeStat;
import org.bukkit.Statistic;
import org.bukkit.block.BlockType;
import org.jspecify.annotations.NullMarked;

import java.util.Map;
import java.util.function.BiConsumer;

@NullMarked
public final class PaperBlockTypeStat extends PaperSubstatistic<BlockType> implements BlockTypeStat {
    public PaperBlockTypeStat(Map<BlockType, Integer> values) {
        super(values);
    }

    public PaperBlockTypeStat() {
    }

    @Override
    public void forEachValue(BiConsumer<BlockType, Integer> action) {
        values.forEach(action);
    }

    @Override
    public Statistic.Type getType() {
        return Statistic.Type.BLOCK;
    }
}
