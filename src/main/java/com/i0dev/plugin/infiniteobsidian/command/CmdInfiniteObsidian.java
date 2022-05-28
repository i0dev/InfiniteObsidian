package com.i0dev.plugin.infiniteobsidian.command;

import com.i0dev.plugin.infiniteobsidian.object.Pair;
import com.i0dev.plugin.infiniteobsidian.template.AbstractCommand;
import com.i0dev.plugin.infiniteobsidian.utility.MsgUtil;
import com.i0dev.plugin.infiniteobsidian.utility.Utility;
import de.tr7zw.nbtapi.NBTItem;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CmdInfiniteObsidian extends AbstractCommand {

    @Getter
    public static final CmdInfiniteObsidian instance = new CmdInfiniteObsidian();

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            help(sender, args);
        } else {
            switch (args[0].toLowerCase()) {
                case "reload":
                    reload(sender, args);
                    break;
                case "version":
                case "ver":
                    version(sender, args);
                    break;
                case "give":
                    give(sender, args);
                    break;
                case "help":
                default:
                    help(sender, args);
            }
        }
    }

    private void give(CommandSender sender, String[] args) {
        if (!hasPermission(sender, "give")) {
            MsgUtil.msg(sender, msg().getString("noPermission"));
            return;
        }
        if (args.length < 2) {
            help(sender, args);
            return;
        }
        Player player = MsgUtil.getPlayer(args[1]);
        if (player == null) {
            MsgUtil.msg(sender, msg().getString("cantFindPlayer"), new Pair<>("{player}", args[1]));
            return;
        }
        int amt = 1;
        if (args.length == 3) {
            Integer amt1 = Utility.getInt(args[2]);
            if (amt1 == null) {
                MsgUtil.msg(sender, msg().getString("invalidNumber"), new Pair<>("{num}", args[2]));
                return;
            }
            amt = amt1;
        }
        List<String> newLore = new ArrayList<>();
        plugin.cnf().getStringList("itemLore").forEach(s -> {
            newLore.add(MsgUtil.pair(s, new Pair<>("{price}", plugin.cnf().getInt("pricePerPlace") + "")));
        });

        ItemStack obby = Utility.makeItemStackManual(Material.getMaterial(plugin.cnf().getString("itemMaterial")), amt, (short) 0, plugin.cnf().getString("itemDisplayName"), newLore, plugin.cnf().getBoolean("itemGlow"));
        NBTItem nbtItem = new NBTItem(obby);
        nbtItem.setBoolean("infiniteObsidian", true);
        obby = nbtItem.getItem();

        player.getInventory().addItem(obby);


        MsgUtil.msg(player, plugin.msg().getString("receivedBlocks"),
                new Pair<>("{amt}", amt + ""),
                new Pair<>("{player}", sender.getName())
        );

        MsgUtil.msg(player, plugin.msg().getString("gaveBlocks"),
                new Pair<>("{amt}", amt + ""),
                new Pair<>("{player}", player.getName())
        );

    }


    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) return tabCompleteHelper(args[0], Arrays.asList("reload", "help", "version", "give"));
        if (args[0].equalsIgnoreCase("give")) {
            if (!hasPermission(sender, "give")) return blank;
            if (args.length == 2) return tabCompleteHelper(args[1], null);
            if (args.length == 3)
                return tabCompleteHelper(args[2], Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"));
        }
        return blank;
    }
}
