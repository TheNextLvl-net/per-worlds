package net.thenextlvl.perworlds.adapter;

import core.nbt.serialization.ParserException;
import core.nbt.serialization.TagAdapter;
import core.nbt.serialization.TagDeserializationContext;
import core.nbt.serialization.TagSerializationContext;
import core.nbt.tag.ByteTag;
import core.nbt.tag.CompoundTag;
import core.nbt.tag.ListTag;
import core.nbt.tag.StringTag;
import core.nbt.tag.Tag;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.util.TriState;
import net.thenextlvl.perworlds.data.AdvancementData;
import net.thenextlvl.perworlds.data.AttributeData;
import net.thenextlvl.perworlds.data.PlayerData;
import net.thenextlvl.perworlds.data.WardenSpawnTracker;
import net.thenextlvl.perworlds.group.PaperGroupProvider;
import net.thenextlvl.perworlds.model.PaperPlayerData;
import net.thenextlvl.perworlds.statistics.Stats;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@NullMarked
public class PlayerDataAdapter implements TagAdapter<PlayerData> {
    private final PaperGroupProvider provider;

    public PlayerDataAdapter(PaperGroupProvider provider) {
        this.provider = provider;
    }

    @Override
    public PlayerData deserialize(Tag tag, TagDeserializationContext context) throws ParserException {
        var data = new PaperPlayerData(null);
        var root = tag.getAsCompound();
        root.optional("advancements").map(Tag::getAsList).map(list ->
                list.stream().map(advancement -> {
                    try {
                        return context.deserialize(advancement, AdvancementData.class);
                    } catch (ParserException e) {
                        provider.getLogger().warn(e.getMessage());
                        return null;
                    }
                }).filter(Objects::nonNull).toList()
        ).ifPresent(data::advancements);
        root.optional("attributes").map(Tag::getAsList).map(list ->
                list.stream().map(attribute -> context.deserialize(attribute, AttributeData.class)).toList()
        ).ifPresent(data::attributes);
        root.optional("enderChest").map(items -> context.deserialize(items, ItemStack[].class)).ifPresent(data::enderChest);
        root.optional("inventory").map(items -> context.deserialize(items, ItemStack[].class)).ifPresent(data::inventory);
        root.optional("respawnLocation").map(location -> {
            try {
                return context.deserialize(location, Location.class);
            } catch (ParserException e) {
                return null;
            }
        }).ifPresent(data::respawnLocation);
        root.optional("recipes").map(Tag::getAsList).map(list ->
                list.stream().map(recipe -> context.deserialize(recipe, NamespacedKey.class)).toList()
        ).ifPresent(data::discoveredRecipes);
        root.optional("potionEffects").map(Tag::getAsList).map(list ->
                list.stream().map(effect -> context.deserialize(effect, PotionEffect.class)).toList()
        ).ifPresent(data::potionEffects);
        root.optional("lastAdvancementTab").map(advancement ->
                context.deserialize(advancement, Key.class)
        ).ifPresent(data::lastAdvancementTab);
        root.optional("statistics").map(stats -> context.deserialize(stats, Stats.class)).ifPresent(data::stats);
        root.optional("gameMode").map(mode -> context.deserialize(mode, GameMode.class)).ifPresent(data::gameMode);
        root.optional("seenCredits").map(Tag::getAsBoolean).ifPresent(data::seenCredits);
        root.optional("absorption").map(Tag::getAsDouble).ifPresent(data::absorption);
        root.optional("health").map(Tag::getAsDouble).ifPresent(data::health);
        root.optional("exhaustion").map(Tag::getAsFloat).ifPresent(data::exhaustion);
        root.optional("experience").map(Tag::getAsFloat).ifPresent(data::experience);
        root.optional("fallDistance").map(Tag::getAsFloat).ifPresent(data::fallDistance);
        root.optional("saturation").map(Tag::getAsFloat).ifPresent(data::saturation);
        root.optional("arrowsInBody").map(Tag::getAsInt).ifPresent(data::arrowsInBody);
        root.optional("beeStingersInBody").map(Tag::getAsInt).ifPresent(data::beeStingersInBody);
        root.optional("fireTicks").map(Tag::getAsInt).ifPresent(data::fireTicks);
        root.optional("foodLevel").map(Tag::getAsInt).ifPresent(data::foodLevel);
        root.optional("mayFly").map(tag1 -> context.deserialize(tag1, TriState.class)).ifPresent(data::mayFly);
        root.optional("flying").map(tag1 -> context.deserialize(tag1, TriState.class)).ifPresent(data::flying);
        root.optional("freezeTicks").map(Tag::getAsInt).ifPresent(data::freezeTicks);
        root.optional("lockFreezeTicks").map(Tag::getAsBoolean).ifPresent(data::lockFreezeTicks);
        root.optional("visualFire").map(tag1 -> tag1 instanceof ByteTag byteTag
                ? (byteTag.getAsBoolean() ? TriState.TRUE : TriState.NOT_SET)
                : context.deserialize(tag1, TriState.class)).ifPresent(data::visualFire);
        root.optional("heldItemSlot").map(Tag::getAsInt).ifPresent(data::heldItemSlot);
        root.optional("level").map(Tag::getAsInt).ifPresent(data::level);
        root.optional("remainingAir").map(Tag::getAsInt).ifPresent(data::remainingAir);
        root.optional("score").map(Tag::getAsInt).ifPresent(data::score);
        root.optional("previousGameMode").map(mode -> context.deserialize(mode, GameMode.class)).ifPresent(data::previousGameMode);
        root.optional("lastDeathLocation").map(location -> {
            try {
                return context.deserialize(location, Location.class);
            } catch (ParserException e) {
                return null;
            }
        }).ifPresent(data::lastDeathLocation);
        root.optional("lastLocation").map(location -> {
            try {
                return context.deserialize(location, Location.class);
            } catch (ParserException e) {
                return null;
            }
        }).ifPresent(data::lastLocation);
        root.optional("gliding").map(Tag::getAsBoolean).ifPresent(data::gliding);
        root.optional("invulnerable").map(Tag::getAsBoolean).ifPresent(data::invulnerable);
        root.optional("portalCooldown").map(Tag::getAsInt).ifPresent(data::portalCooldown);
        root.optional("velocity").map(velocity -> context.deserialize(velocity, Vector.class)).ifPresent(data::velocity);
        root.optional("wardenSpawnTracker").map(tracker -> context.deserialize(tracker, WardenSpawnTracker.class)).ifPresent(data::wardenSpawnTracker);
        return data;
    }

