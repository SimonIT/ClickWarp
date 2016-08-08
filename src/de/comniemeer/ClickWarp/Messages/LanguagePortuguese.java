package de.comniemeer.ClickWarp.Messages;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import de.comniemeer.ClickWarp.ClickWarp;

public class LanguagePortuguese {

	private ClickWarp plugin;

	public LanguagePortuguese(ClickWarp clickwarp) {
		plugin = clickwarp;
	}

	public File file;
	public FileConfiguration cfg;

	private String NoWarps = "&7Nao existe nenhum warp de momento!";
	private String WarpList = "&7No momento temos estes warps:";
	private String WarpNoExist = "&7Nao existe nenhum warp com o nome &6{warp}&7!";
	private String WarpNotEnoughMoney = "&7Nao tens dinheiro suficiente para usar este warp: &6{price} {currency}&7!";
	private String WarpSuccess = "&7[&6Warp&7] Foi teletransportado para o warp &6{warp}&7.";
	private String WarpSuccessPayed = "&7[&6Warp&7] Foste Teletransportado por &6{price} {currency} &7para o warp &6{warp}&7.";
	private String SetwarpInvalidName = "&cNome do warp Invalido!";
	private String SetwarpSuccess = "&7O warp &6{warp} &7foi criado com sucesso.";
	private String SetwarpNeedNumber = "&7Voce deve digitar um numero! exemplo: \"&6/setwarp warp 1&7\"";
	private String SetwarpInvalidItem = "&cItem Invalido!";
	private String SetwarpWrongItemFormat = "&7Apenas podes usar o formato: \"&656&7\" or \"&635:4&7\"!";
	private String DelwarpSuccess = "&7O Warp &6{warp} &7foi excluido com sucesso!";
	private String EditwarpItemSuccess = "&7O warp &6{warp} &7vai ser mostrado com o item \"&6{item}&7\" em &6/invwarp&7!";
	private String EditwarpLoreSuccess = "&7O warp &6{warp} &7tem a submensagem \"&6{lore}&7\" em &6/invwarp&7!";
	private String EditwarpPriceSuccess = "&7O Warp &6{warp} &7foi adicionado com o preco &6{price} {currency}&7.";
	private String SignWarpSpecifyWarp = "&7[&6SignWarp&7] &cEscolha um warp especifico na &6linha 2&c!";
	private String SignWarpSuccess = "&7[&6SignWarp&7] Placa para o warp &6{warp} &7cridado com sucesso!";
	private String InvwarpTooManyWarps = "&7Existe muitos warps! :O";
	private String InvTPNoPlayers = "&7Actualmente nao existe jogadores online!";
	private String InvTPTooManyPlayers = "&7Existe muitos players onlines! :O";
	private String InvTPSuccess = "&7[&6TP&7] Foste teletransportado para o~player &6{player}&7.";
	private String InvTPNotOnline = "&7[&6TP&7] O Jogador &6{player} &7ja nao esta online.";
	private String Delay = "&7Vais ser teletranportador em &6{delay} segundos&7!";
	private String DelayDoNotMove = "&6Nao se mexa! &7vais ser teletransportado em &6{delay} segundos&7!";
	private String DelayTeleportCanceled = "&cO teleport foi cancelado por teres movido!";
	private String PluginReloaded = "&7[&6ClickWarp&7] Reload Completo.";
	private String ErrorFileSaving = "&cErro ao gaurdar o ficheiro veja na consola para mais informacao!";
	private String NoPermission = "&cComando protegido por permissao!";
	private String OnlyPlayers = "&cApenas os jogadores podem usar este comando.";

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
		file = new File("plugins/ClickWarp/Languages", "pt.yml");
		cfg = YamlConfiguration.loadConfiguration(file);

		if (!(file.exists())) {
			cfg.options().header(
					"[Clickwarp] Plugin by comniemeer | Color codes: http://ess.khhq.net/mc/ | Portugese translated by ksocially");
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