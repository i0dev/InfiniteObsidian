package com.i0dev.plugin.infiniteobsidian.manager;


import com.i0dev.plugin.infiniteobsidian.InfiniteObsidianPlugin;
import com.i0dev.plugin.infiniteobsidian.hook.FactionsHook;
import com.i0dev.plugin.infiniteobsidian.hook.VaultHook;
import com.i0dev.plugin.infiniteobsidian.object.Pair;
import com.i0dev.plugin.infiniteobsidian.object.QueuedMessage;
import com.i0dev.plugin.infiniteobsidian.template.AbstractManager;
import com.i0dev.plugin.infiniteobsidian.utility.MsgUtil;
import com.i0dev.plugin.infiniteobsidian.utility.Utility;
import de.tr7zw.nbtapi.NBTItem;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlaceManager extends AbstractManager {


    @Getter
    private static final PlaceManager instance = new PlaceManager();

    BukkitTask messageTask;

    @Override
    public void initialize() {
        getInstance().setListener(true);
        messageTask = Bukkit.getScheduler().runTaskTimerAsynchronously(InfiniteObsidianPlugin.getPlugin(), TaskSendMessage, 20, 20);
    }

    @Override
    public void deinitialize() {
        if (messages != null) messageTask.cancel();
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (e.isCancelled()) return;
        ItemStack hand = e.getPlayer().getItemInHand();
        if (hand == null || Material.AIR.equals(hand.getType())) return;
        NBTItem NBTHand = new NBTItem(hand);
        if (!NBTHand.getBoolean("infiniteObsidian")) return;

        InfiniteObsidianPlugin plugin = InfiniteObsidianPlugin.getPlugin();

        if (plugin.cnf().getBoolean("disablePlacingInSystemFactions") && plugin.getHook(FactionsHook.class).isSystemFaction(e.getBlock().getLocation())) {
            MsgUtil.msg(e.getPlayer(), plugin.msg().getString("cantPlaceInSystemFaction"));
            e.setCancelled(true);
            return;
        }
        VaultHook economy = InfiniteObsidianPlugin.getPlugin().getHook(VaultHook.class);
        int pricePer = plugin.cnf().getInt("pricePerPlace");


        if (economy.getBalance(e.getPlayer()) < pricePer) {
            MsgUtil.msg(e.getPlayer(), plugin.msg().getString("notEnoughMoney"));
            e.setCancelled(true);
            return;
        }
        economy.withdrawMoney(e.getPlayer(), pricePer);
        e.getBlockPlaced().setType(Material.getMaterial(plugin.cnf().getString("placeMaterial")));
        e.getPlayer().getItemInHand().setAmount(64);
        UUID uuid = e.getPlayer().getUniqueId();
        QueuedMessage obj = getQueuedMessage(uuid);

        if (obj == null) {
            messages.add(new QueuedMessage(uuid, System.currentTimeMillis(), pricePer));
        } else {
            obj.setAmountSpent(obj.getAmountSpent() + pricePer);
        }

    }

    List<QueuedMessage> messages = new ArrayList<>();

    public Runnable TaskSendMessage = () -> {
        List<QueuedMessage> toRemove = new ArrayList<>();
        messages.stream().filter(obj -> System.currentTimeMillis() >= (obj.getTime() + InfiniteObsidianPlugin.getPlugin().cnf().getInt("messagePriceEveryXSeconds") * 1000L)).forEach(messageObj -> {
            Player player = Bukkit.getPlayer(messageObj.getUuid());
            if (player != null) {
                MsgUtil.msg(player, InfiniteObsidianPlugin.getPlugin().msg().getString("charged"),
                        new Pair<>("{price}", Utility.formatNumber(messageObj.getAmountSpent())),
                        new Pair<>("{sec}", InfiniteObsidianPlugin.getPlugin().cnf().getInt("messagePriceEveryXSeconds") + "")
                );

                toRemove.add(messageObj);
            }
        });
        toRemove.forEach(messageObj -> messages.remove(messageObj));
    };


    private QueuedMessage getQueuedMessage(UUID uuid) {
        return messages.stream().filter(messageObj -> messageObj.getUuid().equals(uuid)).findFirst().orElse(null);
    }


}
