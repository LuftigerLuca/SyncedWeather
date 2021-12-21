package eu.luftiger.syncedweather.plugin.commands;

import eu.luftiger.syncedweather.plugin.SyncedWeather;
import eu.luftiger.syncedweather.plugin.utils.ConfigService;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadSubcommand {

    private final SyncedWeather plugin;
    private final ConfigService configService;

    public ReloadSubcommand(SyncedWeather plugin){
        this.plugin = plugin;
        this.configService = plugin.getConfigService();
    }

    public void execute(CommandSender sender, String[] args){
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(player.hasPermission("syncedweather.reload")){
                reload(sender);
            }else {
                player.sendMessage(configService.getMessage("Messages.permission_error", true));
            }
        }else {
            reload(sender);
        }
    }

    private void reload(CommandSender sender){
        sender.sendMessage(configService.getMessage("Messages.reload_plugin", true));
        plugin.reload();
        sender.sendMessage(configService.getMessage("Messages.reloaded_plugin",true));
    }
}
