package de.comniemeer.ClickWarp.Commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import de.comniemeer.ClickWarp.Exceptions.InvalidName;
import de.comniemeer.ClickWarp.Warp;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import com.mccraftaholics.warpportals.objects.CoordsPY;

import de.comniemeer.ClickWarp.AutoCommand;
import de.comniemeer.ClickWarp.ClickWarp;

public class CommandImport extends AutoCommand<ClickWarp> {

    public CommandImport(ClickWarp plugin, String cmd, String description) {
        super(plugin, cmd, description);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender.hasPermission("clickwarp.setwarp")) {
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("Essentials") && args[1] != null) {
                    if (args[1].equalsIgnoreCase("all")) {
                        Collection<String> EWarps = this.plugin.IWarps.getList();
                        StringBuilder warp_names = new StringBuilder(ChatColor.GOLD.toString());
                        for (String EWarp : EWarps) {
                            Location loc = null;
                            try {
                                loc = this.plugin.IWarps.getWarp(EWarp);
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                            try {
                                Warp warp = new Warp(EWarp, loc);
                                if (warp.existWarp()) {
                                    for (int i = 1; warp.existWarp(); i++) {
                                        warp = new Warp(EWarp + i, loc);
                                    }
                                }
                                warp.save();
                                warp_names.append(warp.getName()).append(ChatColor.GRAY).append(", ").append(ChatColor.GOLD);
                            } catch (InvalidName invalidName) {
                                invalidName.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.ErrorFileSaving));
                            }

                        }
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpSuccess)
                                .replace("{warp}", ChatColor.translateAlternateColorCodes('&', warp_names.toString())));
                    } else {
                        Location loc = null;
                        if (this.plugin.IWarps != null) {
                            try {
                                loc = this.plugin.IWarps.getWarp(args[1]);
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        } else {
                            sender.sendMessage("[ClickWarp] Failed to load Essentials!");
                            sender.sendMessage(
                                    "[ClickWarp] Install Essentials or set \"ImportEssentialsWarps\" in the config.yml to \"false\"");
                        }
                        try {
                            Warp warp = new Warp(args[1], loc);
                            if (warp.existWarp()) {
                                for (int i = 1; warp.existWarp(); i++) {
                                    warp = new Warp(args[1] + i, loc);
                                }
                            }
                            warp.save();
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpSuccess)
                                    .replace("{warp}", ChatColor.translateAlternateColorCodes('&', warp.getName())));
                        } catch (InvalidName invalidName) {
                            invalidName.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.ErrorFileSaving));
                        }

                    }
                } else if (args[0].equalsIgnoreCase("WarpPortals") && args[1] != null) {
                    if (args[1].equalsIgnoreCase("all")) {
                        Set<String> PortalDests = this.plugin.pdm.getDestinations();
                        StringBuilder warp_names = new StringBuilder(ChatColor.GOLD.toString());
                        for (String PortalDest : PortalDests) {
                            CoordsPY coords;
                            Location loc = null;
                            try {
                                coords = this.plugin.pdm.getDestCoords(PortalDest);
                                loc = new Location(coords.world, coords.x, coords.y, coords.z, coords.yaw,
                                        coords.pitch);
                                Warp warp = new Warp(PortalDest, loc);
                                if (warp.existWarp()) {
                                    for (int i = 1; warp.existWarp(); i++) {
                                        warp = new Warp(PortalDest + i, loc);
                                    }
                                }
                                warp.save();
                                warp_names.append(warp.getName()).append(ChatColor.GRAY).append(", ").append(ChatColor.GOLD);
                            } catch (InvalidName invalidName) {
                                invalidName.printStackTrace();
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpInvalidName));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpSuccess)
                                .replace("{warp}", ChatColor.translateAlternateColorCodes('&', warp_names.toString())));
                    } else {
                        CoordsPY coords;
                        Location loc = null;
                        if (this.plugin.pdm != null) {
                            try {
                                coords = this.plugin.pdm.getDestCoords(args[1]);
                                loc = new Location(coords.world, coords.x, coords.y, coords.z, coords.yaw,
                                        coords.pitch);
                                Warp warp = new Warp(args[1], loc);
                                if (warp.existWarp()) {
                                    for (int i = 1; warp.existWarp(); i++) {
                                        warp = new Warp(args[1] + i, loc);
                                    }
                                }
                                warp.save();
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpSuccess)
                                        .replace("{warp}", ChatColor.translateAlternateColorCodes('&', warp.getName())));
                            } catch (InvalidName invalidName) {
                                invalidName.printStackTrace();
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpInvalidName));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            sender.sendMessage("[ClickWarp] Failed to load WarpPortals!");
                            sender.sendMessage(
                                    "[ClickWarp] Install Essentials or set \"ImportWarpPortalsDestinations\" in the config.yml to \"false\"");
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String label, String[] args) {
        if (sender.hasPermission("clickwarp.setwarp")) {
            List<String> pluginList = new ArrayList<>();
            if (this.plugin.IWarps != null) {
                pluginList.add("Essentials");
            }
            if (this.plugin.pdm != null) {
                pluginList.add("WarpPortals");
            }

            if (args.length == 1) {
                return pluginList;
            } else if (args[0].equalsIgnoreCase("Essentials")) {
                if (args.length == 2) {
                    if (this.plugin.IWarps != null) {
                        List<String> IWarps = new ArrayList<>();
                        if ("all".startsWith(args[1].toLowerCase())) {
                            IWarps.add("all");
                        }

                        Collection<String> EWarps = this.plugin.IWarps.getList();
                        for (String EWarp : EWarps) {
                            if (EWarp.toLowerCase().startsWith(args[1].toLowerCase())) {
                                IWarps.add(EWarp);
                            }
                        }

                        return IWarps;
                    }
                }
            } else if (args[0].equalsIgnoreCase("WarpPortals")) {
                if (args.length == 2) {
                    if (this.plugin.pdm != null) {
                        List<String> IWarps = new ArrayList<>();
                        if ("all".startsWith(args[1].toLowerCase())) {
                            IWarps.add("all");
                        }

                        Collection<String> EWarps = this.plugin.pdm.getDestinations();
                        for (String EWarp : EWarps) {
                            if (EWarp.toLowerCase().startsWith(args[1].toLowerCase())) {
                                IWarps.add(EWarp);
                            }
                        }

                        return IWarps;
                    }
                }
            }
        }
        return null;
    }
}
