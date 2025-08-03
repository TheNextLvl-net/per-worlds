package net.thenextlvl.perworlds.adapter.statistic;

import core.nbt.serialization.ParserException;
import core.nbt.serialization.TagAdapter;
import core.nbt.serialization.TagSerializationContext;
import core.nbt.tag.CompoundTag;
import core.nbt.tag.Tag;
import net.thenextlvl.perworlds.statistics.Substatistic;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class TypedStatAdapter<T extends Substatistic<?>> implements TagAdapter<T> {
    @Override
    public Tag serialize(T stat, TagSerializationContext context) throws ParserException {
        var tag = new CompoundTag();
        stat.getValues().forEach((type, integer) -> {
            if (integer != 0) tag.add(type.key().asString(), integer);
        });
        return tag;
    }
}
