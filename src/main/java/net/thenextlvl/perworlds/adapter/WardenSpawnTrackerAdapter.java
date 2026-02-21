package net.thenextlvl.perworlds.adapter;

import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.CompoundTag;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.perworlds.data.WardenSpawnTracker;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class WardenSpawnTrackerAdapter implements TagAdapter<WardenSpawnTracker> {
    @Override
    public WardenSpawnTracker deserialize(final Tag tag, final TagDeserializationContext context) throws ParserException {
        final var root = tag.getAsCompound();
        final var cooldownTicks = root.get("cooldownTicks").getAsInt();
        final var ticksSinceLastWarning = root.get("ticksSinceLastWarning").getAsInt();
        final var warningLevel = root.get("warningLevel").getAsInt();
        return WardenSpawnTracker.create(cooldownTicks, ticksSinceLastWarning, warningLevel);
    }

    @Override
    public Tag serialize(final WardenSpawnTracker tracker, final TagSerializationContext context) throws ParserException {
        final var tag = CompoundTag.builder();
        tag.put("cooldownTicks", tracker.cooldownTicks());
        tag.put("ticksSinceLastWarning", tracker.ticksSinceLastWarning());
        tag.put("warningLevel", tracker.warningLevel());
        return tag.build();
    }
}
