package de.comniemeer.ClickWarp.Commands;

import java.util.List;

import de.comniemeer.ClickWarp.Exceptions.WarpNoExist;
import de.comniemeer.ClickWarp.Warp;
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
            try {
                Warp warp = new Warp(splits[1]);
                warp.handleWarp((Player) sender, false);
            } catch (WarpNoExist warpNoExist) {
                warpNoExist.printStackTrace();
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.OnlyPlayers));
        }
        return false;
    }


    public List<String> tabComplete(CommandSender sender, String label, String[] args) {
        return null;
    }

}
