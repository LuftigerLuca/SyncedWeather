package eu.luftiger.syncedweather.utils;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import eu.luftiger.syncedweather.SyncedWeather;
import eu.luftiger.syncedweather.model.Weather;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class WeatherService {

	private final SyncedWeather plugin;
	private final ConfigService configService;
	private final Weather weather;

	public WeatherService(SyncedWeather plugin) {
		this.plugin = plugin;
		this.configService = plugin.getConfigService();
		this.weather = new Weather(plugin);
	}

	public void setMinecraftWeather(String weatherName){
		if(weatherName.equalsIgnoreCase("rain")){
			for (String worldName : configService.getConfig().getStringList("Worlds")){
				World world = Bukkit.getWorld(worldName);
				if (world != null) {
					world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
					world.setStorm(true);
				}
			}
		}

		else if(weatherName.equalsIgnoreCase("thunderstorm")){
			for (String worldName : configService.getConfig().getStringList("Worlds")){
				World world = Bukkit.getWorld(worldName);
				if (world != null) {
					world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
					world.setStorm(true);
					world.setThundering(true);
				}
			}
		}

		else if(weatherName.equalsIgnoreCase("clear") || weatherName.equalsIgnoreCase("clouds") || weatherName.equalsIgnoreCase("snow")){
			for (String worldName : configService.getConfig().getStringList("Worlds")){
				World world = Bukkit.getWorld(worldName);
				if (world != null) {
					world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
					world.setStorm(false);
					world.setThundering(false);
				}
			}
		}
	}

	public Weather getWeather() {
		return weather;
	}
}
