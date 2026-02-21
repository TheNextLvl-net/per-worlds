package net.thenextlvl.perworlds.adapter;

import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.CompoundTag;
import net.thenextlvl.nbt.tag.Tag;
import org.bukkit.util.Vector;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class VectorAdapter implements TagAdapter<Vector> {
    @Override
    public Vector deserialize(final Tag tag, final TagDeserializationContext context) throws ParserException {
        final var root = tag.getAsCompound();
        final var x = root.get("x").getAsDouble();
        final var y = root.get("y").getAsDouble();
        final var z = root.get("z").getAsDouble();
        return new Vector(x, y, z);
    }

    @Override
    public Tag serialize(final Vector vector, final TagSerializationContext context) throws ParserException {
        return CompoundTag.builder()
                .put("x", vector.getX())
                .put("y", vector.getY())
                .put("z", vector.getZ())
                .build();
    }
}
