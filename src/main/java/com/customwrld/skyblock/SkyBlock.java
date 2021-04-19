package com.customwrld.skyblock;

import com.customwrld.customlib.CustomLib;
import com.customwrld.customlib.assemble.Assemble;
import com.customwrld.customlib.assemble.AssembleStyle;
import com.customwrld.skyblock.command.IslandCommand;
import com.customwrld.skyblock.command.skyworld;
import com.customwrld.skyblock.events.BlockEvents;
import com.customwrld.skyblock.events.EntityEvents;
import com.customwrld.skyblock.events.PlayerEvents;
import com.customwrld.skyblock.island.Island;
import com.customwrld.skyblock.island.IslandManager;
import com.customwrld.skyblock.scoreboard.ScoreBoardAdapter;
import com.customwrld.skyblock.util.WorldUtil;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public final class SkyBlock extends JavaPlugin {

    @Getter private static SkyBlock skyBlock;

    @Getter private static IslandManager islandManager;

    @Getter private static MongoDatabase database;

    @Override
    public void onEnable() {
        skyBlock = this;

        database = CustomLib.getInstance().getMongoDatabase();

        islandManager = new IslandManager();

        Assemble assemble = new Assemble(this, new ScoreBoardAdapter());

        assemble.setTicks(2);

        assemble.setAssembleStyle(AssembleStyle.MODERN);

        assemble.setup();

        if (Bukkit.getWorld(WorldUtil.SKY_WORLD_NAME) == null) {
            WorldUtil.createSkyBlockWorld();
        }

        try {
            Files.createDirectories(Paths.get(getDataFolder() + "/schematics/"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        WorldUtil.WORLD = Bukkit.getWorld(WorldUtil.SKY_WORLD_NAME);

        Arrays.asList(
                new PlayerEvents(),
                new BlockEvents(),
                new EntityEvents()
        ).forEach(events -> { Bukkit.getServer().getPluginManager().registerEvents(events, this); });

        IslandCommand.command().register();
        skyworld.command().register();
    }

    @Override
    public void onDisable() {
        for (Island island : islandManager.getIslands().values()) {
            islandManager.save(island);
        }
    }
}
