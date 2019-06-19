package de.comniemeer.ClickWarp.Commands;

import de.comniemeer.ClickWarp.AutoCommand;
import de.comniemeer.ClickWarp.ClickWarp;
import de.comniemeer.ClickWarp.Exceptions.CommandNoExist;
import de.comniemeer.ClickWarp.Exceptions.InvalidItem;
import de.comniemeer.ClickWarp.Exceptions.InvalidName;
import de.comniemeer.ClickWarp.Exceptions.WarpNoExist;
import de.comniemeer.ClickWarp.Warp;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommandEditwarp extends AutoCommand<ClickWarp> {

	public CommandEditwarp(ClickWarp plugin, String cmd, String description) {
		super(plugin, cmd, description);
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender.hasPermission("clickwarp.editwarp")) {
			if (args.length >= 3) {
				try {
					Warp warp = new Warp(args[0]);
					if (args[1].equalsIgnoreCase("item")) {
						warp.setItem(args[2]);
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.EditwarpItemSuccess)
								.replace("{warp}", warp.getName()).replace("{item}", args[2]));
					} else if (args[1].equalsIgnoreCase("lore")) {
						warp.setLore(args[2]);
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.EditwarpLoreSuccess)
								.replace("{warp}", warp.getName()).replace("{lore}", ChatColor.translateAlternateColorCodes('&',
										warp.getLore())));
					} else if (args[1].equalsIgnoreCase("price")) {
						double price;

						try {
							price = Double.parseDouble(args[2]);
						} catch (NumberFormatException e) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpNeedNumber));
							return true;
						}

						warp.setPrice(price);

						String pricesuccess = ChatColor
								.translateAlternateColorCodes('&', plugin.msg.EditwarpPriceSuccess)
								.replace("{warp}", warp.getName()).replace("{price}", String.valueOf(price));

						if (price == 1) {
							sender.sendMessage(pricesuccess.replace("{currency}",
									plugin.getConfig().getString("Economy.CurrencySingular")));
						} else {
							sender.sendMessage(pricesuccess.replace("{currency}",
									plugin.getConfig().getString("Economy.CurrencyPlural")));
						}
					} else if (args[1].equalsIgnoreCase("name")) {
						try {
							warp.setName(args[2]);
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.msg.SetwarpSuccess)
									.replace("{warp}", ChatColor.translateAlternateColorCodes('&', args[2])));
						} catch (InvalidName invalidName) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpInvalidName));
						}
					} else if (args[1].equalsIgnoreCase("message")) {
						warp.setMessage(args[2]);
					} else if (args[1].equalsIgnoreCase("sound")) {
						Sound sound;
						try {
							sound = Sound.valueOf(args[2]);
						} catch (NullPointerException e) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpInvalidItem));
							return true;
						}
						warp.setSound(sound);
					} else if (args[1].equalsIgnoreCase("excmd")) {
						if (args[2].equalsIgnoreCase("add")) {
							String cmd = args[3].replace("_", " ");
							if (args.length > 4) {
								warp.addExCmd(cmd, Integer.valueOf(args[4]));
							} else {
								warp.addExCmd(cmd);
							}
						} else if (args[2].equalsIgnoreCase("remove")) {
							String cmd = args[3].replace("_", " ");
							warp.removeExCmd(cmd);
						} else if (args[2].equalsIgnoreCase("list")) {
							List<String> excmds = warp.getExCmds();
							StringBuilder excmd_names = new StringBuilder("§6");
							for (String excmd : excmds) {
								excmd_names.append(excmd).append("§7, §6");
							}
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.WarpList));
							sender.sendMessage(excmd_names.toString());
						}
					} else if (args[1].equalsIgnoreCase("cmd")) {
						if (args[2].equalsIgnoreCase("add")) {
							if (args.length > 4) {
								warp.addCmd(args[3], Integer.valueOf(args[4]));
							} else {
								warp.addCmd(args[3]);
							}
						} else if (args[2].equalsIgnoreCase("remove")) {
							warp.removeCmd(args[3]);
						} else if (args[2].equalsIgnoreCase("list")) {
							List<String> cmds = warp.getCmds();
							StringBuilder cmd_names = new StringBuilder("§6");
							for (String cmd : cmds) {
								cmd_names.append(cmd).append("§7, §6");
							}
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.WarpList));
							sender.sendMessage(cmd_names.toString());
						}
					} else if (args[1].equalsIgnoreCase("mindist")) {
						double dist;

						try {
							dist = Double.parseDouble(args[2]);
						} catch (NumberFormatException e) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpNeedNumber));
							return true;
						}
						warp.setMinDist(dist);
					} else {
						sender.sendMessage(ChatColor.YELLOW + "/editwarp <warp> name <name>");
						sender.sendMessage(ChatColor.YELLOW + "/editwarp <warp> item <item-ID>");
						sender.sendMessage(ChatColor.YELLOW + "/editwarp <warp> lore <Line_1:Line_2:...>");
						sender.sendMessage(ChatColor.YELLOW + "/editwarp <warp> price <Price>");
						sender.sendMessage(ChatColor.YELLOW + "/editwarp <warp> message <Message>");
						sender.sendMessage(ChatColor.YELLOW + "/editwarp <warp> sound <Sound>");
						sender.sendMessage(ChatColor.YELLOW + "/editwarp <warp> mindist <Distance>");
						sender.sendMessage(ChatColor.YELLOW + "/editwarp <warp> cmd add <cmd>");
						sender.sendMessage(ChatColor.YELLOW + "/editwarp <warp> cmd remove <cmd>");
						sender.sendMessage(ChatColor.YELLOW + "/editwarp <warp> cmd list");
						sender.sendMessage(ChatColor.YELLOW + "/editwarp <warp> excmd add <cmd>");
						sender.sendMessage(ChatColor.YELLOW + "/editwarp <warp> excmd remove <cmd>");
						sender.sendMessage(ChatColor.YELLOW + "/editwarp <warp> excmd list");
					}
					warp.save();
				} catch (WarpNoExist warpNoExist) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.WarpNoExist)
							.replace("{warp}", args[0]));
				} catch (InvalidItem invalidItem) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpInvalidItem));
				} catch (IOException e) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.ErrorFileSaving));
				} catch (CommandNoExist commandNoExist) {
				}
			} else {
				sender.sendMessage(ChatColor.YELLOW + "/editwarp <warp> name <name>");
				sender.sendMessage(ChatColor.YELLOW + "/editwarp <warp> item <item-ID>");
				sender.sendMessage(ChatColor.YELLOW + "/editwarp <warp> lore <Line_1:Line_2:...>");
				sender.sendMessage(ChatColor.YELLOW + "/editwarp <warp> price <Price>");
				sender.sendMessage(ChatColor.YELLOW + "/editwarp <warp> message <Message>");
				sender.sendMessage(ChatColor.YELLOW + "/editwarp <warp> sound <Sound>");
				sender.sendMessage(ChatColor.YELLOW + "/editwarp <warp> mindist <Distance>");
				sender.sendMessage(ChatColor.YELLOW + "/editwarp <warp> cmd add <cmd>");
				sender.sendMessage(ChatColor.YELLOW + "/editwarp <warp> cmd remove <cmd>");
				sender.sendMessage(ChatColor.YELLOW + "/editwarp <warp> cmd list");
				sender.sendMessage(ChatColor.YELLOW + "/editwarp <warp> excmd add <cmd>");
				sender.sendMessage(ChatColor.YELLOW + "/editwarp <warp> excmd remove <cmd>");
				sender.sendMessage(ChatColor.YELLOW + "/editwarp <warp> excmd list");
			}
		} else {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoPermission));
		}
		return true;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String label, String[] args) {
		if (sender.hasPermission("clickwarp.editwarp")) {
			List<String> parameters = new ArrayList<>();
			parameters.add("name");
			parameters.add("item");
			parameters.add("lore");
			parameters.add("price");
			parameters.add("message");
			parameters.add("sound");
			parameters.add("mindist");
			parameters.add("excmd");
			parameters.add("cmd");

			List<String> cmd_parameters = new ArrayList<>();
			cmd_parameters.add("add");
			cmd_parameters.add("remove");
			cmd_parameters.add("list");

			List<String> warpList = new ArrayList<>();
			File warps_folder = new File("plugins/ClickWarp/Warps");

			if (warps_folder.isDirectory()) {
				File[] warps = warps_folder.listFiles();

				if (warps != null && warps.length != 0) {
					for (File warp : warps) {
						warpList.add(warp.getName().replace(".yml", ""));
					}
				}
			}

			List<String> soundList = new ArrayList<>();
			for (int i = 0; i < Sound.values().length; i++) {
				soundList.add(Sound.values()[i].toString());
			}

			List<String> materialList = new ArrayList<>();
			for (int i = 0; i < Material.values().length; i++) {
				materialList.add(Material.values()[i].toString());
			}

			if (args.length == 0) {
				return warpList;
			} else if (args.length == 1) {
				List<String> tabList = new ArrayList<>();

				for (String warp : warpList) {
					if (warp.startsWith(args[0].toLowerCase())) {
						tabList.add(warp);
					}
				}

				return tabList;
			} else if (args.length == 2) {
				List<String> tabList = new ArrayList<>();

				for (String parameter : parameters) {
					if (parameter.startsWith(args[1].toLowerCase())) {
						tabList.add(parameter);
					}
				}

				return tabList;

			} else if (args.length == 3) {
				if (args[1].equalsIgnoreCase(parameters.get(1))) {
					List<String> tabList = new ArrayList<>();

					for (String material : materialList) {
						if (material.startsWith(args[2].toUpperCase())) {
							tabList.add(material);
						}
					}
					return tabList;
				} else if (args[1].equalsIgnoreCase(parameters.get(5))) {
					List<String> tabList = new ArrayList<>();

					for (String sound : soundList) {
						if (sound.startsWith(args[2].toUpperCase())) {
							tabList.add(sound);
						}
					}

					return tabList;
				} else if (args[1].equalsIgnoreCase(parameters.get(7)) || args[1].equalsIgnoreCase(parameters.get(8))) {
					List<String> tabList = new ArrayList<>();

					for (String cmd_parameter : cmd_parameters) {
						if (cmd_parameter.toLowerCase().startsWith(args[2].toLowerCase())) {
							tabList.add(cmd_parameter);
						}
					}

					return tabList;
				}
			}
		}
		return null;
	}
}
