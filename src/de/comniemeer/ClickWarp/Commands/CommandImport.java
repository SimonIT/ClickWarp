package de.comniemeer.ClickWarp.Commands;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import de.comniemeer.ClickWarp.AutoCommand;
import de.comniemeer.ClickWarp.ClickWarp;
import de.comniemeer.ClickWarp.Metrics;
import de.comniemeer.ClickWarp.Metrics.Graph;

public class CommandImport extends AutoCommand<ClickWarp> {

	public CommandImport(ClickWarp plugin, String cmd, String description) {
		super(plugin, cmd, description);
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender.hasPermission("clickwarp.importwarp")) {
			if (args.length == 2) {
				if (args[0].equalsIgnoreCase("Essentials") && args[1] != null) {
					if (args[1].equalsIgnoreCase("all")) {
						Collection<String> EWarps = this.plugin.IWarps.getList();
						String warp_names = "§6";
						for (String EWarp : EWarps) {
							Location loc = null;
							String Name = EWarp;
							String str = ChatColor
									.stripColor(ChatColor.translateAlternateColorCodes('&', EWarp.toLowerCase()));
							if (this.plugin.IWarps != null) {
								try {
									loc = this.plugin.IWarps.getWarp(Name);
								} catch (Exception e1) {
									e1.printStackTrace();
								}
							} else {
								this.plugin.log.severe("[ClickWarp] Failed to load Essentials!");
								this.plugin.log.severe(
										"[ClickWarp] Install Essentials or set \"ImportEssentialsWarps\" in the config.yml to \"false\"");
							}
							File warp = new File("plugins/ClickWarp/Warps", str + ".yml");
							if (warp.exists()) {
								String name_new = null;
								String str_new = null;
								for (int i = 1; warp.exists(); i++) {
									name_new = Name + i;
									str_new = str + i;
									warp = new File("plugins/ClickWarp/Warps", str_new + ".yml");
								}
								str = str_new;
								Name = name_new;
							}
							FileConfiguration cfg = YamlConfiguration.loadConfiguration(warp);

							cfg.set(str + ".name", Name);
							cfg.set(str + ".world", loc.getWorld().getName());
							cfg.set(str + ".x", loc.getX());
							cfg.set(str + ".y", loc.getY());
							cfg.set(str + ".z", loc.getZ());
							cfg.set(str + ".yaw", loc.getYaw());
							cfg.set(str + ".pitch", loc.getPitch());

							try {
								cfg.save(warp);
							} catch (IOException e) {
								System.err.println(
										ChatColor.translateAlternateColorCodes('&', this.plugin.msg.ErrorFileSaving));
								e.printStackTrace();
							}
							warp_names += Name + "§7, §6";
						}
						sender.sendMessage(warp_names);
					} else {
						Location loc = null;
						String Name = args[1];
						String str = ChatColor
								.stripColor(ChatColor.translateAlternateColorCodes('&', args[1].toLowerCase()));
						if (this.plugin.IWarps != null) {
							try {
								loc = this.plugin.IWarps.getWarp(Name);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						} else {
							this.plugin.log.severe("[ClickWarp] Failed to load Essentials!");
							this.plugin.log.severe(
									"[ClickWarp] Install Essentials or set \"ImportEssentialsWarps\" in the config.yml to \"false\"");
						}
						File warp = new File("plugins/ClickWarp/Warps", str + ".yml");
						if (warp.exists()) {
							String name_new = null;
							String str_new = null;
							for (int i = 0; warp.exists(); i++) {
								name_new = Name + i;
								str_new = str + i;
								warp = new File("plugins/ClickWarp/Warps", str_new + ".yml");
							}
							str = str_new;
							Name = name_new;
						}
						FileConfiguration cfg = YamlConfiguration.loadConfiguration(warp);

						cfg.set(str + ".name", Name);
						cfg.set(str + ".world", loc.getWorld().getName());
						cfg.set(str + ".x", loc.getX());
						cfg.set(str + ".y", loc.getY());
						cfg.set(str + ".z", loc.getZ());
						cfg.set(str + ".yaw", loc.getYaw());
						cfg.set(str + ".pitch", loc.getPitch());

						try {
							cfg.save(warp);
						} catch (IOException e) {
							System.err.println(
									ChatColor.translateAlternateColorCodes('&', this.plugin.msg.ErrorFileSaving));
							e.printStackTrace();
						}
					}

					File warps_folder = new File("plugins/ClickWarp/Warps");
					File[] warps = warps_folder.listFiles();

					final int files = warps.length;

					try {
						Metrics metrics = new Metrics(this.plugin);
						Graph Warps = metrics.createGraph("Warps");

						Warps.addPlotter(new Metrics.Plotter("Warps") {
							@Override
							public int getValue() {
								return files;
							}
						});

						metrics.start();
					} catch (IOException e) {
						e.printStackTrace();
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
