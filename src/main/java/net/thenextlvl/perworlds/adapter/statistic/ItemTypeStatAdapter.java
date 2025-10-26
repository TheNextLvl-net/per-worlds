package net.thenextlvl.perworlds.adapter.statistic;

import net.kyori.adventure.key.Key;
import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.model.PaperItemTypeStat;
import net.thenextlvl.perworlds.statistics.ItemTypeStat;
import org.bukkit.Registry;
import org.bukkit.inventory.ItemType;
import org.jspecify.annotations.NullMarked;

import java.util.HashMap;

@NullMarked
public final class ItemTypeStatAdapter extends TypedStatAdapter<ItemTypeStat> {
    private final PerWorldsPlugin plugin;

    public ItemTypeStatAdapter(PerWorldsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    @SuppressWarnings("PatternValidation")
    public ItemTypeStat deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        var values = new HashMap<ItemType, Integer>();
        tag.getAsCompound().forEach((type, value) -> {
            var item = Registry.ITEM.get(Key.key(type));
            if (item != null) values.put(item, value.getAsInt());
            else plugin.getComponentLogger().warn("Failed to deserialize statistic: Unknown ItemType {}", type);
        });
        return new PaperItemTypeStat(values);
    }
}
