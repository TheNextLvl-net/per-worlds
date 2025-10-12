package net.thenextlvl.perworlds.importer.multiverse;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.WorldGroup;
import net.thenextlvl.perworlds.data.PlayerData;
import net.thenextlvl.perworlds.importer.Importer;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.generator.WorldInfo;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static java.nio.file.StandardOpenOption.READ;

@NullMarked
public class MVInventoriesImporter extends Importer {

    public MVInventoriesImporter(PerWorldsPlugin plugin) {
        super(plugin, "Multiverse-Inventories");
    }

    @Override
    public Map<String, Set<String>> readGroups() throws IOException {
        var path = getDataPath().resolve("groups.yml");
        if (!Files.exists(path)) return new HashMap<>();

        try (var reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            var config = YamlConfiguration.loadConfiguration(reader);

            var groups = config.getConfigurationSection("groups");
            if (groups == null) return new HashMap<>();

            var result = new HashMap<String, Set<String>>();
            groups.getKeys(false).forEach(group -> {
                var groupSection = groups.getConfigurationSection(group);
                if (groupSection == null) return;

                var worlds = groupSection.getStringList("worlds");
                result.put(group, new HashSet<>(worlds));
            });
            return result;
        }
    }

    @Override
    public Map<UUID, String> readPlayers() throws IOException {
        var path = getDataPath().resolve("playernames.json");
        if (!Files.exists(path)) return new HashMap<>();
        try (var reader = new JsonReader(new InputStreamReader(
                Files.newInputStream(path, READ),
                StandardCharsets.UTF_8
        ))) {
            var object = JsonParser.parseReader(reader).getAsJsonObject();
            var result = new HashMap<UUID, String>(object.size());
            for (var entry : object.entrySet()) {
                try {
                    var uuid = UUID.fromString(entry.getKey());
                    result.put(uuid, entry.getValue().getAsString());
                } catch (IllegalArgumentException ignored) {
                }
            }
            return result;
        } catch (JsonParseException e) {
            plugin.getComponentLogger().warn("Failed to parse {}", path, e);
            return new HashMap<>();
        }
    }

    @Override
    public void readPlayer(UUID uuid, String name, WorldGroup group, PlayerData data) throws IOException {
        var path = group.getWorlds().map(WorldInfo::getName)
                .map(getDataPath().resolve("worlds")::resolve)
                .map(worlds -> worlds.resolve(name + ".json"))
                .filter(Files::isRegularFile)
                .findAny().orElse(null);
        if (path == null) return;
        try (var reader = new JsonReader(new InputStreamReader(
                Files.newInputStream(path, READ),
                StandardCharsets.UTF_8
        ))) {
            Optional.ofNullable(JsonParser.parseReader(reader))
                    .map(this::asObject).flatMap(this::selectBestSnapshot)
                    .ifPresent(snapshot -> applySnapshot(snapshot, group, data));
        } catch (RuntimeException e) {
            plugin.getComponentLogger().warn("Failed to import player data for {} in group {} from {}", name, group, path, e);
        }
    }

    private void applySnapshot(Map.Entry<String, JsonObject> node, WorldGroup group, PlayerData data) {
        var stats = asObject(node.getValue().get("stats"));
        if (stats != null) applyStats(data, stats);

        data.gameMode(GameMode.valueOf(node.getKey().toUpperCase(Locale.ROOT)));
        data.respawnLocation(readLocation(node.getValue().get("bedSpawnLocation"), group)); // DataStrings#PLAYER_BED_SPAWN_LOCATION

        var potions = asArray(node.getValue().get("potions"));
        var effects = potions != null ? readPotions(potions) : null;
        if (effects != null) data.potionEffects(effects);

        var inventory = asObject(node.getValue().get("inventoryContents")); // DataStrings#PLAYER_INVENTORY_CONTENTS
        if (inventory != null) data.inventory(readStorageContents(data.inventory(), inventory));

        var armor = asObject(node.getValue().get("armorContents")); // DataStrings#PLAYER_ARMOR_CONTENTS
        if (armor != null) readArmorContents(data, armor);

        var offHandItem = asObject(node.getValue().get("offHandItem")); // DataStrings#PLAYER_OFF_HAND_ITEM
        if (offHandItem != null) readItem(offHandItem).ifPresent(itemStack -> {
            var clone = data.inventory().clone();
            clone[40] = itemStack; // offhand
            data.inventory(clone);
        });

        var enderChest = asObject(node.getValue().get("enderChestContents")); // DataStrings#ENDER_CHEST_CONTENTS
        if (enderChest != null) data.enderChest(readStorageContents(data.enderChest(), enderChest));
    }

