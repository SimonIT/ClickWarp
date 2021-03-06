package de.comniemeer.ClickWarp.Commands;

import de.comniemeer.ClickWarp.AutoCommand;
import de.comniemeer.ClickWarp.ClickWarp;
import de.comniemeer.ClickWarp.Exceptions.WarpNoExist;
import de.comniemeer.ClickWarp.Warp;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CustomCommands extends AutoCommand<ClickWarp> {

	public CustomCommands(ClickWarp plugin, String command, String description, String... aliases) {
		super(plugin, command, description, aliases);
	}

	public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
		if (sender instanceof Player) {
			String[] splits = description.split(" ");
			try {
				Warp warp = new Warp(splits[1]);
				warp.handleWarp((Player) sender, false);
			} catch (WarpNoExist warpNoExist) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.msg.WarpNoExist)
						.replace("{warp}", splits[1]));
			}
		} else {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.OnlyPlayers));
		}
		return false;
	}


	@NotNull
	public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
		return new ArrayList<>();
	}

}
