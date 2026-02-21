package net.thenextlvl.perworlds.model;

import com.google.common.base.Preconditions;
import net.thenextlvl.perworlds.statistics.BlockTypeStat;
import net.thenextlvl.perworlds.statistics.CustomStat;
import net.thenextlvl.perworlds.statistics.EntityTypeStat;
import net.thenextlvl.perworlds.statistics.ItemTypeStat;
import net.thenextlvl.perworlds.statistics.Stat;
import net.thenextlvl.perworlds.statistics.Statistics;
import org.bukkit.Registry;
import org.bukkit.Statistic;
import org.bukkit.block.BlockType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

@NullMarked
public final class PaperStatistics implements Statistics {
    private final Map<Statistic, Stat> statistics;

    public PaperStatistics(final HashMap<Statistic, Stat> values) {
        this.statistics = values;
    }

    public PaperStatistics() {
        this(new HashMap<>());
    }

    @Override
    public @Unmodifiable Map<Statistic, Stat> getStatistics() {
        return Map.copyOf(statistics);
    }

    @Override
    public int getStatistic(final Statistic statistic) {
        Preconditions.checkState(statistic.getType() == Statistic.Type.UNTYPED, "Statistic type must be UNTYPED");
        return statistics.get(statistic) instanceof final CustomStat stat ? stat.getValue() : 0;
    }

    @Override
    public int getStatistic(final Statistic statistic, final BlockType type) {
        Preconditions.checkState(statistic.getType() == Statistic.Type.BLOCK, "Statistic type must be BLOCK");
        return statistics.get(statistic) instanceof final BlockTypeStat stat ? stat.getValue(type) : 0;
    }

    @Override
    public int getStatistic(final Statistic statistic, final EntityType type) {
        Preconditions.checkState(statistic.getType() == Statistic.Type.ENTITY, "Statistic type must be ENTITY");
        return statistics.get(statistic) instanceof final EntityTypeStat stat ? stat.getValue(type) : 0;
    }

    @Override
    public int getStatistic(final Statistic statistic, final ItemType type) {
        Preconditions.checkState(statistic.getType() == Statistic.Type.ITEM, "Statistic type must be ITEM");
        return statistics.get(statistic) instanceof final ItemTypeStat stat ? stat.getValue(type) : 0;
    }

    @Override
    public void setStatistic(final Statistic statistic, final BlockType type, final int value) {
        Preconditions.checkState(statistic.getType() == Statistic.Type.BLOCK, "Statistic type must be BLOCK");
        ((BlockTypeStat) statistics.computeIfAbsent(statistic, ignored -> new PaperBlockTypeStat())).setValue(type, value);
    }

    @Override
    public void setStatistic(final Statistic statistic, final EntityType type, final int value) {
        Preconditions.checkState(statistic.getType() == Statistic.Type.ENTITY, "Statistic type must be ENTITY");
        ((EntityTypeStat) statistics.computeIfAbsent(statistic, ignored -> new PaperEntityTypeStat())).setValue(type, value);
    }

    @Override
    public void setStatistic(final Statistic statistic, final ItemType type, final int value) {
        Preconditions.checkState(statistic.getType() == Statistic.Type.ITEM, "Statistic type must be ITEM");
        ((ItemTypeStat) statistics.computeIfAbsent(statistic, ignored -> new PaperItemTypeStat())).setValue(type, value);
    }

    @Override
    public void setStatistic(final Statistic statistic, final int value) {
        Preconditions.checkState(statistic.getType() == Statistic.Type.UNTYPED, "Statistic type must be UNTYPED");
        ((CustomStat) statistics.computeIfAbsent(statistic, ignored -> new PaperCustomStat())).setValue(value);
    }

    @Override
    public boolean hasData(final Statistic statistic) {
        final var stat = statistics.get(statistic);
        return stat != null && stat.hasData();
    }

    @Override
    public void forEachStatistic(final BiConsumer<Statistic, Stat> action) {
        statistics.forEach(action);
    }

    public static PaperStatistics of(final Player player) {
        final var stats = new PaperStatistics();
        Registry.STATISTIC.forEach(statistic -> {
            switch (statistic.getType()) {
                case UNTYPED -> stats.setStatistic(statistic, player.getStatistic(statistic));
                case ITEM -> Registry.ITEM.forEach(type -> {
                    final var material = Registry.MATERIAL.get(type.key());
                    if (material == null) return;
                    stats.setStatistic(statistic, type, player.getStatistic(statistic, material));
                });
                case BLOCK -> Registry.BLOCK.forEach(type -> {
                    final var material = Registry.MATERIAL.get(type.key());
                    if (material == null) return;
                    stats.setStatistic(statistic, type, player.getStatistic(statistic, material));
                });
                case ENTITY -> Registry.ENTITY_TYPE.forEach(type -> {
                    stats.setStatistic(statistic, type, player.getStatistic(statistic, type));
                });
            }
        });
        return stats;
    }
}
