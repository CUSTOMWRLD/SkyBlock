package com.customwrld.skyblock.util;

import com.customwrld.skyblock.SkyBlock;
import com.customwrld.skyblock.island.Island;
import org.bukkit.Location;

import java.util.concurrent.ThreadLocalRandom;

public class CuboidUtil {

    private static final int BOUNDS = 25000000;

    public static Location[] getOpenLocation(int borderSize) {
        int x = (int) Math.floor(ThreadLocalRandom.current().nextInt(BOUNDS) / (float) borderSize);
        int z = (int) Math.floor(ThreadLocalRandom.current().nextInt(BOUNDS) / (float) borderSize);

        for (Island island : SkyBlock.getIslandManager().getIslands().values()) {

            Location location = new Location(WorldUtil.WORLD, x, 0D, z);

            if (location.getX() - island.getCuboid().getCenter().getX() < 500 &&
                location.getZ() - island.getCuboid().getCenter().getZ() < 500) {
                return getOpenLocation(borderSize);
            }

        }

        Location location = new Location(WorldUtil.WORLD, x - borderSize, 0D, z - borderSize);
        Location location1 = new Location(WorldUtil.WORLD, x + borderSize, 256D, z + borderSize);
        Location location2 = new Location(WorldUtil.WORLD, x, 60, z);

        return new Location[]{location, location1, location2};
    }
}
