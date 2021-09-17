package com.i0dev.InfiniteObsidian.commands;

import com.google.common.primitives.UnsignedBytes;
import com.i0dev.InfiniteObsidian.Heart;
import com.i0dev.InfiniteObsidian.config.GeneralConfig;
import com.i0dev.InfiniteObsidian.config.MessageConfig;
import com.i0dev.InfiniteObsidian.managers.MessageManager;
import com.i0dev.InfiniteObsidian.templates.AbstractCommand;
import com.i0dev.InfiniteObsidian.utility.Utility;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmdInfiniteObsidian extends AbstractCommand {

    MessageConfig msg;
    MessageManager msgManager;
    GeneralConfig cnf;

    public CmdInfiniteObsidian(Heart heart, String command) {
        super(heart, command);
    }

    @Override
    public void initialize() {
        msgManager = getHeart().getManager(MessageManager.class);
        msg = getHeart().getConfig(MessageConfig.class);
        cnf = getHeart().getConfig(GeneralConfig.class);

    }

    @Override
    public void deinitialize() {
        msgManager = null;
        msg = null;
        cnf = null;
    }


    //infiniteObsidian give <player> [amount]

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            msgManager.msg(sender, msg.getInfiniteObsidianUsage());
            msgManager.msg(sender, msg.getReloadUsage());
            return;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("infiniteObsidian.reload")) {
                msgManager.msg(sender, msg.getNoPermission());
                return;
            }
            getHeart().reload();
            msgManager.msg(sender, msg.getReloadedConfig());
            return;
        }
        if (args[0].equalsIgnoreCase("give")) {
            if (!sender.hasPermission("wands.give")) {
                msgManager.msg(sender, msg.getNoPermission());
                return;
            }

            if (args.length < 2) {
                msgManager.msg(sender, msg.getInfiniteObsidianUsage());
                return;
            }

            Player player = msgManager.getPlayer(args[1]);
            if (player == null) {
                msgManager.msg(sender, msg.getCantFindPlayer(), new MessageManager.Pair<>("{player}", args[1]));
                return;
            }

            int amt = 1;
            if (args.length == 3) {
                Integer amt1 = Utility.getInt(args[2]);
                if (amt1 == null) {
                    msgManager.msg(sender, msg.getInvalidNumber(),
                            new MessageManager.Pair<>("{num}", args[2]));
                    return;
                }
                amt = amt1;
            }

            List<String> newLore = new ArrayList<>();
            cnf.getLore().forEach(s -> {
                newLore.add(msgManager.pair(s,
                        new MessageManager.Pair<>("{price}", cnf.getPricePer() + "")
                ));
            });

            ItemStack obby = Utility.makeItem(Material.getMaterial(cnf.getMaterial()), amt, (short) 0, cnf.getDisplayName(), newLore, cnf.isGlow());
            NBTItem nbtItem = new NBTItem(obby);
            nbtItem.setBoolean("infiniteObsidian", true);
            obby = nbtItem.getItem();

            player.getInventory().addItem(obby);

            msgManager.msg(player, msg.getReceived(),
                    new MessageManager.Pair<>("{amt}", amt + ""),
                    new MessageManager.Pair<>("{player}", sender.getName())
            );

            msgManager.msg(sender, msg.getGave(),
                    new MessageManager.Pair<>("{amt}", amt + ""),
                    new MessageManager.Pair<>("{player}", player.getName())
            );

        }
    }

    List<String> blank = new ArrayList<>();

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) return Arrays.asList("give", "reload");
        if (args[0].equalsIgnoreCase("give")) {
            if (!sender.hasPermission("infiniteObsidian.give")) return blank;
            if (args.length == 2) return null;
            if (args.length == 3) return Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
        }
        return blank;
    }
}
