package net.thenextlvl.perworlds.model;

import net.thenextlvl.perworlds.statistics.CustomStat;
import org.bukkit.Statistic;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PaperCustomStat implements CustomStat {
    private int value;
    
    public PaperCustomStat(final int value) {
        this.value = value;
    }

    public PaperCustomStat() {
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public void setValue(final int value) {
        this.value = value;
    }

    @Override
    public boolean hasData() {
        return value != 0;
    }

    @Override
    public Statistic.Type getType() {
        return Statistic.Type.UNTYPED;
    }
}
