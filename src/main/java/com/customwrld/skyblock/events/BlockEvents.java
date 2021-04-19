package com.customwrld.skyblock.events;

import com.customwrld.customlib.util.Cuboid;
import com.customwrld.skyblock.SkyBlock;
import com.customwrld.skyblock.island.Island;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPistonEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;

import java.util.List;

public class BlockEvents implements Listener {

    @EventHandler
    public void onPistonPush(BlockPistonExtendEvent event) {
        List<Block> blocks = event.getBlocks();
        Cuboid cuboid = null;
        for (Island island : SkyBlock.getIslandManager().getIslands().values()) {
            for (Block block : blocks) {
                if (island.getCuboid().contains(block)) {
                    cuboid = island.getCuboid();
                    break;
                }
            }
        }
        if (cuboid == null) {
            event.setCancelled(true);
            return;
        }
        for (Block block : blocks) {
            if (!cuboid.contains(block)) {
                event.setCancelled(true);
                return;
            }
        }

    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        for (Island island : SkyBlock.getIslandManager().getIslands().values()) {
            if (island.getCuboid().contains(event.getToBlock())) {
                return;
            }
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockExplosion(BlockExplodeEvent event) {
        event.blockList().clear();
    }
}
