package de.comniemeer.ClickWarp.Messages;

import de.comniemeer.ClickWarp.ClickWarp;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class LanguageFrench {

	public File file;
	public FileConfiguration cfg;
	private ClickWarp plugin;
	private String NoWarps = "&7Actuellement, aucun warp est définie!";
	private String WarpList = "&7Il suivons actuellement ces warps:";
	private String WarpNoExist = "&7Le warp \"&6test&7\" n'existe pas.";
	private String WarpNotEnoughMoney = "&7Tu n'as pas assez d'argent! Tu as besoin de &6{price} {currency}&7!";
	private String WarpSuccess = "&7[&6Warp&7] Tu as été téléporté au warp &6{warp}&7.";
	private String WarpSuccessPayed = "&7Tu as téléporté au warp &6{warp} &7 pour &6{price} {currency}&7.";
	private String SetwarpInvalidName = "&cNom invalide!";
	private String SetwarpSuccess = "&7Le warp &6{warp} &7a été créé avec succès.";
	private String SetwarpNeedNumber = "&7Tu dois préciser un nombre! Exemple: \"&6/setwarp warp 1&7\"";
	private String SetwarpInvalidItem = "&cItem inconnu!";
	private String SetwarpWrongItemFormat = "Tu peux préciser uniquement les articles en format \"&656&7\" ou \"&635:4&7\"!";
	private String DelwarpSuccess = "&7Le warp &6{warp} &7a été supprimé avec succès!";
	private String EditwarpItemSuccess = "&7Le warp &6{warp} &7sera affiché avec l'item \"&6{item}&7\" à &6/invwarp&7!";
	private String EditwarpLoreSuccess = "&7Le warp &6{warp} &7a maintenant la description \"&6{lore}&7\" sur &6/invwarp&7!";
	private String EditwarpPriceSuccess = "&7Le warp &6{warp} &7coûte maintenant &6{price} {currency}&7.";
	private String SignWarpSpecifyWarp = "&7[&6SignWarp&7] &cPréciser un warp à la &6ligne 2&c!";
	private String SignWarpSuccess = "&7[&6SignWarp&7] Le panneau pour le warp &6{warp} &7a été créé avec succès!";
	private String InvwarpTooManyWarps = "Il ya trop de warps! :O";
	private String InvTPNoPlayers = "&7Actuellement, il n'y a aucun joueur &7en ligne!";
	private String InvTPTooManyPlayers = "Il ya trop de joueurs en ligne! :O";
	private String InvTPSuccess = "&7[&6TP&7] Tu as été téléporté au joueur &6{player}&7.";
	private String InvTPNotOnline = "&7[&6TP&7] Le joueur &6{player} &7n'est pas en ligne.";
	private String Delay = "&7Tu seras téléporter en &6{delay} secondes&7!";
	private String DelayDoNotMove = "&6Ne bougez pas! &7Tu seras téléporter en &6{delay} secondes&7!";
	private String DelayTeleportCanceled = "&cLa téléportation a été annulée, parce que tu as bougé!";
	private String PluginReloaded = "&7[&6ClickWarp&7] Le plugin a été rechargé.";
	private String ErrorFileSaving = "&cErreur enregistrement d'un fichier! Regardez dans la console pour plus d'informations!";
	private String NoPermission = "&cAucune permission!";
	private String OnlyPlayers = "&cSeul les joueurs sont autorisés à utiliser cette commande.";
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

	public LanguageFrench(ClickWarp clickwarp) {
		plugin = clickwarp;
	}

	public void load() {
		file = new File(this.plugin.getDataFolder() + "/Languages", "fr.yml");
		cfg = YamlConfiguration.loadConfiguration(file);

		if (!(file.exists())) {
			cfg.options().header(
					"[Clickwarp] Plugin by comniemeer | Color codes: http://ess.khhq.net/mc/ | French translated by Shlaten and me ;)");
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
