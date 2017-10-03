package de.comniemeer.ClickWarp.Listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import de.comniemeer.ClickWarp.ClickWarp;

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
            String invwarpitem = plugin.getConfig().getString("InvwarpItem").toUpperCase();
            byte invwarpitem_variant = e.getItem().getData().getData();
            if (invwarpitem.contains(":")) {
                String[] invwarpitem_split = invwarpitem.split(":");
                invwarpitem = invwarpitem_split[0].toUpperCase();
                invwarpitem_variant = Byte.parseByte(invwarpitem_split[1]);
            }
            Material invwarpmaterial = Material.getMaterial(invwarpitem);
            String invtpitem = plugin.getConfig().getString("InvtpItem").toUpperCase();
            byte invtpitem_variant = e.getItem().getData().getData();
            if (invtpitem.contains(":")) {
                String[] invtpitem_split = invtpitem.split(":");
                invtpitem = invtpitem_split[0].toUpperCase();
                invtpitem_variant = Byte.parseByte(invtpitem_split[1]);
            }
            Material invtpmaterial = Material.getMaterial(invtpitem);

            String item_prefix = ChatColor.translateAlternateColorCodes('&',
                    this.plugin.getConfig().getString("Sign.FirstLine")) + " ";

            if (e.getItem() != null && e.getItem().getType() == invwarpmaterial
                    && e.getItem().getData().getData() == invwarpitem_variant) {
                Boolean enableinvwarp = plugin.getConfig().getBoolean("EnableInvwarpItem");

                if (enableinvwarp) {
                    if (p.hasPermission("clickwarp.invwarp.item")) {
                        e.setCancelled(true);
                        p.performCommand("invwarp");
                    } else {
                        e.setCancelled(false);
                    }
                }
            } else if (e.getItem() != null && e.getItem().getType() == invtpmaterial
                    && e.getItem().getData().getData() == invtpitem_variant) {
                Boolean enableinvtp = plugin.getConfig().getBoolean("EnableInvtpItem");

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
                String name = dispname.toLowerCase();
                this.plugin.warphandler.handleWarp(p, name, dispname, Boolean.FALSE);
            } else if ((e.getItem().getItemMeta().hasLore())
                    && (((String) e.getItem().getItemMeta().getLore().get(0)).matches(
                    "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}"))) {
                Player p_ = Bukkit.getPlayer(UUID.fromString(e.getItem().getItemMeta().getLore().get(0)));
                if (p_ != null) {
                    Boolean usedelay = this.plugin.getConfig().getBoolean("Delay.Teleport.EnableDelay");
                    Sound warp_sound = Sound.valueOf(this.plugin.getConfig().getString("WarpSound").toUpperCase());
                    Boolean use_vehicle = false;
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
                        Boolean usedontmove = this.plugin.getConfig().getBoolean("Delay.Teleport.EnableDontMove");
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
        if (player.hasPermission("clickwarp.update") && ClickWarp.update && this.plugin.getConfig().getBoolean("auto-update")) {
            player.sendMessage("An update is available: " + ClickWarp.name + ", a " + ClickWarp.type + " for "
                    + ClickWarp.version_update + " available at " + ClickWarp.link);
            // Will look like - An update is available: AntiCheat v1.5.9, a
            // release for CB 1.6.2-R0.1 available at
            // http://media.curseforge.com/XYZ
            player.sendMessage("Type /clickwarp update if you would like to automatically update.");
        }
    }
}