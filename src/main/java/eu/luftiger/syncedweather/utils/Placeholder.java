package eu.luftiger.syncedweather.utils;

import eu.luftiger.syncedweather.SyncedWeather;
import eu.luftiger.syncedweather.model.Weather;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Placeholder extends PlaceholderExpansion {

    private final SyncedWeather plugin;
    private final Weather weather;

    public Placeholder(SyncedWeather plugin) {
        this.plugin = plugin;
        this.weather = plugin.getWeatherService().getWeather();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "syncedweather";
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String indetifier) {

        if (indetifier.equals("temperatureC")) {
            return String.valueOf(weather.getTempC());
        }

        if (indetifier.equals("temperatureF")) {
            return String.valueOf(weather.getTempF());
        }

        if (indetifier.equals("wind_degree")) {
            return String.valueOf(weather.getWindDegree());
        }

        if (indetifier.equals("wind_compass_direction")) {
            if (weather.getWindDegree() <= 22.5 || weather.getWindDegree() >= 337.5) return "N";
            if (weather.getWindDegree() >= 22.5 && weather.getWindDegree() <= 67.5) return "NO";
            if (weather.getWindDegree() >= 67.5 && weather.getWindDegree() <= 112.5) return "O";
            if (weather.getWindDegree() >= 112.5 && weather.getWindDegree() <= 157.5) return "SO";
            if (weather.getWindDegree() >= 157.5 && weather.getWindDegree() <= 202.5) return "S";
            if (weather.getWindDegree() >= 202.5 && weather.getWindDegree() <= 247.5) return "SW";
            if (weather.getWindDegree() >= 247.5 && weather.getWindDegree() <= 292.5) return "W";
            if (weather.getWindDegree() >= 292.5 && weather.getWindDegree() <= 337.5) return "NW";
        }

        if (indetifier.equals("wind_speed")) {
            return String.valueOf(weather.getWindSpeed());
        }

        if (indetifier.equals("weather")) {
            if (weather.getWeatherName() != null) {
                return weather.getWeatherName();
            } else {
                return "unknown";
            }
        }

        if (indetifier.equals("location")) {
            if (weather.getLocationName() != null) {
                return weather.getLocationName();
            } else {
                return "unknown";
            }

        }

        return null;
    }
}
