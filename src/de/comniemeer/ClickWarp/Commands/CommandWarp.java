package de.comniemeer.ClickWarp.Commands;

import de.comniemeer.ClickWarp.AutoCommand;
import de.comniemeer.ClickWarp.ClickWarp;
import de.comniemeer.ClickWarp.Exceptions.WarpNoExist;
import de.comniemeer.ClickWarp.Warp;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CommandWarp extends AutoCommand<ClickWarp> {

	private final static String GROUP_PREFIX = "g:";

	public CommandWarp(ClickWarp plugin, String cmd, String description, String... alias) {
		super(plugin, cmd, description, alias);
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (args.length == 0) {
			if (!plugin.getConfig().getBoolean("InvwarpInsteadWarp")) {
				if (sender.hasPermission("clickwarp.warps")) {
					List<Warp> warps = Warp.getWarps();

					if (warps.size() == 0) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoWarps));
					} else {

						StringBuilder warp_names = new StringBuilder(ChatColor.GOLD.toString());

						for (Warp warp : warps) {
							warp_names.append(warp.getName()).append(ChatColor.GRAY).append(", ").append(ChatColor.GOLD);
						}
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.WarpList));
						sender.sendMessage(warp_names.toString());
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
					try {
						Warp warp = new Warp(args[0]);
						warp.handleWarp(player, false);
					} catch (WarpNoExist warpNoExist) {
						sender.sendMessage(
								ChatColor.translateAlternateColorCodes('&', this.plugin.msg.WarpNoExist)
										.replace("{warp}", args[0]));
					}
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
						String str = args[0].toLowerCase();
						if (sender.hasPermission("clickwarp.warp.getitem.*")
								|| sender.hasPermission("clickwarp.warp.getitem." + str)) {
							try {
								Warp warp = new Warp(args[0]);
								ItemStack itemstack = warp.getItem();

								List<String> lore = new ArrayList<>();
								boolean useeconomy = this.plugin.getConfig().getBoolean("Economy.Enable");
								if (useeconomy) {
									boolean useshowprice = this.plugin.getConfig().getBoolean("Economy.ShowPrice");
									if ((useshowprice)) {
										double price = warp.getPrice();
										if (price > 0) {
											String priceFormat = ChatColor.translateAlternateColorCodes('&',
													this.plugin.getConfig().getString("Economy.PriceFormat")
															.replace("{price}", String.valueOf(price)));
											if (price == 1) {
												lore.add(priceFormat.replace("{currency}",
														this.plugin.getConfig().getString("Economy.CurrencySingular")));
											} else {
												lore.add(priceFormat.replace("{currency}",
														this.plugin.getConfig().getString("Economy.CurrencyPlural")));
											}
										}
									}
								}
								ItemMeta item_lore = itemstack.getItemMeta();
								lore = warp.getPreparedLore();
								item_lore.setLore(lore);
								String item_prefix = ChatColor.translateAlternateColorCodes('&',
										this.plugin.getConfig().getString("Sign.FirstLine")) + " ";
								item_lore.setDisplayName(item_prefix + warp.getName());
								itemstack.setItemMeta(item_lore);
								PlayerInventory inventory = ((Player) sender).getInventory();
								inventory.addItem(itemstack);
							} catch (WarpNoExist warpNoExist) {
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.msg.WarpNoExist)
										.replace("{warp}", args[0]));
							}
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoPermission));
						}
					}
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.OnlyPlayers));
				}
			} else if (args[1].equalsIgnoreCase("info")) {
				String str = args[0].toLowerCase();
				if (sender.hasPermission("clickwarp.warp.info.*") || sender.hasPermission("clickwarp.warp.info." + str) || sender.hasPermission("clickwarp.warp.*")) {
					try {
						Warp warp = new Warp(args[0]);
						Location loc = warp.getLocation();

						sender.sendMessage(ChatColor.GRAY + "============= Warp Info =============");
						sender.sendMessage(ChatColor.BLUE + "Name: " + ChatColor.GOLD + warp.getName());
						sender.sendMessage(ChatColor.BLUE + "Player: " + ChatColor.GOLD + warp.getPlayer().getName());
						sender.sendMessage(ChatColor.BLUE + "Coordinates: " + ChatColor.GOLD + Math.round(loc.getX()) + ", " + Math.round(loc.getY()) + ", " + Math.round(loc.getZ()));
						sender.sendMessage(ChatColor.BLUE + "Item: " + ChatColor.GOLD + warp.getItem().getType().name());
						if (warp.getPrice() > 0)
							sender.sendMessage(ChatColor.BLUE + "Price: " + ChatColor.GOLD + warp.getPrice());
						sender.sendMessage(ChatColor.BLUE + "Lore: " + ChatColor.GOLD + String.join("\n", warp.getPreparedLore()));
					} catch (WarpNoExist warpNoExist) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.msg.WarpNoExist).replace("{warp}", args[0]));
					}
				}
			} else if (args[1].equalsIgnoreCase("all")) {
				if (sender.hasPermission("clickwarp.warp")) {
					try {
						Warp warp = new Warp(args[0]);
						for (Player player : Bukkit.getOnlinePlayers()) {
							warp.handleWarp(player, false);
						}
					} catch (WarpNoExist warpNoExist) {
						sender.sendMessage(
								ChatColor.translateAlternateColorCodes('&', this.plugin.msg.WarpNoExist)
										.replace("{warp}", args[0]));
					}
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoPermission));
				}
			} else if (args[1].startsWith(GROUP_PREFIX)) {
				if (sender.hasPermission("clickwarp.warp")) {
					if (this.plugin.permission != null) {
						String group = args[1].replace("g:", "");
						try {
							Warp warp = new Warp(args[0]);
							for (Player player : Bukkit.getOnlinePlayers()) {
								if (this.plugin.permission.playerInGroup(player, group)) {
									warp.handleWarp(player, false);
								}
							}
						} catch (WarpNoExist warpNoExist) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.msg.WarpNoExist)
									.replace("{warp}", args[0]));
						}
					}
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoPermission));
				}
			} else if (Bukkit.getPlayer(args[1]) != null) {
				Player player = Bukkit.getPlayer(args[1]);
				if (player.isOnline()) {
					if (sender.hasPermission("clickwarp.warp")) {
						try {
							Warp warp = new Warp(args[0]);
							warp.handleWarp(player, false);
						} catch (WarpNoExist warpNoExist) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.msg.WarpNoExist)
									.replace("{warp}", args[0]));
						}
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoPermission));
					}
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.msg.InvTPNotOnline)
							.replace("{player}", player.getDisplayName()));
				}
			}
		} else {
			sender.sendMessage(ChatColor.YELLOW + "/warps");
			sender.sendMessage(ChatColor.YELLOW + "/warp <warp>");
			sender.sendMessage(ChatColor.YELLOW + "/warp <warp> <user>");
			if (this.plugin.permission != null && this.plugin.permission.hasGroupSupport())
				sender.sendMessage(ChatColor.YELLOW + "/warp <warp> g:<group>");
			sender.sendMessage(ChatColor.YELLOW + "/warp <warp> all");
			sender.sendMessage(ChatColor.YELLOW + "/warp <warp> info");
			sender.sendMessage(ChatColor.YELLOW + "/warp <warp> getitem");
		}

		return true;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String label, String[] args) {
		List<String> warpList = new ArrayList<>();

		List<Warp> warps = Warp.getWarps();

		if (warps != null && warps.size() != 0) {
			for (Warp warp : warps) {
				if (sender.hasPermission("clickwarp.warp." + warp.getName())
						|| sender.hasPermission("clickwarp.warp.*")) {
					warpList.add(warp.getName());
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

			if ("all".startsWith(args[1].toLowerCase())) {
				tabList.add("all");
			}

			if ("info".startsWith(args[1].toLowerCase())) {
				tabList.add("info");
			}

			List<String> playerList = new ArrayList<>();

			for (OfflinePlayer p : Bukkit.getOnlinePlayers()) {
				if (p != null) {
					playerList.add(p.getName());
				}
			}

			for (String player : playerList) {
				if (player != null && player.toLowerCase().startsWith(args[1].toLowerCase())) {
					tabList.add(player);
				}
			}

			if (this.plugin.permission != null && this.plugin.permission.hasGroupSupport()) {
				for (String group : this.plugin.permission.getGroups()) {
					group = GROUP_PREFIX.concat(group.toLowerCase());
					if (group.startsWith(args[1].toLowerCase())) {
						tabList.add(group);
					}
				}
			}

			return tabList;

		}
		return null;
	}
}
