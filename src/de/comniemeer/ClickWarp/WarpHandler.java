package de.comniemeer.ClickWarp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class WarpHandler {

	private ClickWarp plugin;

	public WarpHandler(ClickWarp clickwarp) {
		plugin = clickwarp;
	}

	public void handleWarp(final Player player, String str, String arg, Boolean fromsign) {
		File warp = new File("plugins/ClickWarp/Warps", str + ".yml");

		if (!(warp.exists())) {
			player.sendMessage(
					ChatColor.translateAlternateColorCodes('&', plugin.msg.WarpNoExist).replace("{warp}", arg));
			return;
		}

		if (player.hasPermission("clickwarp.warp." + str) || player.hasPermission("clickwarp.warp.*")) {
			Boolean use_vehicle = false;
			Entity vec = null;
			if (player.getVehicle() != null && player.hasPermission("clickwarp.vehiclewarp")
					&& plugin.getConfig().getBoolean("VehicleWarp")) {
				use_vehicle = true;
				vec = player.getVehicle();
			}

			FileConfiguration cfg = YamlConfiguration.loadConfiguration(warp);
			Boolean enableEconomy = plugin.getConfig().getBoolean("Economy.Enable");
			double _price = 0;
			Boolean _payed = false;

			if (enableEconomy.booleanValue()) {
				if (cfg.get(str + ".price") != null) {
					_price = cfg.getDouble(str + ".price");
					String notEnoughMoney = ChatColor.translateAlternateColorCodes('&', plugin.msg.WarpNotEnoughMoney)
							.replace("{price}", String.valueOf(_price));

					if (plugin.economy.getBalance(player) < _price) {
						if (_price == 1) {
							player.sendMessage(notEnoughMoney.replace("{currency}",
									plugin.getConfig().getString("Economy.CurrencySingular")));
						} else {
							player.sendMessage(notEnoughMoney.replace("{currency}",
									plugin.getConfig().getString("Economy.CurrencyPlural")));
						}
						return;
					} else {
						plugin.economy.withdrawPlayer(player, _price);
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

			String[] message_lines = null;
			List<String> excmd = new ArrayList<>();
			final String name = name_;
			Sound warp_sound = null;
			double minDistance = (double) 1;

			World w = Bukkit.getWorld(cfg.getString(str + ".world"));
			double x = cfg.getDouble(str + ".x");
			double y = cfg.getDouble(str + ".y");
			double z = cfg.getDouble(str + ".z");
			double yaw = cfg.getDouble(str + ".yaw");
			double pitch = cfg.getDouble(str + ".pitch");
			if (cfg.get(str + ".message") != null) {
				message_lines = cfg.getString(str + ".message").split(":");
			}
			if (cfg.get(str + ".excmd") != null) {
				excmd = cfg.getStringList(str + ".excmd");
			}

			if (cfg.get(str + ".sound") != null) {
				warp_sound = Sound.valueOf(cfg.getString(str + ".sound").toUpperCase());
			} else {
				warp_sound = Sound.valueOf(this.plugin.getConfig().getString("WarpSound").toUpperCase());
			}

			if (cfg.get(str + ".mindist") != null) {
				minDistance = cfg.getDouble(str + ".mindist");
			} else {
				minDistance = this.plugin.getConfig().getDouble("minDistance");
			}

			final Location loc = new Location(w, x, y, z, (float) yaw, (float) pitch);
			Boolean usedelay = plugin.getConfig().getBoolean("Delay.Warp.EnableDelay");

			if (!usedelay.booleanValue()) {
				if (loc.getWorld() != player.getWorld() || player.getLocation().distance(loc) >= minDistance) {
					player.playEffect(player.getLocation(), Effect.ENDER_SIGNAL, null);
					player.playSound(player.getLocation(), warp_sound, 1, 0);
					player.teleport(loc);
					player.playSound(loc, warp_sound, 1, 0);
					if (use_vehicle) {
						vec.teleport(loc);
						vec.setPassenger(player);
					}
					player.playEffect(loc, Effect.ENDER_SIGNAL, null);
					if (excmd != null) {
						for (String executeCommand : excmd) {
							player.performCommand(executeCommand.replace("_", " "));
						}
					}
					if (payed.booleanValue()) {
						String payed_ = ChatColor.translateAlternateColorCodes('&', plugin.msg.WarpSuccessPayed)
								.replace("{warp}", name).replace("{price}", String.valueOf(price));

						if (price == 1) {
							player.sendMessage(payed_.replace("{currency}",
									plugin.getConfig().getString("Economy.CurrencySingular")));
						} else {
							player.sendMessage(payed_.replace("{currency}",
									plugin.getConfig().getString("Economy.CurrencyPlural")));
						}
					} else {
						if (message_lines != null) {
							for (int l = 0; l < message_lines.length; l++) {
								player.sendMessage(ChatColor.translateAlternateColorCodes('&',
										message_lines[l].replaceAll("_", " ")));
							}
						} else {
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.WarpSuccess)
									.replace("{warp}", name));
						}
					}
				} else {

				}
			} else {
				if (fromsign.booleanValue()) {
					Boolean usesigndelay = plugin.getConfig().getBoolean("Delay.Warp.Sign.Enable");

					if (!usesigndelay.booleanValue()) {
						if (loc.getWorld() != player.getWorld() || player.getLocation().distance(loc) >= minDistance) {
							player.playEffect(player.getLocation(), Effect.ENDER_SIGNAL, null);
							player.playSound(player.getLocation(), warp_sound, 1, 0);
							player.teleport(loc);
							player.playSound(loc, warp_sound, 1, 0);
							if (use_vehicle) {
								vec.teleport(loc);
								vec.setPassenger(player);
							}
							player.playEffect(loc, Effect.ENDER_SIGNAL, null);
							if (excmd != null) {
								for (String executeCommand : excmd) {
									player.performCommand(executeCommand.replace("_", " "));
								}
							}
							if (payed.booleanValue()) {
								String payed_ = ChatColor.translateAlternateColorCodes('&', plugin.msg.WarpSuccessPayed)
										.replace("{warp}", name).replace("{price}", String.valueOf(price));

								if (price == 1) {
									player.sendMessage(payed_.replace("{currency}",
											plugin.getConfig().getString("Economy.CurrencySingular")));
								} else {
									player.sendMessage(payed_.replace("{currency}",
											plugin.getConfig().getString("Economy.CurrencyPlural")));
								}
							} else {
								if (message_lines != null) {
									for (int l = 0; l < message_lines.length; l++) {
										player.sendMessage(ChatColor.translateAlternateColorCodes('&',
												message_lines[l].replaceAll("_", " ")));
									}
								} else {
									player.sendMessage(
											ChatColor.translateAlternateColorCodes('&', plugin.msg.WarpSuccess)
													.replace("{warp}", name));
								}
							}

							return;
						} else {

						}
					}
				}

				if (player.hasPermission("clickwarp.warp.instant")) {
					if (loc.getWorld() != player.getWorld() || player.getLocation().distance(loc) >= minDistance) {
						player.playEffect(player.getLocation(), Effect.ENDER_SIGNAL, null);
						player.playSound(player.getLocation(), warp_sound, 1, 0);
						player.teleport(loc);
						player.playSound(loc, warp_sound, 1, 0);
						if (use_vehicle) {
							vec.teleport(loc);
							vec.setPassenger(player);
						}
						player.playEffect(loc, Effect.ENDER_SIGNAL, null);
						if (excmd != null) {
							for (String executeCommand : excmd) {
								player.performCommand(executeCommand.replace("_", " "));
							}
						}

						if (payed.booleanValue()) {
							String payed_ = ChatColor.translateAlternateColorCodes('&', plugin.msg.WarpSuccessPayed)
									.replace("{warp}", name).replace("{price}", String.valueOf(price));

							if (price == 1) {
								player.sendMessage(payed_.replace("{currency}",
										plugin.getConfig().getString("Economy.CurrencySingular")));
							} else {
								player.sendMessage(payed_.replace("{currency}",
										plugin.getConfig().getString("Economy.CurrencyPlural")));
							}
						} else {
							if (message_lines != null) {
								for (int l = 0; l < message_lines.length; l++) {
									player.sendMessage(ChatColor.translateAlternateColorCodes('&',
											message_lines[l].replaceAll("_", " ")));
								}
							} else {
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.WarpSuccess)
										.replace("{warp}", name));
							}
						}
					} else {

					}
				} else {
					Boolean usedontmove = plugin.getConfig().getBoolean("Delay.Warp.EnableDontMove");
					int delay = plugin.getConfig().getInt("Delay.Warp.Delay");

					if (usedontmove.booleanValue()) {
						plugin.warp_delay.put(player.getName(), true);
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.DelayDoNotMove)
								.replace("{delay}", String.valueOf(delay)));
					} else {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.Delay)
								.replace("{delay}", String.valueOf(delay)));
					}

					final Boolean _use_vehicle = use_vehicle;
					final Entity _vec = vec;
					final String[] _message_lines = message_lines;
					final Sound _warp_sound = warp_sound;
					final List<String> _excmd = excmd;
					final Double _minDistance = minDistance;
					plugin.delaytask = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						@Override
						public void run() {
							if (loc.getWorld() != player.getWorld() || player.getLocation().distance(loc) >= _minDistance) {
								player.playEffect(player.getLocation(), Effect.ENDER_SIGNAL, null);
								player.playSound(player.getLocation(), _warp_sound, 1, 0);
								player.teleport(loc);
								player.playSound(loc, _warp_sound, 1, 0);
								plugin.warp_delay.remove(player.getName());

								if (_use_vehicle) {
									_vec.teleport(loc);
									_vec.setPassenger(player);
								}
								player.playEffect(loc, Effect.ENDER_SIGNAL, null);
								if (_excmd != null) {
									for (String executeCommand : _excmd) {
										player.performCommand(executeCommand.replace("_", " "));
									}
								}

								if (payed.booleanValue()) {
									String payedstring = ChatColor
											.translateAlternateColorCodes('&', plugin.msg.WarpSuccessPayed)
											.replace("{warp}", name).replace("{price}", String.valueOf(price));

									if (price == 1) {
										player.sendMessage(payedstring.replace("{currency}",
												plugin.getConfig().getString("Economy.CurrencySingular")));
									} else {
										player.sendMessage(payedstring.replace("{currency}",
												plugin.getConfig().getString("Economy.CurrencyPlural")));
									}
								} else {
									if (_message_lines != null) {
										for (int l = 0; l < _message_lines.length; l++) {
											player.sendMessage(ChatColor.translateAlternateColorCodes('&',
													_message_lines[l].replaceAll("_", " ")));
										}
									} else {
										player.sendMessage(
												ChatColor.translateAlternateColorCodes('&', plugin.msg.WarpSuccess)
														.replace("{warp}", name));
									}
								}
							} else {

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