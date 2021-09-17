package com.i0dev.InfiniteObsidian.handlers;

import com.i0dev.InfiniteObsidian.Heart;
import com.i0dev.InfiniteObsidian.config.GeneralConfig;
import com.i0dev.InfiniteObsidian.config.MessageConfig;
import com.i0dev.InfiniteObsidian.hooks.MCoreFactionsHook;
import com.i0dev.InfiniteObsidian.managers.MessageManager;
import com.i0dev.InfiniteObsidian.templates.AbstractListener;
import de.tr7zw.changeme.nbtapi.NBTItem;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InfiniteObsidianHandler extends AbstractListener {
    public InfiniteObsidianHandler(Heart heart) {
        super(heart);
    }

    GeneralConfig cnf;
    MessageManager msgManager;
    MessageConfig msg;

    @Override
    public void initialize() {
        msgManager = getHeart().getManager(MessageManager.class);
        cnf = getHeart().getConfig(GeneralConfig.class);
        msg = getHeart().getConfig(MessageConfig.class);
    }

    @Override
    public void deinitialize() {
        msgManager = null;
        cnf = null;
        msg = null;
    }


    @EventHandler
    public void onInfiniteObsidianPlace(BlockPlaceEvent e) {
        if (e.isCancelled()) return;
        ItemStack hand = e.getPlayer().getItemInHand();
        if (hand == null || Material.AIR.equals(hand.getType())) return;
        NBTItem NBTHand = new NBTItem(hand);
        if (!NBTHand.getBoolean("infiniteObsidian")) return;
        if (cnf.isDisablePlacingInSystemFactions() && MCoreFactionsHook.isSystemFaction(e.getBlock().getLocation())) {
            msgManager.msg(e.getPlayer(), msg.getCantPlaceInSystemFac());
            e.setCancelled(true);
            return;
        }
        OfflinePlayer oPlayer = Bukkit.getOfflinePlayer(e.getPlayer().getUniqueId());
        Economy economy = getHeart().getEconomy();
        if (economy.getBalance(oPlayer) < cnf.getPricePer()) {
            msgManager.msg(e.getPlayer(), msg.getCantAfford());
            e.setCancelled(true);
            return;
        }
        economy.withdrawPlayer(oPlayer, cnf.getPricePer());
        e.getBlockPlaced().setType(Material.getMaterial(cnf.getInfiniteBlockMaterial()));
        e.getPlayer().getItemInHand().setAmount(64);
        UUID uuid = e.getPlayer().getUniqueId();
        MessageObj obj = getObj(uuid);

        if (obj == null) {
            messageObjList.add(new MessageObj(uuid, System.currentTimeMillis(), cnf.getPricePer()));
        } else {
            obj.setAmountSpent(obj.getAmountSpent() + cnf.getPricePer());
        }

    }

    List<MessageObj> messageObjList = new ArrayList<>();

    public Runnable TaskSendMessage = () -> {
        List<MessageObj> toRemove = new ArrayList<>();
        messageObjList.stream().filter(obj -> System.currentTimeMillis() >= (obj.getTime() + cnf.getMessageEveryXSeconds() * 1000L)).forEach(messageObj -> {
            Player player = Bukkit.getPlayer(messageObj.getUuid());
            if (player != null) {
                msgManager.msg(player, msg.getCharged(),
                        new MessageManager.Pair<>("{price}", messageObj.getAmountSpent() + ""),
                        new MessageManager.Pair<>("{sec}", cnf.getMessageEveryXSeconds() + "")
                );

                toRemove.add(messageObj);
            }
        });
        toRemove.forEach(messageObj -> messageObjList.remove(messageObj));
    };

    private MessageObj getObj(UUID uuid) {
        return messageObjList.stream().filter(messageObj -> messageObj.getUuid().equals(uuid)).findFirst().orElse(null);
    }

    private static class MessageObj {
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


}
