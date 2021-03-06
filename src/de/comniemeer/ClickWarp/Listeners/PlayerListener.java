package de.comniemeer.ClickWarp.Listeners;

import de.comniemeer.ClickWarp.ClickWarp;
import de.comniemeer.ClickWarp.Exceptions.WarpNoExist;
import de.comniemeer.ClickWarp.Warp;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.UUID;

public class PlayerListener implements Listener {

	private ClickWarp plugin;

	public PlayerListener(ClickWarp clickwarp) {
		plugin = clickwarp;
	}

	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
				&& e.getItem() != null) {
			Player p = e.getPlayer();
			Material invwarpmaterial = Material.getMaterial(plugin.getConfig().getString("InvwarpItem").toUpperCase());
			Material invtpmaterial = Material.getMaterial(plugin.getConfig().getString("InvtpItem").toUpperCase());

			String item_prefix = ChatColor.translateAlternateColorCodes('&',
					this.plugin.getConfig().getString("Sign.FirstLine")) + " ";

			if (e.getItem() != null && e.getItem().getType() == invwarpmaterial) {
				boolean enableinvwarp = plugin.getConfig().getBoolean("EnableInvwarpItem");

				if (enableinvwarp) {
					if (p.hasPermission("clickwarp.invwarp.item")) {
						e.setCancelled(true);
						p.performCommand("invwarp");
					} else {
						e.setCancelled(false);
					}
				}
			} else if (e.getItem() != null && e.getItem().getType() == invtpmaterial) {
				boolean enableinvtp = plugin.getConfig().getBoolean("EnableInvtpItem");

				if (enableinvtp) {
					if (p.hasPermission("clickwarp.invtp.item")) {
						e.setCancelled(true);
						p.performCommand("invtp");
					} else {
						e.setCancelled(false);
					}
				}
			} else if (e.getItem().getItemMeta().hasDisplayName()
					&& e.getItem().getItemMeta().getDisplayName().contains(item_prefix)) {
				String dispname = e.getItem().getItemMeta().getDisplayName().split(" ")[1];
				try {
					Warp warp = new Warp(dispname);
					warp.handleWarp(p, Boolean.FALSE);
				} catch (WarpNoExist warpNoExist) {
					warpNoExist.printStackTrace();
				}
			} else if ((e.getItem().getItemMeta().hasLore())
					&& (e.getItem().getItemMeta().getLore().get(0).matches(
					"[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}"))) {
				Player p_ = Bukkit.getPlayer(UUID.fromString(e.getItem().getItemMeta().getLore().get(0)));
				if (p_ != null) {
					boolean usedelay = this.plugin.getConfig().getBoolean("Delay.Teleport.EnableDelay");
					Sound warp_sound = Sound.valueOf(this.plugin.getConfig().getString("WarpSound").toUpperCase());
					boolean use_vehicle = false;
					Entity vec = null;
					if (p.getVehicle() != null && p.hasPermission("clickwarp.vehiclewarp")
							&& plugin.getConfig().getBoolean("VehicleWarp")) {
						use_vehicle = true;
						vec = p.getVehicle();
					}
					if (!usedelay) {
						p.playEffect(p.getLocation(), Effect.ENDER_SIGNAL, null);
						p.playSound(p.getLocation(), warp_sound, 1, 0);
						p.teleport(p_);
						p.playSound(p_.getLocation(), warp_sound, 1, 0);
						if (use_vehicle) {
							vec.teleport(p_.getLocation());
							vec.addPassenger(p);
						}
						p.playEffect(p_.getLocation(), Effect.ENDER_SIGNAL, null);
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.msg.InvTPSuccess)
								.replace("{player}", p_.getName()));
					} else if (p.hasPermission("clickwarp.teleport.instant")) {
						p.playEffect(p.getLocation(), Effect.ENDER_SIGNAL, null);
						p.playSound(p.getLocation(), warp_sound, 1, 0);
						p.teleport(p_);
						p.playSound(p_.getLocation(), warp_sound, 1, 0);
						if (use_vehicle) {
							vec.teleport(p_.getLocation());
							vec.addPassenger(p);
						}
						p.playEffect(p_.getLocation(), Effect.ENDER_SIGNAL, null);
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.msg.InvTPSuccess)
								.replace("{player}", p_.getName()));
					} else {
						boolean usedontmove = this.plugin.getConfig().getBoolean("Delay.Teleport.EnableDontMove");
						int delay = this.plugin.getConfig().getInt("Delay.Teleport.Delay");
						if (usedontmove) {
							this.plugin.warp_delay.put(p.getName(), Boolean.TRUE);
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

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (player.hasPermission("clickwarp.update") && this.plugin.update && this.plugin.getConfig().getBoolean("auto-update")) {
			player.sendMessage(String.format("An update is available: %s, a %s for %s available at %s",
					this.plugin.updater.getLatestName(),
					this.plugin.updater.getLatestType(),
					this.plugin.updater.getLatestGameVersion(),
					this.plugin.updater.getLatestFileLink()));
			// Will look like - An update is available: AntiCheat v1.5.9, a
			// release for CB 1.6.2-R0.1 available at
			// http://media.curseforge.com/XYZ
			player.sendMessage("Type /clickwarp update if you would like to automatically update.");
		}
	}
}
