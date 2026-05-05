package net.thenextlvl.perworlds.adapter.statistic;

import net.kyori.adventure.key.Key;
import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.model.PaperEntityTypeStat;
import net.thenextlvl.perworlds.statistics.EntityTypeStat;
import org.bukkit.Registry;
import org.bukkit.entity.EntityType;
import org.jspecify.annotations.NullMarked;

import java.util.HashMap;

@NullMarked
public final class EntityTypeStatAdapter extends TypedStatAdapter<EntityTypeStat> {
    private final PerWorldsPlugin plugin;

    public EntityTypeStatAdapter(final PerWorldsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    @SuppressWarnings("PatternValidation")
    public EntityTypeStat deserialize(final Tag tag, final TagDeserializationContext context) throws ParserException {
        final var values = new HashMap<EntityType, Integer>();
        tag.getAsCompound().forEach((type, value) -> {
            final var entity = Registry.ENTITY_TYPE.get(Key.key(type));
            if (entity != null) values.put(entity, value.getAsInt());
            else plugin.getComponentLogger().warn("Failed to deserialize statistic: Unknown EntityType {}", type);
        });
        return new PaperEntityTypeStat(values);
    }
}
