package net.thenextlvl.perworlds.adapter.statistic;

import net.kyori.adventure.key.Key;
import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.model.PaperBlockTypeStat;
import net.thenextlvl.perworlds.statistics.BlockTypeStat;
import org.bukkit.Registry;
import org.bukkit.block.BlockType;
import org.jspecify.annotations.NullMarked;

import java.util.HashMap;

@NullMarked
public final class BlockTypeStatAdapter extends TypedStatAdapter<BlockTypeStat> {
    private final PerWorldsPlugin plugin;

    public BlockTypeStatAdapter(PerWorldsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    @SuppressWarnings("PatternValidation")
    public BlockTypeStat deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        var values = new HashMap<BlockType, Integer>();
        tag.getAsCompound().forEach((type, value) -> {
            var entity = Registry.BLOCK.get(Key.key(type));
            if (entity != null) values.put(entity, value.getAsInt());
            else plugin.getComponentLogger().warn("Failed to deserialize statistic: Unknown BlockType {}", type);
        });
        return new PaperBlockTypeStat(values);
    }
}
