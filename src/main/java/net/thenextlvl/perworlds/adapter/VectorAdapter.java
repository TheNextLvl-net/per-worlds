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
public class VectorAdapter implements TagAdapter<Vector> {
    @Override
    public Vector deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        var root = tag.getAsCompound();
        var x = root.get("x").getAsDouble();
        var y = root.get("y").getAsDouble();
        var z = root.get("z").getAsDouble();
        return new Vector(x, y, z);
    }

    @Override
    public Tag serialize(Vector vector, TagSerializationContext context) throws ParserException {
        var tag = CompoundTag.empty();
        tag.add("x", vector.getX());
        tag.add("y", vector.getY());
        tag.add("z", vector.getZ());
        return tag;
    }
}
