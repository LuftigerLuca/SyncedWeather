package eu.luftiger.syncedweather.utils;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import eu.luftiger.syncedweather.SyncedWeather;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class WeatherService {

	private final SyncedWeather plugin;
	private final ConfigService configService;
	private final Logger logger;

	public WeatherService(SyncedWeather plugin) {
		this.plugin = plugin;
		this.configService = plugin.getConfigService();
		this.logger = plugin.getLogger();
	}

	public static Map<String,Object> jsonToMap(String str){
		return new Gson().fromJson(str, new TypeToken<HashMap<String,Object>>() {}.getType());
	}

	public Map<String, Object> getWeatherFromLocation(){
		Map<String, Object > respMap = null;
		String API_KEY = configService.getConfig().getString("API_KEY");
		String LOCATION = configService.getConfig().getString("Location");
		String urlString = "http://api.openweathermap.org/data/2.5/weather?q=" + LOCATION + "&appid=" + API_KEY + "&units=imperial";

		try{
			StringBuilder result = new StringBuilder();
			URL url = new URL(urlString);
			URLConnection conn = url.openConnection();
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null){
				result.append(line);
			}
			rd.close();;
			respMap = jsonToMap (result.toString());

		}catch (IOException e){
			logger.warning("There is an error with the request for the data:   " + e.getMessage());
		}
		return respMap;
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
}
