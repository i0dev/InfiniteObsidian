package com.i0dev.InfiniteObsidian;

import com.i0dev.InfiniteObsidian.commands.CmdInfiniteObsidian;
import com.i0dev.InfiniteObsidian.config.GeneralConfig;
import com.i0dev.InfiniteObsidian.config.MessageConfig;
import com.i0dev.InfiniteObsidian.handlers.InfiniteObsidianHandler;
import com.i0dev.InfiniteObsidian.managers.MessageManager;
import com.i0dev.InfiniteObsidian.templates.AbstractCommand;
import com.i0dev.InfiniteObsidian.templates.AbstractConfiguration;
import com.i0dev.InfiniteObsidian.templates.AbstractListener;
import com.i0dev.InfiniteObsidian.templates.AbstractManager;
import com.i0dev.InfiniteObsidian.utility.ConfigUtil;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class Heart extends JavaPlugin {

    List<AbstractManager> managers = new ArrayList<>();
    List<AbstractConfiguration> configs = new ArrayList<>();

    boolean usingPapi;
    boolean usingMCoreFactions;

    @Override
    public void onEnable() {

        usingPapi = getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
        Plugin factions = getServer().getPluginManager().getPlugin("Factions");
        usingMCoreFactions = factions != null && factions.getDescription().getVersion().startsWith("2.");

        managers.addAll(Arrays.asList(
                new InfiniteObsidianHandler(this),
                new MessageManager(this),
                new CmdInfiniteObsidian(this, "infiniteObsidian")
        ));

        configs.addAll(Arrays.asList(
                new GeneralConfig(this, getDataFolder() + "/General.json"),
                new MessageConfig(this, getDataFolder() + "/Messages.json")
        ));

        reload();
        registerManagers();
        setupEconomy();

        System.out.println("\u001B[32m" + this.getDescription().getName() + " by: " + this.getDescription().getAuthors().get(0) + " has been enabled.");
    }

    public void reload() {
        // old ~ new
        ArrayList<MessageManager.Pair<AbstractConfiguration, AbstractConfiguration>> toReplace = new ArrayList<>();
        configs.forEach(abstractConfiguration -> toReplace.add(new MessageManager.Pair<>(abstractConfiguration, ConfigUtil.load(abstractConfiguration, this))));
        toReplace.forEach(pairs -> {
            configs.remove(pairs.getKey());
            configs.add(pairs.getValue());
        });
    }


    @Override
    public void onDisable() {
        configs.clear();
        managers.forEach(AbstractManager::deinitialize);
        HandlerList.unregisterAll(this);
        managers.clear();
        Bukkit.getScheduler().cancelTasks(this);
        System.out.println("\u001B[31m" + this.getDescription().getName() + " by: " + this.getDescription().getAuthors().get(0) + " has been disabled.");
    }

    public <T> T getManager(Class<T> clazz) {
        return (T) managers.stream().filter(manager -> manager.getClass().equals(clazz)).findFirst().orElse(null);
    }

    public <T> T getConfig(Class<T> clazz) {
        return (T) configs.stream().filter(config -> config.getClass().equals(clazz)).findFirst().orElse(null);
    }

    void registerManagers() {
        managers.forEach(abstractManager -> {
            if (abstractManager.isLoaded()) abstractManager.deinitialize();
            if (abstractManager instanceof AbstractListener)
                getServer().getPluginManager().registerEvents((AbstractListener) abstractManager, this);
            else if (abstractManager instanceof AbstractCommand) {
                getCommand(((AbstractCommand) abstractManager).getCommand()).setExecutor(((AbstractCommand) abstractManager));
                getCommand(((AbstractCommand) abstractManager).getCommand()).setTabCompleter(((AbstractCommand) abstractManager));
            }
            abstractManager.initialize();
            abstractManager.setLoaded(true);
        });
    }

    Economy economy = null;

    boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

}
