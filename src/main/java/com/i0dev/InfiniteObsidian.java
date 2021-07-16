package com.i0dev;

import com.i0dev.command.CmdInfiniteObsidian;
import com.i0dev.event.Interaction;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class InfiniteObsidian extends JavaPlugin {

    @Override
    public void onEnable() {
        inst = this;
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getServer().getPluginManager().registerEvents(new Interaction(), this);
        getCommand("infiniteObsidian").setExecutor(new CmdInfiniteObsidian());
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, Interaction.TaskSendMessage, 20L, 20L);
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "InfiniteObsidian Enabled!");
    }

    private static Economy econ = null;

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "InfiniteObsidian Disabled!");
    }

    public static String c(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static List<String> c(List<String> ss) {
        List<String> newLore = new ArrayList<>();
        ss.forEach(s -> {
            newLore.add(c(s));
        });
        return newLore;
    }

    public static ItemStack getItem(int amt) {
        ItemStack item = new ItemStack(Material.valueOf(get().getConfig().getString("item.type")), amt);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(c(get().getConfig().getString("item.displayName")));
        if (get().getConfig().getBoolean("item.glow")) {
            meta.addEnchant(Enchantment.DURABILITY, 100, true);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON);
        }
        meta.setLore(getLore());
        item.setItemMeta(meta);
        return item;
    }

    public static List<String> getLore() {
        List<String> newLore = new ArrayList<>();
        get().getConfig().getStringList("item.lore").forEach(s -> {
            newLore.add(c(s.replace("{price}", get().getConfig().getInt("pricePer") + "")));
        });
        return newLore;
    }


    private static InfiniteObsidian inst;

    public static InfiniteObsidian get() {
        return inst;
    }

    public static Economy getEcon() {
        return econ;
    }
}
