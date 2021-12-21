package eu.luftiger.syncedweather.plugin.commands;

import eu.luftiger.syncedweather.plugin.SyncedWeather;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

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
                case "reload":
                    new ReloadSubcommand(plugin).execute(sender, args);
                    break;
                case "help":
                    new HelpSubcommand(plugin).execute(sender, args);
            }
        }else {
            sender.sendMessage(plugin.getConfigService().getMessage("Messages.lenght_error", true));
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> finalCompletions = new ArrayList<>();

        if(args.length <= 1){
            completions.add("info");
            completions.add("currentWeather");
            completions.add("help");

            if(sender instanceof Player){
                Player player = (Player) sender;

                if(player.hasPermission("syncedweather.reload")) completions.add("reload");
            }
        }

        if (!args[args.length - 1].equals("")) {
            for (String completion : completions) {
                if (completion.toLowerCase().startsWith(args[args.length - 1].toLowerCase())) {
                    finalCompletions.add(completion);
                }
            }
        } else {
            finalCompletions.addAll(completions);
        }

        return finalCompletions;
    }
}
