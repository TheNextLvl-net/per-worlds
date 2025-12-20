package net.thenextlvl.perworlds.adapter;

import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.CompoundTag;
import net.thenextlvl.nbt.tag.ListTag;
import net.thenextlvl.nbt.tag.StringTag;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.perworlds.data.AdvancementData;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.jspecify.annotations.NullMarked;

import java.time.Instant;
import java.util.HashMap;
import java.util.stream.Collectors;

@NullMarked
public final class AdvancementDataAdapter implements TagAdapter<AdvancementData> {
    private final Server server;

    public AdvancementDataAdapter(Server server) {
        this.server = server;
    }

    @Override
    public AdvancementData deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        var root = tag.getAsCompound();
        var key = context.deserialize(root.get("advancement"), NamespacedKey.class);
        var advancement = server.getAdvancement(key);
        if (advancement == null) throw new ParserException("Encountered unknown advancement: " + key);
        var awarded = new HashMap<String, Instant>();
        root.getAsCompound("awarded").forEach((criteria, instant) -> awarded.put(criteria, context.deserialize(instant, Instant.class)));
        var remaining = root.getAsList("remaining").stream().map(Tag::getAsString).collect(Collectors.toSet());
        return AdvancementData.of(advancement, awarded, remaining);
    }

    @Override
    public Tag serialize(AdvancementData data, TagSerializationContext context) throws ParserException {
        var tag = CompoundTag.builder();
        var awarded = CompoundTag.builder();
        data.forEachAwardedCriteria((criteria, date) -> {
            awarded.put(criteria, context.serialize(date));
        });
        tag.put("advancement", context.serialize(data.getAdvancement().key()));
        tag.put("awarded", awarded.build());
        tag.put("remaining", ListTag.of(StringTag.ID, data.getRemainingCriteria().stream().map(StringTag::of).toList()));
        return tag.build();
    }
}
