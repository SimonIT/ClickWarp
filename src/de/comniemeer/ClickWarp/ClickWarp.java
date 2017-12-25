package de.comniemeer.ClickWarp;

import com.sk89q.commandbook.CommandBook;
import com.sk89q.commandbook.locations.*;
import de.comniemeer.ClickWarp.Metrics.Graph;
import de.comniemeer.ClickWarp.Updater.ReleaseType;
import de.comniemeer.ClickWarp.Commands.CommandClickwarp;
import de.comniemeer.ClickWarp.Commands.CommandDelwarp;
import de.comniemeer.ClickWarp.Commands.CommandEditwarp;
import de.comniemeer.ClickWarp.Commands.CommandExportwarp;
import de.comniemeer.ClickWarp.Commands.CommandGettpskull;
import de.comniemeer.ClickWarp.Commands.CommandInvtp;
import de.comniemeer.ClickWarp.Commands.CommandInvwarp;
import de.comniemeer.ClickWarp.Commands.CommandSetwarp;
import de.comniemeer.ClickWarp.Commands.CommandWarp;
import de.comniemeer.ClickWarp.Commands.CommandImportwarp;
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
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.Warps;
import com.mccraftaholics.warpportals.bukkit.PortalPlugin;
import com.mccraftaholics.warpportals.manager.PortalDestManager;
import com.mewin.WGCustomFlags.WGCustomFlagsPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class ClickWarp extends JavaPlugin {

    public final Logger log = Logger.getLogger("Minecraft");
    public String version;
    public String authors;

    public Messages msg;
    public LanguageEnglish en;
    public LanguageGerman de;
    public LanguageFrench fr;
    public LanguagePortuguese pt;
    public LanguageCzech cz;
    public LanguageKorean ko;

    public HashMap<String, String> InvHM = new HashMap<>();
    public HashMap<String, Boolean> warp_delay = new HashMap<>();
    public HashMap<List<String>, List<String>> cmd = new HashMap<>();
    public int delaytask;

    public Permission permission;
    public Economy economy;
    public Warps IWarps;
    public WGCustomFlagsPlugin wgcf;
    public WorldGuardPlugin wg;
    public PortalDestManager pdm;
    public FlatFileLocationsManager fflm;

    //public StateFlag Warp_Flag = null;

    public boolean update = false;
    public String name = "";
    public ReleaseType type = null;
    public String version_update = "";
    public String link = "";
    public final int id = 53812;
    public File file = null;
    public Plugin pl = null;

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        permission = rsp.getProvider();
        return permission != null;
    }

    private boolean setupIWarps() {
        Plugin ess = getServer().getPluginManager().getPlugin("Essentials");
        if ((ess == null) || (!(ess instanceof Essentials))) {
            return false;
        }
        this.IWarps = ((Essentials) ess).getWarps();

        return true;
    }

    private boolean setupWarpPortals() {
        Plugin wapo = getServer().getPluginManager().getPlugin("WarpPortals");
        if ((wapo == null) || (!(wapo instanceof PortalPlugin))) {
            return false;
        }
        this.pdm = ((PortalPlugin) wapo).mPortalManager.mPortalDestManager;

        return true;
    }

    private boolean setupCommandBook() {
        Plugin cb = getServer().getPluginManager().getPlugin("CommandBook");
        if ((cb == null) || (!(cb instanceof CommandBook))) {
            return false;
        }

        this.fflm = new FlatFileLocationsManager(new File(cb.getDataFolder(), "warps.csv"), "warps");

        return true;
    }

    private WGCustomFlagsPlugin getWGCustomFlags() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WGCustomFlags");

        if (plugin == null || !(plugin instanceof WGCustomFlagsPlugin)) {
            return null;
        }

        //this.Warp_Flag = new StateFlag("warp", true);
        return (WGCustomFlagsPlugin) plugin;
    }

    private WorldGuardPlugin getWorldGuard() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

        // WorldGuard may not be loaded
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null; // Maybe you want throw an exception instead
        }

        return (WorldGuardPlugin) plugin;
    }

    public void onDisable() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (this.InvHM.containsKey(p.getName())) {
                p.closeInventory();
                this.InvHM.remove(p.getName());
            }
        }

        this.log.info("[ClickWarp] Plugin v" + this.version + " by " + this.authors + " disabled.");
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

        List<Warp> warps = Warp.getWarps();
        try {
            Metrics metrics = new Metrics(this);

            Graph Warps = metrics.createGraph("Warps");

            Warps.addPlotter(new Metrics.Plotter("Warps") {
                public int getValue() {
                    return warps.size();
                }
            });

            metrics.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Warp warp : warps) {
            List<String> infos = new ArrayList<>();
            infos.add(warp.getName());
            if (warp.getPrice() != 0) {
                Double price = warp.getPrice();
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
            String description = warp.getLore();
            description = description.replace(":", " ");
            description = description.replace("_", " ");
            infos.add(description);
            cmd.put(infos, warp.getCmds());
        }


        Boolean enableEconomy = this.getConfig().getBoolean("Economy.Enable");
        Boolean enableFlags = this.getConfig().getBoolean("Flags.Enable");
        Boolean enableIWarps = this.getConfig().getBoolean("Essentials.Enable");
        Boolean enableWarpPortals = this.getConfig().getBoolean("WarpPortals.Enable");
        Boolean enableCommandBook = this.getConfig().getBoolean("CommandBook.Enable");

        if (enableEconomy) {
            try {
                this.setupEconomy();
                this.setupPermissions();
            } catch (NoClassDefFoundError ncdfe) {
                this.log.severe("[ClickWarp] Failed to load Vault!");
                this.log.severe("[ClickWarp] Install Vault or set \"EnableEconomy\" in the config.yml to \"false\"");
                this.getServer().getPluginManager().disablePlugin(this);
                return;
            }
        }

        if (enableIWarps) {
            try {
                this.setupIWarps();
            } catch (NoClassDefFoundError ncdfe) {
                this.log.severe("[ClickWarp] Failed to load Essentials!");
                this.log.severe(
                        "[ClickWarp] Install Essentials or set \"EnableEssentials\" in the config.yml to \"false\"");
                this.getServer().getPluginManager().disablePlugin(this);
                return;
            }
        }

        if (enableFlags) {
            try {
                this.wg = this.getWorldGuard();
                this.wgcf = this.getWGCustomFlags();
                if (this.wgcf != null) {
                    //this.wgcf.addCustomFlag(this.Warp_Flag);
                }
            } catch (NoClassDefFoundError ncdfe) {
                this.log.severe("[ClickWarp] Failed to load WGCustomFlags or WorldGuard!");
                this.log.severe("[ClickWarp] Install Essentials or set \"EnableFlags\" in the config.yml to \"false\"");
                this.getServer().getPluginManager().disablePlugin(this);
                return;
            }
        }

        if (enableWarpPortals) {
            try {
                this.setupWarpPortals();
            } catch (NoClassDefFoundError ncdfe) {
                this.log.severe("[ClickWarp] Failed to load WarpPortals!");
                this.log.severe(
                        "[ClickWarp] Install Essentials or set \"EnableWarpPortals\" in the config.yml to \"false\"");
                this.getServer().getPluginManager().disablePlugin(this);
                return;
            }
        }

        if (enableCommandBook) {
            try {
                this.setupCommandBook();
            } catch (NoClassDefFoundError ncdfe) {
                this.log.severe("[ClickWarp] Failed to load CommandBook!");
                this.log.severe(
                        "[ClickWarp] Install CommandBook or set \"EnableCommandBook\" in the config.yml to \"false\"");
                this.getServer().getPluginManager().disablePlugin(this);
                return;
            }
        }


        new CommandClickwarp(this, "clickwarp", "ClickWarp command");
        new CommandWarp(this, "warp", "Warp command", "warps", "cwarp");
        new CommandDelwarp(this, "delwarp", "Deletes a warp", "delcwarp");
        new CommandEditwarp(this, "editwarp", "Allows to edit warps");
        new CommandInvtp(this, "invtp", "Inventory-Teleport command", "invteleport");
        new CommandInvwarp(this, "invwarp", "Inventory-Warp command", "invwarps");
        new CommandSetwarp(this, "setwarp", "Sets a warp at the current location", "setcwarp");
        new CommandGettpskull(this, "gettpskull", "Get the skull of a player to teleport you at him");
        new CommandImportwarp(this, "importwarp", "Import the Warps from other Plugins.");
        new CommandExportwarp(this, "exportwarp", "Export the Warps to other Plugins.");
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
        this.authors = String.join(" and ", this.getDescription().getAuthors());

        this.log.info("[ClickWarp] Plugin v" + this.version + " by " + this.authors + " enabled.");

        if (getConfig().getBoolean("auto-update")) {
            Updater updater = new Updater(this, this.id, this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false); // Start
            this.update = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE; // Determine
            this.name = updater.getLatestName(); // Get the latest name
            this.version_update = updater.getLatestGameVersion(); // Get the latest
            this.type = updater.getLatestType(); // Get the latest file's type
            this.link = updater.getLatestFileLink(); // Get the latest link
        }
        this.file = this.getFile();
        this.pl = this;
    }
}