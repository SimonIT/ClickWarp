package de.comniemeer.ClickWarp.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;

import de.comniemeer.ClickWarp.AutoCommand;
import de.comniemeer.ClickWarp.ClickWarp;

public class CommandGettpskull extends AutoCommand<ClickWarp> {
	public CommandGettpskull(ClickWarp plugin, String cmd, String description) {
		super(plugin, cmd, description);
	}

	public boolean execute(CommandSender sender, String label, String[] args) {
		if (this.plugin.getConfig().getBoolean("GetTpSkull")) {
			if (sender.hasPermission("clickwarp.gettpskull")) {
				if (args.length == 1) {
					if (sender instanceof Player) {
						SkullMeta meta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.SKULL_ITEM);
						meta.setOwner(args[0]);
						List<String> lore = new ArrayList<String>();
						meta.setDisplayName("§l" + args[0]);
						lore.add(Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString());
						meta.setLore(lore);
						ItemStack itemstack = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
						itemstack.setItemMeta(meta);
						Player player = (Player) sender;
						PlayerInventory inventory = player.getInventory();
						inventory.setItemInMainHand(itemstack);
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.msg.OnlyPlayers));
					}
				} else {
					sender.sendMessage("§e/gettpskull <user>");
				}
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.msg.NoPermission));

			}

		}
		return true;
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String label, String[] args) {
		if (sender.hasPermission("clickwarp.gettpskull")) {
			List<String> playerList = new ArrayList<>();

			for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
				playerList.add(p.getName());
			}

			if (args.length == 0) {
				return playerList;
			} else if (args.length == 1) {
				List<String> tabList = new ArrayList<>();

				for (String player : playerList) {
					if (player.toLowerCase().startsWith(args[0].toLowerCase())) {
						tabList.add(player);
					}
				}

				return tabList;
			}
		}
		return null;
	}
}
