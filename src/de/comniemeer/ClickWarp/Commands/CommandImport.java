package de.comniemeer.ClickWarp.Commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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
						String warp_names = "§6";
						for (String EWarp : EWarps) {
							Location loc = null;
							String Name = EWarp;
							try {
								loc = this.plugin.IWarps.getWarp(Name);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							if (this.plugin.methods.existWarp(EWarp)) {
								String name_new = Name;
								for (int i = 1; this.plugin.methods.existWarp(name_new); i++) {
									name_new = Name + i;
								}
								Name = name_new;
							}
							this.plugin.methods.setWarp(Name, loc);
							warp_names += Name + "§7, §6";
						}
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpSuccess)
								.replace("{warp}", ChatColor.translateAlternateColorCodes('&', warp_names)));
					} else {
						Location loc = null;
						String Name = args[1];
						if (this.plugin.IWarps != null) {
							try {
								loc = this.plugin.IWarps.getWarp(Name);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						} else {
							sender.sendMessage("[ClickWarp] Failed to load Essentials!");
							sender.sendMessage(
									"[ClickWarp] Install Essentials or set \"ImportEssentialsWarps\" in the config.yml to \"false\"");
						}
						if (this.plugin.methods.existWarp(Name)) {
							String name_new = Name;
							for (int i = 1; this.plugin.methods.existWarp(name_new); i++) {
								name_new = Name + i;
							}
							Name = name_new;
						}
						boolean result = this.plugin.methods.setWarp(Name, loc);
						if (result) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpSuccess)
									.replace("{warp}", ChatColor.translateAlternateColorCodes('&', Name)));
						} else {
							sender.sendMessage(
									ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpInvalidName));
						}
					}

					this.plugin.methods.updateMetrics();
				} else if (args[0].equalsIgnoreCase("WarpPortals") && args[1] != null) {
					if (args[1].equalsIgnoreCase("all")) {
						Set<String> PortalDests = this.plugin.pdm.getDestinations();
						String warp_names = "§6";
						for (String PortalDest : PortalDests) {
							CoordsPY coords = null;
							Location loc = null;
							String Name = PortalDest;
							try {
								coords = this.plugin.pdm.getDestCoords(Name);
								loc = new Location(coords.world, coords.x, coords.y, coords.z, coords.yaw,
										coords.pitch);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							if (this.plugin.methods.existWarp(PortalDest)) {
								String name_new = Name;
								for (int i = 1; this.plugin.methods.existWarp(name_new); i++) {
									name_new = Name + i;
								}
								Name = name_new;
							}
							this.plugin.methods.setWarp(Name, loc);
							warp_names += Name + "§7, §6";
						}
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpSuccess)
								.replace("{warp}", ChatColor.translateAlternateColorCodes('&', warp_names)));
					} else {
						CoordsPY coords = null;
						Location loc = null;
						String Name = args[1];
						if (this.plugin.pdm != null) {
							try {
								coords = this.plugin.pdm.getDestCoords(Name);
								loc = new Location(coords.world, coords.x, coords.y, coords.z, coords.yaw,
										coords.pitch);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						} else {
							sender.sendMessage("[ClickWarp] Failed to load WarpPortals!");
							sender.sendMessage(
									"[ClickWarp] Install Essentials or set \"ImportWarpPortalsDestinations\" in the config.yml to \"false\"");
						}
						if (this.plugin.methods.existWarp(Name)) {
							String name_new = Name;
							for (int i = 1; this.plugin.methods.existWarp(name_new); i++) {
								name_new = Name + i;
							}
							Name = name_new;
						}
						boolean result = this.plugin.methods.setWarp(Name, loc);
						if (result) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpSuccess)
									.replace("{warp}", ChatColor.translateAlternateColorCodes('&', Name)));
						} else {
							sender.sendMessage(
									ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpInvalidName));
						}
					}

					this.plugin.methods.updateMetrics();
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
