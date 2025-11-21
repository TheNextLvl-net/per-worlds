package net.thenextlvl.perworlds.group;

import com.google.common.base.Preconditions;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.util.TriState;
import net.thenextlvl.i18n.ComponentBundle;
import net.thenextlvl.nbt.serialization.NBT;
import net.thenextlvl.nbt.serialization.adapter.EnumAdapter;
import net.thenextlvl.perworlds.GroupData;
import net.thenextlvl.perworlds.GroupProvider;
import net.thenextlvl.perworlds.GroupSettings;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.WorldGroup;
import net.thenextlvl.perworlds.adapter.AdvancementDataAdapter;
import net.thenextlvl.perworlds.adapter.AttributeAdapter;
import net.thenextlvl.perworlds.adapter.AttributeDataAdapter;
import net.thenextlvl.perworlds.adapter.GroupConfigAdapter;
import net.thenextlvl.perworlds.adapter.GroupDataAdapter;
import net.thenextlvl.perworlds.adapter.GroupSettingsAdapter;
import net.thenextlvl.perworlds.adapter.InstantAdapter;
import net.thenextlvl.perworlds.adapter.ItemStackArrayAdapter;
import net.thenextlvl.perworlds.adapter.KeyAdapter;
import net.thenextlvl.perworlds.adapter.LocationAdapter;
import net.thenextlvl.perworlds.adapter.NamespacedKeyAdapter;
import net.thenextlvl.perworlds.adapter.PlayerDataAdapter;
import net.thenextlvl.perworlds.adapter.PotionEffectAdapter;
import net.thenextlvl.perworlds.adapter.PotionEffectTypeAdapter;
import net.thenextlvl.perworlds.adapter.VectorAdapter;
import net.thenextlvl.perworlds.adapter.WardenSpawnTrackerAdapter;
import net.thenextlvl.perworlds.adapter.WorldAdapter;
import net.thenextlvl.perworlds.adapter.WorldBorderAdapter;
import net.thenextlvl.perworlds.adapter.statistic.BlockTypeStatAdapter;
import net.thenextlvl.perworlds.adapter.statistic.CustomStatAdapter;
import net.thenextlvl.perworlds.adapter.statistic.EntityTypeStatAdapter;
import net.thenextlvl.perworlds.adapter.statistic.ItemTypeStatAdapter;
import net.thenextlvl.perworlds.adapter.statistic.StatisticsAdapter;
import net.thenextlvl.perworlds.data.AdvancementData;
import net.thenextlvl.perworlds.data.AttributeData;
import net.thenextlvl.perworlds.data.WardenSpawnTracker;
import net.thenextlvl.perworlds.data.WorldBorderData;
import net.thenextlvl.perworlds.model.PaperPlayerData;
import net.thenextlvl.perworlds.model.config.GroupConfig;
import net.thenextlvl.perworlds.statistics.BlockTypeStat;
import net.thenextlvl.perworlds.statistics.CustomStat;
import net.thenextlvl.perworlds.statistics.EntityTypeStat;
import net.thenextlvl.perworlds.statistics.ItemTypeStat;
import net.thenextlvl.perworlds.statistics.Statistics;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Keyed;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

import java.nio.file.Path;
import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.regex.Pattern;

@NullMarked
public final class PaperGroupProvider implements GroupProvider {
    public final Set<UUID> loadingPlayers = new HashSet<>();
    private final Set<WorldGroup> groups = new HashSet<>();

    private final Path dataFolder;
    private final NBT nbt;
    private final PerWorldsPlugin plugin;
    private final PaperWorldGroup unownedWorldGroup;

    public PaperGroupProvider(PerWorldsPlugin plugin) {
        this.plugin = plugin;
        this.dataFolder = plugin.getDataPath().resolve("groups");
        this.nbt = NBT.builder()
                .registerTypeHierarchyAdapter(AdvancementData.class, new AdvancementDataAdapter(getServer()))
                .registerTypeHierarchyAdapter(Attribute.class, new AttributeAdapter())
                .registerTypeHierarchyAdapter(AttributeData.class, new AttributeDataAdapter())
                .registerTypeHierarchyAdapter(BlockTypeStat.class, new BlockTypeStatAdapter(plugin))
                .registerTypeHierarchyAdapter(CustomStat.class, new CustomStatAdapter())
                .registerTypeHierarchyAdapter(Difficulty.class, new EnumAdapter<>(Difficulty.class))
                .registerTypeHierarchyAdapter(EntityTypeStat.class, new EntityTypeStatAdapter(plugin))
                .registerTypeHierarchyAdapter(GameMode.class, new EnumAdapter<>(GameMode.class))
                .registerTypeHierarchyAdapter(GroupConfig.class, new GroupConfigAdapter())
                .registerTypeHierarchyAdapter(GroupData.class, new GroupDataAdapter(getServer()))
                .registerTypeHierarchyAdapter(GroupSettings.class, new GroupSettingsAdapter())
                .registerTypeHierarchyAdapter(Instant.class, new InstantAdapter())
                .registerTypeHierarchyAdapter(ItemStack[].class, new ItemStackArrayAdapter())
                .registerTypeHierarchyAdapter(ItemTypeStat.class, new ItemTypeStatAdapter(plugin))
                .registerTypeHierarchyAdapter(Key.class, new KeyAdapter())
                .registerTypeHierarchyAdapter(Location.class, new LocationAdapter())
                .registerTypeHierarchyAdapter(NamespacedKey.class, new NamespacedKeyAdapter())
                .registerTypeHierarchyAdapter(PaperPlayerData.class, new PlayerDataAdapter(plugin))
                .registerTypeHierarchyAdapter(PotionEffect.class, new PotionEffectAdapter())
                .registerTypeHierarchyAdapter(PotionEffectType.class, new PotionEffectTypeAdapter())
                .registerTypeHierarchyAdapter(Statistics.class, new StatisticsAdapter(plugin))
                .registerTypeHierarchyAdapter(TriState.class, new EnumAdapter<>(TriState.class))
                .registerTypeHierarchyAdapter(Vector.class, new VectorAdapter())
                .registerTypeHierarchyAdapter(WardenSpawnTracker.class, new WardenSpawnTrackerAdapter())
                .registerTypeHierarchyAdapter(World.class, new WorldAdapter(getServer()))
                .registerTypeHierarchyAdapter(WorldBorderData.class, new WorldBorderAdapter())
                .build();
        this.unownedWorldGroup = new PaperUnownedWorldGroup(this);
    }

