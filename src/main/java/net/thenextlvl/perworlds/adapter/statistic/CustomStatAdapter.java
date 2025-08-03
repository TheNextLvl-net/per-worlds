package net.thenextlvl.perworlds.adapter.statistic;

import core.nbt.serialization.ParserException;
import core.nbt.serialization.TagAdapter;
import core.nbt.serialization.TagDeserializationContext;
import core.nbt.serialization.TagSerializationContext;
import core.nbt.tag.IntTag;
import core.nbt.tag.Tag;
import net.thenextlvl.perworlds.model.PaperCustomStat;
import net.thenextlvl.perworlds.statistics.CustomStat;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class CustomStatAdapter implements TagAdapter<CustomStat> {
    @Override
    public CustomStat deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        return new PaperCustomStat(tag.getAsInt());
    }

    @Override
    public Tag serialize(CustomStat stat, TagSerializationContext context) throws ParserException {
        return new IntTag(stat.getValue());
    }
}
