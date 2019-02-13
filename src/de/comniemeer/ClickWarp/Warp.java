package de.comniemeer.ClickWarp;

import de.comniemeer.ClickWarp.Exceptions.CommandNoExist;
import de.comniemeer.ClickWarp.Exceptions.InvalidItem;
import de.comniemeer.ClickWarp.Exceptions.InvalidName;
import de.comniemeer.ClickWarp.Exceptions.WarpNoExist;
import de.comniemeer.ClickWarp.Metrics.Graph;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Warp {

	private final String[] notAllowedChars = {
			".yml", "\\", "|", "/", ":"
	};
	private ClickWarp clickWarp;
	private String filename;
	private String name;
	private Location location;
	private OfflinePlayer player;
	private ItemStack item;
	private String lore;
	private Double price = 0d;
	private String message;
	private Sound sound;
	private List<String> executeCommands = new ArrayList<>();
	private List<String> aliasCommands = new ArrayList<>();
	private Double minDistance;
	private Entity vec;

	/**
	 * loads a warp
	 *
	 * @param name the name of the warp to load, case insensitive
	 * @throws WarpNoExist thrown if the warp does not exist
	 */
	public Warp(String name) throws WarpNoExist {
		this.clickWarp = (ClickWarp) Bukkit.getPluginManager().getPlugin("ClickWarp");
		this.filename = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', name.toLowerCase()));
		File file = new File(this.clickWarp.getDataFolder() + "/Warps", this.filename + ".yml");

		if (file.exists()) {
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			if (cfg.getString(this.filename + ".name") == null) {
				this.name = this.filename;
			} else {
				this.name = ChatColor.translateAlternateColorCodes('&', cfg.getString(this.filename + ".name"));
			}

			World w = Bukkit.getWorld(cfg.getString(this.filename + ".world"));
			double x = cfg.getDouble(this.filename + ".x");
			double y = cfg.getDouble(this.filename + ".y");
			double z = cfg.getDouble(this.filename + ".z");
			double yaw = cfg.getDouble(this.filename + ".yaw");
			double pitch = cfg.getDouble(this.filename + ".pitch");
			this.location = new Location(w, x, y, z, (float) yaw, (float) pitch);

			if (cfg.get(this.filename + ".player") != null) {
				UUID uuid = UUID.fromString(cfg.getString(this.filename + ".player"));
				this.player = Bukkit.getOfflinePlayer(uuid);
			}

			if (cfg.getString(this.filename + ".item") != null) {
				String mName = cfg.getString(this.filename + ".item").toUpperCase();
				Material material = Material.getMaterial(mName);
				if (material != null) {
					this.item = new ItemStack(material, 1);
				}
			}


			if (cfg.get(this.filename + ".lore") != null) {
				this.lore = cfg.getString(this.filename + ".lore");
			}

			if (cfg.get(this.filename + ".price") != null) {
				this.price = cfg.getDouble(this.filename + ".price");
			}

			if (cfg.get(this.filename + ".message") != null) {
				this.message = cfg.getString(this.filename + ".message");
			}

			if (cfg.get(this.filename + ".sound") != null) {
				this.sound = Sound.valueOf(cfg.getString(this.filename + ".sound").toUpperCase());
			}

			if (cfg.get(this.filename + ".excmd") != null) {
				this.executeCommands = cfg.getStringList(this.filename + ".excmd");
			}

			if (cfg.get(this.filename + ".cmd") != null) {
				this.aliasCommands = cfg.getStringList(this.filename + ".cmd");
			}

			if (cfg.get(this.filename + ".mindist") != null) {
				this.minDistance = cfg.getDouble(this.filename + ".mindist");
			}
		} else {
			throw new WarpNoExist();
		}
	}

	/**
	 * Creates a new warp
	 *
	 * @param name   the warp name
	 * @param loc    the warp position
	 * @param player the player who creates the warp
	 * @throws InvalidName thrown if the name contains invalid characters
	 */
	public Warp(String name, Location loc, OfflinePlayer player) throws InvalidName {
		this.clickWarp = (ClickWarp) Bukkit.getPluginManager().getPlugin("ClickWarp");
		this.filename = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', name.toLowerCase()));
		File file = new File(this.clickWarp.getDataFolder() + "/Warps", this.filename + ".yml");

		if (file.exists()) {
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			if (cfg.getString(this.filename + ".name") == null) {
				this.name = this.filename;
			} else {
				this.name = ChatColor.translateAlternateColorCodes('&', cfg.getString(this.filename + ".name"));
			}

			World w = Bukkit.getWorld(cfg.getString(this.filename + ".world"));
			double x = cfg.getDouble(this.filename + ".x");
			double y = cfg.getDouble(this.filename + ".y");
			double z = cfg.getDouble(this.filename + ".z");
			double yaw = cfg.getDouble(this.filename + ".yaw");
			double pitch = cfg.getDouble(this.filename + ".pitch");
			this.location = new Location(w, x, y, z, (float) yaw, (float) pitch);

			if (cfg.get(this.filename + ".player") != null) {
				UUID uuid = UUID.fromString(cfg.getString(this.filename + ".player"));
				this.player = Bukkit.getOfflinePlayer(uuid);
			}

			if (cfg.getString(this.filename + ".item") != null) {
				this.item = new ItemStack(Material.matchMaterial(cfg.getString(this.filename + ".item").toUpperCase()), 1);
			}


			if (cfg.get(this.filename + ".lore") != null) {
				this.lore = cfg.getString(this.filename + ".lore");
			}

			if (cfg.get(this.filename + ".price") != null) {
				this.price = cfg.getDouble(this.filename + ".price");
			}

			if (cfg.get(this.filename + ".message") != null) {
				this.message = cfg.getString(this.filename + ".message");
			}

			if (cfg.get(this.filename + ".sound") != null) {
				this.sound = Sound.valueOf(cfg.getString(this.filename + ".sound").toUpperCase());
			}

			if (cfg.get(this.filename + ".excmd") != null) {
				this.executeCommands = cfg.getStringList(this.filename + ".excmd");
			}

			if (cfg.get(this.filename + ".cmd") != null) {
				this.aliasCommands = cfg.getStringList(this.filename + ".cmd");
			}

			if (cfg.get(this.filename + ".mindist") != null) {
				this.minDistance = cfg.getDouble(this.filename + ".mindist");
			}
		} else {
			for (String notAllowedChar : notAllowedChars) {
				if (this.filename.contains(notAllowedChar)) {
					throw new InvalidName();
				}
			}
			this.name = name;
			this.location = loc;
			this.player = player;
		}

	}

	private static void updateMetrics() throws IOException {
		Plugin cw = Bukkit.getPluginManager().getPlugin("ClickWarp");
		File warps_folder = new File(cw.getDataFolder() + "/Warps");
		File[] warps = warps_folder.listFiles();

		final int files;
		if (warps != null) {
			files = warps.length;

			Metrics metrics;
			metrics = new Metrics(cw);
			Graph Warps = metrics.createGraph("Warps");

			Warps.addPlotter(new Metrics.Plotter("Warps") {
				@Override
				public int getValue() {
					return files;
				}
			});

			metrics.start();
		}
	}

	public static List<Warp> getWarps(ClickWarp cw) {
		File warps_folder = new File(cw.getDataFolder() + "/Warps");

		File[] warps = warps_folder.listFiles();

		List<Warp> list = new ArrayList<>();

		if (warps != null && warps.length > 0) {
			for (File warp : warps) {
				String Name = warp.getName().replace(".yml", "");
				try {
					list.add(new Warp(Name));
				} catch (WarpNoExist warpNoExist) {
					warpNoExist.printStackTrace();
				}
			}

		}
		return list;
	}

	public void save() throws IOException {
		File warp = new File(this.clickWarp.getDataFolder() + "/Warps", this.filename + ".yml");

		FileConfiguration cfg = YamlConfiguration.loadConfiguration(warp);

		cfg.set(this.filename + ".name", name);

		cfg.set(this.filename + ".world", this.location.getWorld().getName());
		cfg.set(this.filename + ".x", this.location.getX());
		cfg.set(this.filename + ".y", this.location.getY());
		cfg.set(this.filename + ".z", this.location.getZ());
		cfg.set(this.filename + ".yaw", this.location.getYaw());
		cfg.set(this.filename + ".pitch", this.location.getPitch());

		if (this.player != null) {
			cfg.set(this.filename + ".player", this.player.getUniqueId().toString());
		}

		if (this.item != null) {
			cfg.set(this.filename + ".item", this.item.getType());
		}

		if (this.lore != null) {
			cfg.set(this.filename + ".lore", this.lore);
		}

		if (this.price != null) {
			cfg.set(this.filename + ".price", this.price);
		}

		if (this.message != null) {
			cfg.set(this.filename + ".message", this.message);
		}

		if (this.sound != null) {
			cfg.set(this.filename + ".sound", this.sound.toString());
		}

		if (this.executeCommands != null) {
			cfg.set(this.filename + ".excmd", this.executeCommands);
		}

		if (this.aliasCommands != null) {
			cfg.set(this.filename + ".cmd", this.aliasCommands);
		}

		if (this.minDistance != null) {
			cfg.set(this.filename + ".mindist", this.minDistance);
		}

		cfg.save(warp);
		updateMetrics();
	}

	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public void delWarp() {
		File file = new File(this.clickWarp.getDataFolder() + "/Warps", this.filename + ".yml");
		if (file.exists()) {
			file.delete();
		}
	}

	public boolean existWarp() {
		File file = new File(this.clickWarp.getDataFolder() + "/Warps", this.filename + ".yml");
		return file.exists();
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) throws InvalidName {
		this.delWarp();
		this.name = name;
		this.filename = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', name.toLowerCase()));
		for (String notAllowedChar : notAllowedChars) {
			if (this.filename.contains(notAllowedChar)) {
				throw new InvalidName();
			}
		}
	}

	public OfflinePlayer getPlayer() {
		return this.player;
	}

	public void setPlayer(OfflinePlayer player) {
		this.player = player;
	}

	public ItemStack getItem() {
		if (this.item != null) {
			return this.item;
		} else {
			return new ItemStack(Material.matchMaterial(this.clickWarp.getConfig().getString("DefaultWarpItem").toUpperCase()), 1);
		}
	}

	public void setItem(Material item) {
		this.item = new ItemStack(item, 1);
	}

	public void setItem(String item) throws InvalidItem {
		Material material = Material.matchMaterial(item);
		if (material != null) {
			this.item = new ItemStack(material, 1);
		} else {
			throw new InvalidItem();
		}
	}

	public List<String> getPreparedLore() {
		List<String> lore = new ArrayList<>();
		if (this.lore != null) {
			String[] lore_ = this.lore.split(":");
			for (String aLore_ : lore_) {
				lore.add(ChatColor.translateAlternateColorCodes('&', aLore_.replaceAll("_", " ")));
			}
		}
		return lore;
	}

	public String getLore() {
		if (this.lore != null) {
			return this.lore;
		} else {
			return "";
		}
	}

	public void setLore(String lore) {
		this.lore = lore;
	}

	public Double getPrice() {
		if (this.price != null) {
			return this.price;
		} else {
			return 0d;
		}
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public List<String> getPreparedMessage() {
		String[] message_lines_orig;
		List<String> message_lines = new ArrayList<>();
		message_lines_orig = this.message.split(":");
		for (String aMessage_lines_orig : message_lines_orig) {
			message_lines
					.add(ChatColor.translateAlternateColorCodes('&', aMessage_lines_orig.replaceAll("_", " ")));
		}
		return message_lines;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Sound getSound() {
		if (this.sound != null) {
			return this.sound;
		} else {
			Sound soundConfig = Sound.valueOf(this.clickWarp.getConfig().getString("WarpSound").toUpperCase());
			if (soundConfig != null) {
				return sound;
			} else {
				return Sound.ENTITY_ENDERMAN_TELEPORT;
			}
		}
	}

	public void setSound(Sound sound) {
		this.sound = sound;
	}

	public void addExCmd(String cmd) throws CommandNoExist {
		this.addExCmd(cmd, 0);
	}

	public void addExCmd(String cmd, int priority) throws CommandNoExist {
		//TODO Check if command exist
		if (priority > 0 && priority < this.executeCommands.size()) {
			this.executeCommands.add(priority - 1, cmd);
		} else {
			this.executeCommands.add(cmd);
		}
	}

	public void removeExCmd(String cmd) {
		this.executeCommands.remove(cmd);
	}

	public List<String> getExCmds() {
		if (this.executeCommands != null) {
			return this.executeCommands;
		} else {
			return new ArrayList<>();
		}
	}

	public void setExCmds(List<String> excmds) {
		this.executeCommands = excmds;
	}

	public void addCmd(String cmd) throws CommandNoExist {
		this.addCmd(cmd, 0);
	}

	public void addCmd(String cmd, int priority) throws CommandNoExist {
		//TODO Check if command exist
		if (priority > 0 && priority < this.aliasCommands.size()) {
			this.aliasCommands.add(priority - 1, cmd);
		} else {
			this.aliasCommands.add(cmd);
		}
	}

	public void removeCmd(String cmd) {
		this.aliasCommands.remove(cmd);
	}

	public List<String> getCmds() {
		if (this.aliasCommands != null) {
			return this.aliasCommands;
		} else {
			return new ArrayList<>();
		}
	}

	public void setCmds(List<String> cmds) {
		this.aliasCommands = cmds;
	}

	public void setMinDist(double distance) {
		this.minDistance = distance;
	}

	public Double getMinDistance() {
		if (this.minDistance != null) {
			return this.minDistance;
		} else {
			return this.clickWarp.getConfig().getDouble("minDistance");
		}
	}

	public void handleWarp(final Player player, boolean fromsign) {
		boolean flag = true;
		if (this.clickWarp.getConfig().getBoolean("Flags.Enable")) {
			//flag = Util.getFlagValue(this.clickWarp.wg, player.getLocation(), this.clickWarp.Warp_Flag, player) == StateFlag.State.ALLOW;
		}

		if ((player.hasPermission("clickwarp.warp." + this.filename) || player.hasPermission("clickwarp.warp.*")) && flag) {
			boolean use_vehicle;
			if (player.getVehicle() != null && player.hasPermission("clickwarp.vehiclewarp")
					&& this.clickWarp.getConfig().getBoolean("VehicleWarp")) {
				use_vehicle = true;
				vec = player.getVehicle();
			} else {
				use_vehicle = false;
			}

			boolean enableEconomy = this.clickWarp.getConfig().getBoolean("Economy.Enable");
			boolean _payed = false;

			if (enableEconomy && this.clickWarp.economy != null) {
				if (this.price == 0) {
					String notEnoughMoney = ChatColor.translateAlternateColorCodes('&', this.clickWarp.msg.WarpNotEnoughMoney)
							.replace("{price}", String.valueOf(this.price));

					if (this.clickWarp.economy.getBalance(player) < this.price) {
						if (this.price == 1) {
							player.sendMessage(notEnoughMoney.replace("{currency}",
									this.clickWarp.getConfig().getString("Economy.CurrencySingular")));
						} else {
							player.sendMessage(notEnoughMoney.replace("{currency}",
									this.clickWarp.getConfig().getString("Economy.CurrencyPlural")));
						}
						return;
					} else {
						this.clickWarp.economy.withdrawPlayer(player, this.price);
						_payed = true;
					}
				}
			}

			final boolean payed = _payed;

			if (this.location.getWorld() != player.getWorld()) {
				use_vehicle = false;
			}

			boolean usedelay = this.clickWarp.getConfig().getBoolean("Delay.Warp.EnableDelay");

			if (!usedelay) {
				if (this.location.getWorld() != player.getWorld() || player.getLocation().distance(this.location) >= this.getMinDistance()) {
					player.playEffect(player.getLocation(), Effect.ENDER_SIGNAL, null);
					player.playSound(player.getLocation(), this.getSound(), 1, 0);
					player.teleport(this.location);
					player.playSound(this.location, this.getSound(), 1, 0);
					if (use_vehicle) {
						vec.teleport(this.location);
						vec.addPassenger(player);
					}
					player.playEffect(this.location, Effect.ENDER_SIGNAL, null);
					for (String executeCommand : this.getExCmds()) {
						player.performCommand(executeCommand.replace("_", " "));
					}
					if (payed) {
						String payed_ = ChatColor.translateAlternateColorCodes('&', this.clickWarp.msg.WarpSuccessPayed)
								.replace("{warp}", name).replace("{price}", String.valueOf(price));

						if (this.price == 1) {
							player.sendMessage(payed_.replace("{currency}",
									this.clickWarp.getConfig().getString("Economy.CurrencySingular")));
						} else {
							player.sendMessage(payed_.replace("{currency}",
									this.clickWarp.getConfig().getString("Economy.CurrencyPlural")));
						}
					} else {
						if (this.message != null) {
							for (String message_line : this.getPreparedMessage()) {
								player.sendMessage(message_line);
							}
						} else {
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.clickWarp.msg.WarpSuccess)
									.replace("{warp}", this.name));
						}
					}
					this.clickWarp.log.info(
							"[ClickWarp] [" + player.getName() + ": Warped " + player.getName() + " to " + this.name + "]");
				} else {

				}
			} else {
				if (fromsign) {
					boolean usesigndelay = this.clickWarp.getConfig().getBoolean("Delay.Warp.Sign.Enable");

					if (!usesigndelay) {
						if (this.location.getWorld() != player.getWorld() || player.getLocation().distance(this.location) >= this.getMinDistance()) {
							player.playEffect(player.getLocation(), Effect.ENDER_SIGNAL, null);
							player.playSound(player.getLocation(), this.getSound(), 1, 0);
							player.teleport(this.location);
							player.playSound(this.location, this.getSound(), 1, 0);
							if (use_vehicle) {
								vec.teleport(this.location);
								vec.addPassenger(player);
							}
							player.playEffect(this.location, Effect.ENDER_SIGNAL, null);
							for (String executeCommand : this.getExCmds()) {
								player.performCommand(executeCommand.replace("_", " "));
							}
							if (payed) {
								String payed_ = ChatColor.translateAlternateColorCodes('&', this.clickWarp.msg.WarpSuccessPayed)
										.replace("{warp}", name).replace("{price}", String.valueOf(price));

								if (price == 1) {
									player.sendMessage(payed_.replace("{currency}",
											this.clickWarp.getConfig().getString("Economy.CurrencySingular")));
								} else {
									player.sendMessage(payed_.replace("{currency}",
											this.clickWarp.getConfig().getString("Economy.CurrencyPlural")));
								}
							} else {
								if (this.message != null) {
									for (String message_line : getPreparedMessage()) {
										player.sendMessage(message_line);
									}
								} else {
									player.sendMessage(
											ChatColor.translateAlternateColorCodes('&', this.clickWarp.msg.WarpSuccess)
													.replace("{warp}", name));
								}
							}
							this.clickWarp.log.info("[ClickWarp] [" + player.getName() + ": Warped " + player.getName()
									+ " to " + this.name + "]");
							return;
						} else {

						}
					}
				}

				if (player.hasPermission("clickwarp.warp.instant") && flag) {
					if (this.location.getWorld() != player.getWorld() || player.getLocation().distance(this.location) >= this.getMinDistance()) {
						player.playEffect(player.getLocation(), Effect.ENDER_SIGNAL, null);
						player.playSound(player.getLocation(), this.getSound(), 1, 0);
						player.teleport(this.location);
						player.playSound(this.location, this.getSound(), 1, 0);
						if (use_vehicle) {
							vec.teleport(this.location);
							vec.addPassenger(player);
						}
						player.playEffect(this.location, Effect.ENDER_SIGNAL, null);
						for (String executeCommand : this.getExCmds()) {
							player.performCommand(executeCommand.replace("_", " "));
						}

						if (payed) {
							String payed_ = ChatColor.translateAlternateColorCodes('&', this.clickWarp.msg.WarpSuccessPayed)
									.replace("{warp}", name).replace("{price}", String.valueOf(price));

							if (price == 1) {
								player.sendMessage(payed_.replace("{currency}",
										this.clickWarp.getConfig().getString("Economy.CurrencySingular")));
							} else {
								player.sendMessage(payed_.replace("{currency}",
										this.clickWarp.getConfig().getString("Economy.CurrencyPlural")));
							}
						} else {
							if (this.message != null) {
								for (String message_line : this.getPreparedMessage()) {
									player.sendMessage(message_line);
								}
							} else {
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.clickWarp.msg.WarpSuccess)
										.replace("{warp}", name));
							}
						}
						this.clickWarp.log.info("[ClickWarp] [" + player.getName() + ": Warped " + player.getName()
								+ " to " + this.name + "]");
					} else {

					}
				} else {
					boolean usedontmove = this.clickWarp.getConfig().getBoolean("Delay.Warp.EnableDontMove");
					int delay = this.clickWarp.getConfig().getInt("Delay.Warp.Delay");

					if (usedontmove) {
						this.clickWarp.warp_delay.put(player.getName(), true);
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.clickWarp.msg.DelayDoNotMove)
								.replace("{delay}", String.valueOf(delay)));
					} else {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.clickWarp.msg.Delay)
								.replace("{delay}", String.valueOf(delay)));
					}

					this.clickWarp.delaytask = Bukkit.getScheduler().scheduleSyncDelayedTask(this.clickWarp, () -> {
						if (this.location.getWorld() != player.getWorld()
								|| player.getLocation().distance(this.location) >= this.getMinDistance()) {
							player.playEffect(player.getLocation(), Effect.ENDER_SIGNAL, null);
							player.playSound(player.getLocation(), this.getSound(), 1, 0);
							player.teleport(this.location);
							player.playSound(this.location, this.getSound(), 1, 0);
							this.clickWarp.warp_delay.remove(player.getName());

							if (this.clickWarp.getConfig().getBoolean("VehicleWarp")) {
								vec.teleport(this.location);
								vec.addPassenger(player);
							}
							player.playEffect(this.location, Effect.ENDER_SIGNAL, null);
							for (String executeCommand : this.getExCmds()) {
								player.performCommand(executeCommand.replace("_", " "));
							}

							if (payed) {
								String payedstring = ChatColor
										.translateAlternateColorCodes('&', this.clickWarp.msg.WarpSuccessPayed)
										.replace("{warp}", name).replace("{price}", String.valueOf(price));

								if (price == 1) {
									player.sendMessage(payedstring.replace("{currency}",
											this.clickWarp.getConfig().getString("Economy.CurrencySingular")));
								} else {
									player.sendMessage(payedstring.replace("{currency}",
											this.clickWarp.getConfig().getString("Economy.CurrencyPlural")));
								}
							} else {
								if (this.message != null) {
									for (String _message_line : this.getPreparedMessage()) {
										player.sendMessage(ChatColor.translateAlternateColorCodes('&',
												_message_line));
									}
								} else {
									player.sendMessage(
											ChatColor.translateAlternateColorCodes('&', this.clickWarp.msg.WarpSuccess)
													.replace("{warp}", name));
								}
							}
						} else {

						}
					}, delay * 20L);
				}
			}
		} else {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.clickWarp.msg.NoPermission));
		}
	}
}
