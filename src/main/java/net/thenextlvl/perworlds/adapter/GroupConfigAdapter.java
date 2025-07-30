package net.thenextlvl.perworlds.adapter;

import core.nbt.serialization.ParserException;
import core.nbt.serialization.TagAdapter;
import core.nbt.serialization.TagDeserializationContext;
import core.nbt.serialization.TagSerializationContext;
import core.nbt.tag.CompoundTag;
import core.nbt.tag.ListTag;
import core.nbt.tag.Tag;
import net.kyori.adventure.key.Key;
import net.thenextlvl.perworlds.GroupData;
import net.thenextlvl.perworlds.GroupSettings;
import net.thenextlvl.perworlds.group.PaperGroupData;
import net.thenextlvl.perworlds.group.PaperGroupSettings;
import net.thenextlvl.perworlds.model.config.GroupConfig;
import org.jspecify.annotations.NullMarked;

import java.util.HashSet;
import java.util.stream.Collectors;

@NullMarked
public class GroupConfigAdapter implements TagAdapter<GroupConfig> {
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
        var tag = new CompoundTag();
        tag.add("data", context.serialize(config.data()));
        tag.add("settings", context.serialize(config.settings()));
        if (!config.worlds().isEmpty()) tag.add("worlds", new ListTag<>(
                config.worlds().stream().map(context::serialize).toList()
        ));
        return tag;
    }
}
