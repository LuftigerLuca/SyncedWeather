package eu.luftiger.syncedweather;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import eu.luftiger.syncedweather.commands.SyncedWeatherCommand;
import eu.luftiger.syncedweather.listeners.PlayerJoinListener;
import eu.luftiger.syncedweather.scheduler.CheckUpTask;
import eu.luftiger.syncedweather.utils.*;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Logger;


public final class SyncedWeather extends JavaPlugin {

    private final Logger logger = getLogger();
    private WeatherService weatherService;
    private ConfigService configService;
    private CheckUpTask checkUpTask;
    private ProtocolManager protocolManager;
    private ProtocolHandler protocolHandler;
    private boolean isNewerVersion;

    @Override
    public void onEnable() {

        logger.info("enabling plugin...");

        logger.info("loading config...");
        configService = new ConfigService(this);
        configService.createDefaults("config.yml", true, true);

        if (Objects.requireNonNull(configService.getConfig().getString("API_KEY")).isEmpty()) {
            logger.warning("the api key is missing!!");
            logger.info("disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        logger.info("loading weatherservice...");
        this.weatherService = new WeatherService(this);

        logger.info("starting checkup-services...");
        checkUpTask = new CheckUpTask(this);
        if (configService.getConfig().getBoolean("SyncTime") && configService.getConfig().getBoolean("SyncWeather")) {
            checkUpTask.start();
        }

        logger.info("loading commands...");
        getCommand("syncedweather").setExecutor(new SyncedWeatherCommand(this));

        logger.info("registering listeners...");
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(this), this);

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            logger.info("loading placeholders...");
            new Placeholder(this).register();
        }

        if (getServer().getPluginManager().getPlugin("ProtocolLib") != null) {
            logger.info("loading ProtocolLib...");
            protocolManager = ProtocolLibrary.getProtocolManager();
            protocolHandler = new ProtocolHandler(this);
        }

        new UpdateCheckService(this, 97574).getVersion(version -> {
            if (!this.getDescription().getVersion().equals(version)) {
                isNewerVersion = true;
                logger.info("There is a new version of this plugin: https://www.spigotmc.org/resources/syncedweather.97574/!");
            }
        });

        new Metrics(this, 13631);
    }

    public void reload() {
        logger.info("loading the config...");
        configService.createDefaults("config.yml", true, true);

        if (Objects.requireNonNull(configService.getConfig().getString("API_KEY")).isEmpty()) {
            logger.warning("the api key is missing!!");
            logger.info("disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
        }

        logger.info("loading the weather...");
        weatherService.getWeather().update();

        logger.info("loading checkup-services...");

        if (configService.getConfig().getBoolean("SyncTime") && configService.getConfig().getBoolean("SyncWeather")) {
            checkUpTask.start();
        } else checkUpTask.stop();
    }

    public WeatherService getWeatherService() {
        return weatherService;
    }

    public ConfigService getConfigService() {
        return configService;
    }

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    public ProtocolHandler getProtocolHandler() {
        return protocolHandler;
    }

    public boolean isNewerVersion() {
        return isNewerVersion;
    }
}
