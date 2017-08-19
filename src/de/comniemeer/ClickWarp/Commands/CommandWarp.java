package de.comniemeer.ClickWarp.Commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import de.comniemeer.ClickWarp.AutoCommand;
import de.comniemeer.ClickWarp.ClickWarp;

public class CommandWarp extends AutoCommand<ClickWarp> {

	public CommandWarp(ClickWarp plugin, String cmd, String description, String... alias) {
		super(plugin, cmd, description, alias);
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (args.length == 0) {
			if (!plugin.getConfig().getBoolean("InvwarpInsteadWarp")) {
				if (sender.hasPermission("clickwarp.warps")) {
					List<String> warps = this.plugin.methods.getWarps();

					if (warps.size() == 0) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoWarps));
					} else {

						String warp_names = "§6";

						for (int i = 0; i < warps.size(); i++) {

							if (i < warps.size()) {
								warp_names = warp_names + warps.get(i) + "§7, §6";
							}
						}
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.WarpList));
						sender.sendMessage(warp_names);
					}
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoPermission));
				}
			} else {
				CommandInvwarp invwarp = new CommandInvwarp(plugin, "invwarp", "Inventory-Warp command", "invwarps");
				invwarp.execute(sender, label, args);
			}
		} else if (args.length == 1) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				String str = args[0].toLowerCase();

				if (player.hasPermission("clickwarp.warp." + str) || player.hasPermission("clickwarp.warp.*")) {
					plugin.warphandler.handleWarp(player, str, args[0], false);
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoPermission));
				}
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.OnlyPlayers));
			}
		} else if (args.length == 2) {
			if (args[1].equalsIgnoreCase("getitem")) {
				if (sender instanceof Player) {
					if (this.plugin.getConfig().getBoolean("GetWarpItem")) {
						if (sender.hasPermission("clickwarp.warp.getitem.*")
								|| sender.hasPermission("clickwarp.warp.getitem." + args[0])) {
							String str = args[0].toLowerCase();
							if (!this.plugin.methods.existWarp(str)) {
								sender.sendMessage(
										ChatColor.translateAlternateColorCodes('&', this.plugin.msg.WarpNoExist)
												.replace("{warp}", args[0]));
							} else {
								ItemStack itemstack = this.plugin.methods.getItemStack(str);

								List<String> lore = new ArrayList<String>();
								Boolean useeconomy = this.plugin.getConfig().getBoolean("Economy.Enable");
								if (useeconomy.booleanValue()) {
									Boolean useshowprice = this.plugin.getConfig().getBoolean("Economy.ShowPrice");
									if ((useshowprice)) {
										Double price = this.plugin.methods.getPrice(str);
										String priceformat = ChatColor.translateAlternateColorCodes('&',
												this.plugin.getConfig().getString("Economy.PriceFormat")
														.replace("{price}", String.valueOf(price)));
										if (price.doubleValue() == 1.0D) {
											lore.add(priceformat.replace("{currency}",
													this.plugin.getConfig().getString("Economy.CurrencySingular")));
										} else {
											lore.add(priceformat.replace("{currency}",
													this.plugin.getConfig().getString("Economy.CurrencyPlural")));
										}
									}
								}
								ItemMeta item_lore = itemstack.getItemMeta();
								lore = this.plugin.methods.getPreparedLore(str);
								item_lore.setLore(lore);
								String item_prefix = ChatColor.translateAlternateColorCodes('&',
										this.plugin.getConfig().getString("Sign.FirstLine")) + " ";
								item_lore.setDisplayName(item_prefix + this.plugin.methods.getName(args[0]));
								itemstack.setItemMeta(item_lore);
								Player player = (Player) sender;
								PlayerInventory inventory = player.getInventory();
								inventory.setItemInMainHand(itemstack);
							}
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoPermission));
						}
					}
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.OnlyPlayers));
				}
			} else if (args[1].equalsIgnoreCase("all")) {
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (player.hasPermission("clickwarp.warp")) {
						String str = args[0].toLowerCase();

						plugin.warphandler.handleWarp(player, str, args[0], false);
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoPermission));
					}
				}
			} else if (args[1].contains("g:")) {
				String group = args[1].replace("g:", "");
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (plugin.permission.playerInGroup(player.getWorld().getName(), player, group)) {
						if (player.hasPermission("clickwarp.warp")) {
							String str = args[0].toLowerCase();

							plugin.warphandler.handleWarp(player, str, args[0], false);
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoPermission));
						}
					}
				}
			} else if (Bukkit.getPlayer(args[1]) != null) {
				Player player = Bukkit.getPlayer(args[1]);
				if (player.isOnline()) {
					if (player.hasPermission("clickwarp.warp")) {
						String str = args[0].toLowerCase();

						plugin.warphandler.handleWarp(player, str, args[0], false);
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoPermission));
					}
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.msg.InvTPNotOnline)
							.replace("{player}", player.getDisplayName()));
				}
			}
		} else {
			sender.sendMessage("§e/warps");
			sender.sendMessage("§e/warp <warp>");
			sender.sendMessage("§e/warp <warp> <user>");
			sender.sendMessage("§e/warp <warp> getitem");
		}

		return true;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String label, String[] args) {
		List<String> warpList = new ArrayList<>();
		File warps_folder = new File("plugins/ClickWarp/Warps");

		if (warps_folder.isDirectory()) {
			File[] warps = warps_folder.listFiles();

			if (warps.length != 0) {
				for (int i = 0; i < warps.length; i++) {
					if (sender.hasPermission("clickwarp.warp." + warps[i].getName().replace(".yml", ""))
							|| sender.hasPermission("clickwarp.warp.*")) {
						warpList.add(this.plugin.methods.getName(warps[i].getName().replace(".yml", "")));
					}
				}
			}
		}

		if (args.length == 0) {
			return warpList;
		} else if (args.length == 1) {
			List<String> tabList = new ArrayList<>();

			for (String warp : warpList) {
				if (warp.toLowerCase().startsWith(args[0].toLowerCase())) {
					if (sender.hasPermission("clickwarp.warp." + warp.toLowerCase())
							|| sender.hasPermission("clickwarp.warp.*")) {
						tabList.add(warp);
					}
				}
			}

			return tabList;
		} else if (args.length == 2) {
			List<String> tabList = new ArrayList<>();
			if ("getitem".startsWith(args[1].toLowerCase())) {
				tabList.add("getitem");
			}

			List<String> playerList = new ArrayList<>();

			for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
				if (p != null) {
					playerList.add(p.getName());
				}
			}

			for (String player : playerList) {
				if (player != null && player.toLowerCase().startsWith(args[1].toLowerCase())) {
					tabList.add(player);
				}
			}

			return tabList;

		}
		return null;
	}
}