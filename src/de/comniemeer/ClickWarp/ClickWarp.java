package de.comniemeer.ClickWarp;

import de.comniemeer.ClickWarp.Commands.CommandClickwarp;
import de.comniemeer.ClickWarp.Commands.CommandDelwarp;
import de.comniemeer.ClickWarp.Commands.CommandEditwarp;
import de.comniemeer.ClickWarp.Commands.CommandInvtp;
import de.comniemeer.ClickWarp.Commands.CommandInvwarp;
import de.comniemeer.ClickWarp.Commands.CommandSetwarp;
import de.comniemeer.ClickWarp.Commands.CommandWarp;
import de.comniemeer.ClickWarp.Listeners.InventoryListener;
import de.comniemeer.ClickWarp.Listeners.PlayerListener;
import de.comniemeer.ClickWarp.Listeners.SignListener;
import de.comniemeer.ClickWarp.Messages.LanguageCzech;
import de.comniemeer.ClickWarp.Messages.LanguageEnglish;
import de.comniemeer.ClickWarp.Messages.LanguageFrench;
import de.comniemeer.ClickWarp.Messages.LanguageGerman;
import de.comniemeer.ClickWarp.Messages.LanguagePortuguese;
import de.comniemeer.ClickWarp.Messages.Messages;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
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
	
	public WarpHandler warphandler;
	
	public HashMap<String, String> InvHM = new HashMap<String, String>();
	public HashMap<String, Boolean> warp_delay = new HashMap<String, Boolean>();
	
	public int delaytask;
	
	public Economy economy = null;
	public boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		
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
		this.msg = new Messages(this);
		
		this.warphandler = new WarpHandler(this);
		
		this.en.load();
		this.de.load();
		this.fr.load();
		this.pt.load();
		this.cz.load();
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
					
					metrics.addCustomData(new Metrics.Plotter("Warps") {
						public int getValue() {
							return files;
						}
					});
					
					metrics.start();
				} catch (IOException e) {
					e.printStackTrace();
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
		
		new CommandClickwarp(this);
		new CommandDelwarp(this);
		new CommandEditwarp(this);
		new CommandInvtp(this);
		new CommandInvwarp(this);
		new CommandSetwarp(this);
		new CommandWarp(this);
		
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new InventoryListener(this), this);
		pm.registerEvents(new SignListener(this), this);
		pm.registerEvents(new PlayerListener(this), this);
		
		this.version = this.getDescription().getVersion();
		
		this.log.info("[ClickWarp] Plugin v" + this.version + " by comniemeer enabled.");
	}
}