package de.comniemeer.ClickWarp.Commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.comniemeer.ClickWarp.AutoCommand;
import de.comniemeer.ClickWarp.ClickWarp;

public class CommandInvwarp extends AutoCommand<ClickWarp> {

    public CommandInvwarp(ClickWarp plugin, String cmd, String description, String alias) {
        super(plugin, cmd, description, alias);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (sender.hasPermission("clickwarp.invwarp")) {
            if (sender instanceof Player) {
                if (args.length == 0) {
                    Player p = (Player) sender;
                    File warps_folder = new File("plugins/ClickWarp/Warps");

                    if (warps_folder.isDirectory()) {
                        File[] warps = warps_folder.listFiles();

                        if (warps.length == 0) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoWarps));
                            return true;
                        } else if (warps.length > 54) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.InvwarpTooManyWarps));
                            return true;
                        }

                        int lines = 0;

                        while (lines * 9 < warps.length) {
                            lines++;
                        }

                        if (lines > 6) {
                            lines = 6;
                        }

                        Inventory inv = Bukkit.createInventory(null, lines * 9, ChatColor
                                .translateAlternateColorCodes('&', plugin.getConfig().getString("Inventory.Warp")));
                        int slot = 0;
                        List<String> list = this.plugin.methods.getWarps();

                        if (list.size() == 0) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoWarps));
                        } else {
                            for (String aList : list) {
                                ItemStack itemstack = this.plugin.methods.getItemStack(aList);

                                String name = this.plugin.methods.getName(aList);

                                ItemMeta meta = itemstack.getItemMeta();

                                meta.setDisplayName(ChatColor.RESET + name);

                                if (this.plugin.methods.getPreparedLore(aList) != null) {
                                    List<String> lore = this.plugin.methods.getPreparedLore(aList);

                                    Boolean useeconomy = plugin.getConfig().getBoolean("Economy.Enable");

                                    if (useeconomy) {
                                        Boolean useshowprice = plugin.getConfig().getBoolean("Economy.ShowPrice");

                                        if (useshowprice) {
                                            if (this.plugin.methods.getPrice(aList) != null) {
                                                Double price = this.plugin.methods.getPrice(aList);
                                                String priceformat = ChatColor.translateAlternateColorCodes('&',
                                                        plugin.getConfig().getString("Economy.PriceFormat")
                                                                .replace("{price}", String.valueOf(price)));

                                                if (price == 1) {
                                                    lore.add(priceformat.replace("{currency}",
                                                            plugin.getConfig().getString("Economy.CurrencySingular")));
                                                } else {
                                                    lore.add(priceformat.replace("{currency}",
                                                            plugin.getConfig().getString("Economy.CurrencyPlural")));
                                                }
                                            }
                                        }
                                    }

                                    meta.setLore(lore);
                                } else {
                                    Boolean useeconomy = plugin.getConfig().getBoolean("Economy.Enable");

                                    if (useeconomy) {
                                        Boolean useshowprice = plugin.getConfig().getBoolean("Economy.ShowPrice");

                                        if (useshowprice) {
                                            if (this.plugin.methods.getPrice(aList) != null) {
                                                List<String> lore = new ArrayList<String>();
                                                Double price = this.plugin.methods.getPrice(aList);
                                                String priceformat = ChatColor.translateAlternateColorCodes('&',
                                                        plugin.getConfig().getString("Economy.PriceFormat")
                                                                .replace("{price}", String.valueOf(price)));

                                                if (price == 1) {
                                                    lore.add(priceformat.replace("{currency}",
                                                            plugin.getConfig().getString("Economy.CurrencySingular")));
                                                } else {
                                                    lore.add(priceformat.replace("{currency}",
                                                            plugin.getConfig().getString("Economy.CurrencyPlural")));
                                                }

                                                meta.setLore(lore);
                                            }
                                        }
                                    }
                                }

                                itemstack.setItemMeta(meta);
                                inv.setItem(slot, itemstack);
                                slot++;
                            }
                        }

                        p.openInventory(inv);
                        plugin.InvHM.put(p.getName(), "InventarWarp");
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoWarps));
                    }
                } else {
                    sender.sendMessage(ChatColor.YELLOW + "/invwarp");
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.OnlyPlayers));
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msg.NoPermission));
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String label, String[] args) {
        return null;
    }
}