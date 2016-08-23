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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import de.comniemeer.ClickWarp.AutoCommand;
import de.comniemeer.ClickWarp.ClickWarp;

public class CommandWarp extends AutoCommand<ClickWarp> {

	public CommandWarp(ClickWarp plugin, String cmd, String description, String alias) {
		super(plugin, cmd, description, alias);
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (args.length == 0) {
			if (plugin.getConfig().getBoolean("InvwarpInsteadWarp") == false) {
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
			} else {
				CommandInvwarp invwarp = new CommandInvwarp(plugin, "invwarp", "Inventory-Warp command", "invwarps");
				invwarp.execute(sender, label, args);
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
		} else if (args.length == 2) {
			if (args[1].equalsIgnoreCase("getitem")) {
				if (sender instanceof Player) {
					if (this.plugin.getConfig().getBoolean("GetWarpItem")) {
						if (sender.hasPermission("clickwarp.warp.getitem.*")
								|| sender.hasPermission("clickwarp.getwarpitem." + args[0])) {
							String str = args[0].toLowerCase();
							File file = new File("plugins/ClickWarp/Warps", str + ".yml");
							if (!file.exists()) {
								sender.sendMessage(
										ChatColor.translateAlternateColorCodes('&', this.plugin.msg.WarpNoExist)
												.replace("{warp}", args[0]));
							} else {

								FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
								String item__;
								int variant = 0;
								if (cfg.getString(str + ".item") == null) {
									if (this.plugin.getConfig().getString("DefaultWarpItem").contains(":")) {
										String[] item_split = this.plugin.getConfig().getString("DefaultWarpItem")
												.split(":");
										item__ = item_split[0].toUpperCase();
										variant = Integer.parseInt(item_split[1]);
									} else {
										item__ = this.plugin.getConfig().getString("DefaultWarpItem").toUpperCase();
									}
								} else {
									if (cfg.getString(str + ".item").contains(":")) {
										String[] item_split = cfg.getString(str + ".item").split(":");
										item__ = item_split[0].toUpperCase();
										variant = Integer.parseInt(item_split[1]);
									} else {
										item__ = cfg.getString(str + ".item").toUpperCase();
									}
								}

								Material material = Material.getMaterial(item__);
								ItemStack itemstack = new ItemStack(material, 1, (byte) variant);

								List<String> lore = new ArrayList<String>();
								Boolean useeconomy = Boolean
										.valueOf(this.plugin.getConfig().getBoolean("Economy.Enable"));
								if (useeconomy.booleanValue()) {
									Boolean useshowprice = Boolean
											.valueOf(this.plugin.getConfig().getBoolean("Economy.ShowPrice"));
									if ((useshowprice.booleanValue()) && (cfg.getString(str + ".price") != null)) {
										Double price = Double.valueOf(cfg.getDouble(str + ".price"));
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
								if (cfg.get(str + ".lore") != null
										&& !cfg.getString(str + ".lore").equalsIgnoreCase("none")) {
									String[] lore_ = cfg.get(str + ".lore").toString().split(":");
									for (int l = 0; l < lore_.length; l++) {
										lore.add(ChatColor.translateAlternateColorCodes('&',
												lore_[l].replaceAll("_", " ")));
									}
								}
								item_lore.setLore(lore);
								String item_prefix = ChatColor.translateAlternateColorCodes('&',
										this.plugin.getConfig().getString("Sign.FirstLine")) + " ";
								item_lore.setDisplayName(item_prefix + args[0]);
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
						warpList.add(warps[i].getName().replace(".yml", ""));
					}
				}
			}
		}

		if (args.length == 0) {
			return warpList;
		} else if (args.length == 1) {
			List<String> tabList = new ArrayList<>();

			for (String warp : warpList) {
				if (warp.startsWith(args[0].toLowerCase())) {
					if (sender.hasPermission("clickwarp.warp." + warp) || sender.hasPermission("clickwarp.warp.*")) {
						tabList.add(warp);
					}
				}
			}

			return tabList;
		}
		return null;
	}
}