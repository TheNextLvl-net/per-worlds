package net.thenextlvl.perworlds.group;

import net.kyori.adventure.util.TriState;
import net.thenextlvl.perworlds.GroupData;
import net.thenextlvl.perworlds.data.WorldBorderData;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

@NullMarked
public final class PaperGroupData implements GroupData {
    private final Map<GameRule<?>, Object> gameRules = new HashMap<>();
    private @Nullable GameMode defaultGameMode = null;
    private @Nullable Location spawnLocation = null;
    private WorldBorderData worldBorder = WorldBorderData.DEFAULT;
    private Difficulty difficulty = Difficulty.NORMAL;
    private TriState hardcore = TriState.NOT_SET;
    private boolean raining = false;
    private boolean thundering = false;
    private int clearWeatherDuration;
    private int rainDuration;
    private int thunderDuration;
    private long time = 0;

    @Override
    @SuppressWarnings("unchecked")
    public void forEachGameRule(final BiConsumer<GameRule<Object>, Object> action) {
        gameRules.forEach((rule, value) -> action.accept((GameRule<Object>) rule, value));
    }

    @Override
    public <T> Optional<T> getGameRule(final GameRule<T> rule) {
        final var object = gameRules.get(rule);
        if (object == null) return Optional.empty();
        return Optional.of(rule.getType().cast(object));
    }

    @Override
    public <T> boolean setGameRule(final GameRule<T> rule, @Nullable final T value) {
        if (value == null) return gameRules.remove(rule) != null;
        return !value.equals(gameRules.put(rule, value));
    }

    @Override
    public Difficulty getDifficulty() {
        return difficulty;
    }

    @Override
    public void setDifficulty(final Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public Optional<GameMode> getDefaultGameMode() {
        return Optional.ofNullable(defaultGameMode);
    }

    @Override
    public void setDefaultGameMode(@Nullable final GameMode defaultGameMode) {
        this.defaultGameMode = defaultGameMode;
    }

    @Override
    public WorldBorderData getWorldBorder() {
        return worldBorder;
    }

    public void setWorldBorder(final WorldBorderData worldBorder) {
        this.worldBorder = worldBorder;
    }

    @Override
    public Optional<Location> getSpawnLocation() {
        return Optional.ofNullable(spawnLocation).map(Location::clone);
    }

    @Override
    public void setSpawnLocation(@Nullable final Location location) {
        this.spawnLocation = location != null ? location.clone() : null;
    }

    @Override
    public TriState getHardcore() {
        return hardcore;
    }

    @Override
    public void setHardcore(final TriState hardcore) {
        this.hardcore = hardcore;
    }

    @Override
    public boolean isRaining() {
        return raining;
    }

    @Override
    public void setRaining(final boolean raining) {
        this.raining = raining;
    }

    @Override
    public boolean isThundering() {
        return thundering;
    }

    @Override
    public void setThundering(final boolean thundering) {
        this.thundering = thundering;
    }

    @Override
    public int clearWeatherDuration() {
        return clearWeatherDuration;
    }

    @Override
    public void clearWeatherDuration(final int duration) {
        this.clearWeatherDuration = duration;
    }

    @Override
    public int getThunderDuration() {
        return thunderDuration;
    }

    @Override
    public void setThunderDuration(final int duration) {
        this.thunderDuration = duration;
    }

    @Override
    public int getRainDuration() {
        return rainDuration;
    }

    @Override
    public void setRainDuration(final int duration) {
        this.rainDuration = duration;
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public void setTime(final long time) {
        this.time = time;
    }

    @Override
    public GroupData copyFrom(final GroupData other) {
        this.gameRules.clear();
        other.forEachGameRule(gameRules::put);
        this.defaultGameMode = other.getDefaultGameMode().orElse(null);
        this.worldBorder = other.getWorldBorder();
        this.difficulty = other.getDifficulty();
        this.hardcore = other.getHardcore();
        this.raining = other.isRaining();
        this.thundering = other.isThundering();
        this.clearWeatherDuration = other.clearWeatherDuration();
        this.rainDuration = other.getRainDuration();
        this.thunderDuration = other.getThunderDuration();
        this.time = other.getTime();
        return this;
    }
}
