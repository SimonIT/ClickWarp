package de.comniemeer.ClickWarp.Messages;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import de.comniemeer.ClickWarp.ClickWarp;

public class LanguageEnglish {

	private ClickWarp plugin;

	public LanguageEnglish(ClickWarp clickwarp) {
		plugin = clickwarp;
	}

	public File file;
	public FileConfiguration cfg;

	private String NoWarps = "&7Currently is no warp defined!";
	private String WarpList = "&7At the moment we have these warps:";
	private String WarpNoExist = "&7There isn\'t a warp named &6{warp}&7.";
	private String WarpNotEnoughMoney = "&7You don't have enough money for this warp! You need &6{price} {currency}&7!";
	private String WarpSuccess = "&7[&6Warp&7] You were teleported to the warp &6{warp}&7.";
	private String WarpSuccessPayed = "&7[&6Warp&7] You were teleported for &6{price} {currency} &7to the warp &6{warp}&7.";
	private String SetwarpInvalidName = "&cInvalid warpname!";
	private String SetwarpSuccess = "&7The warp &6{warp} &7has been successfully created.";
	private String SetwarpNeedNumber = "&7You have to specify a number! Example: \"&6/setwarp warp 1&7\"";
	private String SetwarpInvalidItem = "&cUnknown item!";
	private String SetwarpWrongItemFormat = "&7You can only set items with the format \"&656&7\" or \"&635:4&7\"!";
	private String DelwarpSuccess = "&7The warp &6{warp} &7has been deleted successfully!";
	private String EditwarpItemSuccess = "&7The warp &6{warp} &7will be displayed as the item \"&6{item}&7\" at &6/invwarp&7!";
	private String EditwarpLoreSuccess = "&7The warp &6{warp} &7has now the lore \"&6{lore}&7\" at &6/invwarp&7!";
	private String EditwarpPriceSuccess = "&7The warp &6{warp} &7has been added the price &6{price} {currency}&7.";
	private String SignWarpSpecifyWarp = "&7[&6SignWarp&7] &cPleasy specify a warp in &6line 2&c!";
	private String SignWarpSuccess = "&7[&6SignWarp&7] Sign for the warp &6{warp} &7successfully created!";
	private String InvwarpTooManyWarps = "&7There are too many warps! :O";
	private String InvTPNoPlayers = "&7At the moment, there aren\'t &6any users &7online!";
	private String InvTPTooManyPlayers = "&7There are too many players online! :O";
	private String InvTPSuccess = "&7[&6TP&7] You were teleported to &6{player}&7.";
	private String InvTPNotOnline = "&7[&6TP&7] The player &6{player} &7is not online.";
	private String Delay = "&7You'll be teleported in &6{delay} seconds&7!";
	private String DelayDoNotMove = "&6Do not move! &7You'll be teleported in &6{delay} seconds&7!";
	private String DelayTeleportCanceled = "&cThe teleport has been canceled, because you moved!";
	private String PluginReloaded = "&7[&6ClickWarp&7] Plugin reloaded.";
	private String ErrorFileSaving = "&cError while saving the file! Look in the console for more information!";
	private String NoPermission = "&cNo Permission!";
	private String OnlyPlayers = "&cOnly players are allowed to use that command.";

