package net.thenextlvl.perworlds.adapter;

import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.LongTag;
import net.thenextlvl.nbt.tag.Tag;
import org.jspecify.annotations.NullMarked;

import java.time.Instant;

@NullMarked
public final class InstantAdapter implements TagAdapter<Instant> {
    @Override
    public Instant deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        return Instant.ofEpochMilli(tag.getAsLong());
    }

    @Override
    public Tag serialize(Instant instant, TagSerializationContext context) throws ParserException {
        return LongTag.of(instant.toEpochMilli());
    }
}
