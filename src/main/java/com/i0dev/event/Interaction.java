package com.i0dev.event;

import com.i0dev.InfiniteObsidian;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Interaction implements Listener {

    static List<MessageObj> messageObjList = new ArrayList<>();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlace(BlockPlaceEvent e) {
        if (e.isCancelled()) return;
        ItemStack hand = e.getPlayer().getItemInHand();
        if (hand == null || Material.AIR.equals(hand.getType()) || !hand.hasItemMeta() || !hand.getItemMeta().hasDisplayName() || !hand.getItemMeta().hasLore())
            return;
        ItemMeta meta = hand.getItemMeta();
        if (!meta.getDisplayName().equals(InfiniteObsidian.c(InfiniteObsidian.get().getConfig().getString("item.displayName"))))
            return;
        if (!meta.getLore().equals(InfiniteObsidian.getLore()))
            return;
        int pricePer = InfiniteObsidian.get().getConfig().getInt("pricePer");
        OfflinePlayer oPlayer = Bukkit.getOfflinePlayer(e.getPlayer().getUniqueId());
        ConfigurationSection cnf = InfiniteObsidian.get().getConfig().getConfigurationSection("message");
        if (InfiniteObsidian.getEcon().getBalance(oPlayer) < pricePer) {
            e.getPlayer().sendMessage(InfiniteObsidian.c(cnf.getString("cantAfford")));
            e.setCancelled(true);
            return;
        }
        InfiniteObsidian.getEcon().withdrawPlayer(oPlayer, pricePer);
        e.getBlockPlaced().setType(Material.valueOf(InfiniteObsidian.get().getConfig().getString("block")));
        e.getPlayer().getItemInHand().setAmount(64);
        UUID uuid = e.getPlayer().getUniqueId();
        MessageObj obj = getObj(uuid);

        if (obj == null) {
            messageObjList.add(new MessageObj(uuid, System.currentTimeMillis(), pricePer));
        } else {
            obj.setAmountSpent(obj.getAmountSpent() + pricePer);
        }

    }

    public static Runnable TaskSendMessage = () -> {
        List<MessageObj> toRemove = new ArrayList<>();
        int sec = InfiniteObsidian.get().getConfig().getInt("messageEveryXSeconds");
        messageObjList.stream().filter(obj -> System.currentTimeMillis() >= (obj.getTime() + sec * 1000L)).forEach(messageObj -> {
            Player player = Bukkit.getPlayer(messageObj.getUuid());
            if (player != null) {
                player.sendMessage(InfiniteObsidian.c(InfiniteObsidian.get().getConfig().getString("message.charged")).replace("{sec}", sec + "").replace("{price}", messageObj.getAmountSpent() + ""));
                toRemove.add(messageObj);
            }
        });
        toRemove.forEach(messageObj -> messageObjList.remove(messageObj));
    };


    private MessageObj getObj(UUID uuid) {
        return messageObjList.stream().filter(messageObj -> messageObj.getUuid().equals(uuid)).findFirst().orElse(null);
    }

}

class MessageObj {
    UUID uuid;
    long time;
    long amountSpent;

    public long getAmountSpent() {
        return amountSpent;
    }

    public long getTime() {
        return time;
    }

    public UUID getUuid() {
        return uuid;
    }


    public MessageObj(UUID uuid, long time, long amountSpent) {
        this.uuid = uuid;
        this.time = time;
        this.amountSpent = amountSpent;
    }

    public void setAmountSpent(long amountSpent) {
        this.amountSpent = amountSpent;
    }
}
