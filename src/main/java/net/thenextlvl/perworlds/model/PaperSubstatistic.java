package net.thenextlvl.perworlds.model;

import net.kyori.adventure.key.Keyed;
import net.thenextlvl.perworlds.statistics.Substatistic;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

import java.util.HashMap;
import java.util.Map;

@NullMarked
public abstract class PaperSubstatistic<T extends Keyed> implements Substatistic<T> {
    protected final Map<T, Integer> values = new HashMap<>();

    protected PaperSubstatistic(final Map<T, Integer> values) {
        this.values.putAll(values);
    }

    protected PaperSubstatistic() {
    }

    @Override
    public @Unmodifiable Map<T, Integer> getValues() {
        return Map.copyOf(values);
    }

    @Override
    public int getValue(final T type) {
        return values.getOrDefault(type, 0);
    }

    @Override
    public void setValue(final T type, final int value) {
        values.put(type, value);
    }

    @Override
    public boolean hasData() {
        return values.values().stream().anyMatch(integer -> integer != 0);
    }
}
