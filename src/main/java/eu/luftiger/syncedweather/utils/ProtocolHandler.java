package eu.luftiger.syncedweather.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import eu.luftiger.syncedweather.SyncedWeather;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class ProtocolHandler {

    private final SyncedWeather plugin;
    private final ProtocolManager protocolManager;

    public ProtocolHandler(SyncedWeather plugin) {
        this.plugin = plugin;
        this.protocolManager = plugin.getProtocolManager();
    }


    public void sendSnowbiome() {
        if (protocolManager == null) {
            return;
        }

        PacketContainer snowPacket = new PacketContainer(PacketType.Play.Server.MAP_CHUNK);
        snowPacket.getDoubles().write(3, 39.936);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            try {
                protocolManager.sendServerPacket(onlinePlayer, snowPacket);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
