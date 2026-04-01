package net.thenextlvl.perworlds.adapter;

import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.StringTag;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Base64;

@NullMarked
public final class ItemStackArrayAdapter implements TagAdapter<@Nullable ItemStack[]> {
    private final PerWorldsPlugin plugin;

    public ItemStackArrayAdapter(final PerWorldsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable ItemStack[] deserialize(final Tag tag, final TagDeserializationContext context) throws ParserException {
        final var bytes = Base64.getDecoder().decode(tag.getAsString());
        return deserializeItemsFromBytes(bytes);
    }

    @Override
    public Tag serialize(@Nullable final ItemStack[] itemStacks, final TagSerializationContext context) throws ParserException {
        final var bytes = ItemStack.serializeItemsAsBytes(itemStacks);
        return StringTag.of(Base64.getEncoder().encodeToString(bytes));
    }

    /**
     * @see ItemStack#ARRAY_SERIALIZATION_VERSION
     */
    @SuppressWarnings("JavadocReference")
    private static final int ARRAY_SERIALIZATION_VERSION = 1;

    /**
     * @see ItemStack#deserializeItemsFromBytes(byte[])
     */
    private ItemStack[] deserializeItemsFromBytes(final byte[] bytes) throws ParserException {
        try (final var inputStream = new ByteArrayInputStream(bytes)) {
            final var input = new DataInputStream(inputStream);
            final byte version = input.readByte();
            if (version != ARRAY_SERIALIZATION_VERSION) {
                throw new ParserException("Unsupported version or bad data: " + version);
            }

            final var count = input.readInt();
            final var items = new ItemStack[count];
            for (var i = 0; i < count; i++) {
                final var length = input.readInt();
                if (length == 0) {
                    items[i] = ItemStack.empty();
                    continue;
                }

                final var itemBytes = new byte[length];
                input.readFully(itemBytes);
                try {
                    items[i] = ItemStack.deserializeBytes(itemBytes);
                } catch (final Exception e) {
                    plugin.getComponentLogger().warn("Error while deserializing item from byte array", e);
                    items[i] = ItemStack.empty();
                }
            }
            return items;
        } catch (final IOException e) {
            throw new ParserException("Error while reading itemstack", e);
        }
    }
}
