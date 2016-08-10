package de.comniemeer.ClickWarp.Listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import de.comniemeer.ClickWarp.ClickWarp;

public class PlayerListener implements Listener {

	private ClickWarp plugin;

	public PlayerListener(ClickWarp clickwarp) {
		plugin = clickwarp;
	}

	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_AIR) {
			Player p = e.getPlayer();
			ItemStack hand = p.getInventory().getItemInMainHand();
			int invwarpitem = plugin.getConfig().getInt("InvwarpItem");
			Material invwarpmaterial = Material.getMaterial(invwarpitem);
			int invtpitem = plugin.getConfig().getInt("InvwarpItem");
			Material invtpmaterial = Material.getMaterial(invtpitem);
			String item_prefix = ChatColor.translateAlternateColorCodes('&',
					this.plugin.getConfig().getString("Sign.FirstLine")) + " ";

			if (hand != null && hand.getType() == invwarpmaterial) {
				Boolean enableinvwarp = plugin.getConfig().getBoolean("EnableInvwarpItem");

				if (enableinvwarp.booleanValue()) {
					if (p.hasPermission("clickwarp.invwarp.item")) {
						e.setCancelled(true);
						p.performCommand("invwarp");
						return;
					} else {
						e.setCancelled(false);
						return;
					}
				}
			} else if (hand != null && hand.getType() == invtpmaterial) {
				Boolean enableinvtp = plugin.getConfig().getBoolean("EnableInvtpItem");

				if (enableinvtp.booleanValue()) {
					if (p.hasPermission("clickwarp.invtp.item")) {
						e.setCancelled(true);
						p.performCommand("invtp");
						return;
					} else {
						e.setCancelled(false);
						return;
					}
				}
			} else if (e.getItem().getItemMeta().hasDisplayName()
					&& e.getItem().getItemMeta().getDisplayName().contains(item_prefix)) {
				String dispname = e.getItem().getItemMeta().getDisplayName().split(" ")[1];
				String name = dispname.toLowerCase();
				this.plugin.warphandler.handleWarp(p, name, dispname, Boolean.valueOf(false));
			} else if ((e.getItem().getItemMeta().hasLore())
					&& (((String) e.getItem().getItemMeta().getLore().get(0)).matches(
							"[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}"))) {
				Player p_ = Bukkit.getPlayer(UUID.fromString(e.getItem().getItemMeta().getLore().get(0)));
				if (p_ != null) {
					Boolean usedelay = Boolean
							.valueOf(this.plugin.getConfig().getBoolean("Delay.Teleport.EnableDelay"));
					if (!usedelay.booleanValue()) {
						p.teleport(p_);
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.msg.InvTPSuccess)
								.replace("{player}", p_.getName()));
					} else if (p.hasPermission("clickwarp.teleport.instant")) {
						p.teleport(p_);
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.msg.InvTPSuccess)
								.replace("{player}", p_.getName()));
					} else {
						Boolean usedontmove = Boolean
								.valueOf(this.plugin.getConfig().getBoolean("Delay.Teleport.EnableDontMove"));
						int delay = this.plugin.getConfig().getInt("Delay.Teleport.Delay");
						if (usedontmove.booleanValue()) {
							this.plugin.warp_delay.put(p.getName(), Boolean.valueOf(true));
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.msg.DelayDoNotMove)
									.replace("{delay}", String.valueOf(delay)));
						} else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.msg.Delay)
									.replace("{delay}", String.valueOf(delay)));
						}
					}
				} else {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.msg.InvTPNotOnline)
							.replace("{player}", e.getItem().getItemMeta().getDisplayName()));
				}
			}
		}
	}

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();

		if (plugin.warp_delay.containsKey(p.getName())) {
			if (e.getFrom().getX() != e.getTo().getX() || e.getFrom().getY() != e.getTo().getY()
					|| e.getFrom().getZ() != e.getTo().getZ()) {
				plugin.warp_delay.remove(p.getName());
				Bukkit.getScheduler().cancelTask(plugin.delaytask);
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.DelayTeleportCanceled));
			}
		}
	}
}