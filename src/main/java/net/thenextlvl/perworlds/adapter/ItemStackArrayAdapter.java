package net.thenextlvl.perworlds.adapter;

import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.StringTag;
import net.thenextlvl.nbt.tag.Tag;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Base64;

@NullMarked
public final class ItemStackArrayAdapter implements TagAdapter<@Nullable ItemStack[]> {
    @Override
    public @Nullable ItemStack[] deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        var bytes = Base64.getDecoder().decode(tag.getAsString());
        return ItemStack.deserializeItemsFromBytes(bytes);
    }

    @Override
    public Tag serialize(@Nullable ItemStack[] itemStacks, TagSerializationContext context) throws ParserException {
        var bytes = ItemStack.serializeItemsAsBytes(itemStacks);
        return StringTag.of(Base64.getEncoder().encodeToString(bytes));
    }
}
