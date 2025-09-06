package net.thenextlvl.perworlds.adapter;

import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.CompoundTag;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.perworlds.data.WorldBorderData;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class WorldBorderAdapter implements TagAdapter<WorldBorderData> {
    @Override
    public WorldBorderData deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        var border = WorldBorderData.DEFAULT;
        var root = tag.getAsCompound();
        var x = root.optional("x").map(Tag::getAsDouble)
                .map(value -> Math.clamp(value, -WorldBorderData.getMaxCenterCoordinate(), WorldBorderData.getMaxCenterCoordinate()))
                .orElse(border.centerX());
        var z = root.optional("z").map(Tag::getAsDouble)
                .map(value -> Math.clamp(value, -WorldBorderData.getMaxCenterCoordinate(), WorldBorderData.getMaxCenterCoordinate()))
                .orElse(border.centerZ());
        var size = root.optional("size").map(Tag::getAsDouble)
                .map(value -> Math.clamp(value, WorldBorderData.getMinSize(), WorldBorderData.getMaxSize()))
                .orElse(border.size());
        var duration = root.optional("duration").map(Tag::getAsLong).orElse(border.duration());
        var damageAmount = root.optional("damageAmount").map(Tag::getAsDouble).orElse(border.damageAmount());
        var damageBuffer = root.optional("damageBuffer").map(Tag::getAsDouble).orElse(border.damageBuffer());
        var warningDistance = root.optional("warningDistance").map(Tag::getAsInt).orElse(border.warningDistance());
        var warningTime = root.optional("warningTime").map(Tag::getAsInt).orElse(border.warningTime());
        return WorldBorderData.create(x, z, size, damageAmount, damageBuffer, duration, warningDistance, warningTime);
    }

    @Override
    public Tag serialize(WorldBorderData data, TagSerializationContext context) throws ParserException {
        var tag = CompoundTag.empty();
        tag.add("x", data.centerX());
        tag.add("z", data.centerZ());
        tag.add("size", data.size());
        tag.add("duration", data.duration());
        tag.add("damageAmount", data.damageAmount());
        tag.add("damageBuffer", data.damageBuffer());
        tag.add("warningDistance", data.warningDistance());
        tag.add("warningTime", data.warningTime());
        return tag;
    }
}
