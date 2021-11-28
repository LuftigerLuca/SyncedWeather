package eu.luftiger.syncedweather.scheduler;

import eu.luftiger.syncedweather.SyncedWeather;
import eu.luftiger.syncedweather.utils.WeatherService;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class CheckUpWeatherTask {

	private final SyncedWeather plugin;
	private final WeatherService weatherService;

	public CheckUpWeatherTask(SyncedWeather plugin) {
		this.plugin = plugin;
		this.weatherService = plugin.getWeatherService();
	}

	public void start() {
		new BukkitRunnable() {

			@Override
			public void run() {
				if(weatherService.getWeatherFromApi() != null && weatherService.getWeatherFromApi().get("weather") != null) {
					weatherService.getWeather().putAll(weatherService.getWeatherFromApi());

					String rawInfo = weatherService.getWeather().get("weather").toString()
							.replace("[", "")
							.replace("]", "")
							.replace("{", "")
							.replace("}", "")
							.replace(" ", "");

					List<String> weather = new ArrayList<>();

					for (String s : rawInfo.split(",")) {
						weather.add(s.split("=")[1]);
					}

					if(weather.size() < 1){
						Bukkit.getLogger().warning("[SyncedWeather] The weather station you have selected is not compatible!");
						cancel();
					}

					weatherService.setMinecraftWeather(weather.get(1));
				}
			}
		}.runTaskTimer(plugin, 0,20L*60);
	}
}
