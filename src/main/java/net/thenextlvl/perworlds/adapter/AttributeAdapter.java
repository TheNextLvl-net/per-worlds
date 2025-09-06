package net.thenextlvl.perworlds.adapter;

import net.kyori.adventure.key.Key;
import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.Tag;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class AttributeAdapter implements TagAdapter<Attribute> {
    @Override
    public Attribute deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        return Registry.ATTRIBUTE.getOrThrow(context.deserialize(tag, Key.class));
    }

    @Override
    public Tag serialize(Attribute attribute, TagSerializationContext context) throws ParserException {
        return context.serialize(attribute.key());
    }
}
