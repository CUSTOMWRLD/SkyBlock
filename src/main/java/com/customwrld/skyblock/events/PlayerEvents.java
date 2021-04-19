package com.customwrld.skyblock.events;

import com.customwrld.customlib.util.CC;
import com.customwrld.skyblock.SkyBlock;
import com.customwrld.skyblock.island.Island;
import com.customwrld.skyblock.util.WorldBorderUtil;
import com.customwrld.skyblock.util.WorldUtil;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Map;
import java.util.UUID;

public class PlayerEvents implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        for (Island island : SkyBlock.getIslandManager().load(event.getPlayer())) {
            SkyBlock.getIslandManager().getIslands().put(event.getPlayer().getUniqueId(), island);
            if (island.getCuboid().contains(event.getPlayer())) {
                WorldBorderUtil.sendWorldBorder(event.getPlayer(), island.getCuboid());
                Bukkit.getScheduler().scheduleSyncDelayedTask(SkyBlock.getSkyBlock(), () -> {
                    WorldBorderUtil.sendWorldBorder(event.getPlayer(), island.getCuboid());
                }, 20);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        SkyBlock.getIslandManager().save(SkyBlock.getIslandManager().getIsland(event.getPlayer()));
        for (Map.Entry<UUID, Island> entry : SkyBlock.getIslandManager().getIslands().entrySet()) {
            if (entry.getKey().equals(event.getPlayer().getUniqueId())) {
                SkyBlock.getIslandManager().getIslands().remove(entry.getKey(), entry.getValue());
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (player.hasPermission("skyblock.admin")) {
            return;
        }

        if (block.getWorld() != WorldUtil.WORLD) {
            return;
        }

        for (Island island : SkyBlock.getIslandManager().getIslands().values()) {
            if (island.getCuboid().contains(block)) {
                if (island.getOwner() == player.getUniqueId() || island.getTrusted().contains(player.getUniqueId())) {
                    return;
                }
            }
        }
        event.setCancelled(true);
        player.spawnParticle(Particle.SMOKE_NORMAL, block.getLocation(), 3);
        player.sendMessage(CC.RED + "You do not have permission for this island.");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (player.hasPermission("skyblock.admin")) {
            return;
        }

        if (block.getWorld() != WorldUtil.WORLD) {
            return;
        }

        for (Island island : SkyBlock.getIslandManager().getIslands().values()) {
            if (island.getCuboid().contains(block)) {
                if (island.getOwner() == player.getUniqueId() || island.getTrusted().contains(player.getUniqueId())) {
                    return;
                }
            }
        }
        event.setCancelled(true);
        player.spawnParticle(Particle.SMOKE_NORMAL, block.getLocation(), 3);
        player.sendMessage(CC.RED + "You do not have permission for this island.");
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (event.getTo().getWorld() == WorldUtil.WORLD) {
            for (Island island : SkyBlock.getIslandManager().getIslands().values()) {
                if (island.getCuboid().contains(event.getTo())) {
                    if (island.getBanned().contains(player.getUniqueId())) {
                        player.sendMessage(CC.RED + "You are banned from this island!");
                        event.setCancelled(true);
                        return;
                    }
                    WorldBorderUtil.sendWorldBorder(player, island.getCuboid());
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteractBlock(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("skyblock.admin")) {
            return;
        }
        if (player.getWorld() != WorldUtil.WORLD) {
            return;
        }
        if (event.getAction() != Action.PHYSICAL || event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Block block = event.getClickedBlock();
        for (Island island : SkyBlock.getIslandManager().getIslands().values()) {
            if (island.getCuboid().contains(player) && island.getCuboid().contains(block)) {
                if (island.getOwner() == player.getUniqueId() || island.getTrusted().contains(player.getUniqueId())) {
                    return;
                }
            }
        }
        event.setCancelled(true);
        assert block != null;
        player.spawnParticle(Particle.SMOKE_NORMAL, block.getLocation(), 3);
        player.sendMessage(CC.RED + "You do not have permission for this island.");
    }
}
