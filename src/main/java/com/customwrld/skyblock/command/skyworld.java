package com.customwrld.skyblock.command;

import com.customwrld.skyblock.util.WorldUtil;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class skyworld {

    public static CommandAPICommand command() {
        return new CommandAPICommand("skyworld")
                .executesPlayer((sender, args) -> {
                    sender.teleport(new Location(Bukkit.getWorld(WorldUtil.SKY_WORLD_NAME), 0, 100, 0));
                });
    }
}
