package net.thenextlvl.perworlds.model;

import net.thenextlvl.perworlds.statistics.ItemTypeStat;
import org.bukkit.Statistic;
import org.bukkit.inventory.ItemType;
import org.jspecify.annotations.NullMarked;

import java.util.Map;
import java.util.function.BiConsumer;

@NullMarked
public final class PaperItemTypeStat extends PaperSubstatistic<ItemType> implements ItemTypeStat {
    public PaperItemTypeStat(Map<ItemType, Integer> values) {
        super(values);
    }

    public PaperItemTypeStat() {
    }

    @Override
    public void forEachValue(BiConsumer<ItemType, Integer> action) {
        values.forEach(action);
    }

    @Override
    public Statistic.Type getType() {
        return Statistic.Type.ITEM;
    }
}
