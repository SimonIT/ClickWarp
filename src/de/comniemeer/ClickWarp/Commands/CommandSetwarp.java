package de.comniemeer.ClickWarp.Commands;

import de.comniemeer.ClickWarp.AutoCommand;
import de.comniemeer.ClickWarp.ClickWarp;
import de.comniemeer.ClickWarp.Exceptions.InvalidItem;
import de.comniemeer.ClickWarp.Exceptions.InvalidName;
import de.comniemeer.ClickWarp.Warp;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.List;

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
					try {
						Warp warp = new Warp(args[0], player.getLocation(), player);
						warp.save();
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpSuccess)
								.replace("{warp}", ChatColor.translateAlternateColorCodes('&', warp.getName())));
					} catch (InvalidName invalidName) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpInvalidName));
					} catch (IOException e) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.ErrorFileSaving));
					}
				} else if (args.length == 2) {
					try {
						Warp warp = new Warp(args[0], player.getLocation(), player);
						warp.setItem(args[1]);
						warp.save();
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpSuccess)
								.replace("{warp}", ChatColor.translateAlternateColorCodes('&', warp.getName())));
					} catch (InvalidName invalidName) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpInvalidName));
					} catch (InvalidItem invalidItem) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpInvalidItem));
					} catch (IOException e) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.ErrorFileSaving));
					}
				} else if (args.length == 3) {
					try {
						Warp warp = new Warp(args[0], player.getLocation(), player);
						warp.setItem(args[1]);
						double price = Double.parseDouble(args[2]);
						warp.setPrice(price);
						warp.save();
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpSuccess)
								.replace("{warp}", ChatColor.translateAlternateColorCodes('&', warp.getName())));
					} catch (InvalidName invalidName) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpInvalidName));
					} catch (InvalidItem invalidItem) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpInvalidItem));
					} catch (NumberFormatException e) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpNeedNumber));
					} catch (IOException e) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.ErrorFileSaving));
					}
				} else {
					sender.sendMessage(ChatColor.YELLOW + "/setwarp <name>");
					sender.sendMessage(ChatColor.YELLOW + "/setwarp <name> <item>");
					sender.sendMessage(ChatColor.YELLOW + "/setwarp <name> <item> <price>");
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
