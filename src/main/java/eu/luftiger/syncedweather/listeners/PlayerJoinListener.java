package eu.luftiger.syncedweather.listeners;

import eu.luftiger.syncedweather.SyncedWeather;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final SyncedWeather plugin;

    public PlayerJoinListener(SyncedWeather plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("syncedweather.updateinfo") || player.isOp()) {
            if (plugin.isNewerVersion()) {
                player.sendMessage(plugin.getConfigService().getMessage("Messages.prefix", false) + "§fThere is a new version of this plugin: https://www.spigotmc.org/resources/syncedweather.97574/ !");
            }
        }
    }
}
