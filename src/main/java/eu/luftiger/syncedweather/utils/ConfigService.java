package eu.luftiger.syncedweather.utils;

import eu.luftiger.luftigerlib.configuration.spigot.SpigotConfiguration;
import eu.luftiger.syncedweather.SyncedWeather;

public class ConfigService extends SpigotConfiguration {

    public ConfigService(SyncedWeather plugin) {
        super(plugin);
    }

    public String getMessage(String path, boolean withPrefix) {
        if (withPrefix) return getConverted("Messages.prefix") + " " + getConverted(path);
        return getConverted(path);
    }
}