    private void readArmorContents(PlayerData data, JsonObject contents) {
        var inventory = data.inventory();
        readItem(contents.get("0")).ifPresent(itemStack -> inventory[36] = itemStack); // boots
        readItem(contents.get("1")).ifPresent(itemStack -> inventory[37] = itemStack); // leggings
        readItem(contents.get("2")).ifPresent(itemStack -> inventory[38] = itemStack); // chestplate
        readItem(contents.get("3")).ifPresent(itemStack -> inventory[39] = itemStack); // helmet
        data.inventory(inventory);
    }

    private void applyStats(PlayerData data, JsonObject stats) {
        data.health(asDouble(stats.get("hp"), data.health())); // DataStrings#PLAYER_HEALTH
        data.level(asInt(stats.get("el"), data.level())); // DataStrings#PLAYER_LEVEL
        data.experience(asFloat(stats.get("xp"), data.experience())); // DataStrings#PLAYER_EXPERIENCE
        data.foodLevel(asInt(stats.get("fl"), data.foodLevel())); // DataStrings#PLAYER_FOOD_LEVEL
        data.exhaustion(asFloat(stats.get("ex"), data.exhaustion())); // DataStrings#PLAYER_EXHAUSTION
        data.saturation(asFloat(stats.get("sa"), data.saturation())); // DataStrings#PLAYER_SATURATION
        data.fallDistance(asFloat(stats.get("fd"), data.fallDistance())); // DataStrings#PLAYER_FALL_DISTANCE
        data.fireTicks(asInt(stats.get("ft"), data.fireTicks())); // DataStrings#PLAYER_FIRE_TICKS
        data.remainingAir(asInt(stats.get("ra"), data.remainingAir())); // DataStrings#PLAYER_REMAINING_AIR
    }

    private Optional<Map.Entry<String, JsonObject>> selectBestSnapshot(JsonObject root) {
        var gameModes = Arrays.stream(GameMode.values()).map(Enum::name).toList();
        return root.entrySet().stream()
                .map(entry -> {
                    var object = asObject(entry.getValue());
                    return object != null ? Map.entry(entry.getKey(), object) : null;
                })
                .filter(Objects::nonNull)
                .sorted((entry1, entry2) -> {
                    var mode1 = gameModes.contains(entry1.getKey().toUpperCase(Locale.ROOT));
                    var mode2 = gameModes.contains(entry2.getKey().toUpperCase(Locale.ROOT));
                    return Boolean.compare(mode1, mode2);
                })
                .max(Comparator.comparingInt(entry -> entry.getValue().size()));
    }

    private @Nullable ItemStack[] readStorageContents(@Nullable ItemStack[] items, JsonObject contents) {
        for (var i = 0; i < items.length; i++) items[i] = readItem(contents.get(String.valueOf(i))).orElse(null);
        return items;
    }

    @SuppressWarnings("deprecation")
    private Optional<ItemStack> readItem(JsonElement node) {
        try {
            var string = asString(node);
            if (string != null) {
                var bytes = Base64.getDecoder().decode(string);
                return Optional.of(ItemStack.deserializeBytes(bytes));
            }

            var object = asObject(node);
            if (object != null) {
                var unsafe = plugin.getServer().getUnsafe();
                return Optional.of(unsafe.deserializeItemFromJson(object));
            }

            plugin.getComponentLogger().warn("Don't know how to turn '{}' into an item", node);
            return Optional.empty();
        } catch (Exception e) {
            plugin.getComponentLogger().warn("Failed to deserialize item '{}'", node, e);
            return Optional.empty();
        }
    }

