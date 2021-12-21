package eu.luftiger.syncedweather.plugin.commands;

import eu.luftiger.syncedweather.plugin.SyncedWeather;
import eu.luftiger.syncedweather.plugin.model.Weather;
import eu.luftiger.syncedweather.plugin.utils.ConfigService;
import eu.luftiger.syncedweather.plugin.utils.WeatherService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.swing.*;
import java.util.List;

public class CurrentWeatherSubcommand {

    private final SyncedWeather plugin;
    private final ConfigService configService;
    private final WeatherService weatherService;

    public CurrentWeatherSubcommand(SyncedWeather plugin){
        this.plugin = plugin;
        this.configService = plugin.getConfigService();
        this.weatherService = plugin.getWeatherService();
    }

    public void execute(CommandSender sender, String[] args){
        Weather weather = weatherService.getWeather();

        List<String> lines = configService.getConfig().getStringList("Messages.info_map.lines");
        String unknown = configService.getMessage("Messages.info_map.unknown", false);

        String weatherName = unknown;
        String locationName = unknown;
        String temp = weather.getTempC() + "°C §8/§r " + weather.getTempF() + "°F";
        String wind;

        if (weather.getWeatherName() != null) weatherName = configService.getMessage("Messages.info_map.weathernames." + weather.getWeatherName().toLowerCase(), false);
        if (weather.getLocationName() != null) locationName = weather.getLocationName();

        String compassDirection = unknown;
        if(weather.getWindDegree() <= 22.5 || weather.getWindDegree() >= 337.5) compassDirection = "N";
        if(weather.getWindDegree() >= 22.5 && weather.getWindDegree() <= 67.5) compassDirection = "NO";
        if(weather.getWindDegree() >= 67.5 && weather.getWindDegree() <= 112.5) compassDirection = "O";
        if(weather.getWindDegree() >= 112.5 && weather.getWindDegree() <= 157.5) compassDirection = "SO";
        if(weather.getWindDegree() >= 157.5 && weather.getWindDegree() <= 202.5) compassDirection = "S";
        if(weather.getWindDegree() >= 202.5 && weather.getWindDegree() <= 247.5) compassDirection = "SW";
        if(weather.getWindDegree() >= 247.5 && weather.getWindDegree() <= 292.5) compassDirection = "W";
        if(weather.getWindDegree() >= 292.5 && weather.getWindDegree() <= 337.5) compassDirection = "NW";

        wind = weather.getWindSpeed() + "km/h§8,§r " + compassDirection;

        for(String line : lines){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', line)
                    .replace("{location}", locationName)
                    .replace("{temperature}", temp)
                    .replace("{weather}", weatherName)
                    .replace("{wind}", wind));
        }

        plugin.getVersionWrapper().sendSnow((Player) sender);

    }
}
