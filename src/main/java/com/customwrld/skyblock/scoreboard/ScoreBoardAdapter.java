package com.customwrld.skyblock.scoreboard;

import com.customwrld.customlib.assemble.AssembleAdapter;
import com.customwrld.customlib.util.CC;
import com.customwrld.skyblock.util.ColorUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ScoreBoardAdapter implements AssembleAdapter {
    @Override
    public String getTitle(Player player) {
        return CC.AQUA + "CustomWRLD " + CC.GRAY + "(SkyBlock)";
    }

    @Override
    public List<String> getLines(Player player) {
        List<String> lines = new ArrayList<>();

        lines.add(CC.RGB(ColorUtil.latestRGB().getRGB()) + "Sample Text");

        return lines;
    }
}
