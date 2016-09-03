package de.comniemeer.ClickWarp.Commands;

import java.util.Collection;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import de.comniemeer.ClickWarp.AutoCommand;
import de.comniemeer.ClickWarp.ClickWarp;

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
							if (this.plugin.methods.existWarp(EWarp)) {
								String name_new = null;
								for (int i = 1; this.plugin.methods.existWarp(name_new); i++) {
									name_new = Name + i;
								}
								Name = name_new;
							}
							boolean result = this.plugin.methods.setWarp(Name, loc);
							if (result) {
								this.plugin.log
										.severe(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpSuccess)
												.replace("{warp}", ChatColor.translateAlternateColorCodes('&', Name)));
							} else {
								this.plugin.log.severe(
										ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpInvalidName));
							}
							warp_names += Name + "§7, §6";
						}
						sender.sendMessage(warp_names);
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
							this.plugin.log.severe("[ClickWarp] Failed to load Essentials!");
							this.plugin.log.severe(
									"[ClickWarp] Install Essentials or set \"ImportEssentialsWarps\" in the config.yml to \"false\"");
						}
						if (this.plugin.methods.existWarp(Name)) {
							String name_new = null;
							for (int i = 1; this.plugin.methods.existWarp(name_new); i++) {
								name_new = Name + i;
							}
							Name = name_new;
						}
						boolean result = this.plugin.methods.setWarp(Name, loc);
						if (result) {
							this.plugin.log
									.severe(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpSuccess)
											.replace("{warp}", ChatColor.translateAlternateColorCodes('&', Name)));
						} else {
							this.plugin.log
									.severe(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpInvalidName));
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
		return null;
	}
}
