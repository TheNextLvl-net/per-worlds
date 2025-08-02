package net.thenextlvl.perworlds.model;

import core.nbt.serialization.ParserException;
import core.nbt.tag.Tag;
import net.kyori.adventure.key.Key;
import net.thenextlvl.perworlds.statistics.BlockTypeStat;
import org.bukkit.Registry;
import org.bukkit.block.BlockType;
import org.jspecify.annotations.NullMarked;

import java.util.function.BiConsumer;

@NullMarked
public class PaperBlockTypeStat extends PaperStat<BlockType> implements BlockTypeStat {
    @Override
    @SuppressWarnings("PatternValidation")
    public void deserialize(Tag tag) throws ParserException {
        tag.getAsCompound().forEach((type, value) -> {
            var entity = Registry.BLOCK.getOrThrow(Key.key(type));
            values.put(entity, value.getAsInt());
        });
    }

    @Override
    public void forEachValue(BiConsumer<BlockType, Integer> action) {
        values.forEach(action);
    }
}
