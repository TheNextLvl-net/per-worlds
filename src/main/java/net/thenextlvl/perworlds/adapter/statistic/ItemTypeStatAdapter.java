package net.thenextlvl.perworlds.adapter.statistic;

import net.kyori.adventure.key.Key;
import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.perworlds.model.PaperItemTypeStat;
import net.thenextlvl.perworlds.statistics.ItemTypeStat;
import org.bukkit.Registry;
import org.bukkit.inventory.ItemType;
import org.jspecify.annotations.NullMarked;

import java.util.HashMap;

@NullMarked
public class ItemTypeStatAdapter extends TypedStatAdapter<ItemTypeStat> {
    @Override
    @SuppressWarnings("PatternValidation")
    public ItemTypeStat deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        var values = new HashMap<ItemType, Integer>();
        tag.getAsCompound().forEach((type, value) -> {
            var entity = Registry.ITEM.getOrThrow(Key.key(type));
            values.put(entity, value.getAsInt());
        });
        return new PaperItemTypeStat(values);
    }
}
