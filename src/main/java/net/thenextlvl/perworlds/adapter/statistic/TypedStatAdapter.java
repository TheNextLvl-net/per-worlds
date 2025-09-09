package net.thenextlvl.perworlds.adapter.statistic;

import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.CompoundTag;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.perworlds.statistics.Substatistic;
import org.jspecify.annotations.NullMarked;

@NullMarked
abstract class TypedStatAdapter<T extends Substatistic<?>> implements TagAdapter<T> {
    @Override
    public Tag serialize(T stat, TagSerializationContext context) throws ParserException {
        var tag = CompoundTag.empty();
        stat.getValues().forEach((type, integer) -> {
            if (integer != 0) tag.add(type.key().asString(), integer);
        });
        return tag;
    }
}
