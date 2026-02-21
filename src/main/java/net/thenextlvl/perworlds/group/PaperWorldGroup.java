package net.thenextlvl.perworlds.group;

import com.google.common.base.Preconditions;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.util.TriState;
import net.thenextlvl.nbt.NBTInputStream;
import net.thenextlvl.nbt.NBTOutputStream;
import net.thenextlvl.perworlds.GroupData;
import net.thenextlvl.perworlds.GroupSettings;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.WorldGroup;
import net.thenextlvl.perworlds.data.PlayerData;
import net.thenextlvl.perworlds.data.WorldBorderData;
import net.thenextlvl.perworlds.model.PaperPlayerData;
import net.thenextlvl.perworlds.model.config.GroupConfig;
import org.bukkit.GameRule;
import org.bukkit.Keyed;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.EOFException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static net.thenextlvl.perworlds.PerWorldsPlugin.ISSUES;

@NullMarked
public class PaperWorldGroup implements WorldGroup {
    protected final PaperGroupProvider provider;

    private final Path dataFolder;
    private final Path configFile;
    private final Path configFileBackup;
    private final GroupConfig config;
    private final String name;

    public PaperWorldGroup(final PaperGroupProvider provider, final String name, final GroupData data, final GroupSettings settings, final Collection<World> worlds) {
        this.name = name;
        this.provider = provider;
        this.dataFolder = provider.getDataFolder().resolve(name);
        this.configFile = provider.getDataFolder().resolve(name + ".dat");
        this.configFileBackup = provider.getDataFolder().resolve(name + ".dat_old");
        this.config = readConfig().orElseGet(() -> {
            return new GroupConfig(worlds.stream().map(Keyed::key)
                    .collect(Collectors.toSet()), data, settings);
        });
    }

    private Optional<GroupConfig> readConfig() {
        try {
            return readFile(configFile, configFileBackup, GroupConfig.class);
        } catch (final EOFException e) {
            provider.getLogger().error("The world group config file {} is irrecoverably broken", configFile);
            return Optional.empty();
        } catch (final Exception e) {
            provider.getLogger().error("Failed to load world group data from {}", configFile, e);
            provider.getLogger().error("Please look for similar issues or report this on GitHub: {}", ISSUES);
            PerWorldsPlugin.ERROR_TRACKER.trackError(e);
            return Optional.empty();
        }
    }

    @Override
    public Path getDataFolder() {
        return dataFolder;
    }

    @Override
    public Path getConfigFile() {
        return configFile;
    }

    @Override
    public Path getConfigFileBackup() {
        return configFileBackup;
    }

    @Override
    public GroupData getGroupData() {
        return config.data();
    }

    @Override
    public PaperGroupProvider getGroupProvider() {
        return provider;
    }

    @Override
    public GroupSettings getSettings() {
        return config.settings();
    }

    @Override
    public @Unmodifiable List<Player> getPlayers() {
        return getWorlds().flatMap(this::getPlayers).toList();
    }

    @Override
    public Optional<Location> getSpawnLocation(final OfflinePlayer player) {
        return readPlayerData(player).flatMap(this::getSpawnLocation);
    }

    @Override
    public Optional<Location> getSpawnLocation(final PlayerData data) {
        return Optional.ofNullable(data.lastLocation())
                .filter(location -> getSettings().lastLocation())
                .or(this::getSpawnLocation);
    }

    @Override
    public Optional<Location> getSpawnLocation() {
        return getGroupData().getSpawnLocation()
                .or(() -> getSpawnWorld().map(World::getSpawnLocation))
                .map(Location::clone);
    }

    @Override
    public Optional<World> getSpawnWorld() {
        return getGroupData().getSpawnLocation()
                .map(Location::getWorld)
                .or(() -> getWorlds().min(this::compare));
    }

    private int compare(final World world, final World other) {
        final var x = getPriority(world.getEnvironment());
        final var y = getPriority(other.getEnvironment());
        return Integer.compare(x, y);
    }

    private int getPriority(final Environment environment) {
        return switch (environment) {
            case NORMAL -> 0;
            case NETHER -> 1;
            case THE_END -> 2;
            default -> 3;
        };
    }

    @Override
    public @Unmodifiable Set<Key> getPersistedWorlds() {
        return Set.copyOf(config.worlds());
    }

