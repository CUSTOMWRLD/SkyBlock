package com.customwrld.skyblock.island;

import com.customwrld.customlib.util.Cuboid;
import com.customwrld.skyblock.SkyBlock;
import com.customwrld.skyblock.island.warp.Warp;
import com.customwrld.skyblock.util.CuboidUtil;
import com.customwrld.skyblock.util.WorldUtil;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.sk89q.worldedit.WorldEditException;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class IslandManager {

    @Getter private HashMap<UUID, Island> islands;

    @Getter private MongoCollection<Document> islandCollection;

    public IslandManager() {
        islands = new HashMap<>();
        islandCollection = SkyBlock.getDatabase().getCollection("islands");

    }

    public void createIsland(Player player) {

        Location[] locations = CuboidUtil.getOpenLocation(250);

        Island island = new Island(player.getUniqueId(), new Cuboid(locations[0], locations[1]), locations[2]);

        try {
            WorldUtil.loadSchematic(locations[2], "island_classic.schem");
            Bukkit.getScheduler().scheduleSyncDelayedTask(SkyBlock.getSkyBlock(), () -> {
                player.teleport(locations[2]);
            }, 20L);
        } catch (IOException | WorldEditException e) {
            e.printStackTrace();
        }

        islands.put(player.getUniqueId(), island);
        SkyBlock.getIslandManager().save(island);

    }

    public Island getIsland(Player player) {
        if (islands.containsKey(player.getUniqueId())) {
            return islands.get(player.getUniqueId());
        }
        return null;
    }

    public void save(Island island) {
        islandCollection.updateOne(Filters.eq(island.getId()),
                Updates.combine(
                        Updates.set("owner", island.getOwner().toString()),
                        Updates.set("cuboid", island.getCuboid().serialize()),

                        Updates.set("trusted", island.getTrusted().stream().map(UUID::toString).collect(Collectors.toList())),
                        Updates.set("banned", island.getBanned().stream().map(UUID::toString).collect(Collectors.toList())),

                        Updates.set("spawn", island.getSpawn().serialize()),
                        Updates.set("warps", island.getWarps().stream().map(Warp::serialize).collect(Collectors.toList())),

                        Updates.set("open", island.isOpen())
                ),

                new UpdateOptions().upsert(true)
        );
    }

    public Set<Island> load(Player player) {
        Set<Island> loadedIslands = new HashSet<>();
        try (MongoCursor<Document> cursor = islandCollection.find(
                Filters.eq("owner", player.getUniqueId().toString())
        ).cursor()) {
            while (cursor.hasNext()) {
                Island island = new Island(cursor.next());
                loadedIslands.add(island);
            }
        }
        return loadedIslands;
    }
}
