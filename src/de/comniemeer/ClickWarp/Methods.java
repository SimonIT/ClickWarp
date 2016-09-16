package de.comniemeer.ClickWarp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import de.comniemeer.ClickWarp.Metrics.Graph;

public class Methods {

	private ClickWarp plugin;

	public Methods(ClickWarp clickwarp) {
		plugin = clickwarp;
	}

	public void updateMetrics() {

		File warps_folder = new File("plugins/ClickWarp/Warps");
		File[] warps = warps_folder.listFiles();

		final int files = warps.length;

		Metrics metrics;
		try {
			metrics = new Metrics(plugin);
			Graph Warps = metrics.createGraph("Warps");

			Warps.addPlotter(new Metrics.Plotter("Warps") {
				@Override
				public int getValue() {
					return files;
				}
			});

			metrics.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public FileConfiguration getWarpConfiguartion(String warp) {
		String str = warp.toLowerCase();
		File file = new File("plugins/ClickWarp/Warps", str + ".yml");

		if (!(file.exists())) {
			return null;
		}
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		return cfg;
	}

	public boolean setWarp(String name, Location loc) {
		if (name.contains(".yml") || name.contains("\\") || name.contains("|") || name.contains("/")
				|| name.contains(":")) {
			return false;
		} else {
			String str = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', name.toLowerCase()));
			File warp = new File("plugins/ClickWarp/Warps", str + ".yml");
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(warp);

			cfg.set(str + ".name", name);
			cfg.set(str + ".world", loc.getWorld().getName());
			cfg.set(str + ".x", loc.getX());
			cfg.set(str + ".y", loc.getY());
			cfg.set(str + ".z", loc.getZ());
			cfg.set(str + ".yaw", loc.getYaw());
			cfg.set(str + ".pitch", loc.getPitch());

			try {
				cfg.save(warp);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}

			updateMetrics();

			return true;
		}
	}

	public Location getWarp(String name) {
		String str = name.toLowerCase();
		FileConfiguration cfg = getWarpConfiguartion(name);

		World w = Bukkit.getWorld(cfg.getString(str + ".world"));
		double x = cfg.getDouble(str + ".x");
		double y = cfg.getDouble(str + ".y");
		double z = cfg.getDouble(str + ".z");
		double yaw = cfg.getDouble(str + ".yaw");
		double pitch = cfg.getDouble(str + ".pitch");

		final Location loc = new Location(w, x, y, z, (float) yaw, (float) pitch);
		return loc;
	}

	public boolean delWarp(String name) {
		File file = new File(plugin.getDataFolder() + "/Warps", name.toLowerCase() + ".yml");

		if (file.exists()) {
			file.delete();
			return true;
		} else {
			return false;
		}
	}

	public List<String> getWarps() {
		File warps_folder = new File(plugin.getDataFolder() + "/Warps");

		File[] warps = warps_folder.listFiles();

		List<String> list = new ArrayList<String>();

		if (warps.length > 0) {
			for (int i = 0; i < warps.length; i++) {
				FileConfiguration cfg = YamlConfiguration.loadConfiguration(warps[i]);
				String Name = cfg.getString(warps[i].getName().replace(".yml", "") + ".name");
				list.add(Name);
			}

		}
		return list;
	}

	public boolean existWarp(String warp) {
		String str = warp.toLowerCase();
		File file = new File("plugins/ClickWarp/Warps", str + ".yml");

		if (!(file.exists())) {
			return false;
		}
		return true;
	}

	public boolean setName(String warp, String name) {
		return true;
	}

	public String getName(String warp) {
		String str = warp.toLowerCase();
		FileConfiguration cfg = getWarpConfiguartion(warp);
		String name = "";
		if (cfg.getString(str + ".name") == null) {
			name = str;
		} else {
			name = ChatColor.translateAlternateColorCodes('&', cfg.getString(str + ".name"));
		}
		return name;
	}

	public boolean setItem(String warp, String item) {
		String str = warp.toLowerCase();
		File file = new File("plugins/ClickWarp/Warps", str + ".yml");

		if (!(file.exists())) {
			return false;
		}

		FileConfiguration cfg = getWarpConfiguartion(warp);
		String item_;
		int variant = 0;
		if (item.contains(":")) {
			String[] item_split = item.split(":");
			item_ = item_split[0];
			variant = Integer.parseInt(item_split[1]);
		} else {
			item_ = item;
		}
		Material item_name;

		try {
			item_name = Material.getMaterial(item_.toUpperCase());
			cfg.set(str + ".item", item_name.toString() + ":" + variant);
		} catch (NullPointerException e) {
			return false;
		}

		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean setItem(String warp, String material, byte variant) {
		String str = warp.toLowerCase();
		File file = new File("plugins/ClickWarp/Warps", str + ".yml");

		if (!(file.exists())) {
			return false;
		}

		FileConfiguration cfg = getWarpConfiguartion(warp);

		variant = 0;

		Material item_name;

		try {
			item_name = Material.getMaterial(material.toUpperCase());
			cfg.set(str + ".item", item_name.toString() + ":" + variant);
		} catch (NullPointerException e) {
			return false;
		}

		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public ItemStack getItemStack(String warp) {
		ItemStack itemstack = new ItemStack(getItemMaterial(warp), 1, getItemVariant(warp));
		return itemstack;
	}

	public Material getItemMaterial(String warp) {
		String str = warp.toLowerCase();

		FileConfiguration cfg = getWarpConfiguartion(warp);
		String item__;
		if (cfg.getString(str + ".item") == null) {
			if (this.plugin.getConfig().getString("DefaultWarpItem").contains(":")) {
				String[] item_split = this.plugin.getConfig().getString("DefaultWarpItem").split(":");
				item__ = item_split[0].toUpperCase();
			} else {
				item__ = this.plugin.getConfig().getString("DefaultWarpItem").toUpperCase();
			}
		} else {
			if (cfg.getString(str + ".item").contains(":")) {
				String[] item_split = cfg.getString(str + ".item").split(":");
				item__ = item_split[0].toUpperCase();
			} else {
				item__ = cfg.getString(str + ".item").toUpperCase();
			}
		}
		Material material = Material.getMaterial(item__);
		return material;

	}

	public byte getItemVariant(String warp) {
		String str = warp.toLowerCase();
		FileConfiguration cfg = getWarpConfiguartion(warp);
		byte variant = 0;
		if (cfg.getString(str + ".item") == null) {
			if (this.plugin.getConfig().getString("DefaultWarpItem").contains(":")) {
				String[] item_split = this.plugin.getConfig().getString("DefaultWarpItem").split(":");
				variant = Byte.parseByte(item_split[1]);
			}
		} else {
			if (cfg.getString(str + ".item").contains(":")) {
				String[] item_split = cfg.getString(str + ".item").split(":");
				variant = Byte.parseByte(item_split[1]);
			}
		}
		return variant;

	}

	public boolean setLore(String warp, String lore) {
		String str = warp.toLowerCase();
		File file = new File("plugins/ClickWarp/Warps", str + ".yml");

		if (!(file.exists())) {
			return false;
		}
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

		cfg.set(str + ".lore", lore);

		try {
			cfg.save(file);
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public List<String> getPreparedLore(String warp) {
		String str = warp.toLowerCase();
		FileConfiguration cfg = getWarpConfiguartion(warp);
		List<String> lore = new ArrayList<String>();
		if (cfg.get(str + ".lore") != null) {
			String[] lore_ = cfg.get(str + ".lore").toString().split(":");
			for (int l = 0; l < lore_.length; l++) {
				lore.add(ChatColor.translateAlternateColorCodes('&', lore_[l].replaceAll("_", " ")));
			}
		}
		return lore;
	}

	public String getLore(String warp) {
		String str = warp.toLowerCase();
		FileConfiguration cfg = getWarpConfiguartion(warp);
		String lore_ = null;
		if (cfg.get(str + ".lore") != null) {
			lore_ = cfg.getString(str + ".lore");

		}
		return lore_;
	}

	public boolean setPrice(String warp, double price) {
		String str = warp.toLowerCase();
		File file = new File("plugins/ClickWarp/Warps", str + ".yml");

		if (!(file.exists())) {
			return false;
		}
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

		cfg.set(str + ".price", price);

		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public Double getPrice(String warp) {
		String str = warp.toLowerCase();
		FileConfiguration cfg = getWarpConfiguartion(warp);
		Double price = (double) 0;
		if (cfg.get(str + ".lore") != null) {
			price = Double.valueOf(cfg.getDouble(str + ".price"));
		}
		return price;
	}

	public boolean setMessage(String warp, String message) {
		String str = warp.toLowerCase();
		File file = new File("plugins/ClickWarp/Warps", str + ".yml");

		if (!(file.exists())) {
			return false;
		}
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

		cfg.set(str + ".message", message);

		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public List<String> getPreparedMessage(String warp) {
		String str = warp.toLowerCase();
		FileConfiguration cfg = getWarpConfiguartion(warp);
		String[] message_lines_orig = null;
		List<String> message_lines = new ArrayList<String>();
		if (cfg.get(str + ".message") != null) {
			message_lines_orig = cfg.getString(str + ".message").split(":");
			for (int l = 0; l < message_lines_orig.length; l++) {
				message_lines
						.add(ChatColor.translateAlternateColorCodes('&', message_lines_orig[l].replaceAll("_", " ")));
			}

		}
		return message_lines;
	}

	public String getMessage(String warp) {
		String str = warp.toLowerCase();
		FileConfiguration cfg = getWarpConfiguartion(warp);
		String message = null;
		if (cfg.get(str + ".message") != null) {
			message = cfg.getString(str + ".message");

		}
		return message;
	}

	public boolean setSound(String warp, Sound sound) {
		String str = warp.toLowerCase();
		File file = new File("plugins/ClickWarp/Warps", str + ".yml");

		if (!(file.exists())) {
			return false;
		}
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

		cfg.set(str + ".sound", sound.toString());

		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public Sound getSound(String warp) {
		String str = warp.toLowerCase();
		FileConfiguration cfg = getWarpConfiguartion(warp);
		Sound warp_sound;
		if (cfg.get(str + ".sound") != null) {
			warp_sound = Sound.valueOf(cfg.getString(str + ".sound").toUpperCase());
		} else {
			warp_sound = Sound.valueOf(this.plugin.getConfig().getString("WarpSound").toUpperCase());
		}
		return warp_sound;
	}

	public boolean setExCmds(String warp, List<String> excmds) {
		String str = warp.toLowerCase();
		File file = new File("plugins/ClickWarp/Warps", str + ".yml");

		if (!(file.exists())) {
			return false;
		}
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

		cfg.set(str + ".excmd", excmds);

		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean addExCmd(String warp, String cmd) {
		String str = warp.toLowerCase();
		File file = new File("plugins/ClickWarp/Warps", str + ".yml");

		if (!(file.exists())) {
			return false;
		}
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

		List<String> excmds = new ArrayList<String>();
		excmds = cfg.getStringList(str + ".excmd");

		excmds.add(cmd);
		cfg.set(str + ".excmd", excmds);

		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean addExCmd(String warp, String cmd, int priority) {
		String str = warp.toLowerCase();
		File file = new File("plugins/ClickWarp/Warps", str + ".yml");

		if (!(file.exists())) {
			return false;
		}
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

		List<String> excmds = new ArrayList<String>();
		excmds = cfg.getStringList(str + ".excmd");

		excmds.add(priority, cmd);
		cfg.set(str + ".excmd", excmds);

		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean removeExCmd(String warp, String cmd) {
		String str = warp.toLowerCase();
		File file = new File("plugins/ClickWarp/Warps", str + ".yml");

		if (!(file.exists())) {
			return false;
		}
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

		List<String> excmds = new ArrayList<String>();
		excmds = cfg.getStringList(str + ".excmd");

		if (excmds.contains(cmd)) {
			excmds.remove(cmd);
		}
		cfg.set(str + ".excmd", excmds);

		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public List<String> getExCmds(String warp) {
		String str = warp.toLowerCase();
		File file = new File("plugins/ClickWarp/Warps", str + ".yml");

		if (!(file.exists())) {
			return null;
		}
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

		List<String> excmds = new ArrayList<String>();
		excmds = cfg.getStringList(str + ".excmd");

		return excmds;
	}

	public boolean setCmds(String warp, List<String> cmds) {
		String str = warp.toLowerCase();
		File file = new File("plugins/ClickWarp/Warps", str + ".yml");

		if (!(file.exists())) {
			return false;
		}
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

		cfg.set(str + ".cmd", cmds);

		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean addCmd(String warp, String cmd) {
		String str = warp.toLowerCase();
		File file = new File("plugins/ClickWarp/Warps", str + ".yml");

		if (!(file.exists())) {
			return false;
		}
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

		List<String> cmds = new ArrayList<String>();
		cmds = cfg.getStringList(str + ".cmd");

		cmds.add(cmd);
		cfg.set(str + ".cmd", cmds);

		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean addCmd(String warp, String cmd, int priority) {
		String str = warp.toLowerCase();
		File file = new File("plugins/ClickWarp/Warps", str + ".yml");

		if (!(file.exists())) {
			return false;
		}
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

		List<String> cmds = new ArrayList<String>();
		cmds = cfg.getStringList(str + ".cmd");

		cmds.add(priority, cmd);
		cfg.set(str + ".cmd", cmds);

		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean removeCmd(String warp, String cmd) {
		String str = warp.toLowerCase();
		File file = new File("plugins/ClickWarp/Warps", str + ".yml");

		if (!(file.exists())) {
			return false;
		}
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

		List<String> cmds = new ArrayList<String>();
		cmds = cfg.getStringList(str + ".cmd");

		if (cmds.contains(cmd)) {
			cmds.remove(cmd);
		}
		cfg.set(str + ".cmd", cmds);

		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public List<String> getCmds(String warp) {
		String str = warp.toLowerCase();
		FileConfiguration cfg = getWarpConfiguartion(warp);

		List<String> cmds = new ArrayList<String>();
		cmds = cfg.getStringList(str + ".cmd");

		return cmds;
	}

	public boolean setMinDist(String warp, double distance) {
		String str = warp.toLowerCase();
		File file = new File("plugins/ClickWarp/Warps", str + ".yml");

		if (!(file.exists())) {
			return false;
		}
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

		cfg.set(str + ".mindist", distance);

		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public Double getMinDistance(String warp) {
		String str = warp.toLowerCase();
		FileConfiguration cfg = getWarpConfiguartion(warp);
		double minDistance = (double) 1;
		if (cfg.get(str + ".mindist") != null) {
			minDistance = cfg.getDouble(str + ".mindist");
		} else {
			minDistance = this.plugin.getConfig().getDouble("minDistance");
		}
		return minDistance;
	}
}
