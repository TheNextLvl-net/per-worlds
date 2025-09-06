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
public class AttributeDataAdapter implements TagAdapter<AttributeData> {
    @Override
    public AttributeData deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        var root = tag.getAsCompound();
        var attribute = context.deserialize(root.get("id"), Attribute.class);
        var value = root.get("base").getAsDouble();
        return AttributeData.of(attribute, value);
    }

    @Override
    public Tag serialize(AttributeData data, TagSerializationContext context) throws ParserException {
        var tag = CompoundTag.empty();
        tag.add("id", context.serialize(data.attribute()));
        tag.add("base", data.baseValue());
        return tag;
    }
}
