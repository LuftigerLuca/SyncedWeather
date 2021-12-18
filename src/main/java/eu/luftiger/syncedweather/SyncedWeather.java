package eu.luftiger.syncedweather;

import eu.luftiger.syncedweather.commands.SyncedWeatherCommand;
import eu.luftiger.syncedweather.listeners.PlayerJoinListener;
import eu.luftiger.syncedweather.model.Weather;
import eu.luftiger.syncedweather.scheduler.CheckUpTimeTask;
import eu.luftiger.syncedweather.scheduler.CheckUpWeatherTask;
import eu.luftiger.syncedweather.utils.ConfigService;
import eu.luftiger.syncedweather.utils.Placeholder;
import eu.luftiger.syncedweather.utils.UpdateCheckService;
import eu.luftiger.syncedweather.utils.WeatherService;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class SyncedWeather extends JavaPlugin {

	private final Logger logger = Bukkit.getLogger();
	private WeatherService weatherService;
	private ConfigService configService;

	private String consolePrefix;
	private final String consoleLogo =
			"\n   _____                          ___          __        _   _                \n" +
			"  / ____|                        | \\ \\        / /       | | | |               \n" +
			" | (___  _   _ _ __   ___ ___  __| |\\ \\  /\\  / /__  __ _| |_| |__   ___ _ __  \n" +
			"  \\___ \\| | | | '_ \\ / __/ _ \\/ _` | \\ \\/  \\/ / _ \\/ _` | __| '_ \\ / _ \\ '__| \n" +
			"  ____) | |_| | | | | (_|  __/ (_| |  \\  /\\  /  __/ (_| | |_| | | |  __/ |    \n" +
			" |_____/ \\__, |_| |_|\\___\\___|\\__,_|   \\/  \\/ \\___|\\__,_|\\__|_| |_|\\___|_|    \n" +
			"          __/ |                                                               \n" +
			"         |___/";

	private boolean isNewerVersion;

	@Override
	public void onEnable() {
		logger.info(consoleLogo);
		consolePrefix = "[" + this.getDescription().getPrefix() + "]";

		logger.info(consolePrefix + " enabling plugin...");

		logger.info(consolePrefix + " loading config...");
		configService = new ConfigService(this);
		configService.createDefaults();

		if(configService.getConfig().getString("API_KEY").isEmpty()){
			logger.warning(consolePrefix + " the api key is missing!!");
			logger.info(consolePrefix + " disabling plugin...");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		logger.info(consolePrefix + " loading weatherservice...");
		this.weatherService = new WeatherService(this);

		logger.info(consolePrefix + " starting checkup-services...");
		if(configService.getConfig().getBoolean("SyncWeather")){
			new CheckUpWeatherTask(this).start();
		}
		if(configService.getConfig().getBoolean("SyncTime")) {
			new CheckUpTimeTask(this).start();
		}

		logger.info(consolePrefix + " loading commands...");
		getCommand("syncedweather").setExecutor(new SyncedWeatherCommand(this));

		logger.info(consolePrefix + " registering listeners...");
		PluginManager pluginManager = Bukkit.getPluginManager();
		pluginManager.registerEvents(new PlayerJoinListener(this), this);

		logger.info(consolePrefix + " checking for updates...");

		new UpdateCheckService(this, 97574).getVersion(version -> {
			if(!this.getDescription().getVersion().equals(version)){
				isNewerVersion = true;
				logger.info(consolePrefix + " there is a new version of this plugin: https://www.spigotmc.org/resources/syncedweather.97574/");
			}
		});


		if(getServer().getPluginManager().getPlugin("PlaceholderAPI") != null){
			logger.info(consolePrefix + " loading placeholders...");
			new Placeholder(this).register();
		}

		Metrics metrics = new Metrics(this, 	13631);
	}

	public void reload(){
		logger.info(consolePrefix + " loading the config...");
		configService.createDefaults();

		if(configService.getConfig().getString("API_KEY").isEmpty()){
			logger.warning(consolePrefix + " the api key is missing!!");
			logger.info(consolePrefix + " disabling plugin...");
			getServer().getPluginManager().disablePlugin(this);
		}

		logger.info(consolePrefix + " loading the weather...");
		weatherService.getWeather().update();
	}

	public WeatherService getWeatherService() {
		return weatherService;
	}

	public ConfigService getConfigService() {
		return configService;
	}

	public String getConsolePrefix() {
		return consolePrefix;
	}

	public boolean isNewerVersion() {
		return isNewerVersion;
	}
}
