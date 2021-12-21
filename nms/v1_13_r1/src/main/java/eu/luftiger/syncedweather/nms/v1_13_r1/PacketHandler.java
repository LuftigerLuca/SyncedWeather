package eu.luftiger.syncedweather.nms.v1_13_r1;

import eu.luftiger.syncedweather.nms.common.VersionWrapper;
import net.minecraft.server.v1_13_R2.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_13_R2.Particles;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PacketHandler implements VersionWrapper {

    @Override
    public void sendSnow(Player player) {
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(Particles.w, true, (float) player.getLocation().getX(), (float) player.getLocation().getY(), (float) player.getLocation().getZ(), 0, 0, 0, 0, 1);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}
