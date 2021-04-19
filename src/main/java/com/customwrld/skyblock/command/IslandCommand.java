package com.customwrld.skyblock.command;

import com.customwrld.customlib.util.CC;
import com.customwrld.skyblock.SkyBlock;
import com.customwrld.skyblock.island.Island;
import com.customwrld.skyblock.island.warp.Warp;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.entity.Player;

public class IslandCommand {

    public static CommandAPICommand command() {
        return new CommandAPICommand("island")
                .withAliases("is")
                .withSubcommand(create)
                .withSubcommand(home)
                .withSubcommand(visit)
                .withSubcommand(open)
                .withSubcommand(close)
                .withSubcommand(warp)
                .withSubcommand(setwarp)
                .withSubcommand(listwarps)
                .withSubcommand(delwarp)
                .withSubcommand(trust)
                .withSubcommand(untrust)
                .withSubcommand(ban)
                .withSubcommand(unban);
    }

    public static CommandAPICommand create = new CommandAPICommand("create")
            .executesPlayer((sender, args) -> {
                if (SkyBlock.getIslandManager().getIsland(sender) != null) {
                    sender.sendMessage(CC.RED + "You already have an island!");
                    return;
                }
                SkyBlock.getIslandManager().createIsland(sender);

            });

    public static CommandAPICommand home = new CommandAPICommand("home")
            .executesPlayer((sender, args) -> {
                if (SkyBlock.getIslandManager().getIsland(sender) == null) {
                    sender.sendMessage(CC.RED + "You do not have an island, please create one!");
                    return;
                }
                sender.teleport(SkyBlock.getIslandManager().getIsland(sender).getSpawn());
            });

    public static CommandAPICommand visit = new CommandAPICommand("visit")
            .withArguments(new PlayerArgument("player"))
            .executesPlayer((sender, args) -> {

                Player player = (Player) args[0];

                if (SkyBlock.getIslandManager().getIsland(player) == null) {
                    sender.sendMessage(CC.RED + player.getName() + " does not have an island!");
                    return;
                }
                Island island = SkyBlock.getIslandManager().getIsland(player);

                if (island.isOpen() || island.getTrusted().contains(sender.getUniqueId())) {
                    if (!island.getBanned().contains(sender.getUniqueId())) {
                        sender.teleport(island.getSpawn());
                    } else {
                        sender.sendMessage(CC.RED + "You are banned from this island!");
                    }
                } else {
                    sender.sendMessage(CC.RED + "This island is not open!");
                }
            });

    public static CommandAPICommand open = new CommandAPICommand("open")
            .executesPlayer((sender, args) -> {
                if (SkyBlock.getIslandManager().getIsland(sender) == null) {
                    sender.sendMessage(CC.RED + "You do not have an island, please create one!");
                    return;
                }
                SkyBlock.getIslandManager().getIsland(sender).setOpen(true);
                sender.sendMessage(CC.GREEN + "Your island has been opened!");
                SkyBlock.getIslandManager().save(SkyBlock.getIslandManager().getIsland(sender));
            });

    public static CommandAPICommand close = new CommandAPICommand("close")
            .executesPlayer((sender, args) -> {
                if (SkyBlock.getIslandManager().getIsland(sender) == null) {
                    sender.sendMessage(CC.RED + "You do not have an island, please create one!");
                    return;
                }
                SkyBlock.getIslandManager().getIsland(sender).setOpen(false);
                sender.sendMessage(CC.RED + "Your island has been closed!");
                SkyBlock.getIslandManager().save(SkyBlock.getIslandManager().getIsland(sender));
            });

    public static CommandAPICommand trust = new CommandAPICommand("trust")
            .withArguments(new PlayerArgument("player"))
            .executesPlayer((sender, args) -> {
                Player player = (Player) args[0];
                if (SkyBlock.getIslandManager().getIsland(sender) == null) {
                    sender.sendMessage(CC.RED + "You do not have an island, please create one!");
                    return;
                }
                Island island = SkyBlock.getIslandManager().getIsland(sender);
                if (island.getTrusted().contains(player.getUniqueId())) {
                    sender.sendMessage(CC.RED + "This player is already trusted!");
                    return;
                }
                island.getTrusted().add(player.getUniqueId());
                sender.sendMessage(CC.GREEN + player.getName() + " has been trusted!");
                SkyBlock.getIslandManager().save(island);
            });

    public static CommandAPICommand setwarp = new CommandAPICommand("setwarp")
            .withArguments(new StringArgument("name"))
            .executesPlayer((sender, args) -> {
                if (SkyBlock.getIslandManager().getIsland(sender) == null) {
                    sender.sendMessage(CC.RED + "You do not have an island, please create one!");
                    return;
                }
                Island island = SkyBlock.getIslandManager().getIsland(sender);
                String name = (String) args[0];
                island.getWarps().add(new Warp(name, sender.getLocation()));
                sender.sendMessage(CC.GREEN + "Warp " + name + " has been created!");
                SkyBlock.getIslandManager().save(island);
            });

