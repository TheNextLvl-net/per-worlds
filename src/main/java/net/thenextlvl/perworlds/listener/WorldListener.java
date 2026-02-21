package net.thenextlvl.perworlds.listener;

import io.papermc.paper.event.world.WorldDifficultyChangeEvent;
import io.papermc.paper.event.world.WorldGameRuleChangeEvent;
import io.papermc.paper.event.world.border.WorldBorderBoundsChangeEvent;
import io.papermc.paper.event.world.border.WorldBorderCenterChangeEvent;
import net.kyori.adventure.key.Key;
import net.thenextlvl.perworlds.GroupData;
import net.thenextlvl.perworlds.GroupData.Type;
import net.thenextlvl.perworlds.WorldGroup;
import net.thenextlvl.perworlds.group.PaperGroupProvider;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.TimeSkipEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.jspecify.annotations.NullMarked;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

@NullMarked
public final class WorldListener implements Listener {
    private final PaperGroupProvider provider;

    private final Map<Type, Set<WorldGroup>> lock = new HashMap<>();
    private final Set<Key> allowed = new HashSet<>();

    public WorldListener(final PaperGroupProvider provider) {
        this.provider = provider;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldInit(final WorldLoadEvent event) {
        provider.getGroup(event.getWorld())
                .orElse(provider.getUnownedWorldGroup())
                .updateWorldData(event.getWorld());
        allowed.add(event.getWorld().key());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onWorldUnload(final WorldUnloadEvent event) {
        allowed.remove(event.getWorld().key());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldDifficultyChange(final WorldDifficultyChangeEvent event) {
        processWorldDataUpdate(event.getWorld(), Type.DIFFICULTY, data -> {
            data.setDifficulty(event.getDifficulty());
        });
    }

    @SuppressWarnings("unchecked")
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWorldGameRuleChange(final WorldGameRuleChangeEvent event) {
        processWorldDataUpdate(event.getWorld(), Type.GAME_RULE, data -> {
            final var gameRule = (GameRule<Object>) event.getGameRule();
            final var value = parseValue(gameRule, event.getValue());
            data.setGameRule(gameRule, value);
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTimeSkip(final TimeSkipEvent event) {
        processWorldDataUpdate(event.getWorld(), Type.TIME, data -> {
            data.setTime(event.getWorld().getFullTime() + event.getSkipAmount());
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWeatherChange(final WeatherChangeEvent event) {
        processWorldDataUpdate(event.getWorld(), Type.WEATHER, data -> {
            data.setRaining(event.toWeatherState());
            data.setRainDuration(event.getWorld().getWeatherDuration());
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onThunderChange(final ThunderChangeEvent event) {
        processWorldDataUpdate(event.getWorld(), Type.WEATHER, data -> {
            data.setThundering(event.toThunderState());
            data.setThunderDuration(event.getWorld().getThunderDuration());
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWorldBorderChange(final WorldBorderBoundsChangeEvent event) {
        processWorldDataUpdate(event.getWorld(), Type.WORLD_BORDER, data -> {
            data.setWorldBorder(data.getWorldBorder().size(event.getNewSize()).duration(event.getDuration()));
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWorldBorderChange(final WorldBorderCenterChangeEvent event) {
        processWorldDataUpdate(event.getWorld(), Type.WORLD_BORDER, data -> {
            data.setWorldBorder(data.getWorldBorder().center(event.getNewCenter()));
        });
    }

    private void processWorldDataUpdate(final World world, final Type type, final Consumer<GroupData> process) {
        if (!allowed.contains(world.key())) return;
        final var group = provider.getGroup(world).orElse(provider.getUnownedWorldGroup());
        if (!lock.computeIfAbsent(type, ignored -> new HashSet<>()).add(group)) return;
        process.accept(group.getGroupData());
        group.getWorlds()
                .filter(target -> !target.equals(world))
                .forEach(target -> group.updateWorldData(target, type));
        lock.computeIfPresent(type, (ignored, groups) -> {
            groups.remove(group);
            return groups.isEmpty() ? null : groups;
        });
    }

    private Object parseValue(final GameRule<?> rule, final String value) {
        return rule.getType().equals(Integer.class) ? Integer.parseInt(value) : Boolean.parseBoolean(value);
    }
}
