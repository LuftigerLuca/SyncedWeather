package eu.luftiger.syncedweather.plugin.commands;

import eu.luftiger.syncedweather.plugin.SyncedWeather;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class HelpSubcommand {

    private final SyncedWeather plugin;

    public HelpSubcommand(SyncedWeather plugin) {
        this.plugin = plugin;
    }

    public void execute(CommandSender sender, String[] args){
        for (String line : plugin.getConfigService().getConfig().getStringList("Messages.help")){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
        }
    }
}
