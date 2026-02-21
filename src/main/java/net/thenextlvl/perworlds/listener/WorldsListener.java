package net.thenextlvl.perworlds.listener;

import net.thenextlvl.perworlds.group.PaperGroupProvider;
import net.thenextlvl.worlds.api.event.WorldDeleteEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public final class WorldsListener implements Listener {
    private final PaperGroupProvider provider;

    public WorldsListener(final PaperGroupProvider provider) {
        this.provider = provider;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWorldDelete(final WorldDeleteEvent event) {
        provider.getGroup(event.getWorld()).ifPresent(group -> {
            if (group.removeWorld(event.getWorld())) return;
            provider.getLogger().error("Failed to remove deleted world {} from group {}",
                    event.getWorld().getName(), group.getName());
        });
    }
}
