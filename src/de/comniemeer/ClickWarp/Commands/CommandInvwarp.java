package de.comniemeer.ClickWarp.Commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.comniemeer.ClickWarp.AutoCommand;
import de.comniemeer.ClickWarp.ClickWarp;

public class CommandInvwarp extends AutoCommand<ClickWarp> {
	
	public CommandInvwarp (ClickWarp plugin) {
		super(plugin, "invwarp", "Warp through an inventory");
	}
	
	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender.hasPermission("clickwarp.invwarp")) {
			if (sender instanceof Player) {
				if (args.length == 0) {
					Player p = (Player) sender;
					File warps_folder = new File("plugins/ClickWarp/Warps");
					
					if (warps_folder.isDirectory()) {
						File[] warps = warps_folder.listFiles();
						
						if (warps.length == 0) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoWarps));
							return true;
						} else if (warps.length > 54) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.InvwarpTooManyWarps));
							return true;
						}
						
						int lines = 0;
						
						while (lines * 9 < warps.length) {
							lines++;
						}
						
						if (lines > 6) {
							lines = 6;
						}
						
						Inventory inv = Bukkit.createInventory(null, lines*9, ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Inventory.Warp")));
						int slot = 0;
						List<String> list = new ArrayList<String>();
						
						for (int i = 0; i < warps.length; i++) {
							if (p.hasPermission("clickwarp.warp." + warps[i].getName().replace(".yml", "")) || p.hasPermission("clickwarp.warp.*")) {
								list.add(warps[i].getName().replace(".yml", ""));
							}
						}
						
						if (list.size() == 0) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoWarps));
						} else {
							for (int i = 0; i < list.size(); i++) {
								File warp = new File("plugins/ClickWarp/Warps", list.get(i) + ".yml");
								FileConfiguration cfg = YamlConfiguration.loadConfiguration(warp);
								String str = warp.getName().replace(".yml", "");
								ItemStack itemstack = null;
								
								if (cfg.get(str + ".item") != null) {
									String item__ = cfg.getString(str + ".item");
									Material material = null;
									int item_id = 0;
									int item_meta = 0;
									
									if (item__.contains(":")) {
										String[] item___ = item__.split(":");
										
										for (int j = 0; j < item___.length; j++) {
											item_meta = Integer.parseInt(item___[j]);
											
											if (j + 1 < item___.length) {
												item_id = Integer.parseInt(item___[j]);
											}
										}
										
										material = Material.getMaterial(item_id);
										itemstack = new ItemStack(material.getId(), 1, (short) item_meta);
									} else {
										item_id = Integer.parseInt(item__);
										material = Material.getMaterial(item_id);
										itemstack = new ItemStack(material.getId());
									}
								} else {
									String item__ = plugin.getConfig().getString("DefaultWarpItem");
									Material material = null;
									int item_id = 0;
									int item_meta = 0;
									
									if (item__.contains(":")) {
										String[] item___ = item__.split(":");
										
										for (int j = 0; j < item___.length; j++) {
											item_meta = Integer.parseInt(item___[j]);
											
											if (j + 1 < item___.length) {
												item_id = Integer.parseInt(item___[j]);
											}
										}
										
										material = Material.getMaterial(item_id);
										itemstack = new ItemStack(material.getId(), 1, (short) item_meta);
									} else {
										item_id = Integer.parseInt(item__);
										material = Material.getMaterial(item_id);
										itemstack = new ItemStack(material.getId());
									}
								}
								
								String name = "";
								
								if (cfg.getString(str + ".name") == null) {
									name = str;
								} else {
									name = ChatColor.translateAlternateColorCodes('&', cfg.getString(str + ".name"));
								}
								
								ItemMeta meta = itemstack.getItemMeta();
								
								meta.setDisplayName("§r" + name);
								
								if (cfg.get(str + ".lore") != null) {
									String[] lore_ = cfg.get(str + ".lore").toString().split(":");
									List<String> lore = new ArrayList<String>();
									
									for (int l = 0; l < lore_.length; l++) {
										lore.add(ChatColor.translateAlternateColorCodes('&', lore_[l].replaceAll("_", " ")));
									}
									
									Boolean useeconomy = plugin.getConfig().getBoolean("Economy.Enable");
									
									if (useeconomy.booleanValue()) {
										Boolean useshowprice = plugin.getConfig().getBoolean("Economy.ShowPrice");
										
										if (useshowprice.booleanValue()) {
											if (cfg.getString(str + ".price") != null) {
												Double price = cfg.getDouble(str + ".price");
												String priceformat = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Economy.PriceFormat").replace("{price}", String.valueOf(price)));

												if (price == 1) {
													lore.add(priceformat.replace("{currency}", plugin.getConfig().getString("Economy.CurrencySingular")));
												} else {
													lore.add(priceformat.replace("{currency}", plugin.getConfig().getString("Economy.CurrencyPlural")));
												}
											}
										}
									}
									
									meta.setLore(lore);
								} else {
									Boolean useeconomy = plugin.getConfig().getBoolean("Economy.Enable");
									
									if (useeconomy.booleanValue()) {
										Boolean useshowprice = plugin.getConfig().getBoolean("Economy.ShowPrice");
										
										if (useshowprice.booleanValue()) {
											if (cfg.getString(str + ".price") != null) {
												List<String> lore = new ArrayList<String>();
												Double price = cfg.getDouble(str + ".price");
												String priceformat = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Economy.PriceFormat").replace("{price}", String.valueOf(price)));
												
												if (price == 1) {
													lore.add(priceformat.replace("{currency}", plugin.getConfig().getString("Economy.CurrencySingular")));
												} else {
													lore.add(priceformat.replace("{currency}", plugin.getConfig().getString("Economy.CurrencyPlural")));
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
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoWarps));
					}
				} else {
					sender.sendMessage("§e/invwarp");
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