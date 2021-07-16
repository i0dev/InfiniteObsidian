package com.i0dev.command;

import com.i0dev.InfiniteObsidian;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class CmdInfiniteObsidian implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        ConfigurationSection cnf = InfiniteObsidian.get().getConfig().getConfigurationSection("message");

        if (!sender.hasPermission("infiniteobsidian.give")) {
            sender.sendMessage(InfiniteObsidian.c(cnf.getString("noPermission")));
            return false;
        }
        if (args.length < 2) {
            sender.sendMessage(InfiniteObsidian.c(cnf.getString("usage")));
            return false;
        }
        Player user = Bukkit.getPlayer(args[1]);
        if (user == null) {
            sender.sendMessage(InfiniteObsidian.c(cnf.getString("cantFind")));
            return false;
        }
        int amt = 1;
        if (args.length == 3) {
            if (!isInt(args[2])) {
                sender.sendMessage(InfiniteObsidian.c(cnf.getString("invalidNumber")));
                return false;
            }
            amt = Integer.parseInt(args[2]);
        }
        user.getInventory().addItem(InfiniteObsidian.getItem(amt));
        sender.sendMessage(InfiniteObsidian.c(cnf.getString("youGave").replace("{player}", user.getDisplayName()).replace("{amt}", amt + "")));
        return true;
    }

    private static boolean isInt(String checker) {
        try {
            Integer.parseInt(checker);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }


}
