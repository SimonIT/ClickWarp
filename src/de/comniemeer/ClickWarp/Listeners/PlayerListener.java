package de.comniemeer.ClickWarp.Listeners;

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
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Player p = e.getPlayer();
			ItemStack hand = p.getInventory().getItemInMainHand();
			int invwarpitem = plugin.getConfig().getInt("InvwarpItem");
			Material invwarpmaterial = Material.getMaterial(invwarpitem);
			int invtpitem = plugin.getConfig().getInt("InvwarpItem");
			Material invtpmaterial = Material.getMaterial(invtpitem);

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