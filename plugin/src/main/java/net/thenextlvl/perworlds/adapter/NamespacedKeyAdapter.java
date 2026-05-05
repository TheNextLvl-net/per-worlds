package net.thenextlvl.perworlds.adapter;

import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.StringTag;
import net.thenextlvl.nbt.tag.Tag;
import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@NullMarked
public final class NamespacedKeyAdapter implements TagAdapter<NamespacedKey> {
    @Override
    public NamespacedKey deserialize(final Tag tag, final TagDeserializationContext context) throws ParserException {
        return Objects.requireNonNull(NamespacedKey.fromString(tag.getAsString()), "Encountered invalid namespaced key: " + tag.getAsString());
    }

    @Override
    public Tag serialize(final NamespacedKey key, final TagSerializationContext context) throws ParserException {
        return StringTag.of(key.asString());
    }
}
