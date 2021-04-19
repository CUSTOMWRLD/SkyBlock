package com.customwrld.skyblock.island.warp;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Warp implements ConfigurationSerializable {

    @Getter private final String name;
    @Getter private final Location location;

    public Warp(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("name", name);
        data.put("location", location.serialize());
        return data;
    }

    public static Warp deserialize(Map<String, Object> args) {
        return new Warp((String) args.get("name"), Location.deserialize((Map<String, Object>) args.get("location")));
    }
}
