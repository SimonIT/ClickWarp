package de.comniemeer.ClickWarp.Commands;

import de.comniemeer.ClickWarp.AutoCommand;
import de.comniemeer.ClickWarp.ClickWarp;
import de.comniemeer.ClickWarp.Warp;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CommandInvwarp extends AutoCommand<ClickWarp> {

	public CommandInvwarp(ClickWarp plugin, String cmd, String description, String alias) {
		super(plugin, cmd, description, alias);
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender.hasPermission("clickwarp.invwarp")) {
			if (sender instanceof Player) {
				if (args.length == 0) {
					Player p = (Player) sender;

					List<Warp> warps = Warp.getWarps(this.plugin);

					if (warps != null) {
						if (warps.size() == 0) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoWarps));
							return true;
						} else if (warps.size() > 54) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.InvwarpTooManyWarps));
							return true;
						}
					}

					int lines = 0;

					if (warps != null) {
						while (lines * 9 < warps.size()) {
							lines++;
						}
					}

					if (lines > 6) {
						lines = 6;
					}

					Inventory inv = Bukkit.createInventory(null, lines * 9, ChatColor
							.translateAlternateColorCodes('&', plugin.getConfig().getString("Inventory.Warp")));
					int slot = 0;

					if (warps != null) {
						if (warps.size() == 0) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoWarps));
						} else {
							for (Warp aList : warps) {
								ItemStack itemstack = aList.getItem();

								String name = aList.getName();

								ItemMeta meta = itemstack.getItemMeta();

								meta.setDisplayName(ChatColor.RESET + name);

								if (aList.getPreparedLore() != null) {
									List<String> lore = aList.getPreparedLore();

									boolean useeconomy = plugin.getConfig().getBoolean("Economy.Enable");

									if (useeconomy) {
										boolean useshowprice = plugin.getConfig().getBoolean("Economy.ShowPrice");

										if (useshowprice) {
											if (aList.getPrice() != null) {
												Double price = aList.getPrice();
												String priceformat = ChatColor.translateAlternateColorCodes('&',
														plugin.getConfig().getString("Economy.PriceFormat")
																.replace("{price}", String.valueOf(price)));

												if (price == 1) {
													lore.add(priceformat.replace("{currency}",
															plugin.getConfig().getString("Economy.CurrencySingular")));
												} else {
													lore.add(priceformat.replace("{currency}",
															plugin.getConfig().getString("Economy.CurrencyPlural")));
												}
											}
										}
									}

									meta.setLore(lore);
								} else {
									boolean useeconomy = plugin.getConfig().getBoolean("Economy.Enable");

									if (useeconomy) {
										boolean useshowprice = plugin.getConfig().getBoolean("Economy.ShowPrice");

										if (useshowprice) {
											if (aList.getPrice() != null) {
												List<String> lore = new ArrayList<String>();
												Double price = aList.getPrice();
												String priceformat = ChatColor.translateAlternateColorCodes('&',
														plugin.getConfig().getString("Economy.PriceFormat")
																.replace("{price}", String.valueOf(price)));

												if (price == 1) {
													lore.add(priceformat.replace("{currency}",
															plugin.getConfig().getString("Economy.CurrencySingular")));
												} else {
													lore.add(priceformat.replace("{currency}",
															plugin.getConfig().getString("Economy.CurrencyPlural")));
												}

												meta.setLore(lore);
											}
										}
									}
								}

								itemstack.setItemMeta(meta);
								inv.setItem(slot, itemstack);
								slot++;
							}
						}
						p.openInventory(inv);
						plugin.InvHM.put(p.getName(), "InventarWarp");
					}
				} else {
					sender.sendMessage(ChatColor.YELLOW + "/invwarp");
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
