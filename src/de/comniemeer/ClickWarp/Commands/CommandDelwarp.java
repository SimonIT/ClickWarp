package de.comniemeer.ClickWarp.Commands;

import de.comniemeer.ClickWarp.AutoCommand;
import de.comniemeer.ClickWarp.ClickWarp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CommandDelwarp extends AutoCommand<ClickWarp> {

    public CommandDelwarp(ClickWarp plugin, String cmd, String description, String... alias) {
        super(plugin, cmd, description, alias);
    }

    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender.hasPermission("clickwarp.delwarp")) {
            if (args.length == 1) {
                boolean result = this.plugin.methods.delWarp(args[0]);
                if (result) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.msg.DelwarpSuccess)
                            .replace("{warp}", args[0]));
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.msg.WarpNoExist)
                            .replace("{warp}", args[0]));
                }
            } else {
                sender.sendMessage(ChatColor.YELLOW + "/delwarp <warp>");
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.msg.NoPermission));
        }

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String label, String[] args) {
        if (sender.hasPermission("clickwarp.delwarp")) {
            List<String> warpList = new ArrayList<>();
            File warps_folder = new File("plugins/ClickWarp/Warps");

            if (warps_folder.isDirectory()) {
                File[] warps = warps_folder.listFiles();

                if (warps.length != 0) {
                    for (File warp : warps) {
                        warpList.add(warp.getName().replace(".yml", ""));
                    }
                }
            }

            if (args.length == 0) {
                return warpList;
            } else if (args.length == 1) {
                List<String> tabList = new ArrayList<>();

                for (String warp : warpList) {
                    if (warp.startsWith(args[0].toLowerCase())) {
                        tabList.add(warp);
                    }
                }

                return tabList;
            }
        }

        return null;
    }
}