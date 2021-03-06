package de.comniemeer.ClickWarp.Commands;

import de.comniemeer.ClickWarp.AutoCommand;
import de.comniemeer.ClickWarp.ClickWarp;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandGettpskull extends AutoCommand<ClickWarp> {
	public CommandGettpskull(ClickWarp plugin, String cmd, String description) {
		super(plugin, cmd, description);
	}

	public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
		if (this.plugin.getConfig().getBoolean("GetTpSkull")) {
			if (sender.hasPermission("clickwarp.gettpskull")) {
				if (args.length == 1) {
					if (sender instanceof Player) {
						OfflinePlayer p = Bukkit.getOfflinePlayer(args[0]);
						SkullMeta meta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.PLAYER_HEAD);
						meta.setOwningPlayer(p);
						List<String> lore = new ArrayList<>();
						meta.setDisplayName(ChatColor.BOLD + p.getName());
						lore.add(p.getUniqueId().toString());
						meta.setLore(lore);
						ItemStack itemstack = new ItemStack(Material.PLAYER_HEAD, 1);
						itemstack.setItemMeta(meta);
						Player player = (Player) sender;
						PlayerInventory inventory = player.getInventory();
						inventory.addItem(itemstack);
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.msg.OnlyPlayers));
					}
				} else {
					sender.sendMessage(ChatColor.YELLOW + "/gettpskull <user>");
				}
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.msg.NoPermission));

			}

		}
		return true;
	}

	@NotNull
	@Override
	public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
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
					if (player != null && player.toLowerCase().startsWith(args[0].toLowerCase())) {
						tabList.add(player);
					}
				}

				return tabList;
			}
		}
		return new ArrayList<>();
	}
}
