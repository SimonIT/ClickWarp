package de.comniemeer.ClickWarp.Commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.comniemeer.ClickWarp.Exceptions.WarpNoExist;
import de.comniemeer.ClickWarp.Warp;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import com.mccraftaholics.warpportals.objects.CoordsPY;

import de.comniemeer.ClickWarp.AutoCommand;
import de.comniemeer.ClickWarp.ClickWarp;

public class CommandExport extends AutoCommand<ClickWarp> {

    public CommandExport(ClickWarp plugin, String cmd, String description) {
        super(plugin, cmd, description);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender.hasPermission("clickwarp.export")) {
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("Essentials") && this.plugin.getConfig().getBoolean("Essentials.Enable") && args[1] != null) {
                    if (args[1].equalsIgnoreCase("all")) {
                        List<Warp> warps = Warp.getWarps();
                        if (warps.size() == 0) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoWarps));
                        } else {
                            for (Warp warp : warps) {
                                try {
                                    this.plugin.IWarps.setWarp(warp.getName(), warp.getLocation());
                                } catch (Exception e) {
                                }
                            }
                        }
                    } else {
                        try {
                            Warp warp = new Warp(args[1]);
                            try {
                                this.plugin.IWarps.setWarp(warp.getName(), warp.getLocation());
                            } catch (Exception e) {
                            }
                        } catch (WarpNoExist warpNoExist) {
                        }
                    }
                }

            } else {
                this.plugin.log.severe("[ClickWarp] Failed to load Essentials!");
                this.plugin.log.severe(
                        "[ClickWarp] Install Essentials or set \"EnableEssentials\" in the config.yml to \"false\"");
            }
        } else if (args[0].equalsIgnoreCase("WarpPortals") && this.plugin.getConfig().getBoolean("WarpPortals.Enable") && args[1] != null) {
            if (this.plugin.pdm != null) {
                List<Warp> warps = Warp.getWarps();

                if (warps.size() == 0) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoWarps));
                    return true;
                }
                if (args[1].equalsIgnoreCase("all")) {
                    for (Warp warp : warps) {
                        CoordsPY coords = new CoordsPY(warp.getLocation());
                        this.plugin.pdm.addDestination(warp.getName(), coords);

                    }
                } else {
                    try {
                        Warp warp = new Warp(args[1]);
                        CoordsPY coords = new CoordsPY(warp.getLocation());
                        this.plugin.pdm.addDestination(warp.getName(), coords);
                    } catch (WarpNoExist warpNoExist) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.WarpNoExist)
                                .replace("{warp}", args[0]));
                    }
                }
            } else {
                this.plugin.log.severe("[ClickWarp] Failed to load WarpPortals!");
                this.plugin.log.severe(
                        "[ClickWarp] Install WarpPortals or set \"EnableWarpPortals\" in the config.yml to \"false\"");
            }
        }
        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String label, String[] args) {
        if (sender.hasPermission("clickwarp.export")) {
            List<String> pluginList = new ArrayList<>();
            if (this.plugin.IWarps != null) {
                pluginList.add("Essentials");
            }
            if (this.plugin.pdm != null) {
                pluginList.add("WarpPortals");
            }

            if (args.length == 1) {
                return pluginList;
            } else if (args.length == 2) {
                List<String> tabList = new ArrayList<>();

                if ("all".startsWith(args[1].toLowerCase())) {
                    tabList.add("all");
                }

                for (Warp warp : Warp.getWarps()) {
                    if (warp.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                        tabList.add(warp.getName());
                    }
                }

                return tabList;
            }
        }
        return null;
    }

}
