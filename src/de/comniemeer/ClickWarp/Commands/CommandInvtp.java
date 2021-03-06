package de.comniemeer.ClickWarp.Commands;

import de.comniemeer.ClickWarp.AutoCommand;
import de.comniemeer.ClickWarp.ClickWarp;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandInvtp extends AutoCommand<ClickWarp> {

	public CommandInvtp(ClickWarp plugin, String cmd, String description, String alias) {
		super(plugin, cmd, description, alias);
	}

	@Override
	public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
		if (sender.hasPermission("clickwarp.invtp")) {
			if (sender instanceof Player) {
				if (args.length == 0) {
					Player p = (Player) sender;
					int lines = 0;
					Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[0]);

					if (players.length == 1) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.InvTPNoPlayers));
						return true;
					} else if (players.length > 54) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.InvTPTooManyPlayers));
						return true;
					}

					while (lines * 9 < players.length - 1) {
						lines++;
					}

					if (lines > 6) {
						lines = 6;
					}

					String title = ChatColor.translateAlternateColorCodes('&',
							plugin.getConfig().getString("Inventory.Teleport"));
					Inventory inv = Bukkit.createInventory(null, lines * 9, title);
					int slot = 0;

					for (Player p_ : players) {
						if (!p_.equals(p)) {
							ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
							SkullMeta meta = (SkullMeta) item.getItemMeta();

							meta.setOwningPlayer(p_);
							meta.setDisplayName(ChatColor.BOLD + p_.getName());
							item.setItemMeta(meta);
							inv.setItem(slot, item);
							slot++;
						}
					}

					p.openInventory(inv);
					plugin.InvHM.put(p.getName(), "InventarTP");
				} else {
					sender.sendMessage(ChatColor.YELLOW + "/invtp");
				}
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.OnlyPlayers));
			}
		} else {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoPermission));
		}
		return true;
	}

	@NotNull
	@Override
	public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
		return new ArrayList<>();
	}
}
