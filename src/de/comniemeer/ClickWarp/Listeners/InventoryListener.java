package de.comniemeer.ClickWarp.Listeners;

import de.comniemeer.ClickWarp.ClickWarp;
import de.comniemeer.ClickWarp.Exceptions.WarpNoExist;
import de.comniemeer.ClickWarp.Warp;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {

	private ClickWarp plugin;

	public InventoryListener(ClickWarp clickwarp) {
		plugin = clickwarp;
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getSlot() == e.getRawSlot()) {
			final Player player = (Player) e.getWhoClicked();

			if (plugin.InvHM.containsKey(player.getName())) {
				if (plugin.InvHM.get(player.getName()).equals("InventarWarp")) {
					e.setCancelled(true);
					ItemStack item = e.getCurrentItem();

					if (item != null && item.getType() != Material.AIR) {
						String dispname = item.getItemMeta().getDisplayName();
						try {
							Warp warp = new Warp(dispname);
							warp.handleWarp(player, false);
						} catch (WarpNoExist warpNoExist) {
							warpNoExist.printStackTrace();
						}
						this.closeInv(player);
					}
				} else if (plugin.InvHM.get(player.getName()).equals("InventarTP")) {
					e.setCancelled(true);
					ItemStack item = e.getCurrentItem();

					if (item != null && item.getType() != Material.AIR) {
						String name = ChatColor.stripColor(item.getItemMeta().getDisplayName());
						final Player p_ = Bukkit.getPlayerExact(name);

						if (p_ != null) {
							boolean usedelay = plugin.getConfig().getBoolean("Delay.Teleport.EnableDelay");

							if (!usedelay) {
								boolean use_vehicle = false;
								Entity vec = null;
								if (player.getVehicle() != null && player.hasPermission("clickwarp.vehiclewarp")
										&& plugin.getConfig().getBoolean("VehicleWarp")) {
									use_vehicle = true;
									vec = player.getVehicle();
								}
								Sound warp_sound = Sound
										.valueOf(this.plugin.getConfig().getString("WarpSound").toUpperCase());
								player.playEffect(player.getLocation(), Effect.ENDER_SIGNAL, null);
								player.playSound(player.getLocation(), warp_sound, 1, 0);
								player.teleport(p_);
								player.playSound(p_.getLocation(), warp_sound, 1, 0);
								if (use_vehicle) {
									vec.teleport(p_.getLocation());
									vec.addPassenger(player);
								}
								player.playEffect(p_.getLocation(), Effect.ENDER_SIGNAL, null);
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.InvTPSuccess)
										.replace("{player}", p_.getName()));
								this.plugin.log.info("[ClickWarp] [" + player.getName() + ": Warped " + player.getName()
										+ " to " + p_.getName() + "]");
							} else {
								if (player.hasPermission("clickwarp.teleport.instant")) {
									player.teleport(p_);
									player.sendMessage(
											ChatColor.translateAlternateColorCodes('&', plugin.msg.InvTPSuccess)
													.replace("{player}", p_.getName()));
									this.plugin.log.info("[ClickWarp] [" + player.getName() + ": Warped "
											+ player.getName() + " to " + p_.getName() + "]");
								} else {
									boolean usedontmove = plugin.getConfig()
											.getBoolean("Delay.Teleport.EnableDontMove");
									int delay = plugin.getConfig().getInt("Delay.Teleport.Delay");

									if (usedontmove) {
										plugin.warp_delay.put(player.getName(), true);
										player.sendMessage(
												ChatColor.translateAlternateColorCodes('&', plugin.msg.DelayDoNotMove)
														.replace("{delay}", String.valueOf(delay)));
									} else {
										player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.Delay)
												.replace("{delay}", String.valueOf(delay)));
									}

									plugin.delaytask = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,
											() -> {
												player.teleport(p_);
												plugin.warp_delay.remove(player.getName());
												player.sendMessage(ChatColor
														.translateAlternateColorCodes('&', plugin.msg.InvTPSuccess)
														.replace("{player}", p_.getName()));
											}, delay * 20L);
								}
							}
						} else {
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.InvTPNotOnline)
									.replace("{player}", name));
						}

						this.closeInv(player);
					}
				}
			}
		}
	}

	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		plugin.InvHM.remove(event.getPlayer().getName());
	}

	private void closeInv(Player player) {
		player.closeInventory();
		plugin.InvHM.remove(player.getName());
	}
}
