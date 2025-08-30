package net.thenextlvl.perworlds;

import core.i18n.file.ComponentBundle;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.key.Key;
import net.thenextlvl.perworlds.command.WorldCommand;
import net.thenextlvl.perworlds.group.PaperGroupProvider;
import net.thenextlvl.perworlds.listener.ChatListener;
import net.thenextlvl.perworlds.listener.ConnectionListener;
import net.thenextlvl.perworlds.listener.MessageListener;
import net.thenextlvl.perworlds.listener.RespawnListener;
import net.thenextlvl.perworlds.listener.TeleportListener;
import net.thenextlvl.perworlds.listener.WorldListener;
import net.thenextlvl.perworlds.listener.WorldsListener;
import net.thenextlvl.perworlds.version.PluginVersionChecker;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@NullMarked
public class PerWorldsPlugin extends JavaPlugin {
    public static final String ISSUES = "https://github.com/TheNextLvl-net/per-worlds/issues/new?template=bug_report.yml";

    private final PluginVersionChecker versionChecker = new PluginVersionChecker(this);
    private final Metrics metrics = new Metrics(this, 25295);

    private final Key key = Key.key("perworlds", "translations");
    private final Path translations = getDataPath().resolve("translations");
    private final ComponentBundle bundle = ComponentBundle.builder(key, translations)
            .placeholder("prefix", "prefix")
            .resource("per-worlds.properties", Locale.US)
            .resource("per-worlds_german.properties", Locale.GERMANY)
            .build();

    private final PaperGroupProvider provider = new PaperGroupProvider(this);
    private final boolean groupsExist = Files.exists(provider.getDataFolder());

    public PerWorldsPlugin() {
        registerCommands();
    }

    @Override
    public void onLoad() {
        versionChecker.checkVersion();
        addCustomCharts();
        registerServices();
        loadGroups();
    }

    @Override
    public void onEnable() {
        scheduleDefaultGroupCreation();
        registerListeners();
        warnWorldManager();
    }

    @Override
    public void onDisable() {
        metrics.shutdown();
        persistGroups();
    }

    private void registerServices() {
        getServer().getServicesManager().register(GroupProvider.class, provider, this, ServicePriority.Highest);
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new ChatListener(provider), this);
        getServer().getPluginManager().registerEvents(new ConnectionListener(provider), this);
        getServer().getPluginManager().registerEvents(new MessageListener(provider), this);
        getServer().getPluginManager().registerEvents(new RespawnListener(provider), this);
        getServer().getPluginManager().registerEvents(new TeleportListener(provider), this);
        getServer().getPluginManager().registerEvents(new WorldListener(provider), this);

        if (getServer().getPluginManager().getPlugin("Worlds") == null) return;
        getServer().getPluginManager().registerEvents(new WorldsListener(provider), this);
    }

    private void scheduleDefaultGroupCreation() {
        if (!groupsExist) getServer().getGlobalRegionScheduler().execute(this, () -> {
            var defaultGroupName = "default";

            var defaultGroup = groupProvider().getGroup(defaultGroupName)
                    .orElseGet(() -> groupProvider().createGroup(defaultGroupName));

            Optional.ofNullable(getServer().getWorld(Key.key(Key.MINECRAFT_NAMESPACE, "overworld")))
                    .ifPresent(defaultGroup::addWorld);
            Optional.ofNullable(getServer().getWorld(Key.key(Key.MINECRAFT_NAMESPACE, "the_nether")))
                    .ifPresent(defaultGroup::addWorld);
            Optional.ofNullable(getServer().getWorld(Key.key(Key.MINECRAFT_NAMESPACE, "the_end")))
                    .ifPresent(defaultGroup::addWorld);
        });
    }

    private void warnWorldManager() {
        var plugin = knownWorldManagers.stream()
                .filter(name -> !name.equals("Worlds"))
                .map(getServer().getPluginManager()::getPlugin)
                .filter(Objects::nonNull)
                .findAny()
                .map(Plugin::getName)
                .orElse(null);
        if (plugin == null) return;
        getComponentLogger().warn("It appears you are using a third party world management plugin");
        getComponentLogger().warn("Consider switching from '{}' to 'Worlds' for first hand support", plugin);
        getComponentLogger().warn("Download at: https://modrinth.com/project/gBIw3Gvy");
    }

    private void loadGroups() {
        if (!groupsExist) return;
        var suffix = ".dat";
        try (var files = Files.list(provider.getDataFolder())) {
            files.map(path -> path.getFileName().toString())
                    .filter(name -> name.endsWith(suffix))
                    .forEach(name -> {
                        var trimmed = name.substring(0, name.length() - suffix.length());
                        if (!provider.hasGroup(trimmed)) provider.createGroup(trimmed);
                    });
        } catch (IOException e) {
            getComponentLogger().error("Failed to load groups", e);
        }
    }

    private void persistGroups() {
        provider.getAllGroups().forEach(group -> {
            group.persistPlayerData();
            group.persist();
        });
    }

    private void registerCommands() {
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            var command = WorldCommand.create(this);
            var world = event.registrar().getDispatcher().getRoot().getChild("world");
            if (world != null) {
                world.getChildren().forEach(command::addChild);
                var requirement = command.getRequirement();
                command.requirement = source -> requirement.test(source) || world.canUse(source);
            }
            event.registrar().register(command);
        });
    }

    private final Set<String> knownWorldManagers = Set.of( // list ordered by likelihood of a plugin being used
            "Worlds", // https://github.com/TheNextLvl-net/worlds
            "Multiverse-Core", // https://github.com/Multiverse/Multiverse-Core/
            "My_Worlds", // https://github.com/bergerhealer/MyWorlds
            "MultiWorld", // https://dev.bukkit.org/projects/multiworld-v-2-0 // https://modrinth.com/plugin/multiworld-bukkit
            "PhantomWorlds", // https://github.com/TheNewEconomy/PhantomWorlds
            "Hyperverse", // https://github.com/Incendo/Hyperverse
            "LightWorlds", // https://github.com/justin0-0/LightWorlds
            "SolarSystem", // https://github.com/OneLiteFeatherNET/SolarSystemPlugin
            "MoreFoWorld", // https://github.com/Folia-Inquisitors/MoreFoWorld
            "WorldManager", // https://www.spigotmc.org/resources/worldmanager-1-8-1-18-free-download-api.101875/
            "LilWorlds", // https://github.com/QQuantumBits/LilWorlds
            "WorldMaster", // https://www.spigotmc.org/resources/worldmaster.101171/
            "TheGalaxyLimits", // https://hangar.papermc.io/TheGlitchedVirus/thegalaxylimits
            "WorldMagic", // https://github.com/hotwopik/WorldMagic
            "BulMultiverse", // https://github.com/BulPlugins/BulMultiverse
            "worldmgr" // https://dev.bukkit.org/projects/worldmgr
    );

    private void addCustomCharts() {
        var worldManager = knownWorldManagers.stream()
                .filter(name -> getServer().getPluginManager().getPlugin(name) != null)
                .findAny().orElse("None");
        metrics.addCustomChart(new SimplePie("world_management_plugin", () -> worldManager));
    }

    public GroupProvider groupProvider() {
        return provider;
    }

    public ComponentBundle bundle() {
        return bundle;
    }
}
