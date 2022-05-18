package com.i0dev.plugin.infiniteobsidian.hook;

import com.i0dev.plugin.infiniteobsidian.InfiniteObsidianPlugin;
import com.i0dev.plugin.infiniteobsidian.template.AbstractHook;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;

public class VaultHook extends AbstractHook {

    Economy economy;

    @Override
    public void initialize() {
        economy = InfiniteObsidianPlugin.getPlugin().getServer().getServicesManager().getRegistration(Economy.class).getProvider();
    }

    public double getBalance(Player player) {
        return economy.getBalance(player);
    }

    public EconomyResponse withdrawMoney(Player player, double amount) {
        return economy.withdrawPlayer(player, amount);
    }
}