    @Override
    public CompoundTag serialize(PlayerData data, TagSerializationContext context) throws ParserException {
        var tag = new CompoundTag();
        tag.add("advancements", new ListTag<>(data.advancements().stream().map(context::serialize).toList(), CompoundTag.ID));
        tag.add("attributes", new ListTag<>(data.attributes().stream().map(context::serialize).toList(), CompoundTag.ID));
        tag.add("enderChest", context.serialize(data.enderChest()));
        tag.add("inventory", context.serialize(data.inventory()));
        var lastAdvancementTab = data.lastAdvancementTab();
        if (lastAdvancementTab != null) tag.add("lastAdvancementTab", context.serialize(lastAdvancementTab));
        var respawnLocation = data.respawnLocation();
        if (respawnLocation != null) tag.add("respawnLocation", context.serialize(respawnLocation));
        var previousGameMode = data.previousGameMode();
        if (previousGameMode != null) tag.add("previousGameMode", context.serialize(previousGameMode));
        var lastDeathLocation = data.lastDeathLocation();
        if (lastDeathLocation != null) tag.add("lastDeathLocation", context.serialize(lastDeathLocation));
        var lastLocation = data.lastLocation();
        if (lastLocation != null) tag.add("lastLocation", context.serialize(lastLocation));
        var gameMode = data.gameMode();
        if (gameMode != null) tag.add("gameMode", context.serialize(gameMode));
        tag.add("recipes", new ListTag<>(data.discoveredRecipes().stream().map(context::serialize).toList(), StringTag.ID));
        tag.add("potionEffects", new ListTag<>(data.potionEffects().stream().map(context::serialize).toList(), CompoundTag.ID));
        tag.add("statistics", context.serialize(data.stats()));
        tag.add("seenCredits", data.seenCredits());
        tag.add("absorption", data.absorption());
        tag.add("mayFly", context.serialize(data.mayFly()));
        tag.add("flying", context.serialize(data.flying()));
        tag.add("health", data.health());
        tag.add("exhaustion", data.exhaustion());
        tag.add("lockFreezeTicks", data.lockFreezeTicks());
        tag.add("visualFire", context.serialize(data.visualFire()));
        tag.add("experience", data.experience());
        tag.add("gliding", data.gliding());
        tag.add("invulnerable", data.invulnerable());
        tag.add("portalCooldown", data.portalCooldown());
        tag.add("velocity", context.serialize(data.velocity()));
        tag.add("wardenSpawnTracker", context.serialize(data.wardenSpawnTracker()));
        tag.add("fallDistance", data.fallDistance());
        tag.add("saturation", data.saturation());
        tag.add("flySpeed", data.flySpeed());
        tag.add("walkSpeed", data.walkSpeed());
        tag.add("arrowsInBody", data.arrowsInBody());
        tag.add("beeStingersInBody", data.beeStingersInBody());
        tag.add("fireTicks", data.fireTicks());
        tag.add("foodLevel", data.foodLevel());
        tag.add("freezeTicks", data.freezeTicks());
        tag.add("heldItemSlot", data.heldItemSlot());
        tag.add("level", data.level());
        tag.add("remainingAir", data.remainingAir());
        tag.add("score", data.score());
        return tag;
    }
}