	private String NoWarpsPath = "Warp.NoWarps";
	private String WarpListPath = "Warp.List";
	private String WarpNoExistPath = "Warp.NoExist";
	private String WarpNotEnoughMoneyPath = "Warp.NotEnoughMoney";
	private String WarpSuccessPath = "Warp.Success";
	private String WarpSuccessPayedPath = "Warp.SuccessPrice";
	private String SetwarpInvalidNamePath = "Setwarp.InvalidName";
	private String SetwarpSuccessPath = "Setwarp.Success";
	private String SetwarpNeedNumberPath = "Setwarp.NeedNumber";
	private String SetwarpInvalidItemPath = "Setwarp.InvalidItem";
	private String SetwarpWrongItemFormatPath = "Setwarp.WrongItemFormat";
	private String DelwarpSuccessPath = "Delwarp.Success";
	private String EditwarpItemSuccessPath = "Editwarp.ItemSuccess";
	private String EditwarpLoreSuccessPath = "Editwarp.LoreSuccess";
	private String EditwarpPriceSuccessPath = "Editwarp.PriceSuccess";
	private String SignWarpSpecifyWarpPath = "SignWarp.SpecifyWarp";
	private String SignWarpSuccessPath = "SignWarp.Success";
	private String InvwarpTooManyWarpsPath = "Invwarp.TooManyWarps";
	private String InvTPNoPlayersPath = "InvTP.NoPlayers";
	private String InvTPTooManyPlayersPath = "InvTP.TooManyPlayers";
	private String InvTPSuccessPath = "InvTP.Success";
	private String InvTPNotOnlinePath = "InvTP.NotOnline";
	private String DelayPath = "Delay.Delay";
	private String DelayDoNotMovePath = "Delay.DoNotMove";
	private String DelayTeleportCanceledPath = "Delay.TeleportCanceled";
	private String PluginReloadedPath = "Various.PluginReloaded";
	private String ErrorFileSavingPath = "Various.ErrorFileSaving";
	private String NoPermissionPath = "Various.NoPermission";
	private String OnlyPlayersPath = "Various.OnlyPlayers";

