package eu.luftiger.syncedweather.commands;

import eu.luftiger.syncedweather.SyncedWeather;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class SyncedWeatherCommand implements TabExecutor {

    private final SyncedWeather plugin;

    public SyncedWeatherCommand(SyncedWeather plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length >= 1){
            switch (args[0].toLowerCase()){
                case "info":
                    new InfoSubcommand(plugin).execute(sender, args);
                    break;
                case "currentweather":
                    new CurrentWeatherSubcommand(plugin).execute(sender, args);
                    break;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();

        if(args.length <= 1){
            completions.add("info");
            completions.add("currentWeather");
        }
        return null;
    }
}
