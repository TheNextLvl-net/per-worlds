package net.thenextlvl.perworlds.adapter;

import net.kyori.adventure.key.Key;
import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.Tag;
import org.bukkit.Registry;
import org.bukkit.potion.PotionEffectType;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PotionEffectTypeAdapter implements TagAdapter<PotionEffectType> {
    @Override
    public PotionEffectType deserialize(final Tag tag, final TagDeserializationContext context) throws ParserException {
        return Registry.MOB_EFFECT.getOrThrow(context.deserialize(tag, Key.class));
    }

    @Override
    public Tag serialize(final PotionEffectType effectType, final TagSerializationContext context) throws ParserException {
        return context.serialize(effectType.key());
    }
}
