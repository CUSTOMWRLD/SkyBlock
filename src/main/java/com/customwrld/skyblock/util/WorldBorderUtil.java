package com.customwrld.skyblock.util;

import com.customwrld.customlib.util.Cuboid;
import com.customwrld.customlib.util.NMSUtil;
import net.minecraft.server.v1_16_R3.PacketPlayOutWorldBorder;
import net.minecraft.server.v1_16_R3.WorldBorder;
import net.minecraft.server.v1_16_R3.WorldServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.Player;

public class WorldBorderUtil {

    public static void sendWorldBorder(Player player, Cuboid cuboid) {
        WorldBorder worldBorder = new WorldBorder();
        worldBorder.setCenter(cuboid.getCenter().getX(), cuboid.getCenter().getZ());
        worldBorder.setSize(cuboid.getSizeX());
        worldBorder.setDamageAmount(0);
        worldBorder.setDamageBuffer(0);
        worldBorder.setWarningDistance(5);
        worldBorder.setWarningTime(2);
        worldBorder.world = ((CraftWorld) WorldUtil.WORLD).getHandle();
        NMSUtil.sendPacket(player, new PacketPlayOutWorldBorder(worldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.INITIALIZE));
    }
}
