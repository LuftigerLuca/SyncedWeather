package eu.luftiger.syncedweather.utils;

import eu.luftiger.syncedweather.SyncedWeather;
import eu.luftiger.syncedweather.model.Weather;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;

public class WeatherService {

    private final ConfigService configService;
    private final ProtocolHandler protocolHandler;
    private final Weather weather;

    public WeatherService(SyncedWeather plugin) {
        this.configService = plugin.getConfigService();
        this.protocolHandler = plugin.getProtocolHandler();
        this.weather = new Weather(plugin);
    }

    public void setMinecraftWeather(String weatherName) {
        if (weatherName.equalsIgnoreCase("rain")) {
            for (String worldName : configService.getConfig().getStringList("Worlds")) {
                World world = Bukkit.getWorld(worldName);
                if (world != null) {
                    world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
                    world.setStorm(true);
                }
            }
        } else if (weatherName.equalsIgnoreCase("thunderstorm")) {
            for (String worldName : configService.getConfig().getStringList("Worlds")) {
                World world = Bukkit.getWorld(worldName);
                if (world != null) {
                    world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
                    world.setStorm(true);
                    world.setThundering(true);
                }
            }
        } else if (weatherName.equalsIgnoreCase("clear") || weatherName.equalsIgnoreCase("clouds") || weatherName.equalsIgnoreCase("snow")) {
            for (String worldName : configService.getConfig().getStringList("Worlds")) {
                World world = Bukkit.getWorld(worldName);
                if (world != null) {
                    world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
                    world.setStorm(false);
                    world.setThundering(false);
                }
            }
        }

        switch (weatherName.toLowerCase()) {
            case "rain":
                for (String worldName : configService.getConfig().getStringList("Worlds")) {
                    World world = Bukkit.getWorld(worldName);
                    if (world != null) {
                        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
                        world.setStorm(true);
                    }
                }
                break;

            case "thunderstorm":
                for (String worldName : configService.getConfig().getStringList("Worlds")) {
                    World world = Bukkit.getWorld(worldName);
                    if (world != null) {
                        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
                        world.setStorm(true);
                        world.setThundering(true);
                    }
                }
                break;

            case "clear", "clouds":
                for (String worldName : configService.getConfig().getStringList("Worlds")) {
                    World world = Bukkit.getWorld(worldName);
                    if (world != null) {
                        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
                        world.setStorm(false);
                        world.setThundering(false);
                    }
                }
                break;

            case "snow":
                if (protocolHandler != null) {
                    for (String worldName : configService.getConfig().getStringList("Worlds")) {
                        World world = Bukkit.getWorld(worldName);
                        if (world != null) {
                            world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
                            world.setStorm(true);
                            protocolHandler.sendSnowbiome();
                        }
                    }
                }
                break;
        }
    }

    public Weather getWeather() {
        return weather;
    }
}
