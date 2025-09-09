package net.thenextlvl.perworlds.adapter.statistic;

import net.kyori.adventure.key.Key;
import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.perworlds.model.PaperEntityTypeStat;
import net.thenextlvl.perworlds.statistics.EntityTypeStat;
import org.bukkit.Registry;
import org.bukkit.entity.EntityType;
import org.jspecify.annotations.NullMarked;

import java.util.HashMap;

@NullMarked
public final class EntityTypeStatAdapter extends TypedStatAdapter<EntityTypeStat> {
    @Override
    @SuppressWarnings("PatternValidation")
    public EntityTypeStat deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        var values = new HashMap<EntityType, Integer>();
        tag.getAsCompound().forEach((type, value) -> {
            var entity = Registry.ENTITY_TYPE.getOrThrow(Key.key(type));
            values.put(entity, value.getAsInt());
        });
        return new PaperEntityTypeStat(values);
    }
}
