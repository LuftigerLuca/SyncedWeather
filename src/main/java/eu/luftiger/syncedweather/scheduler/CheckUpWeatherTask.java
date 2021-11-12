package eu.luftiger.syncedweather.scheduler;

import eu.luftiger.syncedweather.SyncedWeather;
import eu.luftiger.syncedweather.utils.ConfigService;
import eu.luftiger.syncedweather.utils.WeatherService;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class CheckUpWeatherTask {

	private final SyncedWeather plugin;
	private final WeatherService weatherService;
	private final ConfigService configService;
	private String lastWeather;

	public CheckUpWeatherTask(SyncedWeather plugin) {
		this.plugin = plugin;
		this.weatherService = plugin.getWeatherService();
		this.configService = plugin.getConfigService();
	}

	public void start(){
		new BukkitRunnable(){
			@Override
			public void run() {

				String rawInfo = weatherService.getWeatherFromLocation().get("weather").toString()
						.replace("[", "")
						.replace("]", "")
						.replace("{", "")
						.replace("}", "")
						.replace(" ", "");

				List<String> weather = new ArrayList<>();
				for(String s : rawInfo.split(",")){
					weather.add(s.split("=")[1]);
				}

				lastWeather = weather.get(1);
				weatherService.setMinecraftWeather(weather.get(1));
			}
		}.runTaskTimer(plugin, 0 , 20L * 30);
	}
}
