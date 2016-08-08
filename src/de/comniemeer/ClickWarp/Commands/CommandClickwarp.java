package de.comniemeer.ClickWarp.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import de.comniemeer.ClickWarp.AutoCommand;
import de.comniemeer.ClickWarp.ClickWarp;

public class CommandClickwarp extends AutoCommand<ClickWarp> {

	public CommandClickwarp(ClickWarp plugin) {
		super(plugin, "clickwarp", "ClickWarp main command");
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender.hasPermission("clickwarp.clickwarp")) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("reload")) {
					this.reload();
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.PluginReloaded));
				} else if (args.length == 1 && args[0].equalsIgnoreCase("version")) {
					sender.sendMessage("§7[§6ClickWarp§7] Version §6" + plugin.version + "§7 by §6comniemeer");
				}
			} else {
				sender.sendMessage("§e/clickwarp <version | reload>");
			}
		} else {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoPermission));
		}

		return true;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String label, String[] args) {
		if (sender.hasPermission("clickwarp.clickwarp")) {
			List<String> arguments = new ArrayList<>();

			arguments.add("reload");
			arguments.add("version");

			if (args.length == 0) {
				return arguments;
			} else if (args.length == 1) {
				List<String> tabList = new ArrayList<>();

				for (String str : arguments) {
					if (str.startsWith(args[0].toLowerCase())) {
						tabList.add(str);
					}
				}

				return tabList;
			}
		}

		return null;
	}

	public void reload() {
		plugin.reloadConfig();

		plugin.en.load();
		plugin.de.load();
		plugin.fr.load();
		plugin.pt.load();
		plugin.cz.load();
		plugin.msg.load();
	}
}