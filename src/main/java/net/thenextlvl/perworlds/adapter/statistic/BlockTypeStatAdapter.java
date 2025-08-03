package net.thenextlvl.perworlds.adapter.statistic;

import core.nbt.serialization.ParserException;
import core.nbt.serialization.TagDeserializationContext;
import core.nbt.tag.Tag;
import net.kyori.adventure.key.Key;
import net.thenextlvl.perworlds.model.PaperBlockTypeStat;
import net.thenextlvl.perworlds.statistics.BlockTypeStat;
import org.bukkit.Registry;
import org.bukkit.block.BlockType;
import org.jspecify.annotations.NullMarked;

import java.util.HashMap;

@NullMarked
public class BlockTypeStatAdapter extends TypedStatAdapter<BlockTypeStat> {
    @Override
    @SuppressWarnings("PatternValidation")
    public BlockTypeStat deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        var values = new HashMap<BlockType, Integer>();
        tag.getAsCompound().forEach((type, value) -> {
            var entity = Registry.BLOCK.getOrThrow(Key.key(type));
            values.put(entity, value.getAsInt());
        });
        return new PaperBlockTypeStat(values);
    }
}
