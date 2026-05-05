package net.thenextlvl.perworlds.adapter;

import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.CompoundTag;
import net.thenextlvl.nbt.tag.Tag;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PotionEffectAdapter implements TagAdapter<PotionEffect> {
    @Override
    public PotionEffect deserialize(final Tag tag, final TagDeserializationContext context) throws ParserException {
        final var root = tag.getAsCompound();
        final var type = context.deserialize(root.get("type"), PotionEffectType.class);
        final var duration = root.get("duration").getAsInt();
        final var amplifier = root.get("amplifier").getAsInt();
        final var ambient = root.get("ambient").getAsBoolean();
        final var particles = root.get("particles").getAsBoolean();
        final var icon = root.get("icon").getAsBoolean();
        return new PotionEffect(type, duration, amplifier, ambient, particles, icon);
    }

    @Override
    public Tag serialize(final PotionEffect effect, final TagSerializationContext context) throws ParserException {
        return CompoundTag.builder()
                .put("type", context.serialize(effect.getType()))
                .put("duration", effect.getDuration())
                .put("amplifier", effect.getAmplifier())
                .put("ambient", effect.isAmbient())
                .put("particles", effect.hasParticles())
                .put("icon", effect.hasIcon())
                .build();
    }
}
