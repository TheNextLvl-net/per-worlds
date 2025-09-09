package net.thenextlvl.perworlds.model;

import net.thenextlvl.perworlds.statistics.EntityTypeStat;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.jspecify.annotations.NullMarked;

import java.util.Map;
import java.util.function.BiConsumer;

@NullMarked
public final class PaperEntityTypeStat extends PaperSubstatistic<EntityType> implements EntityTypeStat {
    public PaperEntityTypeStat(Map<EntityType, Integer> values) {
        super(values);
    }

    public PaperEntityTypeStat() {
    }

    @Override
    public void forEachValue(BiConsumer<EntityType, Integer> action) {
        values.forEach(action);
    }

    @Override
    public Statistic.Type getType() {
        return Statistic.Type.ENTITY;
    }
}
