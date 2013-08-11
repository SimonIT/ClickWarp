package de.comniemeer.ClickWarp;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class WarpHandler {
	
	private ClickWarp plugin;
	public WarpHandler(ClickWarp clickwarp) {
		plugin = clickwarp;
	}
	
	public void handleWarp(final Player player, String str, String arg, Boolean fromsign) {
		File warp = new File("plugins/ClickWarp/Warps", str + ".yml");
		
		if (!(warp.exists())) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.WarpNoExist).replace("{warp}", arg));
			return;
		}
		
		if (player.hasPermission("clickwarp.warp." + str) || player.hasPermission("clickwarp.warp.*")) {
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(warp);
			Boolean enableEconomy = plugin.getConfig().getBoolean("Economy.Enable");
			double _price = 0;
			Boolean _payed = false;
			
			if (enableEconomy.booleanValue()) {
				if (cfg.get(str + ".price") != null) {
					_price = cfg.getDouble(str + ".price");
					String notEnoughMoney = ChatColor.translateAlternateColorCodes('&', plugin.msg.WarpNotEnoughMoney).replace("{price}", String.valueOf(_price));
					
					if (plugin.economy.getBalance(player.getName()) < _price) {
						if (_price == 1) {
							player.sendMessage(notEnoughMoney.replace("{currency}", plugin.getConfig().getString("Economy.CurrencySingular")));
						} else {
							player.sendMessage(notEnoughMoney.replace("{currency}", plugin.getConfig().getString("Economy.CurrencyPlural")));
						}
						return;
					} else {
						plugin.economy.withdrawPlayer(player.getName(), _price);
						_payed = true;
					}
				}
			}
			
			final double price = _price;
			final Boolean payed = _payed;
			String name_ = "";
			
			if (cfg.getString(str + ".name") == null) {
				name_ = str;
			} else {
				name_ = ChatColor.translateAlternateColorCodes('&', cfg.getString(str + ".name"));
			}
			
			final String name = name_;
			
			World w = Bukkit.getWorld(cfg.getString(str + ".world"));
			double x = cfg.getDouble(str + ".x");
			double y = cfg.getDouble(str + ".y");
			double z = cfg.getDouble(str + ".z");
			double yaw = cfg.getDouble(str + ".yaw");
			double pitch = cfg.getDouble(str + ".pitch");
			
			final Location loc = new Location(w, x, y, z, (float) yaw, (float) pitch);
			Boolean usedelay = plugin.getConfig().getBoolean("Delay.Warp.EnableDelay");
			
			if (!usedelay.booleanValue()) {
				player.teleport(loc);
				
				if (payed.booleanValue()) {
					String payed_ = ChatColor.translateAlternateColorCodes('&', plugin.msg.WarpSuccessPayed).replace("{warp}", name).replace("{price}", String.valueOf(price));
					
					if (price == 1) {
						player.sendMessage(payed_.replace("{currency}", plugin.getConfig().getString("Economy.CurrencySingular")));									
					} else {
						player.sendMessage(payed_.replace("{currency}", plugin.getConfig().getString("Economy.CurrencyPlural")));
					}
				} else {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.WarpSuccess).replace("{warp}", name));
				}
			} else {
				if (fromsign.booleanValue()) {
					Boolean usesigndelay = plugin.getConfig().getBoolean("Delay.Warp.Sign.Enable");
					
					if (!usesigndelay.booleanValue()) {
						player.teleport(loc);
						
						if (payed.booleanValue()) {
							String payed_ = ChatColor.translateAlternateColorCodes('&', plugin.msg.WarpSuccessPayed).replace("{warp}", name).replace("{price}", String.valueOf(price));

							if (price == 1) {
								player.sendMessage(payed_.replace("{currency}", plugin.getConfig().getString("Economy.CurrencySingular")));									
							} else {
								player.sendMessage(payed_.replace("{currency}", plugin.getConfig().getString("Economy.CurrencyPlural")));
							}
						} else {
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.WarpSuccess).replace("{warp}", name));
						}
						
						return;
					}
				}
				
				if (player.hasPermission("clickwarp.warp.instant")) {
					player.teleport(loc);
					
					if (payed.booleanValue()) {
						String payed_ = ChatColor.translateAlternateColorCodes('&', plugin.msg.WarpSuccessPayed).replace("{warp}", name).replace("{price}", String.valueOf(price));

						if (price == 1) {
							player.sendMessage(payed_.replace("{currency}", plugin.getConfig().getString("Economy.CurrencySingular")));									
						} else {
							player.sendMessage(payed_.replace("{currency}", plugin.getConfig().getString("Economy.CurrencyPlural")));
						}
					} else {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.WarpSuccess).replace("{warp}", name));
					}
				} else {
					Boolean usedontmove = plugin.getConfig().getBoolean("Delay.Warp.EnableDontMove");
					int delay = plugin.getConfig().getInt("Delay.Warp.Delay");
					
					if (usedontmove.booleanValue()) {
						plugin.warp_delay.put(player.getName(), true);
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.DelayDoNotMove).replace("{delay}", String.valueOf(delay)));
					} else {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.Delay).replace("{delay}", String.valueOf(delay)));
					}
					
					plugin.delaytask = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						@Override
						public void run() {									
							player.teleport(loc);
							plugin.warp_delay.remove(player.getName());
							
							if (payed.booleanValue()) {
								String payedstring = ChatColor.translateAlternateColorCodes('&', plugin.msg.WarpSuccessPayed).replace("{warp}", name).replace("{price}", String.valueOf(price));

								if (price == 1) {
									player.sendMessage(payedstring.replace("{currency}", plugin.getConfig().getString("Economy.CurrencySingular")));									
								} else {
									player.sendMessage(payedstring.replace("{currency}", plugin.getConfig().getString("Economy.CurrencyPlural")));
								}
							} else {
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.WarpSuccess).replace("{warp}", name));
							}
						}
					}, delay * 20L);
				}
			}
		} else {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoPermission));
		}
	}
}