    public static CommandAPICommand listwarps = new CommandAPICommand("listwarps")
            .executesPlayer((sender, args) -> {
                if (SkyBlock.getIslandManager().getIsland(sender) == null) {
                    sender.sendMessage(CC.RED + "You do not have an island, please create one!");
                    return;
                }
                Island island = SkyBlock.getIslandManager().getIsland(sender);
                for (Warp warp1 : island.getWarps()) {
                    sender.sendMessage(warp1.getName());
                }
            });

    public static CommandAPICommand warp = new CommandAPICommand("warp")
            .withArguments(new StringArgument("warp"))
            .executesPlayer((sender, args) -> {
                if (SkyBlock.getIslandManager().getIsland(sender) == null) {
                    sender.sendMessage(CC.RED + "You do not have an island, please create one!");
                    return;
                }
                Island island = SkyBlock.getIslandManager().getIsland(sender);
                String name = (String) args[0];
                for (Warp warp1 : island.getWarps()) {
                    if (warp1.getName().equalsIgnoreCase(name)) {
                        sender.teleport(warp1.getLocation());
                        sender.sendMessage(CC.GREEN + "Warp you to warp " + name + "!");
                        return;
                    }
                }
                sender.sendMessage(CC.RED + "There is no warp called " + name + "!");
            });

    public static CommandAPICommand delwarp = new CommandAPICommand("delwarp")
            .withArguments(new StringArgument("name"))
            .executesPlayer((sender, args) -> {
                if (SkyBlock.getIslandManager().getIsland(sender) == null) {
                    sender.sendMessage(CC.RED + "You do not have an island, please create one!");
                    return;
                }
                Island island = SkyBlock.getIslandManager().getIsland(sender);
                String name = (String) args[0];
                for (Warp warp1 : island.getWarps()) {
                    if (warp1.getName().equalsIgnoreCase(name)) {
                        island.getWarps().remove(warp1);
                        sender.sendMessage(CC.RED + "Warp " + name + " has been deleted!");
                        SkyBlock.getIslandManager().save(island);
                        return;
                    }
                }
                sender.sendMessage(CC.RED + "There is no warp called " + name + "!");
            });

    public static CommandAPICommand untrust = new CommandAPICommand("untrust")
            .withArguments(new PlayerArgument("player"))
            .executesPlayer((sender, args) -> {
                Player player = (Player) args[0];
                if (SkyBlock.getIslandManager().getIsland(sender) == null) {
                    sender.sendMessage(CC.RED + "You do not have an island, please create one!");
                    return;
                }
                Island island = SkyBlock.getIslandManager().getIsland(sender);
                if (!island.getTrusted().contains(player.getUniqueId())) {
                    sender.sendMessage(CC.RED + "This player is not trusted!");
                    return;
                }
                island.getTrusted().remove(player.getUniqueId());
                sender.sendMessage(CC.RED + player.getName() + " has been untrusted!");
                SkyBlock.getIslandManager().save(island);
            });

    public static CommandAPICommand ban = new CommandAPICommand("ban")
            .withArguments(new PlayerArgument("player"))
            .executesPlayer((sender, args) -> {
                Player player = (Player) args[0];
                if (SkyBlock.getIslandManager().getIsland(sender) == null) {
                    sender.sendMessage(CC.RED + "You do not have an island, please create one!");
                    return;
                }
                Island island = SkyBlock.getIslandManager().getIsland(sender);
                if (island.getBanned().contains(player.getUniqueId())) {
                    sender.sendMessage(CC.RED + "This player is already banned!");
                    return;
                }
                island.getBanned().add(player.getUniqueId());
                sender.sendMessage(CC.RED + player.getName() + " has been banned from the island!");
                SkyBlock.getIslandManager().save(island);
            });

    public static CommandAPICommand unban = new CommandAPICommand("unban")
            .withArguments(new PlayerArgument("player"))
            .executesPlayer((sender, args) -> {
                Player player = (Player) args[0];
                if (SkyBlock.getIslandManager().getIsland(sender) == null) {
                    sender.sendMessage(CC.RED + "You do not have an island, please create one!");
                    return;
                }
                Island island = SkyBlock.getIslandManager().getIsland(sender);
                if (!island.getBanned().contains(player.getUniqueId())) {
                    sender.sendMessage(CC.RED + "This player is not banned!");
                    return;
                }
                island.getBanned().remove(player.getUniqueId());
                sender.sendMessage(CC.GREEN + player.getName() + " has been unbanned from the island!");
                SkyBlock.getIslandManager().save(island);
            });
}
