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
    public PotionEffect deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        var root = tag.getAsCompound();
        var type = context.deserialize(root.get("type"), PotionEffectType.class);
        var duration = root.get("duration").getAsInt();
        var amplifier = root.get("amplifier").getAsInt();
        var ambient = root.get("ambient").getAsBoolean();
        var particles = root.get("particles").getAsBoolean();
        var icon = root.get("icon").getAsBoolean();
        return new PotionEffect(type, duration, amplifier, ambient, particles, icon);
    }

    @Override
    public Tag serialize(PotionEffect effect, TagSerializationContext context) throws ParserException {
        var tag = CompoundTag.empty();
        tag.add("type", context.serialize(effect.getType()));
        tag.add("duration", effect.getDuration());
        tag.add("amplifier", effect.getAmplifier());
        tag.add("ambient", effect.isAmbient());
        tag.add("particles", effect.hasParticles());
        tag.add("icon", effect.hasIcon());
        return tag;
    }
}
