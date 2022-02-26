package eu.luftiger.syncedweather.scheduler;

import eu.luftiger.syncedweather.SyncedWeather;
import eu.luftiger.syncedweather.utils.WeatherService;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class CheckUpWeatherTask {

	private final SyncedWeather plugin;
	private final WeatherService weatherService;
	private BukkitTask runnable;

	public CheckUpWeatherTask(SyncedWeather plugin) {
		this.plugin = plugin;
		this.weatherService = plugin.getWeatherService();
	}

	public void start() {
		runnable = new BukkitRunnable() {

			@Override
			public void run() {
				weatherService.getWeather().update();
				if(weatherService.getWeather().getWeatherName() != null){
					Bukkit.getScheduler().runTask(plugin, new Runnable() {
						@Override
						public void run() {
							weatherService.setMinecraftWeather(weatherService.getWeather().getWeatherName());
						}
					});
				}
			}
		}.runTaskTimerAsynchronously(plugin, 0,20L*120);
	}

	public void stop(){
		if(runnable != null){
			runnable.cancel();
		}
	}
}
