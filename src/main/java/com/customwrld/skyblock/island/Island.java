package com.customwrld.skyblock.island;

import com.customwrld.customlib.util.CollectionsUtil;
import com.customwrld.customlib.util.Cuboid;
import com.customwrld.skyblock.island.warp.Warp;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.bukkit.Location;

import java.util.*;
import java.util.stream.Collectors;

public class Island {

    @Getter private ObjectId id;
    @Getter private UUID owner;
    @Getter private Cuboid cuboid;
    @Getter private Set<UUID> trusted;
    @Getter private Set<UUID> banned;
    @Getter @Setter private Location spawn;
    @Getter private Set<Warp> warps;

    @Getter @Setter private boolean open;

    public Island(UUID owner, Cuboid cuboid, Location spawn) {
        this.id = new ObjectId();
        this.owner = owner;
        this.cuboid = cuboid;
        this.spawn = spawn;
        this.trusted = new HashSet<>();
        this.banned = new HashSet<>();
        this.warps = new HashSet<>();

        this.open = false;
    }

    public Island(Document document) {
        this.id = document.getObjectId("_id");
        this.owner = UUID.fromString(document.getString("owner"));
        this.cuboid = new Cuboid(CollectionsUtil.genericMapToType(document.get("cuboid", Map.class), String.class, Object.class));
        this.trusted = document.getList("trusted", String.class).stream().map(UUID::fromString).collect(Collectors.toSet());
        this.banned = document.getList("banned", String.class).stream().map(UUID::fromString).collect(Collectors.toSet());
        this.spawn = Location.deserialize(CollectionsUtil.genericMapToType(document.get("spawn", Map.class), String.class, Object.class));
        this.warps = document.getList("warps", Map.class).stream().map(obj -> Warp.deserialize((Map<String, Object>) obj)).collect(Collectors.toSet());

        this.open = document.getBoolean("open");
    }

}
