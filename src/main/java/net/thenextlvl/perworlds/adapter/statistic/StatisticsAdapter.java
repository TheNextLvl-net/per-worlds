package net.thenextlvl.perworlds.adapter.statistic;

import net.kyori.adventure.key.Key;
import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.CompoundTag;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.model.PaperStatistics;
import net.thenextlvl.perworlds.statistics.BlockTypeStat;
import net.thenextlvl.perworlds.statistics.CustomStat;
import net.thenextlvl.perworlds.statistics.EntityTypeStat;
import net.thenextlvl.perworlds.statistics.ItemTypeStat;
import net.thenextlvl.perworlds.statistics.Stat;
import net.thenextlvl.perworlds.statistics.Statistics;
import org.bukkit.Registry;
import org.bukkit.Statistic;
import org.jspecify.annotations.NullMarked;

import java.util.HashMap;

@NullMarked
public final class StatisticsAdapter implements TagAdapter<Statistics> {
    private final PerWorldsPlugin plugin;

    public StatisticsAdapter(PerWorldsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    @SuppressWarnings("PatternValidation")
    public Statistics deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        var values = new HashMap<Statistic, Stat>();
        tag.getAsCompound().forEach((key, value) -> {
            var statistic = Registry.STATISTIC.get(Key.key(key));
            if (statistic == null) plugin.getLogger().warning("Unknown statistic: " + key);
            else values.put(statistic, context.deserialize(value, getType(statistic)));
        });
        return new PaperStatistics(values);
    }

    @Override
    public Tag serialize(Statistics statistics, TagSerializationContext context) throws ParserException {
        var tag = CompoundTag.empty();
        statistics.forEachStatistic((statistic, value) -> {
            if (value.hasData()) tag.add(statistic.key().asString(), context.serialize(value));
        });
        return tag;
    }

    private static Class<? extends Stat> getType(Statistic statistic) {
        return switch (statistic.getType()) {
            case UNTYPED -> CustomStat.class;
            case ITEM -> ItemTypeStat.class;
            case BLOCK -> BlockTypeStat.class;
            case ENTITY -> EntityTypeStat.class;
        };
    }
}
