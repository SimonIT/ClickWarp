package de.comniemeer.ClickWarp.Commands;import de.comniemeer.ClickWarp.AutoCommand;import de.comniemeer.ClickWarp.ClickWarp;import java.io.File;import java.util.ArrayList;import java.util.List;import org.bukkit.ChatColor;import org.bukkit.command.CommandSender;import org.bukkit.configuration.file.FileConfiguration;import org.bukkit.configuration.file.YamlConfiguration;public class CommandDelwarp extends AutoCommand<ClickWarp> {	public CommandDelwarp(ClickWarp plugin) {		super(plugin, "delwarp", "Deletes a warp");	}	public boolean execute(CommandSender sender, String label, String[] args) {		if (sender.hasPermission("clickwarp.delwarp")) {			if (args.length == 1) {				File file = new File(plugin.getDataFolder() + "/Warps", args[0].toLowerCase() + ".yml");				if (file.exists()) {					FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);					String str = args[0].toLowerCase();					String name = "";					if (cfg.getString(str + ".name") == null) {						name = str;					} else {						name = ChatColor.translateAlternateColorCodes('&', cfg.getString(str + ".name"));					}					file.delete();					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.msg.DelwarpSuccess)							.replace("{warp}", name));				} else {					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.msg.WarpNoExist)							.replace("{warp}", args[0]));				}			} else {				sender.sendMessage("�e/delwarp <warp>");			}		} else {			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.msg.NoPermission));		}		return true;	}	@Override	public List<String> tabComplete(CommandSender sender, String label, String[] args) {		if (sender.hasPermission("clickwarp.delwarp")) {			List<String> warpList = new ArrayList<>();			File warps_folder = new File("plugins/ClickWarp/Warps");			if (warps_folder.isDirectory()) {				File[] warps = warps_folder.listFiles();				if (warps.length != 0) {					for (int i = 0; i < warps.length; i++) {						warpList.add(warps[i].getName().replace(".yml", ""));					}				}			}			if (args.length == 0) {				return warpList;			} else if (args.length == 1) {				List<String> tabList = new ArrayList<>();				for (String warp : warpList) {					if (warp.startsWith(args[0].toLowerCase())) {						tabList.add(warp);					}				}				return tabList;			}		}		return null;	}}