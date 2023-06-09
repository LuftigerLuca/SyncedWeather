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

public class CheckUpTask {

    private final SyncedWeather plugin;
    private final ConfigService configService;
    private final WeatherService weatherService;
    private BukkitTask runnable;

    public CheckUpTask(SyncedWeather plugin) {
        this.plugin = plugin;
        this.configService = plugin.getConfigService();
        this.weatherService = plugin.getWeatherService();
    }

    public void start() {
        DateTimeZone timeZone = DateTimeZone.forID(configService.getConfig().getString("TimeZone").trim());

        runnable = new BukkitRunnable() {
            int cycle = 0;

            @Override
            public void run() {
                // This is the code that will be executed every second and checks if the daytime is synced
                if(configService.getConfig().getBoolean("SyncTime")){
                    DateTime dateTime = new DateTime(timeZone);
                    for (String worldName : configService.getConfig().getStringList("Worlds")) {
                        World world = Bukkit.getWorld(worldName);
                        if (world != null) {

                            double time = ((dateTime.getHourOfDay() * 1000) - 6000) + (dateTime.getMinuteOfHour() * 16.6);

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

                if(configService.getConfig().getBoolean("SyncWeather")){
                    cycle++;
                    if(cycle == 2){
                        weatherService.getWeather().update();
                        if (weatherService.getWeather().getWeatherName() != null) {
                            Bukkit.getScheduler().runTask(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    System.out.println("Setting weather to " + weatherService.getWeather().getWeatherName());
                                    weatherService.setMinecraftWeather(weatherService.getWeather().getWeatherName());
                                }
                            });
                        }

                        cycle = 0;
                    }
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0, 20L * 30);
    }

    public void stop() {
        if (runnable != null) {
            runnable.cancel();
        }
    }
}
