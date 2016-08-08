package de.comniemeer.ClickWarp.Commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.comniemeer.ClickWarp.AutoCommand;
import de.comniemeer.ClickWarp.ClickWarp;

public class CommandWarp extends AutoCommand<ClickWarp> {

	public CommandWarp(ClickWarp plugin) {
		super(plugin, "warp", "List all warps and warp to them", "warps");
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (args.length == 0) {
			if (sender.hasPermission("clickwarp.warps")) {
				File warps_folder = new File(plugin.getDataFolder() + "/Warps");

				if (warps_folder.isDirectory()) {
					File[] warps = warps_folder.listFiles();

					if (warps.length == 0) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoWarps));
					} else {
						List<String> list = new ArrayList<String>();

						for (int i = 0; i < warps.length; i++) {
							if (sender.hasPermission("clickwarp.warp." + warps[i].getName().replace(".yml", ""))
									|| sender.hasPermission("clickwarp.warp.*")) {
								list.add(warps[i].getName().replace(".yml", ""));
							}
						}

						if (list.size() == 0) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoWarps));
						} else {
							String warp_names = "§6";

							for (int i = 0; i < list.size(); i++) {
								File warp = new File("plugins/ClickWarp/Warps", list.get(i) + ".yml");
								FileConfiguration cfg = YamlConfiguration.loadConfiguration(warp);

								if (cfg.getString(warp.getName().replace(".yml", "") + ".name") == null) {
									warp_names += warp.getName().replace(".yml", "");
								} else {
									warp_names += ChatColor.translateAlternateColorCodes('&',
											cfg.getString(warp.getName().replace(".yml", "") + ".name"));
								}

								if (i + 1 < list.size()) {
									warp_names = warp_names + "§7, §6";
								}
							}

							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.WarpList));
							sender.sendMessage(warp_names);
						}
					}
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoWarps));
				}
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoPermission));
			}
		} else if (args.length == 1) {
			if (sender instanceof Player) {
				Player player = (Player) sender;

				if (player.hasPermission("clickwarp.warp")) {
					String str = args[0].toLowerCase();

					plugin.warphandler.handleWarp(player, str, args[0], false);
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoPermission));
				}
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.OnlyPlayers));
			}
		} else {
			sender.sendMessage("§e/warps");
			sender.sendMessage("§e/warp <warp>");
		}

		return true;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String label, String[] args) {
		if (sender.hasPermission("clickwarp.delwarp")) {
			List<String> warpList = new ArrayList<>();
			File warps_folder = new File("plugins/ClickWarp/Warps");

			if (warps_folder.isDirectory()) {
				File[] warps = warps_folder.listFiles();

				if (warps.length != 0) {
					for (int i = 0; i < warps.length; i++) {
						warpList.add(warps[i].getName().replace(".yml", ""));
					}
				}
			}

			if (args.length == 0) {
				return warpList;
			} else if (args.length == 1) {
				List<String> tabList = new ArrayList<>();

				for (String warp : warpList) {
					if (warp.startsWith(args[0].toLowerCase())) {
						tabList.add(warp);
					}
				}

				return tabList;
			}
		}
		return null;
	}
}