	public void load() {
		file = new File("plugins/ClickWarp/Languages", "en.yml");
		cfg = YamlConfiguration.loadConfiguration(file);

		if (!(file.exists())) {
			cfg.options().header("[Clickwarp] Plugin by comniemeer | Color codes: http://ess.khhq.net/mc/");
			cfg.set(NoWarpsPath, NoWarps);
			cfg.set(WarpListPath, WarpList);
			cfg.set(WarpNoExistPath, WarpNoExist);
			cfg.set(WarpNotEnoughMoneyPath, WarpNotEnoughMoney);
			cfg.set(WarpSuccessPath, WarpSuccess);
			cfg.set(WarpSuccessPayedPath, WarpSuccessPayed);
			cfg.set(SetwarpInvalidNamePath, SetwarpInvalidName);
			cfg.set(SetwarpSuccessPath, SetwarpSuccess);
			cfg.set(SetwarpNeedNumberPath, SetwarpNeedNumber);
			cfg.set(SetwarpInvalidItemPath, SetwarpInvalidItem);
			cfg.set(SetwarpWrongItemFormatPath, SetwarpWrongItemFormat);
			cfg.set(DelwarpSuccessPath, DelwarpSuccess);
			cfg.set(EditwarpItemSuccessPath, EditwarpItemSuccess);
			cfg.set(EditwarpLoreSuccessPath, EditwarpLoreSuccess);
			cfg.set(EditwarpPriceSuccessPath, EditwarpPriceSuccess);
			cfg.set(SignWarpSpecifyWarpPath, SignWarpSpecifyWarp);
			cfg.set(SignWarpSuccessPath, SignWarpSuccess);
			cfg.set(InvwarpTooManyWarpsPath, InvwarpTooManyWarps);
			cfg.set(InvTPNoPlayersPath, InvTPNoPlayers);
			cfg.set(InvTPTooManyPlayersPath, InvTPTooManyPlayers);
			cfg.set(InvTPSuccessPath, InvTPSuccess);
			cfg.set(InvTPNotOnlinePath, InvTPNotOnline);
			cfg.set(DelayPath, Delay);
			cfg.set(DelayDoNotMovePath, DelayDoNotMove);
			cfg.set(DelayTeleportCanceledPath, DelayTeleportCanceled);
			cfg.set(PluginReloadedPath, PluginReloaded);
			cfg.set(ErrorFileSavingPath, ErrorFileSaving);
			cfg.set(NoPermissionPath, NoPermission);
			cfg.set(OnlyPlayersPath, OnlyPlayers);

			try {
				cfg.save(file);
			} catch (IOException e) {
				plugin.log.info("Error while saving the " + file.getName() + ".");
				e.printStackTrace();
			}
		} else {
			if (cfg.getString(NoWarpsPath) == null) {
				cfg.set(NoWarpsPath, NoWarps);
			}
			if (cfg.getString(WarpListPath) == null) {
				cfg.set(WarpListPath, WarpList);
			}
			if (cfg.getString(WarpNoExistPath) == null) {
				cfg.set(WarpNoExistPath, WarpNoExist);
			}
			if (cfg.getString(WarpSuccessPath) == null) {
				cfg.set(WarpSuccessPath, WarpSuccess);
			}
			if (cfg.getString(WarpSuccessPayedPath) == null) {
				cfg.set(WarpSuccessPayedPath, WarpSuccessPayed);
			}
			if (cfg.getString(SetwarpInvalidNamePath) == null) {
				cfg.set(SetwarpInvalidNamePath, SetwarpInvalidName);
			}
			if (cfg.getString(SetwarpSuccessPath) == null) {
				cfg.set(SetwarpSuccessPath, SetwarpSuccess);
			}
			if (cfg.getString(SetwarpNeedNumberPath) == null) {
				cfg.set(SetwarpNeedNumberPath, SetwarpNeedNumber);
			}
			if (cfg.getString(SetwarpInvalidItemPath) == null) {
				cfg.set(SetwarpInvalidItemPath, SetwarpInvalidItem);
			}
			if (cfg.getString(SetwarpWrongItemFormatPath) == null) {
				cfg.set(SetwarpWrongItemFormatPath, SetwarpWrongItemFormat);
			}
			if (cfg.getString(DelwarpSuccessPath) == null) {
				cfg.set(DelwarpSuccessPath, DelwarpSuccess);
			}
			if (cfg.getString(EditwarpItemSuccessPath) == null) {
				cfg.set(EditwarpItemSuccessPath, EditwarpItemSuccess);
			}
			if (cfg.getString(EditwarpLoreSuccessPath) == null) {
				cfg.set(EditwarpLoreSuccessPath, EditwarpLoreSuccess);
			}
			if (cfg.getString(EditwarpPriceSuccessPath) == null) {
				cfg.set(EditwarpPriceSuccessPath, EditwarpPriceSuccess);
			}
			if (cfg.getString(SignWarpSpecifyWarpPath) == null) {
				cfg.set(SignWarpSpecifyWarpPath, SignWarpSpecifyWarp);
			}
			if (cfg.getString(SignWarpSuccessPath) == null) {
				cfg.set(SignWarpSuccessPath, SignWarpSuccess);
			}
			if (cfg.getString(InvwarpTooManyWarpsPath) == null) {
				cfg.set(InvwarpTooManyWarpsPath, InvwarpTooManyWarps);
			}
			if (cfg.getString(InvTPNoPlayersPath) == null) {
				cfg.set(InvTPNoPlayersPath, InvTPNoPlayers);
			}
			if (cfg.getString(InvTPTooManyPlayersPath) == null) {
				cfg.set(InvTPTooManyPlayersPath, InvTPTooManyPlayers);
			}
			if (cfg.getString(InvTPSuccessPath) == null) {
				cfg.set(InvTPSuccessPath, InvTPSuccess);
			}
			if (cfg.getString(InvTPNotOnlinePath) == null) {
				cfg.set(InvTPNotOnlinePath, InvTPNotOnline);
			}
			if (cfg.getString(WarpNotEnoughMoneyPath) == null) {
				cfg.set(WarpNotEnoughMoneyPath, WarpNotEnoughMoney);
			}
			if (cfg.getString(DelayPath) == null) {
				cfg.set(DelayPath, Delay);
			}
			if (cfg.getString(DelayDoNotMovePath) == null) {
				cfg.set(DelayDoNotMovePath, DelayDoNotMove);
			}
			if (cfg.getString(DelayTeleportCanceledPath) == null) {
				cfg.set(DelayTeleportCanceledPath, DelayTeleportCanceled);
			}
			if (cfg.getString(PluginReloadedPath) == null) {
				cfg.set(PluginReloadedPath, PluginReloaded);
			}
			if (cfg.getString(ErrorFileSavingPath) == null) {
				cfg.set(ErrorFileSavingPath, ErrorFileSaving);
			}
			if (cfg.getString(NoPermissionPath) == null) {
				cfg.set(NoPermissionPath, NoPermission);
			}
			if (cfg.getString(OnlyPlayersPath) == null) {
				cfg.set(OnlyPlayersPath, OnlyPlayers);
			}

			try {
				cfg.save(file);
			} catch (IOException e) {
				plugin.log.info("Error while saving the " + file.getName() + ".");
				e.printStackTrace();
			}
		}
	}
}