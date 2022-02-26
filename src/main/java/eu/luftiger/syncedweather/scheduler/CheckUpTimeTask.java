package eu.luftiger.syncedweather.scheduler;

import eu.luftiger.syncedweather.SyncedWeather;
import eu.luftiger.syncedweather.utils.ConfigService;
import eu.luftiger.syncedweather.utils.WeatherService;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class CheckUpTimeTask {

	private final SyncedWeather plugin;
	private final ConfigService configService;
	private BukkitTask runnable;

	public CheckUpTimeTask(SyncedWeather plugin) {
		this.plugin = plugin;
		this.configService = plugin.getConfigService();
	}

	public void start(){
		DateTimeZone timeZone = DateTimeZone.forID(configService.getConfig().getString("TimeZone"));
		runnable = new BukkitRunnable() {
			@Override
			public void run() {
				DateTime dateTime = new DateTime(timeZone);
				for(String worldName : configService.getConfig().getStringList("Worlds")){
					World world = Bukkit.getWorld(worldName);
					if(world != null){

						double time = ((dateTime.getHourOfDay()*1000)-6000)+(dateTime.getMinuteOfHour()*16.6);

						Bukkit.getScheduler().runTask(plugin, new Runnable() {
							@Override
							public void run() {
								world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
								world.setTime((long) time);
							}
						});
					}
				}
			}
		}.runTaskTimerAsynchronously(plugin, 0, 20L * 30);
	}

	public void stop(){
		if(runnable != null){
			runnable.cancel();
		}
	}
}
