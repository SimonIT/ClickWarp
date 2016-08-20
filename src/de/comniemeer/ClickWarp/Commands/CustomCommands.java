package de.comniemeer.ClickWarp.Commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.comniemeer.ClickWarp.AutoCommand;
import de.comniemeer.ClickWarp.ClickWarp;

public class CustomCommands extends AutoCommand<ClickWarp> {

	public CustomCommands(ClickWarp plugin, String command, String description, String... aliases) {
		super(plugin, command, description, aliases);
	}

	public boolean execute(CommandSender sender, String label, String[] args) {
		if (sender instanceof Player) {
			String[] splits = description.split(" ");
			this.plugin.warphandler.handleWarp((Player) sender, splits[1].toLowerCase(), "", false);
		} else {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.OnlyPlayers));
		}
		return false;
	}


	public List<String> tabComplete(CommandSender sender, String label, String[] args) {
		return null;
	}

}
