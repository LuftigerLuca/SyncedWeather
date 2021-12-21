package eu.luftiger.syncedweather.plugin.scheduler;

import eu.luftiger.syncedweather.plugin.SyncedWeather;
import eu.luftiger.syncedweather.plugin.utils.ConfigService;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class CheckUpTimeTask {
	private final SyncedWeather plugin;
	private final ConfigService configService;

	public CheckUpTimeTask(SyncedWeather plugin) {
		this.plugin = plugin;
		this.configService = plugin.getConfigService();
	}

	public void start(){
		DateTimeZone timeZone = DateTimeZone.forID(configService.getConfig().getString("TimeZone"));
		new BukkitRunnable() {
			@Override
			public void run() {
				DateTime dateTime = new DateTime(timeZone);
				for(String worldName : configService.getConfig().getStringList("Worlds")){
					World world = Bukkit.getWorld(worldName);
					if(world != null){
						world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
						double time = ((dateTime.getHourOfDay()*1000)-6000)+(dateTime.getMinuteOfHour()*16.6);
						world.setTime((long) time);
					}
				}
			}
		}.runTaskTimer(plugin, 0, 20L * 30);
	}
}