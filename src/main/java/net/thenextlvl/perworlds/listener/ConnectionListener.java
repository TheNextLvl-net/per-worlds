package net.thenextlvl.perworlds.listener;

import net.thenextlvl.perworlds.group.PaperGroupProvider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jspecify.annotations.NullMarked;

import java.util.Locale;

@NullMarked
public final class ConnectionListener implements Listener {
    private final PaperGroupProvider provider;

    public ConnectionListener(PaperGroupProvider provider) {
        this.provider = provider;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        if (!provider.getPlugin().isFirstSetup()) return;
        var result = provider.getPlugin().bundle().component("perworlds.setup", Locale.US);
        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, result);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        provider.getGroup(event.getPlayer().getWorld())
                .orElse(provider.getUnownedWorldGroup())
                .loadPlayerData(event.getPlayer(), true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        provider.getGroup(event.getPlayer().getWorld())
                .orElse(provider.getUnownedWorldGroup())
                .persistPlayerData(event.getPlayer());
    }
}
