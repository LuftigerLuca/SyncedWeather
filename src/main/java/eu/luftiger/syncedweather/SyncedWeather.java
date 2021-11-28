package eu.luftiger.syncedweather;

import eu.luftiger.syncedweather.scheduler.CheckUpTimeTask;
import eu.luftiger.syncedweather.scheduler.CheckUpWeatherTask;
import eu.luftiger.syncedweather.utils.ConfigService;
import eu.luftiger.syncedweather.utils.WeatherService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class SyncedWeather extends JavaPlugin {

	private final Logger logger = Bukkit.getLogger();
	private WeatherService weatherService;
	private ConfigService configService;

	private String consolePrefix;
	private String consoleLogo =
			"\n   _____                          ___          __        _   _                \n" +
			"  / ____|                        | \\ \\        / /       | | | |               \n" +
			" | (___  _   _ _ __   ___ ___  __| |\\ \\  /\\  / /__  __ _| |_| |__   ___ _ __  \n" +
			"  \\___ \\| | | | '_ \\ / __/ _ \\/ _` | \\ \\/  \\/ / _ \\/ _` | __| '_ \\ / _ \\ '__| \n" +
			"  ____) | |_| | | | | (_|  __/ (_| |  \\  /\\  /  __/ (_| | |_| | | |  __/ |    \n" +
			" |_____/ \\__, |_| |_|\\___\\___|\\__,_|   \\/  \\/ \\___|\\__,_|\\__|_| |_|\\___|_|    \n" +
			"          __/ |                                                               \n" +
			"         |___/";

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

		logger.info(consolePrefix + " loading weather-service...");
		weatherService = new WeatherService(this);

		logger.info(consolePrefix + " starting checkup-services...");
		if(configService.getConfig().getBoolean("SyncWeather")){
			new CheckUpWeatherTask(this).start();
		}
		if(configService.getConfig().getBoolean("SyncTime")) {
			new CheckUpTimeTask(this).start();
		}

	}

	public WeatherService getWeatherService() {
		return weatherService;
	}

	public ConfigService getConfigService() {
		return configService;
	}
}
