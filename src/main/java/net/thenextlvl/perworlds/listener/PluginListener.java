package net.thenextlvl.perworlds.listener;

import net.thenextlvl.perworlds.group.PaperGroupProvider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;

public class PluginListener implements Listener {
    private final PaperGroupProvider provider;

    public PluginListener(PaperGroupProvider provider) {
        this.provider = provider;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPluginEnable(PluginEnableEvent event) {
        if (event.getPlugin().getName().equals("Worlds")) {
            provider.getServer().getPluginManager().registerEvents(new WorldsListener(provider), event.getPlugin());
        }
    }
}
