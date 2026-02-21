package net.thenextlvl.perworlds.adapter;

import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.CompoundTag;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.perworlds.data.AttributeData;
import org.bukkit.attribute.Attribute;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class AttributeDataAdapter implements TagAdapter<AttributeData> {
    @Override
    public AttributeData deserialize(final Tag tag, final TagDeserializationContext context) throws ParserException {
        final var root = tag.getAsCompound();
        final var attribute = context.deserialize(root.get("id"), Attribute.class);
        final var value = root.get("base").getAsDouble();
        return AttributeData.of(attribute, value);
    }

    @Override
    public Tag serialize(final AttributeData data, final TagSerializationContext context) throws ParserException {
        return CompoundTag.builder()
                .put("id", context.serialize(data.attribute()))
                .put("base", data.baseValue())
                .build();
    }
}
