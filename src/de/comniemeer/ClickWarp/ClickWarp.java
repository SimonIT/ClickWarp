package de.comniemeer.ClickWarp;

import de.comniemeer.ClickWarp.Metrics.Graph;
import de.comniemeer.ClickWarp.Commands.CommandClickwarp;
import de.comniemeer.ClickWarp.Commands.CommandDelwarp;
import de.comniemeer.ClickWarp.Commands.CommandEditwarp;
import de.comniemeer.ClickWarp.Commands.CommandGettpskull;
import de.comniemeer.ClickWarp.Commands.CommandInvtp;
import de.comniemeer.ClickWarp.Commands.CommandInvwarp;
import de.comniemeer.ClickWarp.Commands.CommandSetwarp;
import de.comniemeer.ClickWarp.Commands.CommandWarp;
import de.comniemeer.ClickWarp.Commands.CustomCommands;
import de.comniemeer.ClickWarp.Listeners.InventoryListener;
import de.comniemeer.ClickWarp.Listeners.PlayerListener;
import de.comniemeer.ClickWarp.Listeners.SignListener;
import de.comniemeer.ClickWarp.Messages.LanguageCzech;
import de.comniemeer.ClickWarp.Messages.LanguageEnglish;
import de.comniemeer.ClickWarp.Messages.LanguageFrench;
import de.comniemeer.ClickWarp.Messages.LanguageGerman;
import de.comniemeer.ClickWarp.Messages.LanguageKorean;
import de.comniemeer.ClickWarp.Messages.LanguagePortuguese;
import de.comniemeer.ClickWarp.Messages.Messages;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class ClickWarp extends JavaPlugin {

	public final Logger log = Logger.getLogger("Minecraft");
	public String version;

	public Messages msg;
	public LanguageEnglish en;
	public LanguageGerman de;
	public LanguageFrench fr;
	public LanguagePortuguese pt;
	public LanguageCzech cz;
	public LanguageKorean ko;

	public WarpHandler warphandler;

	public HashMap<String, String> InvHM = new HashMap<String, String>();
	public HashMap<String, Boolean> warp_delay = new HashMap<String, Boolean>();
	public HashMap<List<String>, List<String>> cmd = new HashMap<List<String>, List<String>>();
	public int delaytask;

	public Economy economy = null;

	public boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager()
				.getRegistration(net.milkbowl.vault.economy.Economy.class);

		if (economyProvider != null) {
			economy = economyProvider.getProvider();
		}

		return (economy != null);
	}

	public void onDisable() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (this.InvHM.containsKey(p.getName())) {
				p.closeInventory();
				this.InvHM.remove(p.getName());
			}
		}

		this.log.info("[ClickWarp] Plugin v" + this.version + " by comniemeer disabled.");
	}

	public void onEnable() {
		this.saveDefaultConfig();

		this.en = new LanguageEnglish(this);
		this.de = new LanguageGerman(this);
		this.fr = new LanguageFrench(this);
		this.pt = new LanguagePortuguese(this);
		this.cz = new LanguageCzech(this);
		this.ko = new LanguageKorean(this);
		this.msg = new Messages(this);

		this.warphandler = new WarpHandler(this);

		this.en.load();
		this.de.load();
		this.fr.load();
		this.pt.load();
		this.cz.load();
		this.ko.load();
		this.msg.load();

		try {
			Metrics metrics = new Metrics(this);

			metrics.start();
			this.log.info("[ClickWarp] Metrics started!");
		} catch (IOException e) {
			this.log.severe("[ClickWarp] Failed to submit the stats!");
		}

		File warps_folder = new File("plugins/ClickWarp/Warps");

		if (warps_folder.isDirectory()) {
			File[] warps = warps_folder.listFiles();

			final int files = warps.length;

			if (files != 0) {
				try {
					Metrics metrics = new Metrics(this);

					Graph Warps = metrics.createGraph("Warps");

					Warps.addPlotter(new Metrics.Plotter("Warps") {
						public int getValue() {
							return files;
						}
					});

					metrics.start();
				} catch (IOException e) {
					e.printStackTrace();
				}
				for (File warp : warps) {
					FileConfiguration cfg = YamlConfiguration.loadConfiguration(warp);

					String str = warp.getName().replace(".yml", "");
					List<String> infos = new ArrayList<String>();
					infos.add(cfg.getString(str + ".name"));
					if (cfg.get(str + ".price") != null) {
						Double price = cfg.getDouble(str + ".price");
						String priceformat = ChatColor.translateAlternateColorCodes('&',
								getConfig().getString("Economy.PriceFormat").replace("{price}", String.valueOf(price)));

						if (price == 1) {
							infos.add(priceformat.replace("{currency}",
									getConfig().getString("Economy.CurrencySingular")));
						} else {
							infos.add(
									priceformat.replace("{currency}", getConfig().getString("Economy.CurrencyPlural")));
						}
					} else {
						infos.add("free");
					}
					if (cfg.get(str + ".lore") != null) {
						String description = cfg.getString(str + ".lore");
						description = description.replace(":", " ");
						description = description.replace("_", " ");
						infos.add(description);
					} else {
						infos.add("");
					}
					cmd.put(infos, cfg.getStringList(str + ".cmd"));
				}
			}
		}

		Boolean enableEconomy = this.getConfig().getBoolean("Economy.Enable");

		if (enableEconomy.booleanValue()) {
			try {
				this.setupEconomy();
			} catch (NoClassDefFoundError ncdfe) {
				this.log.severe("[ClickWarp] Failed to load Vault!");
				this.log.severe("[ClickWarp] Install Vault or set \"EnableEconomy\" in the config.yml to \"false\"");
				this.getServer().getPluginManager().disablePlugin(this);
				return;
			}
		}

		new CommandClickwarp(this, "clickwarp", "ClickWarp command");
		new CommandWarp(this, "warp", "Warp command", "warps");
		new CommandDelwarp(this, "delwarp", "Deletes a warp");
		new CommandEditwarp(this, "editwarp", "Allows to edit warps");
		new CommandInvtp(this, "invtp", "Inventory-Teleport command", "invteleport");
		new CommandInvwarp(this, "invwarp", "Inventory-Warp command", "invwarps");
		new CommandSetwarp(this, "setwarp", "Sets a warp at the current location");
		new CommandGettpskull(this, "gettpskull", "Get the skull of a player to teleport you at him");
		for (Entry<List<String>, List<String>> entry : cmd.entrySet()) {
			List<String> key = entry.getKey();
			List<String> value = entry.getValue();

			if (value.size() > 0) {
				String mainCommand = value.get(0);
				value.remove(0);
				String[] alias = value.toArray(new String[value.size()]);

				new CustomCommands(this, mainCommand,
						ChatColor.translateAlternateColorCodes('&', getConfig().getString("Sign.FirstLine")) + " "
								+ key.get(0) + " | " + key.get(1) + " | " + key.get(2),
						alias);
			}
		}

		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new InventoryListener(this), this);
		pm.registerEvents(new SignListener(this), this);
		pm.registerEvents(new PlayerListener(this), this);

		this.version = this.getDescription().getVersion();

		this.log.info("[ClickWarp] Plugin v" + this.version + " by comniemeer enabled.");
	}
}