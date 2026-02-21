package net.thenextlvl.perworlds.listener;

import net.thenextlvl.perworlds.group.PaperGroupProvider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TeleportListener implements Listener {
    private final PaperGroupProvider provider;

    public TeleportListener(final PaperGroupProvider provider) {
        this.provider = provider;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleport(final PlayerTeleportEvent event) {
        if (event.getFrom().getWorld().equals(event.getTo().getWorld())) return;
        final var from = provider.getGroup(event.getFrom().getWorld()).orElse(provider.getUnownedWorldGroup());
        final var to = provider.getGroup(event.getTo().getWorld()).orElse(provider.getUnownedWorldGroup());
        if (!from.equals(to)) from.persistPlayerData(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChangedWorld(final PlayerChangedWorldEvent event) {
        final var from = provider.getGroup(event.getFrom()).orElse(provider.getUnownedWorldGroup());
        final var to = provider.getGroup(event.getPlayer().getWorld()).orElse(provider.getUnownedWorldGroup());
        if (!from.equals(to)) to.loadPlayerData(event.getPlayer());
    }
}
