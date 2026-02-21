package net.thenextlvl.perworlds.adapter;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.util.TriState;
import net.thenextlvl.nbt.serialization.ParserException;
import net.thenextlvl.nbt.serialization.TagAdapter;
import net.thenextlvl.nbt.serialization.TagDeserializationContext;
import net.thenextlvl.nbt.serialization.TagSerializationContext;
import net.thenextlvl.nbt.tag.ByteTag;
import net.thenextlvl.nbt.tag.CompoundTag;
import net.thenextlvl.nbt.tag.ListTag;
import net.thenextlvl.nbt.tag.StringTag;
import net.thenextlvl.nbt.tag.Tag;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.data.AdvancementData;
import net.thenextlvl.perworlds.data.AttributeData;
import net.thenextlvl.perworlds.data.PlayerData;
import net.thenextlvl.perworlds.data.WardenSpawnTracker;
import net.thenextlvl.perworlds.model.PaperPlayerData;
import net.thenextlvl.perworlds.statistics.Statistics;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@NullMarked
public final class PlayerDataAdapter implements TagAdapter<PlayerData> {
    private final PerWorldsPlugin plugin;

    public PlayerDataAdapter(final PerWorldsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public PlayerData deserialize(final Tag tag, final TagDeserializationContext context) throws ParserException {
        final var data = new PaperPlayerData(null, null);
        final var root = tag.getAsCompound();
        root.optional("advancements").map(Tag::getAsList).map(list ->
                list.stream().map(advancement -> {
                    try {
                        return context.deserialize(advancement, AdvancementData.class);
                    } catch (final ParserException e) {
                        plugin.getComponentLogger().warn(e.getMessage());
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
            } catch (final ParserException e) {
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
        root.optional("statistics").map(stats -> context.deserialize(stats, Statistics.class)).ifPresent(data::stats);
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
        root.optional("visualFire").map(tag1 -> tag1 instanceof final ByteTag byteTag
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
            } catch (final ParserException e) {
                return null;
            }
        }).ifPresent(data::lastDeathLocation);
        root.optional("lastLocation").map(location -> {
            try {
                return context.deserialize(location, Location.class);
            } catch (final ParserException e) {
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
    public CompoundTag serialize(final PlayerData data, final TagSerializationContext context) throws ParserException {
        final var tag = CompoundTag.builder();
        tag.put("advancements", ListTag.of(CompoundTag.ID, data.advancements().stream().map(context::serialize).toList()));
        tag.put("attributes", ListTag.of(CompoundTag.ID, data.attributes().stream().map(context::serialize).toList()));
        tag.put("enderChest", context.serialize(data.enderChest()));
        tag.put("inventory", context.serialize(data.inventory()));
        final var lastAdvancementTab = data.lastAdvancementTab();
        if (lastAdvancementTab != null) tag.put("lastAdvancementTab", context.serialize(lastAdvancementTab));
        final var respawnLocation = data.respawnLocation();
        if (respawnLocation != null) tag.put("respawnLocation", context.serialize(respawnLocation));
        final var previousGameMode = data.previousGameMode();
        if (previousGameMode != null) tag.put("previousGameMode", context.serialize(previousGameMode));
        final var lastDeathLocation = data.lastDeathLocation();
        if (lastDeathLocation != null) tag.put("lastDeathLocation", context.serialize(lastDeathLocation));
        final var lastLocation = data.lastLocation();
        if (lastLocation != null) tag.put("lastLocation", context.serialize(lastLocation));
        final var gameMode = data.gameMode();
        if (gameMode != null) tag.put("gameMode", context.serialize(gameMode));
        tag.put("recipes", ListTag.of(StringTag.ID, data.discoveredRecipes().stream().map(context::serialize).toList()));
        tag.put("potionEffects", ListTag.of(CompoundTag.ID, data.potionEffects().stream().map(context::serialize).toList()));
        tag.put("statistics", context.serialize(data.stats()));
        tag.put("seenCredits", data.seenCredits());
        tag.put("absorption", data.absorption());
        tag.put("mayFly", context.serialize(data.mayFly()));
        tag.put("flying", context.serialize(data.flying()));
        tag.put("health", data.health());
        tag.put("exhaustion", data.exhaustion());
        tag.put("lockFreezeTicks", data.lockFreezeTicks());
        tag.put("visualFire", context.serialize(data.visualFire()));
        tag.put("experience", data.experience());
        tag.put("gliding", data.gliding());
        tag.put("invulnerable", data.invulnerable());
        tag.put("portalCooldown", data.portalCooldown());
        tag.put("velocity", context.serialize(data.velocity()));
        tag.put("wardenSpawnTracker", context.serialize(data.wardenSpawnTracker()));
        tag.put("fallDistance", data.fallDistance());
        tag.put("saturation", data.saturation());
        tag.put("flySpeed", data.flySpeed());
        tag.put("walkSpeed", data.walkSpeed());
        tag.put("arrowsInBody", data.arrowsInBody());
        tag.put("beeStingersInBody", data.beeStingersInBody());
        tag.put("fireTicks", data.fireTicks());
        tag.put("foodLevel", data.foodLevel());
        tag.put("freezeTicks", data.freezeTicks());
        tag.put("heldItemSlot", data.heldItemSlot());
        tag.put("level", data.level());
        tag.put("remainingAir", data.remainingAir());
        tag.put("score", data.score());
        return tag.build();
    }
}
