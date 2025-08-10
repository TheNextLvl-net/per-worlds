package net.thenextlvl.perworlds.importer.multiverse;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.WorldGroup;
import net.thenextlvl.perworlds.data.PlayerData;
import net.thenextlvl.perworlds.group.PaperWorldGroup;
import net.thenextlvl.perworlds.importer.Importer;
import net.thenextlvl.perworlds.model.PaperPlayerData;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
    public PlayerData readPlayer(UUID uuid, String name, WorldGroup group) throws IOException {
        var data = new PaperPlayerData(uuid, ((PaperWorldGroup) group));
        
        return data;
    }
}
