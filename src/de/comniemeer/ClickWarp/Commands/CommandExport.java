package de.comniemeer.ClickWarp.Commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
                if (args[0].equalsIgnoreCase("Essentials") && args[1] != null) {
                    if (this.plugin.IWarps != null) {
                        List<String> warps = this.plugin.methods.getWarps();

                        if (warps.size() == 0) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoWarps));
                            return true;
                        }
                        if (args[1].equalsIgnoreCase("all")) {
                            for (String warp : warps) {

                                String name_ = this.plugin.methods.getName(warp);
                                Location loc = this.plugin.methods.getWarp(warp);

                                try {
                                    this.plugin.IWarps.setWarp(name_, loc);
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }

                            }
                        } else {
                            String name_ = this.plugin.methods.getName(args[1]);
                            Location loc = this.plugin.methods.getWarp(args[1]);

                            try {
                                this.plugin.IWarps.setWarp(name_, loc);
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                    } else {
                        this.plugin.log.severe("[ClickWarp] Failed to load Essentials!");
                        this.plugin.log.severe(
                                "[ClickWarp] Install Essentials or set \"EnableEssentials\" in the config.yml to \"false\"");
                    }
                } else if (args[0].equalsIgnoreCase("WarpPortals") && args[1] != null) {
                    if (this.plugin.pdm != null) {
                        List<String> warps = this.plugin.methods.getWarps();

                        if (warps.size() == 0) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoWarps));
                            return true;
                        }
                        if (args[1].equalsIgnoreCase("all")) {
                            for (String warp : warps) {

                                String name_ = this.plugin.methods.getName(warp);
                                CoordsPY coords = new CoordsPY(this.plugin.methods.getWarp(warp));

                                this.plugin.pdm.addDestination(name_, coords);

                            }
                        } else {
                            String name_ = this.plugin.methods.getName(args[1]);
                            CoordsPY coords = new CoordsPY(this.plugin.methods.getWarp(args[1]));

                            this.plugin.pdm.addDestination(name_, coords);

                        }
                    } else {
                        this.plugin.log.severe("[ClickWarp] Failed to load WarpPortals!");
                        this.plugin.log.severe(
                                "[ClickWarp] Install WarpPortals or set \"EnableWarpPortals\" in the config.yml to \"false\"");
                    }
                }
            }
        }
        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String label, String[] args) {
        if (sender.hasPermission("clickwarp.export")) {
            List<String> pluginList = new ArrayList<>();
            List<String> warpList = new ArrayList<>();
            if (this.plugin.IWarps != null) {
                pluginList.add("Essentials");
            }
            if (this.plugin.pdm != null) {
                pluginList.add("WarpPortals");
            }
            File warps_folder = new File("plugins/ClickWarp/Warps");

            if (warps_folder.isDirectory()) {
                File[] warps = warps_folder.listFiles();

                if (warps.length > 1) {
                    for (File warp : warps) {
                        warpList.add(this.plugin.methods.getName(warp.getName().replace(".yml", "")));
                    }
                }
            }

            if (args.length == 1) {
                return pluginList;
            } else if (args.length == 2) {
                List<String> tabList = new ArrayList<>();

                if ("all".startsWith(args[1].toLowerCase())) {
                    tabList.add("all");
                }

                for (String warp : warpList) {
                    if (warp.toLowerCase().startsWith(args[1].toLowerCase())) {
                        tabList.add(warp);
                    }
                }

                return tabList;
            }
        }
        return null;
    }

}
