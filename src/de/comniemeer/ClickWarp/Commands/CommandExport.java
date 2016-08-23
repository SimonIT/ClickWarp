package de.comniemeer.ClickWarp.Commands;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import de.comniemeer.ClickWarp.AutoCommand;
import de.comniemeer.ClickWarp.ClickWarp;

public class CommandExport extends AutoCommand<ClickWarp> {

	public CommandExport(ClickWarp plugin, String cmd, String description) {
		super(plugin, cmd, description);
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender.hasPermission("clickwarp.importwarp")) {
			if (args.length == 2) {
				if (args[0].equalsIgnoreCase("Essentials") && args[1] != null) {
					File warps_folder = new File("plugins/ClickWarp/Warps");

					if (warps_folder.isDirectory()) {
						File[] warps = warps_folder.listFiles();

						if (warps.length == 0) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoWarps));
							return true;
						}
						if (args[1].equalsIgnoreCase("all")) {
							for (File warp : warps) {

								String str = warp.getName().replace(".yml", "");

								FileConfiguration cfg = YamlConfiguration.loadConfiguration(warp);

								String name_ = null;
								if (cfg.getString(str + ".name") == null) {
									name_ = str;
								} else {
									name_ = ChatColor.translateAlternateColorCodes('&', cfg.getString(str + ".name"));
								}

								World w = Bukkit.getWorld(cfg.getString(str + ".world"));
								double x = cfg.getDouble(str + ".x");
								double y = cfg.getDouble(str + ".y");
								double z = cfg.getDouble(str + ".z");
								double yaw = cfg.getDouble(str + ".yaw");
								double pitch = cfg.getDouble(str + ".pitch");

								Location loc = new Location(w, x, y, z, (float) yaw, (float) pitch);

								if (this.plugin.IWarps != null) {
									try {
										this.plugin.IWarps.setWarp(name_, loc);
									} catch (Exception e1) {
										e1.printStackTrace();
									}

								} else {
									this.plugin.log.severe("[ClickWarp] Failed to load Essentials!");
									this.plugin.log.severe(
											"[ClickWarp] Install Essentials or set \"ImportEssentialsWarps\" in the config.yml to \"false\"");
								}
							}
						} else {
							String str = ChatColor
									.stripColor(ChatColor.translateAlternateColorCodes('&', args[1].toLowerCase()));

							File warp = new File("plugins/ClickWarp/Warps", str + ".yml");
							FileConfiguration cfg = YamlConfiguration.loadConfiguration(warp);

							String name_ = null;
							if (cfg.getString(str + ".name") == null) {
								name_ = str;
							} else {
								name_ = ChatColor.translateAlternateColorCodes('&', cfg.getString(str + ".name"));
							}

							World w = Bukkit.getWorld(cfg.getString(str + ".world"));
							double x = cfg.getDouble(str + ".x");
							double y = cfg.getDouble(str + ".y");
							double z = cfg.getDouble(str + ".z");
							double yaw = cfg.getDouble(str + ".yaw");
							double pitch = cfg.getDouble(str + ".pitch");

							Location loc = new Location(w, x, y, z, (float) yaw, (float) pitch);

							if (this.plugin.IWarps != null) {
								try {
									this.plugin.IWarps.setWarp(name_, loc);
								} catch (Exception e1) {
									e1.printStackTrace();
								}

							} else {
								this.plugin.log.severe("[ClickWarp] Failed to load Essentials!");
								this.plugin.log.severe(
										"[ClickWarp] Install Essentials or set \"ImportEssentialsWarps\" in the config.yml to \"false\"");
							}
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String label, String[] args) {
		return null;
	}

}
