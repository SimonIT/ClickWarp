package de.comniemeer.ClickWarp.Commands;

import com.mccraftaholics.warpportals.objects.CoordsPY;
import de.comniemeer.ClickWarp.AutoCommand;
import de.comniemeer.ClickWarp.ClickWarp;
import de.comniemeer.ClickWarp.Exceptions.WarpNoExist;
import de.comniemeer.ClickWarp.Warp;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommandExportwarp extends AutoCommand<ClickWarp> {

	public CommandExportwarp(ClickWarp plugin, String cmd, String description) {
		super(plugin, cmd, description);
	}

	@Override
	public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
		if (sender.hasPermission("clickwarp.exportwarp")) {
			if (args.length == 2) {
				if (args[0].equalsIgnoreCase("Essentials") && this.plugin.getConfig().getBoolean("Essentials.Enable") && args[1] != null) {
					if (this.plugin.IWarps != null) {
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


					} else {
						this.plugin.log.severe("[ClickWarp] Failed to load Essentials!");
						this.plugin.log.severe(
								"[ClickWarp] Install Essentials or set \"EnableEssentials\" in the config.yml to \"false\"");
					}


				} else if (args[0].equalsIgnoreCase("CommandBook") && this.plugin.getConfig().getBoolean("CommandBook.Enable") && args[1] != null) {
					if (this.plugin.fflm != null) {
						try {
							this.plugin.fflm.load();
							if (args[1].equalsIgnoreCase("all")) {
								List<Warp> warps = Warp.getWarps();

								if (warps.size() == 0) {
									sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoWarps));
									return true;
								}
								for (Warp warp : warps) {
									this.plugin.fflm.create(warp.getName(), warp.getLocation(), (Player) warp.getPlayer());
								}
							} else {
								try {
									Warp warp = new Warp(args[1]);
									this.plugin.fflm.create(warp.getName(), warp.getLocation(), (Player) warp.getPlayer());
								} catch (WarpNoExist warpNoExist) {
									sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.WarpNoExist)
											.replace("{warp}", args[0]));
								}
							}
							//this.plugin.fflm.save();
							sender.sendMessage("Sorry, at the moment, warps can't exported to CommandBook :(");
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						this.plugin.log.severe("[ClickWarp] Failed to load CommandBook!");
						this.plugin.log.severe(
								"[ClickWarp] Install WarpPortals or set \"EnableCommandBook\" in the config.yml to \"false\"");
					}
				} else if (args[0].equalsIgnoreCase("WarpPortals") && this.plugin.getConfig().getBoolean("WarpPortals.Enable") && args[1] != null) {
					if (this.plugin.pdm != null) {
						if (args[1].equalsIgnoreCase("all")) {
							List<Warp> warps = Warp.getWarps();

							if (warps.size() == 0) {
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoWarps));
								return true;
							}
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
			}
		}
		return false;
	}

	@NotNull
	@Override
	public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
		if (sender.hasPermission("clickwarp.exportwarp")) {
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
