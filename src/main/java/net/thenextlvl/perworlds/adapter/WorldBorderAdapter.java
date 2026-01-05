package net.thenextlvl.perworlds.adapter;

import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.CompoundTag;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.perworlds.data.WorldBorderData;
import org.jspecify.annotations.NullMarked;

import java.time.Duration;

@NullMarked
public final class WorldBorderAdapter implements TagAdapter<WorldBorderData> {
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
        var duration = root.optional("duration").map(tag1 -> context.deserialize(tag1, Duration.class)).orElse(border.getTransitionDuration());
        var damageAmount = root.optional("damageAmount").map(Tag::getAsDouble).orElse(border.damageAmount());
        var damageBuffer = root.optional("damageBuffer").map(Tag::getAsDouble).orElse(border.damageBuffer());
        var warningDistance = root.optional("warningDistance").map(Tag::getAsInt).orElse(border.warningDistance());
        var warningTime = root.optional("warningTime").map(tag1 -> context.deserialize(tag1, Duration.class)).orElse(border.getWarningTime());
        return WorldBorderData.create(x, z, size, damageAmount, damageBuffer, duration, warningDistance, warningTime);
    }

    @Override
    public Tag serialize(WorldBorderData data, TagSerializationContext context) throws ParserException {
        return CompoundTag.builder()
                .put("x", data.centerX())
                .put("z", data.centerZ())
                .put("size", data.size())
                .put("duration", context.serialize(data.getTransitionDuration()))
                .put("damageAmount", data.damageAmount())
                .put("damageBuffer", data.damageBuffer())
                .put("warningDistance", data.warningDistance())
                .put("warningTime", context.serialize(data.getWarningTime()))
                .build();
    }
}
