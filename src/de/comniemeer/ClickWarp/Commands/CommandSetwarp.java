package de.comniemeer.ClickWarp.Commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.comniemeer.ClickWarp.AutoCommand;
import de.comniemeer.ClickWarp.ClickWarp;

public class CommandSetwarp extends AutoCommand<ClickWarp> {

	public CommandSetwarp(ClickWarp plugin, String cmd, String description, String... alias) {
		super(plugin, cmd, description, alias);
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender.hasPermission("clickwarp.setwarp")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (args.length == 1) {
					boolean result = this.plugin.methods.setWarp(args[0], player.getLocation());
					if (result) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpSuccess)
								.replace("{warp}", ChatColor.translateAlternateColorCodes('&', args[0])));
					} else {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpInvalidName));
					}
				} else if (args.length == 2) {
					boolean result = this.plugin.methods.setWarp(args[0], player.getLocation());
					boolean result1 = this.plugin.methods.setItem(args[0], args[1]);

					if (result) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpSuccess)
								.replace("{warp}", ChatColor.translateAlternateColorCodes('&', args[0])));
					}
					if (!result1) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpInvalidItem));
					}

					this.plugin.methods.updateMetrics();
				} else if (args.length == 3) {
					boolean result = this.plugin.methods.setWarp(args[0], player.getLocation());
					boolean result1 = this.plugin.methods.setItem(args[0], args[1]);

					Double price;
					try {
						price = Double.parseDouble(args[2]);
					} catch (NumberFormatException e) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpNeedNumber));
						return true;
					}

					this.plugin.methods.setPrice(args[0], price);

					this.plugin.methods.updateMetrics();

					if (result) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpSuccess)
								.replace("{warp}", ChatColor.translateAlternateColorCodes('&', args[0])));
					}
					if (!result1) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpInvalidItem));
					}
				} else {
					sender.sendMessage("§e/setwarp <name>");
					sender.sendMessage("§e/setwarp <name> <item>");
					sender.sendMessage("§e/setwarp <name> <item> <price>");
				}
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.OnlyPlayers));
			}
		} else {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoPermission));
		}

		return true;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String label, String[] args) {
		return null;
	}
}