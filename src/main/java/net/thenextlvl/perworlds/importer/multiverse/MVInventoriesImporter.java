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
        applyStats(node.getValue(), data);

        data.gameMode(GameMode.valueOf(node.getKey().toUpperCase(Locale.ROOT)));
        data.respawnLocation(readLocation(node.getValue().get("bedSpawnLocation"), group));

        var potions = asArray(node.getValue().get("potions"));
        var effects = potions != null ? readPotions(potions) : null;
        if (effects != null) data.potionEffects(effects);

        // todo: read armor contents, inventory, offhand, enderchest
    }

    private void applyStats(JsonObject node, PlayerData data) {
        var stats = asObject(node.get("stats"));
        if (stats == null) return;
        data.health(asDouble(stats.get("hp"), data.health()));
        data.level(asInt(stats.get("el"), data.level()));
        data.experience(asFloat(stats.get("xp"), data.experience()));
        data.foodLevel(asInt(stats.get("fl"), data.foodLevel()));
        data.exhaustion(asFloat(stats.get("ex"), data.exhaustion()));
        data.saturation(asFloat(stats.get("sa"), data.saturation()));
        data.fallDistance(asFloat(stats.get("fd"), data.fallDistance()));
        data.fireTicks(asInt(stats.get("ft"), data.fireTicks()));
        data.remainingAir(asInt(stats.get("ra"), data.remainingAir()));
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

    @SuppressWarnings("deprecation")
    private @Nullable ItemStack readItem(JsonElement node) {
        try {
            var string = asString(node);
            if (string != null) {
                var bytes = Base64.getDecoder().decode(string);
                return ItemStack.deserializeBytes(bytes);
            }

            var object = asObject(node);
            if (object != null) {
                var unsafe = plugin.getServer().getUnsafe();
                return unsafe.deserializeItemFromJson(object);
            }

            plugin.getComponentLogger().warn("Don't know how to turn '{}' into an item", node);
            return null;
        } catch (Exception e) {
            plugin.getComponentLogger().warn("Failed to deserialize item '{}'", node, e);
            return null;
        }
    }

    private @Nullable Location readLocation(@Nullable JsonElement element, WorldGroup group) {
        var location = asObject(element);
        if (location == null) return null;

        var worldName = asString(location.get("world"));
        if (worldName == null) worldName = asString(location.get("wo"));

        var world = worldName != null ? plugin.getServer().getWorld(worldName) : null;
        if (world == null || !group.containsWorld(world)) return null;

        var x = asDouble(location.get("x"), 0);
        var y = asDouble(location.get("y"), 0);
        var z = asDouble(location.get("z"), 0);
        var pitch = asFloat(location.get("pitch"), asFloat(location.get("pi"), 0));
        var yaw = asFloat(location.get("yaw"), asFloat(location.get("ya"), 0));

        return new Location(world, x, y, z, yaw, pitch);
    }

    private List<PotionEffect> readPotions(JsonArray array) {
        var list = new ArrayList<PotionEffect>(array.size());
        array.forEach(element -> {
            var object = asObject(element);
            if (object == null) return;

            var effect = asString(object.get("effect"));
            if (effect == null) effect = asString(object.get("pt"));

            var key = effect != null ? NamespacedKey.fromString(effect.toLowerCase(Locale.ROOT)) : null;
            var type = key != null ? Registry.EFFECT.get(key) : null;
            if (type == null) return;

            var duration = asInt(object.get("duration"), asInt(object.get("pd"), 0));
            var amplifier = asInt(object.get("amplifier"), asInt(object.get("pa"), 0));
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
