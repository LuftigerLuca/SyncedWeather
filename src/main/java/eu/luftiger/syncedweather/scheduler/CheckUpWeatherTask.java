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
				weatherService.getWeather().update();
				if(weatherService.getWeather().getWeatherName() != null){
					weatherService.setMinecraftWeather(weatherService.getWeather().getWeatherName());
				}
			}
		}.runTaskTimer(plugin, 0,20L*60);
	}
}
