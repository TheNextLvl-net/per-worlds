package net.thenextlvl.perworlds;

import core.file.FileIO;
import core.file.format.GsonFile;
import core.i18n.file.ComponentBundle;
import core.io.IO;
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
import net.thenextlvl.perworlds.model.config.PluginConfig;
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
import java.util.Set;

@NullMarked
public final class PerWorldsPlugin extends JavaPlugin {
    public static final String ISSUES = "https://github.com/TheNextLvl-net/per-worlds/issues/new?template=bug_report.yml";
    public static final String DOCS_URL = "https://thenextlvl.net/docs/perworlds";

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

    private final FileIO<PluginConfig> config = new GsonFile<>(
            IO.of(getDataPath().resolve("config.json")), new PluginConfig()
    ).saveIfAbsent();

    public PerWorldsPlugin() {
        registerCommands();
    }

    @Override
    public void onLoad() {
        versionChecker.checkVersion();
        addCustomCharts();
        registerServices();
    }

    @Override
    public void onEnable() {
        scheduleDelayedInitTask();
        registerListeners();
        warnWorldManager();
        loadGroups();
    }

    @Override
    public void onDisable() {
        metrics.shutdown();
        persistGroups();
    }

    private void registerServices() {
        getServer().getServicesManager().register(GroupProvider.class, provider, this, ServicePriority.Highest);
    }

    private void scheduleDelayedInitTask() {
        if (!groupsExist) getServer().getGlobalRegionScheduler().execute(this, this::firstUseNotice);
    }

    private void firstUseNotice() {
        var separator = "-".repeat(86);
        getComponentLogger().warn(separator);
        getComponentLogger().warn("This is your first startup using PerWorlds");
        getComponentLogger().warn("The main command to interact with PerWorlds is '/world group'");
        getComponentLogger().warn("To automatically group all existing worlds, run '/world group auto'");
        getComponentLogger().warn("");
        getComponentLogger().warn("Before your settings come into effect, you have to define a group");
        getComponentLogger().warn("to migrate all pre-existing player data to");
        getComponentLogger().warn("You can do this by running '/world group migrate <group>'");
        getComponentLogger().warn("");
        getComponentLogger().warn("Refer to the wiki to learn how to manage groups: {}", DOCS_URL);
        getComponentLogger().warn(separator);
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
            event.registrar().register(command, "The main command to interact with this plugin");
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

    public FileIO<PluginConfig> configFile() {
        return config;
    }

    public PluginConfig config() {
        return config.getRoot();
    }

    public PaperGroupProvider groupProvider() {
        return provider;
    }

    public ComponentBundle bundle() {
        return bundle;
    }
}
