package com.customwrld.skyblock.util;

import com.customwrld.skyblock.SkyBlock;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import lombok.SneakyThrows;
import org.bukkit.*;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

public class WorldUtil {

    public static final String SKY_WORLD_NAME = "world_skyblock";

    public static World WORLD;

    public static void createSkyBlockWorld() {
        WorldCreator worldCreator = new WorldCreator(SKY_WORLD_NAME);

        worldCreator.generateStructures(false);
        worldCreator.type(WorldType.FLAT);
        worldCreator.generator(new EmptyChunkGenerator());
        worldCreator.createWorld();

        World world = Bukkit.getWorld(SKY_WORLD_NAME);
        assert world != null;
        world.setAutoSave(true);
        world.setKeepSpawnInMemory(false);
        world.setGameRule(GameRule.DO_FIRE_TICK, false);
    }

    public static void loadSchematic(Location location, String schem) throws IOException, WorldEditException {
        File dir = new File(SkyBlock.getSkyBlock().getDataFolder(), File.separator + "/schematics/" + schem);
        ClipboardFormat format = ClipboardFormats.findByFile(dir);
        assert format != null;
        ClipboardReader clipboardReader = format.getReader(new FileInputStream(dir));
        Clipboard clipboard = clipboardReader.read();
        try(EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(location.getWorld()), -1)) {
            Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(BlockVector3.at(location.getX(), location.getY(), location.getZ()))
                    .ignoreAirBlocks(false)
                    .build();
            Operations.complete(operation);
        }
    }

    public static class EmptyChunkGenerator extends ChunkGenerator {
        @Override
        public @NotNull ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int x, int z, @NotNull BiomeGrid biome) {
            return createChunkData(world);
        }
    }
}
