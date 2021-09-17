package com.i0dev.InfiniteObsidian.config;

import com.i0dev.InfiniteObsidian.Heart;
import com.i0dev.InfiniteObsidian.templates.AbstractConfiguration;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MessageConfig extends AbstractConfiguration {

    String reloadUsage = "&cUsage: &7/infiniteObsidian reload";
    String infiniteObsidianUsage = "&cUsage: &7/infiniteObsidian give <player> [amount]";
    String received = "&7You have received &fx{amt}&7 Infinite Obsidian from &c{player}";
    String gave = "&7You have given &c{player}&f x{amt} &7Infinite Obsidian.";
    String cantAfford = "&cYou do not have a sufficient balance to place an Infinite Obsidian block";
    String charged = "&7You we're charged &a${price} &7for your recent Infinite Obsidian placements in the last {sec} seconds.";
    String cantPlaceInSystemFac = "&cYou cannot place Infinite Obsidian in system factions.";

    String reloadedConfig = "&7You have&a reloaded&7 the configuration.";
    String noPermission = "&cYou don not have permission to run that command.";
    String cantFindPlayer = "&cThe player: &f{player}&c cannot be found!";
    String invalidNumber = "&cThe number &f{num} &cis invalid! Try again.";

    public MessageConfig(Heart heart, String path) {
        this.path = path;
        this.heart = heart;
    }
}
