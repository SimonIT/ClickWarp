package de.comniemeer.ClickWarp.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import de.comniemeer.ClickWarp.ClickWarp;

public class SignListener implements Listener {

	private ClickWarp plugin;

	public SignListener(ClickWarp clickwarp) {
		plugin = clickwarp;
	}

	@EventHandler
	public void onSignChange(SignChangeEvent e) {
		Player player = e.getPlayer();

		if (e.getLine(0).equalsIgnoreCase("[Warp]")) {
			if (player.hasPermission("clickwarp.sign.create")) {
				if (e.getLine(1).isEmpty()) {
					e.setCancelled(true);
					e.getBlock().breakNaturally();
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SignWarpSpecifyWarp));
					return;
				} else {
					String _line = e.getLine(1);
					if (!this.plugin.methods.existWarp(_line)) {
						e.setCancelled(true);
						e.getBlock().breakNaturally();
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.WarpNoExist)
								.replace("{warp}", _line));
						return;
					}

					String name = this.plugin.methods.getName(_line);

					e.setLine(0, ChatColor.translateAlternateColorCodes('&',
							plugin.getConfig().getString("Sign.FirstLine")));
					e.setLine(1, name);
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.SignWarpSuccess)
							.replace("{warp}", name));
					return;
				}
			} else {
				e.setCancelled(true);
				e.getBlock().breakNaturally();
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoPermission));
				return;
			}
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();

		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			BlockState bs = event.getClickedBlock().getState();

			if (bs instanceof Sign) {
				Sign sign = (Sign) bs;

				if (sign.getLine(0).equalsIgnoreCase(
						ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Sign.FirstLine")))) {
					if (player.hasPermission("clickwarp.sign.use")) {
						event.setCancelled(true);

						String str = ChatColor.stripColor(sign.getLine(1).toLowerCase());

						plugin.warphandler.handleWarp(player, str, sign.getLine(1), true);
						return;
					} else {
						event.setCancelled(true);
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoPermission));
						return;
					}
				}
			}
		}
	}
}