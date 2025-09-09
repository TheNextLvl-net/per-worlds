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
    public WardenSpawnTracker deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        var root = tag.getAsCompound();
        var cooldownTicks = root.get("cooldownTicks").getAsInt();
        var ticksSinceLastWarning = root.get("ticksSinceLastWarning").getAsInt();
        var warningLevel = root.get("warningLevel").getAsInt();
        return WardenSpawnTracker.create(cooldownTicks, ticksSinceLastWarning, warningLevel);
    }

    @Override
    public Tag serialize(WardenSpawnTracker tracker, TagSerializationContext context) throws ParserException {
        var tag = CompoundTag.empty();
        tag.add("cooldownTicks", tracker.cooldownTicks());
        tag.add("ticksSinceLastWarning", tracker.ticksSinceLastWarning());
        tag.add("warningLevel", tracker.warningLevel());
        return tag;
    }
}