    @Override
    public @Unmodifiable Stream<World> getWorlds() {
        return config.worlds().stream()
                .map(provider.getServer()::getWorld)
                .filter(Objects::nonNull);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean addWorld(final World world) {
        if (provider.hasGroup(world)) return false;
        final var previous = provider.getGroup(world).orElse(provider.getUnownedWorldGroup());
        getPlayers(world).forEach(previous::persistPlayerData);
        if (!config.worlds().add(world.key())) return false;
        getPlayers(world).forEach(this::loadPlayerData);
        if (config.worlds().size() == 1) loadWorldData(world);
        else updateWorldData(world);
        return true;
    }

    @Override
    public boolean containsWorld(final World world) {
        return config.worlds().contains(world.key());
    }

    @Override
    public boolean delete() {
        return provider.removeGroup(this) | delete(configFile) | delete(configFileBackup) | delete(dataFolder);
    }

    @Override
    public boolean hasPlayerData(final OfflinePlayer player) {
        return Files.exists(getDataFolder().resolve(player.getUniqueId() + ".dat"));
    }

    protected boolean delete(final Path path) {
        try {
            if (!Files.isDirectory(path)) return Files.deleteIfExists(path);
            else try (final var files = Files.list(path)) {
                files.forEach(this::delete);
                return Files.deleteIfExists(path);
            }
        } catch (final IOException e) {
            provider.getLogger().warn("Failed to delete {}", path, e);
            provider.getLogger().warn("Please look for similar issues or report this on GitHub: {}", ISSUES);
            PerWorldsPlugin.ERROR_TRACKER.trackError(e);
            return false;
        }
    }

    @Override
    public boolean persist() {
        try {
            final var file = configFile;
            if (Files.exists(file)) Files.move(file, configFileBackup, REPLACE_EXISTING);
            else Files.createDirectories(file.toAbsolutePath().getParent());
            try (final var outputStream = NBTOutputStream.create(file)) {
                outputStream.writeTag(null, provider.nbt().serialize(config));
                return true;
            }
        } catch (final Throwable t) {
            if (Files.exists(configFileBackup)) try {
                Files.copy(configFileBackup, configFile, REPLACE_EXISTING);
                provider.getLogger().warn("Recovered {} from potential data loss", configFile);
            } catch (final IOException e) {
                provider.getLogger().error("Failed to recover world group config {}", configFile, e);
            }
            provider.getLogger().error("Failed to save world group config {}", configFile, t);
            provider.getLogger().error("Please look for similar issues or report this on GitHub: {}", ISSUES);
            PerWorldsPlugin.ERROR_TRACKER.trackError(t);
            return false;
        }
    }

    @Override
    public boolean removeWorld(final World world) {
        if (!config.worlds().remove(world.key())) return false;
        getPlayers(world).forEach(provider.getUnownedWorldGroup()::loadPlayerData);
        provider.getUnownedWorldGroup().updateWorldData(world);
        return true;
    }

    private Stream<Player> getPlayers(final World world) {
        return world.getPlayers().stream().filter(player -> !player.hasMetadata("NPC"));
    }

    @Override
    public boolean removeWorld(final Key key) {
        final var world = provider.getServer().getWorld(key);
        return world != null ? removeWorld(world) : config.worlds().remove(key);
    }

    @Override
    public Optional<PlayerData> readPlayerData(final OfflinePlayer player) {
        final var file = getDataFolder().resolve(player.getUniqueId() + ".dat");
        try {
            return readPlayerData(player, file);
        } catch (final EOFException e) {
            provider.getLogger().error("The player data file {} is irrecoverably broken", file);
            return Optional.empty();
        } catch (final Exception e) {
            provider.getLogger().error("Failed to load player data from {}", file, e);
            provider.getLogger().error("Please look for similar issues or report this on GitHub: {}", ISSUES);
            PerWorldsPlugin.ERROR_TRACKER.trackError(e);
            return Optional.empty();
        }
    }

    @Override
    public boolean editPlayerData(final OfflinePlayer player, final Consumer<PlayerData> data) {
        return readPlayerData(player).map(paperPlayerData -> {
            data.accept(paperPlayerData);
            return writePlayerData(player, paperPlayerData);
        }).orElse(false);
    }

    @Override
    public boolean writePlayerData(final OfflinePlayer player, final PlayerData data) {
        if (player instanceof final Player online) Preconditions.checkState(containsWorld(online.getWorld()),
                "Failed to persist player data: World mismatch between group '%s' and player '%s'. Expected any of %s but got %s",
                getName(), player.getName(), getPersistedWorlds(), online.getWorld().key());

        if (!hasPlayerData(player) && getMigratingTo() == null) {
            provider.getLogger().warn("Failed to persist player data for {}: No group to migrate to", player.getName());
            provider.getLogger().warn("Use '/world group migrate <group>' to define a group to migrate to");
            return false;
        }

        return writePlayerDataUnsafe(player, data);
    }

    public boolean writePlayerDataUnsafe(final OfflinePlayer player, final PlayerData data) {
        final var file = getDataFolder().resolve(player.getUniqueId() + ".dat");
        final var backup = getDataFolder().resolve(player.getUniqueId() + ".dat_old");
        try {
            if (Files.isRegularFile(file)) Files.move(file, backup, REPLACE_EXISTING);
            else Files.createDirectories(file.toAbsolutePath().getParent());
            try (final var outputStream = NBTOutputStream.create(file)) {
                outputStream.writeTag(null, provider.nbt().serialize(data, PaperPlayerData.class));
                return true;
            }
        } catch (final Throwable t) {
            if (Files.isRegularFile(backup)) try {
                Files.copy(backup, file, REPLACE_EXISTING);
                provider.getLogger().warn("Recovered {} from potential data loss", player.getUniqueId());
            } catch (final IOException e) {
                provider.getLogger().error("Failed to recover player data {}", player.getUniqueId(), e);
            }
            provider.getLogger().error("Failed to save player data {}", player.getUniqueId(), t);
            provider.getLogger().error("Please look for similar issues or report this on GitHub: {}", ISSUES);
            PerWorldsPlugin.ERROR_TRACKER.trackError(t);
            return false;
        }
    }

    @Override
    public CompletableFuture<Boolean> loadPlayerData(final Player player) {
        return loadPlayerData(player, false);
    }

    @Override
    public CompletableFuture<Boolean> loadPlayerData(final Player player, final boolean position) {
        if (!getSettings().enabled()) return CompletableFuture.completedFuture(false);
        if (provider.isLoadingData(player)) return CompletableFuture.completedFuture(false);

        provider.loadingPlayers.add(player.getUniqueId());
        final var playerData = readPlayerData(player);
        final var group = playerData.isEmpty() ? getMigratingTo() : null;

        if (group == null && playerData.isEmpty()) {
            provider.loadingPlayers.remove(player.getUniqueId());
            provider.getLogger().warn("Failed to load player data for {}: No group to migrate to", player.getName());
            provider.getLogger().warn("Use '/world group migrate <group>' to define a group to migrate to");
            if (player.hasPermission("perworlds.command.group.migrate"))
                provider.bundle().sendMessage(player, "group.migrate.prompt");
            return CompletableFuture.completedFuture(false);
        }

        return playerData.orElseGet(() -> migratePlayerData(group, player)).load(player, position)
                .whenComplete((success, throwable) -> provider.loadingPlayers.remove(player.getUniqueId()))
                .exceptionally(throwable -> {
                    provider.getLogger().error("Failed to load group data for player {}", player.getName(), throwable);
                    provider.getLogger().error("Please look for similar issues or report this on GitHub: {}", ISSUES);
                    player.kick(provider.bundle().component("group.load.failed", player));
                    return false;
                });
    }

    private @Nullable PaperWorldGroup getMigratingTo() {
        return provider.getPlugin().config().getMigrateToGroup(provider)
                .map(PaperWorldGroup.class::cast).orElse(null);
    }

    private PaperPlayerData migratePlayerData(final PaperWorldGroup group, final Player player) {
        if (!group.hasPlayerData(player)) {
            final var data = PaperPlayerData.of(player, group);
            if (equals(group)) return data;
            group.writePlayerDataUnsafe(player, data);
        }
        return new PaperPlayerData(player.getUniqueId(), this);
    }

    @Override
    public void updateWorldData(final World world) throws IllegalArgumentException {
        Preconditions.checkArgument(containsWorld(world), "World '%s' is not part of group '%s'", world.getName(), getName());
        if (!getSettings().enabled()) return;
        for (final var type : GroupData.Type.values()) updateWorldData(world, type);
    }

    @Override
    public void updateWorldData(final World world, final GroupData.Type type) throws IllegalArgumentException {
        Preconditions.checkArgument(containsWorld(world), "World '%s' is not part of group '%s'", world.getName(), getName());
        if (isEnabled(type)) switch (type) {
            case DIFFICULTY -> world.setDifficulty(getGroupData().getDifficulty());
            case TIME -> world.setFullTime(getGroupData().getTime());
            case GAME_RULE -> applyGameRules(world);
            case WORLD_BORDER -> applyWorldBorder(world);
            case HARDCORE -> {
                final var hardcore = getGroupData().getHardcore().toBooleanOrElse(provider.getServer().isHardcore());
                world.setHardcore(hardcore);
            }
            case WEATHER -> applyWeather(world);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void loadWorldData(final World world) {
        getGroupData().setDifficulty(world.getDifficulty());
        getGroupData().setTime(world.getFullTime());

        Arrays.stream(GameRule.values())
                .filter(gameRule -> world.getFeatureFlags().containsAll(gameRule.requiredFeatures()))
                .map(gameRule -> (GameRule<Object>) gameRule)
                .forEach(rule -> getGroupData().setGameRule(rule, world.getGameRuleValue(rule)));

        getGroupData().setWorldBorder(WorldBorderData.of(world.getWorldBorder()));
        getGroupData().setHardcore(TriState.byBoolean(world.isHardcore()));

        getGroupData().setRaining(world.hasStorm());
        getGroupData().setThundering(world.isThundering());
        getGroupData().clearWeatherDuration(world.getClearWeatherDuration());
        getGroupData().setThunderDuration(world.getThunderDuration());
        getGroupData().setRainDuration(world.getWeatherDuration());

        getWorlds().filter(other -> other != world).forEach(this::updateWorldData);
    }

    private boolean isEnabled(final GroupData.Type type) {
        return getSettings().enabled() && switch (type) {
            case DEFAULT_GAME_MODE -> getSettings().gameMode();
            case DIFFICULTY, HARDCORE -> getSettings().difficulty();
            case GAME_RULE -> getSettings().gameRules();
            case SPAWN_LOCATION -> true;
            case TIME -> getSettings().time();
            case WEATHER -> getSettings().weather();
            case WORLD_BORDER -> getSettings().worldBorder();
        };
    }

    private void applyWeather(final World world) {
        world.setStorm(getGroupData().isRaining());
        world.setThundering(getGroupData().isThundering());
        world.setClearWeatherDuration(getGroupData().clearWeatherDuration());
        world.setThunderDuration(getGroupData().getThunderDuration());
        world.setWeatherDuration(getGroupData().getRainDuration());
    }

    @SuppressWarnings("unchecked")
    private void applyGameRules(final World world) {
        Arrays.stream(world.getGameRules()).map(GameRule::getByName)
                .map(gameRule -> ((GameRule<Object>) gameRule)).filter(Objects::nonNull)
                .forEach(rule -> getGroupData().getGameRule(rule)
                        .or(() -> Optional.ofNullable(world.getGameRuleDefault(rule)))
                        .ifPresent(value -> world.setGameRule(rule, value)));
    }

    private void applyWorldBorder(final World world) {
        final var border = getGroupData().getWorldBorder();
        final var worldBorder = world.getWorldBorder();
        worldBorder.setSize(border.size(), TimeUnit.MILLISECONDS, border.duration());
        worldBorder.setCenter(border.centerX(), border.centerZ());
        worldBorder.setDamageAmount(border.damageAmount());
        worldBorder.setDamageBuffer(border.damageBuffer());
        worldBorder.setWarningDistance(border.warningDistance());
        worldBorder.setWarningTime(border.warningTime());
    }

    @Override
    public void persistPlayerData() {
        getPlayers().forEach(this::persistPlayerData);
    }

    @Override
    public void persistPlayerData(final Player player) {
        writePlayerData(player, PaperPlayerData.of(player, this));
    }

    @Override
    public void persistPlayerData(final Player player, final Consumer<PlayerData> data) {
        final var playerData = PaperPlayerData.of(player, this);
        data.accept(playerData);
        writePlayerData(player, playerData);
    }

    private Optional<PlayerData> readPlayerData(final OfflinePlayer player, final Path file) throws IOException {
        return readFile(file, file.resolveSibling(file.getFileName() + "_old"), PaperPlayerData.class)
                .map(paperPlayerData -> paperPlayerData.finalize(player, this));
    }

    private <T> Optional<T> readFile(final Path file, final Path backup, final Class<T> type) throws IOException {
        if (!Files.exists(file)) return Optional.empty();
        try (final var inputStream = NBTInputStream.create(file)) {
            return Optional.of(inputStream.readTag()).map(tag -> provider.nbt().deserialize(tag, type));
        } catch (final Exception e) {
            if (!Files.exists(backup)) throw e;
            provider.getLogger().warn("Failed to load data from {}", file, e);
            provider.getLogger().warn("Falling back to {}", backup);
            try (final var inputStream = NBTInputStream.create(backup)) {
                return Optional.of(inputStream.readTag()).map(tag -> provider.nbt().deserialize(tag, type));
            }
        }
    }
}
