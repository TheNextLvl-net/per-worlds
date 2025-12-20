package net.thenextlvl.perworlds.adapter;

import net.kyori.adventure.key.Key;
import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.CompoundTag;
import net.thenextlvl.nbt.tag.ListTag;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.perworlds.GroupData;
import net.thenextlvl.perworlds.GroupSettings;
import net.thenextlvl.perworlds.group.PaperGroupData;
import net.thenextlvl.perworlds.group.PaperGroupSettings;
import net.thenextlvl.perworlds.model.config.GroupConfig;
import org.jspecify.annotations.NullMarked;

import java.util.HashSet;
import java.util.stream.Collectors;

@NullMarked
public final class GroupConfigAdapter implements TagAdapter<GroupConfig> {
    @Override
    public GroupConfig deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        var data = tag.getAsCompound().optional("data")
                .map(tag1 -> context.deserialize(tag1, GroupData.class))
                .orElseGet(PaperGroupData::new);
        var settings = tag.getAsCompound().optional("settings")
                .map(tag1 -> context.deserialize(tag1, GroupSettings.class))
                .orElseGet(PaperGroupSettings::new);
        var worlds = tag.getAsCompound().optional("worlds")
                .map(Tag::getAsList).map(tags -> tags.stream()
                        .map(world -> context.deserialize(world, Key.class))
                        .collect(Collectors.toSet()))
                .orElseGet(HashSet::new);
        return new GroupConfig(worlds, data, settings);
    }

    @Override
    public Tag serialize(GroupConfig config, TagSerializationContext context) throws ParserException {
        var tag = CompoundTag.builder();
        tag.put("data", context.serialize(config.data()));
        tag.put("settings", context.serialize(config.settings()));
        if (!config.worlds().isEmpty()) tag.put("worlds", ListTag.of(
                config.worlds().stream().map(context::serialize).toList()
        ));
        return tag.build();
    }
}
