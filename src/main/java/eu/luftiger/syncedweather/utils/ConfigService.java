package eu.luftiger.syncedweather.utils;

import eu.luftiger.syncedweather.SyncedWeather;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ConfigService {

	private final SyncedWeather plugin;
	private YamlConfiguration config;

	public ConfigService(SyncedWeather plugin){
		this.plugin = plugin;
	}

	public void createDefaults() {

		String name = "config.yml";

		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}

		File file = new File(plugin.getDataFolder().getPath() + "/" + name);

		if (!file.exists()) {
			InputStream inputStream = plugin.getResource(name);
			try {
				assert inputStream != null;
				Files.copy(inputStream, Paths.get(plugin.getDataFolder().getPath() + "/" + name), new CopyOption[0]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				ConfigUpdater.update(plugin, name, file, new ArrayList<>());
			} catch (IOException e) {
				e.printStackTrace();

			}
		}
		config = YamlConfiguration.loadConfiguration(file);
	}

	public String getMessage(String path, boolean withPrefix){
		if(withPrefix) return ChatColor.translateAlternateColorCodes('&', config.getString("Messages.prefix") + " " + config.getString(path));
		return ChatColor.translateAlternateColorCodes('&', config.getString(path));
	}

	public YamlConfiguration getConfig() {
		return config;
	}
}
