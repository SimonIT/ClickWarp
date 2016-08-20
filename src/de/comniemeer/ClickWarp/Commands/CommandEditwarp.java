package de.comniemeer.ClickWarp.Commands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

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
				File file = new File("plugins/ClickWarp/Warps", str + ".yml");

				if (!(file.exists())) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.WarpNoExist)
							.replace("{warp}", args[0]));
					return true;
				}

				FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
				String name = "";

				if (cfg.getString(str + ".name") == null) {
					name = str;
				} else {
					name = ChatColor.translateAlternateColorCodes('&', cfg.getString(str + ".name"));
				}

				if (args[1].equalsIgnoreCase("item")) {
					String item_;
					int variant = 0;
					if (args[2].contains(":")) {
						String[] item_split = args[2].split(":");
						item_ = item_split[0];
						variant = Integer.parseInt(item_split[1]);
					} else {
						item_ = args[2];
					}
					Material item_name;

					try {
						item_name = Material.getMaterial(item_.toUpperCase());
						cfg.set(str + ".item", item_name.toString() + ":" + variant);
					} catch (NullPointerException e) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpInvalidItem));
						return true;
					}

					try {
						cfg.save(file);
					} catch (IOException e) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.ErrorFileSaving));
						e.printStackTrace();
						return true;
					}

					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.EditwarpItemSuccess)
							.replace("{warp}", name).replace("{item}", item_name.toString()));
					return true;

				} else if (args[1].equalsIgnoreCase("lore")) {
					String lore = args[2];

					cfg.set(str + ".lore", lore);

					try {
						cfg.save(file);
					} catch (IOException e) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.ErrorFileSaving));
						e.printStackTrace();
						return true;
					}

					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.EditwarpLoreSuccess)
							.replace("{warp}", name).replace("{lore}", ChatColor.translateAlternateColorCodes('&',
									lore.replace("_", " ").replace(":", "§r:"))));
					return true;
				} else if (args[1].equalsIgnoreCase("price")) {
					double price;

					try {
						price = Double.parseDouble(args[2]);
					} catch (NumberFormatException e) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpNeedNumber));
						return true;
					}

					cfg.set(str + ".price", price);

					try {
						cfg.save(file);
					} catch (IOException e) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.ErrorFileSaving));
						e.printStackTrace();
						return true;
					}

					String pricesuccess = ChatColor.translateAlternateColorCodes('&', plugin.msg.EditwarpPriceSuccess)
							.replace("{warp}", name).replace("{price}", String.valueOf(price));

					if (price == 1) {
						sender.sendMessage(pricesuccess.replace("{currency}",
								plugin.getConfig().getString("Economy.CurrencySingular")));
					} else {
						sender.sendMessage(pricesuccess.replace("{currency}",
								plugin.getConfig().getString("Economy.CurrencyPlural")));
					}
				} else if (args[1].equalsIgnoreCase("name")) {
					String newname = args[2].toLowerCase();
					File newfile = new File("plugins/ClickWarp/Warps", newname + ".yml");
					FileConfiguration newcfg = YamlConfiguration.loadConfiguration(newfile);
					if ((newname.contains(".yml")) || (newname.contains("\\")) || (newname.contains("|"))
							|| (newname.contains("/")) || (newname.contains(":"))) {
						sender.sendMessage(
								ChatColor.translateAlternateColorCodes('&', this.plugin.msg.SetwarpInvalidName));
						return true;
					}
					newcfg.set(newname + ".name", args[2]);
					newcfg.set(newname + ".world", cfg.getString(str + ".world"));
					newcfg.set(newname + ".x", cfg.getDouble(str + ".x"));
					newcfg.set(newname + ".y", cfg.getDouble(str + ".y"));
					newcfg.set(newname + ".z", cfg.getDouble(str + ".z"));
					newcfg.set(newname + ".yaw", cfg.getDouble(str + ".yaw"));
					newcfg.set(newname + ".pitch", cfg.getDouble(str + ".pitch"));
					newcfg.set(newname + ".item", cfg.getInt(str + ".item"));
					newcfg.set(newname + ".message", cfg.getString(str + ".message"));
					newcfg.set(newname + ".sound", cfg.getString(str + ".sound"));
					try {
						newcfg.save(newfile);
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.msg.SetwarpSuccess)
								.replace("{warp}", ChatColor.translateAlternateColorCodes('&', args[2])));
					} catch (IOException e) {
						sender.sendMessage(
								ChatColor.translateAlternateColorCodes('&', this.plugin.msg.ErrorFileSaving));
						e.printStackTrace();
					}
					file.delete();
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.msg.DelwarpSuccess)
							.replace("{warp}", name));
					return true;
				} else if (args[1].equalsIgnoreCase("message")) {
					String message = args[2];

					cfg.set(str + ".message", message);

					try {
						cfg.save(file);
					} catch (IOException e) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.ErrorFileSaving));
						e.printStackTrace();
						return true;
					}

					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.EditwarpLoreSuccess)
							.replace("{warp}", name).replace("{lore}", ChatColor.translateAlternateColorCodes('&',
									message.replace("_", " ").replace(":", "§r:"))));
					return true;
				} else if (args[1].equalsIgnoreCase("sound")) {
					String sound_name = args[2];
					Sound sound;
					try {
						sound = Sound.valueOf(sound_name);
						cfg.set(str + ".sound", sound.toString());
					} catch (NullPointerException e) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SetwarpInvalidItem));
						return true;
					}

					try {
						cfg.save(file);
					} catch (IOException e) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.ErrorFileSaving));
						e.printStackTrace();
						return true;
					}

					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.EditwarpItemSuccess)
							.replace("{warp}", name).replace("{item}", sound.toString()));
				} else if (args[1].equalsIgnoreCase("excmd")) {
					List<String> excmds = new ArrayList<String>();
					excmds = cfg.getStringList(str + ".excmd");
					if (args[2].equalsIgnoreCase("add")) {
						if (args[4] != null) {
							excmds.add(Integer.valueOf(args[4]), args[3]);
						} else {
							excmds.add(args[2]);
						}
						cfg.set(str + ".excmd", excmds);
						try {
							cfg.save(file);
						} catch (IOException e) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.ErrorFileSaving));
							e.printStackTrace();
							return true;
						}
					} else if (args[2].equalsIgnoreCase("remove")) {
						if(excmds.contains(args[3])){
							excmds.remove(args[3]);
						}
						cfg.set(str + ".excmd", excmds);
						try {
							cfg.save(file);
						} catch (IOException e) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.ErrorFileSaving));
							e.printStackTrace();
							return true;
						}
					} else if (args[2].equalsIgnoreCase("list")) {
						String excmd_names = "§6";
						for (String excmd : excmds) {
							excmd_names += excmd + "§7, §6";
						}
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.WarpList));
						sender.sendMessage(excmd_names);
					}
				} else if (args[1].equalsIgnoreCase("cmd")) {
					List<String> cmds = new ArrayList<String>();
					cmds = cfg.getStringList(str + ".cmd");
					if (args[2].equalsIgnoreCase("add")) {
						if (args[4] != null) {
							cmds.add(Integer.valueOf(args[4]), args[3]);
						} else {
							cmds.add(args[2]);
						}
						cfg.set(str + ".cmd", cmds);
						try {
							cfg.save(file);
						} catch (IOException e) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.ErrorFileSaving));
							e.printStackTrace();
							return true;
						}
					} else if (args[2].equalsIgnoreCase("remove")) {
						if(cmds.contains(args[3])){
							cmds.remove(args[3]);
						}
						cfg.set(str + ".cmd", cmds);
						try {
							cfg.save(file);
						} catch (IOException e) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.ErrorFileSaving));
							e.printStackTrace();
							return true;
						}
					} else if (args[2].equalsIgnoreCase("list")) {
						String cmd_names = "§6";
						for (String cmd : cmds) {
							cmd_names += cmd + "§7, §6";
						}
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.WarpList));
						sender.sendMessage(cmd_names);
					}
				} else {
					sender.sendMessage("§e/editwarp <warp> name <newname>");
					sender.sendMessage("§e/editwarp <warp> item <item-ID>");
					sender.sendMessage("§e/editwarp <warp> lore <Line_1:Line_2:...>");
					sender.sendMessage("§e/editwarp <warp> price <Price>");
					sender.sendMessage("§e/editwarp <warp> message <Message>");
					sender.sendMessage("§e/editwarp <warp> sound <Sound>");
				}
			} else {
				sender.sendMessage("§e/editwarp <warp> name <newname>");
				sender.sendMessage("§e/editwarp <warp> item <item-ID>");
				sender.sendMessage("§e/editwarp <warp> lore <Line_1:Line_2:...>");
				sender.sendMessage("§e/editwarp <warp> price <Price>");
				sender.sendMessage("§e/editwarp <warp> message <Message>");
				sender.sendMessage("§e/editwarp <warp> sound <Sound>");
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