    private @Nullable Location readLocation(@Nullable JsonElement element, WorldGroup group) {
        var location = asObject(element);
        if (location == null) return null;

        var worldName = asString(location.get("world"), asString(location.get("wo"))); // DataStrings#LOCATION_WORLD
        var world = worldName != null ? plugin.getServer().getWorld(worldName) : null;
        if (world == null || !group.containsWorld(world)) return null;

        var x = asDouble(location.get("x"), 0); // DataStrings#LOCATION_X
        var y = asDouble(location.get("y"), 0); // DataStrings#LOCATION_Y
        var z = asDouble(location.get("z"), 0); // DataStrings#LOCATION_Z
        var pitch = asFloat(location.get("pitch"), asFloat(location.get("pi"), 0)); // DataStrings#LOCATION_PITCH
        var yaw = asFloat(location.get("yaw"), asFloat(location.get("ya"), 0)); // DataStrings#LOCATION_YAW

        return new Location(world, x, y, z, yaw, pitch);
    }

    private List<PotionEffect> readPotions(JsonArray array) {
        var list = new ArrayList<PotionEffect>(array.size());
        array.forEach(element -> {
            var object = asObject(element);
            if (object == null) return;

            var effect = asString(object.get("effect"), asString(object.get("pt"))); // DataStrings#POTION_TYPE
            var key = effect != null ? NamespacedKey.fromString(effect.toLowerCase(Locale.ROOT)) : null;
            var type = key != null ? Registry.EFFECT.get(key) : null;
            if (type == null) return;

            var duration = asInt(object.get("duration"), asInt(object.get("pd"), 0)); // DataStrings#POTION_DURATION
            var amplifier = asInt(object.get("amplifier"), asInt(object.get("pa"), 0)); // DataStrings#POTION_AMPLIFIER
            var ambient = asBoolean(object.get("ambient"), false);
            var particles = asBoolean(object.get("particles"), true);
            var icon = asBoolean(object.get("icon"), true);

            list.add(new PotionEffect(type, duration, amplifier, ambient, particles, icon));
        });
        return list;
    }

    private @Nullable JsonObject asObject(@Nullable JsonElement element) {
        return element instanceof JsonObject primitive ? primitive.getAsJsonObject() : null;
    }

    private @Nullable JsonArray asArray(@Nullable JsonElement element) {
        return element instanceof JsonArray primitive ? primitive.getAsJsonArray() : null;
    }

    private @Nullable String asString(@Nullable JsonElement element) {
        return element instanceof JsonPrimitive primitive ? primitive.getAsString() : null;
    }

    @Contract("null, _ -> null; !null, null -> null; !null, !null -> !null")
    private @Nullable String asString(@Nullable JsonElement element, @Nullable String defaultValue) {
        return element instanceof JsonPrimitive primitive ? primitive.getAsString() : defaultValue;
    }

    private double asDouble(@Nullable JsonElement element, double defaultValue) {
        if (element instanceof JsonPrimitive primitive) try {
            if (primitive.isNumber()) return primitive.getAsDouble();
            if (primitive.isString()) return Double.parseDouble(primitive.getAsString());
        } catch (NumberFormatException ignored) {
        }
        return defaultValue;
    }

    private float asFloat(@Nullable JsonElement element, float defaultValue) {
        if (element instanceof JsonPrimitive primitive) try {
            if (primitive.isNumber()) return primitive.getAsFloat();
            if (primitive.isString()) return Float.parseFloat(primitive.getAsString());
        } catch (NumberFormatException ignored) {
        }
        return defaultValue;
    }

    private int asInt(@Nullable JsonElement element, int defaultValue) {
        if (element instanceof JsonPrimitive primitive) try {
            if (primitive.isNumber()) return primitive.getAsInt();
            if (primitive.isString()) return Integer.parseInt(primitive.getAsString());
        } catch (NumberFormatException ignored) {
        }
        return defaultValue;
    }

    private boolean asBoolean(@Nullable JsonElement element, boolean defaultValue) {
        if (element instanceof JsonPrimitive primitive) try {
            if (primitive.isBoolean()) return primitive.getAsBoolean();
            if (primitive.isString()) return Boolean.parseBoolean(primitive.getAsString());
        } catch (NumberFormatException ignored) {
        }
        return defaultValue;
    }
}
