package eu.luftiger.syncedweather.commands;

import eu.luftiger.syncedweather.SyncedWeather;
import eu.luftiger.syncedweather.utils.ConfigService;
import eu.luftiger.syncedweather.utils.WeatherService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

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
        List<String> lines = configService.getConfig().getStringList("Messages.InfoMap.lines");
        Map<String, Object> weatherMap = weatherService.getWeather();



        String rawTemp = weatherService.getMap(weatherMap.get("main").toString()).get("temp").toString();
        DecimalFormat format = new DecimalFormat("#0.00");
        float tempF = Float.parseFloat(rawTemp);
        float tempC = (tempF - 32) * 5/9;

        for(String line : lines){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', line)
                    .replace("{location}", weatherMap.get("name").toString())
                    .replace("{temperature}", format.format(tempF) + "°F / " + format.format(tempC) + "°C"));
        }
    }
}