    public ComponentLogger getLogger() {
        return plugin.getComponentLogger();
    }

    public Server getServer() {
        return plugin.getServer();
    }

    public PerWorldsPlugin getPlugin() {
        return plugin;
    }

    @Override
    public Path getDataFolder() {
        return dataFolder;
    }

    public NBT nbt() {
        return nbt;
    }

    public ComponentBundle bundle() {
        return plugin.bundle();
    }

    @Override
    public @Unmodifiable Set<WorldGroup> getAllGroups() {
        var groups = new HashSet<>(getGroups());
        groups.add(unownedWorldGroup);
        return Set.copyOf(groups);
    }

    @Override
    public @Unmodifiable Set<WorldGroup> getGroups() {
        return Set.copyOf(groups);
    }

    @Override
    public Optional<WorldGroup> getGroup(String name) {
        return unownedWorldGroup.getName().equals(name) ? Optional.of(unownedWorldGroup)
                : groups.stream().filter(group -> group.getName().equals(name)).findAny();
    }

    @Override
    public Optional<WorldGroup> getGroup(World world) {
        return groups.stream().filter(group -> group.containsWorld(world)).findAny();
    }

    @Override
    public PaperWorldGroup getUnownedWorldGroup() {
        return unownedWorldGroup;
    }

    @Override
    public PaperWorldGroup createGroup(String name, Consumer<GroupData> data, Consumer<GroupSettings> settings, Collection<World> worlds) {
        Preconditions.checkState(!hasGroup(name), "A WorldGroup named '%s' already exists", name);
        var invalid = worlds.stream().filter(this::hasGroup).map(Keyed::key).map(Key::asString).toList();
        Preconditions.checkState(invalid.isEmpty(), "Worlds cannot be in multiple groups: {}", String.join(", ", invalid));

        var groupSettings = new PaperGroupSettings();
        var groupData = new PaperGroupData();
        settings.accept(groupSettings);
        data.accept(groupData);

        var group = new PaperWorldGroup(this, name, groupData, groupSettings, Set.copyOf(worlds));
        groups.add(group);
        return group;
    }

    @Override
    public PaperWorldGroup createGroup(String name, Collection<World> worlds) throws IllegalStateException {
        return createGroup(name, data -> {
        }, worlds);
    }

    @Override
    public PaperWorldGroup createGroup(String name, Consumer<GroupData> data, World... worlds) throws IllegalStateException {
        return createGroup(name, data, settings -> {
        }, worlds);
    }

    @Override
    public PaperWorldGroup createGroup(String name, Consumer<GroupData> data, Collection<World> worlds) throws IllegalStateException {
        return createGroup(name, data, settings -> {
        }, worlds);
    }

    @Override
    public PaperWorldGroup createGroup(String name, Consumer<GroupData> data, Consumer<GroupSettings> settings, World... worlds) {
        return createGroup(name, data, settings, List.of(worlds));
    }

    @Override
    public PaperWorldGroup createGroup(String name, World... worlds) throws IllegalStateException {
        return createGroup(name, data -> {
        }, worlds);
    }

    @Override
    public boolean hasGroup(String name) {
        return unownedWorldGroup.getName().equals(name)
                || groups.stream().anyMatch(group -> group.getName().equals(name));
    }
    
    public String findFreeName(String name) {
        var usedNames = getAllGroups().stream().map(WorldGroup::getName).toList();
        if (!usedNames.contains(name)) return name;

        var baseName = name;
        int suffix = 1;
        String candidate = baseName + " (1)";

        var pattern = Pattern.compile("^(.+) \\((\\d+)\\)$");
        var matcher = pattern.matcher(name);

        if (matcher.matches()) {
            baseName = matcher.group(1);
            suffix = Integer.parseInt(matcher.group(2)) + 1;
            candidate = baseName + " (" + suffix + ")";
            suffix++;
        }

        while (usedNames.contains(candidate)) {
            candidate = baseName + " (" + suffix++ + ")";
        }

        return candidate;
    }

    @Override
    public boolean hasGroup(World world) {
        return groups.stream().anyMatch(group -> group.containsWorld(world));
    }

    @Override
    public boolean hasGroup(WorldGroup group) {
        return groups.contains(group);
    }

    @Override
    public boolean removeGroup(String name) {
        return getGroup(name).map(this::removeGroup).orElse(false);
    }

    @Override
    public boolean removeGroup(WorldGroup group) {
        if (!groups.remove(group)) return false;
        group.getPlayers().forEach(getUnownedWorldGroup()::loadPlayerData);
        return true;
    }

    @Override
    public boolean isLoadingData(Player player) {
        return loadingPlayers.contains(player.getUniqueId());
    }
}
