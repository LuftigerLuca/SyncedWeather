package eu.luftiger.syncedweather;

import eu.luftiger.syncedweather.scheduler.CheckUpTimeTask;
import eu.luftiger.syncedweather.scheduler.CheckUpWeatherTask;
import eu.luftiger.syncedweather.utils.ConfigService;
import eu.luftiger.syncedweather.utils.WeatherService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class SyncedWeather extends JavaPlugin {

	private final Logger logger = Bukkit.getLogger();
	private WeatherService weatherService;
	private ConfigService configService;

	@Override
	public void onEnable() {

		logger.info("------------[SyncedWeather]------------");
		logger.info("enabling plugin...");

		logger.info("loading config...");
		configService = new ConfigService(this);
		configService.createDefaults();

		if(configService.getConfig().getString("API_KEY").isEmpty()){
			logger.warning("the api key is missing!!");
			logger.info("disabling plugin...");
			logger.info("------------[SyncedWeather]------------");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		logger.info("loading weather-service...");
		weatherService = new WeatherService(this);

		logger.info("starting checkup-services...");
		if(configService.getConfig().getBoolean("SyncWeather")){
			new CheckUpWeatherTask(this).start();
		}
		if(configService.getConfig().getBoolean("SyncTime")) {
			new CheckUpTimeTask(this).start();
		}

		logger.info("------------[SyncedWeather]------------");
	}

	public WeatherService getWeatherService() {
		return weatherService;
	}

	public ConfigService getConfigService() {
		return configService;
	}
}
