package de.comniemeer.ClickWarp.Commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
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
					List<String> warps = this.plugin.methods.getWarps();

					if (warps.size() == 0) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoWarps));
						return true;
					}
					if (args[1].equalsIgnoreCase("all")) {
						for (String warp : warps) {

							String str = warp.replace(".yml", "");

							String name_ = this.plugin.methods.getName(str);
							Location loc = this.plugin.methods.getWarp(str);

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
						String name_ = this.plugin.methods.getName(args[1]);
						Location loc = this.plugin.methods.getWarp(args[1]);

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
		return false;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String label, String[] args) {
		return null;
	}

}