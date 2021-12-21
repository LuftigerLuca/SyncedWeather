package eu.luftiger.syncedweather.plugin.utils;

import eu.luftiger.syncedweather.nms.common.VersionWrapper;
import org.bukkit.Bukkit;

public class VersionMatcher {

    public VersionWrapper match() {
        final String serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].substring(1).toLowerCase();
        try {
            return (VersionWrapper) Class.forName("eu.luftiger.syncedweather.nms.v" + serverVersion + ".PacketHandler").newInstance();
        } catch (IllegalAccessException | InstantiationException exception) {
            throw new IllegalStateException("Failed to instantiate version wrapper for version " + serverVersion, exception);
        } catch (ClassNotFoundException exception) {
            throw new IllegalStateException("SyncedWeather does not support server version \"" + serverVersion + "\"", exception);
        }
    }
}