package net.thenextlvl.perworlds.adapter.statistic;

import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.IntTag;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.perworlds.model.PaperCustomStat;
import net.thenextlvl.perworlds.statistics.CustomStat;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class CustomStatAdapter implements TagAdapter<CustomStat> {
    @Override
    public CustomStat deserialize(final Tag tag, final TagDeserializationContext context) throws ParserException {
        return new PaperCustomStat(tag.getAsInt());
    }

    @Override
    public Tag serialize(final CustomStat stat, final TagSerializationContext context) throws ParserException {
        return IntTag.of(stat.getValue());
    }
}
