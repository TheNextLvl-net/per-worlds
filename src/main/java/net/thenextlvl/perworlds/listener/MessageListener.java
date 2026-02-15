package net.thenextlvl.perworlds.listener;

import net.kyori.adventure.text.Component;
import net.thenextlvl.perworlds.GroupSettings;
import net.thenextlvl.perworlds.PerWorldsPlugin;
import net.thenextlvl.perworlds.WorldGroup;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.bukkit.GameRules.SHOW_ADVANCEMENT_MESSAGES;
import static org.bukkit.GameRules.SHOW_DEATH_MESSAGES;

@NullMarked
public final class MessageListener implements Listener {
    private final PerWorldsPlugin plugin;

    public MessageListener(final PerWorldsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerAdvancementDone(final PlayerAdvancementDoneEvent event) {
        if (!plugin.config().handleAdvancementMessages) return;
        handle(event.getPlayer().getWorld(), SHOW_ADVANCEMENT_MESSAGES, GroupSettings::advancementMessages, event::message, event.message());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDeath(final PlayerDeathEvent event) {
        if (!plugin.config().handleDeathMessages) return;
        handle(event.getPlayer().getWorld(), SHOW_DEATH_MESSAGES, GroupSettings::deathMessages, event::deathMessage, event.deathMessage());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        if (!plugin.config().handleJoinMessages) return;
        handle(event.getPlayer().getWorld(), null, GroupSettings::joinMessages, event::joinMessage, event.joinMessage());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        if (!plugin.config().handleQuitMessages) return;
        handle(event.getPlayer().getWorld(), null, GroupSettings::quitMessages, event::quitMessage, event.quitMessage());
    }

    private void handle(final World world, @Nullable final GameRule<Boolean> gameRule, final Predicate<GroupSettings> enabled,
                        final Consumer<@Nullable Component> disable, @Nullable final Component message) {
        if (message == null) return;
        final var receivers = receivers(world, gameRule, enabled);
        if (receivers == null) return;
        receivers.forEach(player -> player.sendMessage(message));
        disable.accept(null);
    }

    private @Nullable List<Player> receivers(final World world, @Nullable final GameRule<Boolean> gameRule, final Predicate<GroupSettings> enabled) {
        if (!canReceive(gameRule, world)) return null;
        final var provider = plugin.groupProvider();
        final var group = provider.getGroup(world).orElse(provider.getUnownedWorldGroup());
        return group.getSettings().enabled() && enabled.test(group.getSettings())
                ? group.getPlayers() : provider.getAllGroups().stream()
                .filter(target -> !enabled.test(target.getSettings()))
                .filter(target -> canReceive(gameRule, target))
                .map(WorldGroup::getPlayers)
                .flatMap(Collection::stream)
                .toList();
    }

    private boolean canReceive(@Nullable final GameRule<Boolean> gameRule, final WorldGroup group) {
        return gameRule == null || group.getGroupData().getGameRule(gameRule)
                .or(() -> group.getWorlds().findAny().map(world -> canReceive(gameRule, world)))
                .orElse(true);
    }

    private boolean canReceive(@Nullable final GameRule<Boolean> gameRule, final World world) {
        return gameRule == null || Boolean.TRUE.equals(world.getGameRuleValue(gameRule));
    }
}
