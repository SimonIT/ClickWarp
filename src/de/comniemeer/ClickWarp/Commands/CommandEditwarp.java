package de.comniemeer.ClickWarp.Commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import de.comniemeer.ClickWarp.AutoCommand;
import de.comniemeer.ClickWarp.ClickWarp;

public class CommandEditwarp extends AutoCommand<ClickWarp> {

	public CommandEditwarp(ClickWarp plugin, String cmd, String description) {
		super(plugin, cmd, description);
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender.hasPermission("clickwarp.editwarp")) {
			if (args.length >= 3) {
				String str = args[0].toLowerCase();
				if (!this.plugin.methods.existWarp(str)) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.WarpNoExist)
							.replace("{warp}", args[0]));
					return true;
				}

				String name = this.plugin.methods.getName(str);

				if (args[1].equalsIgnoreCase("item")) {
					boolean result = this.plugin.methods.setItem(str, args[2]);
					if (result) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.EditwarpItemSuccess)
								.replace("{warp}", name).replace("{item}", args[2]));
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpInvalidItem));
					}
					return true;
				} else if (args[1].equalsIgnoreCase("lore")) {
					boolean result = this.plugin.methods.setLore(str, args[2]);
					if (result) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.EditwarpLoreSuccess)
								.replace("{warp}", name).replace("{lore}", ChatColor.translateAlternateColorCodes('&',
										args[2].replace("_", " ").replace(":", "§r:"))));
					}
					return true;
				} else if (args[1].equalsIgnoreCase("price")) {
					double price;

					try {
						price = Double.parseDouble(args[2]);
					} catch (NumberFormatException e) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpNeedNumber));
						return true;
					}

					boolean result = this.plugin.methods.setPrice(str, price);

					if (result) {
						String pricesuccess = ChatColor
								.translateAlternateColorCodes('&', plugin.msg.EditwarpPriceSuccess)
								.replace("{warp}", name).replace("{price}", String.valueOf(price));

						if (price == 1) {
							sender.sendMessage(pricesuccess.replace("{currency}",
									plugin.getConfig().getString("Economy.CurrencySingular")));
						} else {
							sender.sendMessage(pricesuccess.replace("{currency}",
									plugin.getConfig().getString("Economy.CurrencyPlural")));
						}
					}
				} else if (args[1].equalsIgnoreCase("name")) {
					String newname = args[2].toLowerCase();
					if ((newname.contains(".yml")) || (newname.contains("\\")) || (newname.contains("|"))
							|| (newname.contains("/")) || (newname.contains(":"))) {
						sender.sendMessage(
								ChatColor.translateAlternateColorCodes('&', this.plugin.msg.SetwarpInvalidName));
						return true;
					}
					this.plugin.methods.setWarp(args[2], this.plugin.methods.getWarp(str));
					this.plugin.methods.setItem(args[2], this.plugin.methods.getItemMaterial(str).toString(), this.plugin.methods.getItemVariant(str));
					this.plugin.methods.setLore(args[2], this.plugin.methods.getLore(str));
					this.plugin.methods.setPrice(args[2], this.plugin.methods.getPrice(str));
					this.plugin.methods.setMessage(args[2], this.plugin.methods.getMessage(str));
					this.plugin.methods.setSound(args[2], this.plugin.methods.getSound(str));

						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.msg.SetwarpSuccess)
								.replace("{warp}", ChatColor.translateAlternateColorCodes('&', args[2])));

						sender.sendMessage(
								ChatColor.translateAlternateColorCodes('&', this.plugin.msg.ErrorFileSaving));
					this.plugin.methods.delWarp(str);
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.msg.DelwarpSuccess)
							.replace("{warp}", name));
					return true;
				} else if (args[1].equalsIgnoreCase("message")) {
					boolean result = this.plugin.methods.setMessage(str, args[2]);

					if (result) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.EditwarpLoreSuccess)
								.replace("{warp}", name).replace("{lore}", ChatColor.translateAlternateColorCodes('&',
										args[2].replace("_", " ").replace(":", "§r:"))));
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.ErrorFileSaving));
					}

					return true;
				} else if (args[1].equalsIgnoreCase("sound")) {
					Sound sound;
					try {
						sound = Sound.valueOf(args[2]);
					} catch (NullPointerException e) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpInvalidItem));
						return true;
					}

					boolean result = this.plugin.methods.setSound(str, sound);

					if (result) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.EditwarpItemSuccess)
								.replace("{warp}", name).replace("{item}", sound.toString()));
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.ErrorFileSaving));
					}
				} else if (args[1].equalsIgnoreCase("excmd")) {
					if (args[2].equalsIgnoreCase("add")) {
						boolean result;
						if (args[4] != null) {
							result = this.plugin.methods.addExCmd(str, args[3], Integer.valueOf(args[4]));
						} else {
							result = this.plugin.methods.addExCmd(str, args[3]);
						}
						if (result) {
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.ErrorFileSaving));
							return true;
						}
					} else if (args[2].equalsIgnoreCase("remove")) {
						boolean result = this.plugin.methods.removeExCmd(str, args[3]);

						if (result) {
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.ErrorFileSaving));
							return true;
						}
					} else if (args[2].equalsIgnoreCase("list")) {
						List<String> excmds = this.plugin.methods.getExCmds(str);
						String excmd_names = "§6";
						for (String excmd : excmds) {
							excmd_names += excmd + "§7, §6";
						}
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.WarpList));
						sender.sendMessage(excmd_names);
					}
				} else if (args[1].equalsIgnoreCase("cmd")) {
					if (args[2].equalsIgnoreCase("add")) {
						boolean result;
						if (args[4] != null) {
							result = this.plugin.methods.addCmd(str, args[3], Integer.valueOf(args[4]));
						} else {
							result = this.plugin.methods.addCmd(str, args[3]);
						}
						if (result) {
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.ErrorFileSaving));
							return true;
						}
					} else if (args[2].equalsIgnoreCase("remove")) {
						boolean result = this.plugin.methods.removeCmd(str, args[3]);

						if (result) {
						} else {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.ErrorFileSaving));
							return true;
						}
					} else if (args[2].equalsIgnoreCase("list")) {
						List<String> cmds = this.plugin.methods.getCmds(str);
						String cmd_names = "§6";
						for (String cmd : cmds) {
							cmd_names += cmd + "§7, §6";
						}
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.WarpList));
						sender.sendMessage(cmd_names);
					}
				} else if (args[1].equalsIgnoreCase("mindist")) {
					double dist;

					try {
						dist = Double.parseDouble(args[2]);
					} catch (NumberFormatException e) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpNeedNumber));
						return true;
					}

					boolean result = this.plugin.methods.setMinDist(str, dist);

					if (result) {

					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.ErrorFileSaving));
					}
				} else {
					sender.sendMessage("§e/editwarp <warp> name <newname>");
					sender.sendMessage("§e/editwarp <warp> item <item-ID>");
					sender.sendMessage("§e/editwarp <warp> lore <Line_1:Line_2:...>");
					sender.sendMessage("§e/editwarp <warp> price <Price>");
					sender.sendMessage("§e/editwarp <warp> message <Message>");
					sender.sendMessage("§e/editwarp <warp> sound <Sound>");
					sender.sendMessage("§e/editwarp <warp> mindist <Distance>");
					sender.sendMessage("§e/editwarp <warp> cmd add <cmd>");
					sender.sendMessage("§e/editwarp <warp> cmd remove <cmd>");
					sender.sendMessage("§e/editwarp <warp> excmd add <cmd>");
					sender.sendMessage("§e/editwarp <warp> excmd remove <cmd>");
				}
			} else {
				sender.sendMessage("§e/editwarp <warp> name <newname>");
				sender.sendMessage("§e/editwarp <warp> item <item-ID>");
				sender.sendMessage("§e/editwarp <warp> lore <Line_1:Line_2:...>");
				sender.sendMessage("§e/editwarp <warp> price <Price>");
				sender.sendMessage("§e/editwarp <warp> message <Message>");
				sender.sendMessage("§e/editwarp <warp> sound <Sound>");
				sender.sendMessage("§e/editwarp <warp> mindist <Distance>");
				sender.sendMessage("§e/editwarp <warp> cmd add <cmd>");
				sender.sendMessage("§e/editwarp <warp> cmd remove <cmd>");
				sender.sendMessage("§e/editwarp <warp> excmd add <cmd>");
				sender.sendMessage("§e/editwarp <warp> excmd remove <cmd>");
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

			List<String> warpList = new ArrayList<>();
			File warps_folder = new File("plugins/ClickWarp/Warps");

			if (warps_folder.isDirectory()) {
				File[] warps = warps_folder.listFiles();

				if (warps.length != 0) {
					for (int i = 0; i < warps.length; i++) {
						warpList.add(warps[i].getName().replace(".yml", ""));
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
				if (!warpList.contains(args[0])) {
					List<String> tabList = new ArrayList<>();

					for (String warp : warpList) {
						if (warp.startsWith(args[0].toLowerCase())) {
							tabList.add(warp);
						}
					}

					return tabList;
				} else {
					List<String> tabList = new ArrayList<>();

					for (String parameter : parameters) {
						tabList.add(parameter);
					}
					return tabList;
				}
			} else if (args.length == 2) {
				if (!parameters.contains(args[1])) {
					List<String> tabList = new ArrayList<>();

					for (String parameter : parameters) {
						if (parameter.startsWith(args[1].toLowerCase())) {
							tabList.add(parameter);
						}
					}

					return tabList;
				} else {
					List<String> tabList = new ArrayList<>();

					if (args[1].equalsIgnoreCase(parameters.get(1))) {
						for (String material : materialList) {
							tabList.add(material);
						}
					} else if (args[1].equalsIgnoreCase(parameters.get(5))) {
						for (String sound : soundList) {
							tabList.add(sound);
						}
					}
					return tabList;
				}
			} else if (args.length == 3) {
				if (args[1].equalsIgnoreCase(parameters.get(1))) {
					if (!materialList.contains(args[2])) {
						List<String> tabList = new ArrayList<>();

						for (String material : materialList) {
							if (material.startsWith(args[2].toUpperCase())) {
								tabList.add(material);
							}
						}
					}
				} else if (args[1].equalsIgnoreCase(parameters.get(5))) {
					if (!soundList.contains(args[2])) {
						List<String> tabList = new ArrayList<>();

						for (String sound : soundList) {
							if (sound.startsWith(args[2].toUpperCase())) {
								tabList.add(sound);
							}
						}

						return tabList;
					}
				}
			}
		}
		return null;
	}
}