package de.comniemeer.ClickWarp.Commands;

import com.mccraftaholics.warpportals.objects.CoordsPY;
import com.sk89q.commandbook.locations.NamedLocation;
import de.comniemeer.ClickWarp.AutoCommand;
import de.comniemeer.ClickWarp.ClickWarp;
import de.comniemeer.ClickWarp.Exceptions.InvalidName;
import de.comniemeer.ClickWarp.Warp;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class CommandImportwarp extends AutoCommand<ClickWarp> {

	public CommandImportwarp(ClickWarp plugin, String cmd, String description) {
		super(plugin, cmd, description);
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender.hasPermission("clickwarp.setwarp")) {
			if (args.length == 2) {
				if (args[0].equalsIgnoreCase("Essentials") && this.plugin.getConfig().getBoolean("Essentials.Enable") && args[1] != null) {
					if (args[1].equalsIgnoreCase("all")) {
						Collection<String> EWarps = this.plugin.IWarps.getList();
						StringBuilder warp_names = new StringBuilder(ChatColor.GOLD.toString());
						for (String EWarp : EWarps) {
							Location loc = null;
							try {
								loc = this.plugin.IWarps.getWarp(EWarp);
							} catch (Exception e1) {
							}
							try {
								Warp warp = new Warp(EWarp, loc, (Player) sender);
								if (warp.existWarp()) {
									for (int i = 1; warp.existWarp(); i++) {
										warp = new Warp(EWarp + i, loc, (Player) sender);
									}
								}
								warp.save();
								warp_names.append(warp.getName()).append(ChatColor.GRAY).append(", ").append(ChatColor.GOLD);
							} catch (InvalidName invalidName) {
							} catch (IOException e) {
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
							}
						} else {
							sender.sendMessage("[ClickWarp] Failed to load Essentials!");
							sender.sendMessage(
									"[ClickWarp] Install Essentials or set \"EssentialsEnable\" in the config.yml to \"false\"");
						}
						try {
							Warp warp = new Warp(args[1], loc, (Player) sender);
							if (warp.existWarp()) {
								for (int i = 1; warp.existWarp(); i++) {
									warp = new Warp(args[1] + i, loc, (Player) sender);
								}
							}
							warp.save();
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpSuccess)
									.replace("{warp}", ChatColor.translateAlternateColorCodes('&', warp.getName())));
						} catch (InvalidName invalidName) {
						} catch (IOException e) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.ErrorFileSaving));
						}

					}
				} else if (args[0].equalsIgnoreCase("CommandBook") && this.plugin.getConfig().getBoolean("CommandBook.Enable") && args[1] != null) {
					if (args[1].equalsIgnoreCase("all")) {
						StringBuilder warp_names = new StringBuilder(ChatColor.GOLD.toString());
						try {
							this.plugin.fflm.load();
							List<NamedLocation> lnl = this.plugin.fflm.getLocations();
							for (NamedLocation nl : lnl) {
								Warp warp = new Warp(nl.getName(), nl.getLocation(), Bukkit.getOfflinePlayer(nl.getCreatorID()));
								if (warp.existWarp()) {
									for (int i = 1; warp.existWarp(); i++) {
										warp = new Warp(nl.getName() + i, nl.getLocation(), Bukkit.getPlayer(nl.getCreatorID()));
									}
								}
								warp.save();
								warp_names.append(warp.getName()).append(ChatColor.GRAY).append(", ").append(ChatColor.GOLD);
							}
						} catch (IOException e) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.ErrorFileSaving));
						} catch (InvalidName invalidName) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpInvalidName));
						}
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpSuccess)
								.replace("{warp}", ChatColor.translateAlternateColorCodes('&', warp_names.toString())));
					} else {
						if (this.plugin.fflm != null) {
							try {
								this.plugin.fflm.load();
								NamedLocation nl = this.plugin.fflm.get(args[1]);
								Warp warp = new Warp(nl.getName(), nl.getLocation(), Bukkit.getPlayer(nl.getCreatorID()));
								if (warp.existWarp()) {
									for (int i = 1; warp.existWarp(); i++) {
										warp = new Warp(nl.getName() + i, nl.getLocation(), Bukkit.getPlayer(nl.getCreatorID()));
									}
								}
								warp.save();
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpSuccess)
										.replace("{warp}", ChatColor.translateAlternateColorCodes('&', warp.getName())));
							} catch (InvalidName invalidName) {
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpInvalidName));
							} catch (IOException e) {
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.ErrorFileSaving));
							}
						} else {
							sender.sendMessage("[ClickWarp] Failed to load CommandBook!");
							sender.sendMessage(
									"[ClickWarp] Install Essentials or set \"CommandBookEnable\" in the config.yml to \"false\"");
						}
					}
				} else if (args[0].equalsIgnoreCase("WarpPortals") && this.plugin.getConfig().getBoolean("WarpPortals.Enable") && args[1] != null) {
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
								Warp warp = new Warp(PortalDest, loc, (Player) sender);
								if (warp.existWarp()) {
									for (int i = 1; warp.existWarp(); i++) {
										warp = new Warp(PortalDest + i, loc, (Player) sender);
									}
								}
								warp.save();
								warp_names.append(warp.getName()).append(ChatColor.GRAY).append(", ").append(ChatColor.GOLD);
							} catch (InvalidName invalidName) {
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpInvalidName));
							} catch (IOException e) {
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.ErrorFileSaving));
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
								Warp warp = new Warp(args[1], loc, (Player) sender);
								if (warp.existWarp()) {
									for (int i = 1; warp.existWarp(); i++) {
										warp = new Warp(args[1] + i, loc, (Player) sender);
									}
								}
								warp.save();
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpSuccess)
										.replace("{warp}", ChatColor.translateAlternateColorCodes('&', warp.getName())));
							} catch (InvalidName invalidName) {
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpInvalidName));
							} catch (IOException e) {
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.ErrorFileSaving));
							}
						} else {
							sender.sendMessage("[ClickWarp] Failed to load WarpPortals!");
							sender.sendMessage(
									"[ClickWarp] Install Essentials or set \"WarpPortalsEnable\" in the config.yml to \"false\"");
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

			if (this.plugin.fflm != null) {
				pluginList.add("CommandBook");
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
			} else if (args[0].equalsIgnoreCase("CommandBook")) {
				if (args.length == 2) {
					if (this.plugin.fflm != null) {
						List<String> CWarps = new ArrayList<>();
						if ("all".startsWith(args[1].toLowerCase())) {
							CWarps.add("all");
						}

						List<NamedLocation> EWarps = this.plugin.fflm.getLocations();
						for (NamedLocation EWarp : EWarps) {
							if (EWarp.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
								CWarps.add(EWarp.getName());
							}
						}

						return CWarps;
					}
				}
			}
		}
		return null;
	}
}
