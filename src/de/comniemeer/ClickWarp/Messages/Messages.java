package de.comniemeer.ClickWarp.Messages;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import de.comniemeer.ClickWarp.ClickWarp;

public class Messages {

    private ClickWarp plugin;

    public Messages(ClickWarp clickwarp) {
        plugin = clickwarp;
    }

    public File messagesFile;
    public FileConfiguration messages;

    public String NoWarps;
    public String WarpList;
    public String WarpNoExist;
    public String WarpNotEnoughMoney;
    public String WarpSuccess;
    public String WarpSuccessPayed;
    public String SetwarpInvalidName;
    public String SetwarpSuccess;
    public String SetwarpNeedNumber;
    public String SetwarpInvalidItem;
    public String SetwarpWrongItemFormat;
    public String DelwarpSuccess;
    public String EditwarpItemSuccess;
    public String EditwarpLoreSuccess;
    public String EditwarpPriceSuccess;
    public String SignWarpSpecifyWarp;
    public String SignWarpSuccess;
    public String InvwarpTooManyWarps;
    public String InvTPNoPlayers;
    public String InvTPTooManyPlayers;
    public String InvTPSuccess;
    public String InvTPNotOnline;
    public String Delay;
    public String DelayDoNotMove;
    public String DelayTeleportCanceled;
    public String PluginReloaded;
    public String ErrorFileSaving;
    public String NoPermission;
    public String OnlyPlayers;

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
        messagesFile = new File(this.plugin.getDataFolder() + "/Languages", plugin.getConfig().getString("Language") + ".yml");
        messages = YamlConfiguration.loadConfiguration(messagesFile);

        NoWarps = messages.getString(NoWarpsPath);
        WarpList = messages.getString(WarpListPath);
        WarpNoExist = messages.getString(WarpNoExistPath);
        WarpSuccess = messages.getString(WarpSuccessPath);
        WarpSuccessPayed = messages.getString(WarpSuccessPayedPath);
        SetwarpInvalidName = messages.getString(SetwarpInvalidNamePath);
        SetwarpSuccess = messages.getString(SetwarpSuccessPath);
        SetwarpNeedNumber = messages.getString(SetwarpNeedNumberPath);
        SetwarpInvalidItem = messages.getString(SetwarpInvalidItemPath);
        SetwarpWrongItemFormat = messages.getString(SetwarpWrongItemFormatPath);
        DelwarpSuccess = messages.getString(DelwarpSuccessPath);
        EditwarpItemSuccess = messages.getString(EditwarpItemSuccessPath);
        EditwarpLoreSuccess = messages.getString(EditwarpLoreSuccessPath);
        EditwarpPriceSuccess = messages.getString(EditwarpPriceSuccessPath);
        SignWarpSpecifyWarp = messages.getString(SignWarpSpecifyWarpPath);
        SignWarpSuccess = messages.getString(SignWarpSuccessPath);
        InvwarpTooManyWarps = messages.getString(InvwarpTooManyWarpsPath);
        InvTPNoPlayers = messages.getString(InvTPNoPlayersPath);
        InvTPTooManyPlayers = messages.getString(InvTPTooManyPlayersPath);
        InvTPSuccess = messages.getString(InvTPSuccessPath);
        InvTPNotOnline = messages.getString(InvTPNotOnlinePath);
        WarpNotEnoughMoney = messages.getString(WarpNotEnoughMoneyPath);
        Delay = messages.getString(DelayPath);
        DelayDoNotMove = messages.getString(DelayDoNotMovePath);
        DelayTeleportCanceled = messages.getString(DelayTeleportCanceledPath);
        PluginReloaded = messages.getString(PluginReloadedPath);
        ErrorFileSaving = messages.getString(ErrorFileSavingPath);
        NoPermission = messages.getString(NoPermissionPath);
        OnlyPlayers = messages.getString(OnlyPlayersPath);
    }
}