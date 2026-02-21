package net.thenextlvl.perworlds.listener;

import net.thenextlvl.perworlds.group.PaperGroupProvider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ConnectionListener implements Listener {
    private final PaperGroupProvider provider;

    public ConnectionListener(final PaperGroupProvider provider) {
        this.provider = provider;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        provider.getGroup(event.getPlayer().getWorld())
                .orElse(provider.getUnownedWorldGroup())
                .loadPlayerData(event.getPlayer(), true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        provider.getGroup(event.getPlayer().getWorld())
                .orElse(provider.getUnownedWorldGroup())
                .persistPlayerData(event.getPlayer());
    }
}
