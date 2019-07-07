package de.comniemeer.ClickWarp.Commands;

import de.comniemeer.ClickWarp.AutoCommand;
import de.comniemeer.ClickWarp.ClickWarp;
import de.comniemeer.ClickWarp.Updater;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandClickwarp extends AutoCommand<ClickWarp> {

	public CommandClickwarp(ClickWarp plugin, String cmd, String description) {
		super(plugin, cmd, description);
	}

	@Override
	public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
		if (sender.hasPermission("clickwarp.clickwarp")) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("reload")) {
					this.reload();
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.PluginReloaded));
				} else if (args[0].equalsIgnoreCase("version")) {
					sender.sendMessage(ChatColor.GRAY + "[" + ChatColor.GOLD + "ClickWarp" + ChatColor.GRAY + "] Version " + ChatColor.GOLD + plugin.version + ChatColor.GRAY + " by " + ChatColor.GOLD + plugin.authors);
				} else if (args[0].equalsIgnoreCase("update")) {
					new Updater(this.plugin, this.plugin.id, this.plugin.file, Updater.UpdateType.NO_VERSION_CHECK, true); // Go
					sender.sendMessage("The Update is downloading");
				}
			} else {
				sender.sendMessage(ChatColor.YELLOW + "/clickwarp <version | reload | update>");
			}
		} else {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoPermission));
		}

		return true;
	}

	@NotNull
	@Override
	public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
		if (sender.hasPermission("clickwarp.clickwarp")) {
			List<String> arguments = new ArrayList<>();

			arguments.add("reload");
			arguments.add("version");

			if (args.length == 0) {
				return arguments;
			} else if (args.length == 1) {
				List<String> tabList = new ArrayList<>();

				for (String str : arguments) {
					if (str.startsWith(args[0].toLowerCase())) {
						tabList.add(str);
					}
				}

				return tabList;
			}
		}

		return new ArrayList<>();
	}

	private void reload() {
		plugin.reloadConfig();

		plugin.en.load();
		plugin.de.load();
		plugin.fr.load();
		plugin.pt.load();
		plugin.cz.load();
		plugin.ko.load();
		plugin.msg.load();
	}
}
