package com.i0dev.plugin.infiniteobsidian.config;

import com.i0dev.plugin.infiniteobsidian.template.AbstractConfiguration;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageConfig extends AbstractConfiguration {

    public MessageConfig(String path, String... header) {
        super(path, header);
    }

    protected void setValues() {
        config.set("notEnoughMoney", "&cYou don't have enough money to use this.");
        config.set("receivedBlocks", "&7You have received &fx{amt}&7 Infinite Obsidian Blocks.");
        config.set("gaveBlocks", "&7You have given &c{player}&f x{amt} &7Infinite Obsidian blocks.");
        config.set("charged", "&7You we're charged &a${price} &7for your recent Infinite Obsidian placements in the last {sec} seconds.");
        config.set("cantPlaceInSystemFaction", "&cYou cannot place Infinite Obsidian in system factions.");

        config.set("reloadedConfig", "&7You have&a reloaded&7 the configuration.");
        config.set("noPermission", "&cYou don not have permission to run that command.");
        config.set("cantFindPlayer", "&cThe player: &f{player}&c cannot be found!");
        config.set("invalidNumber", "&cThe number &f{num} &cis invalid! Try again.");

        config.set("helpPageTitle", "&8_______&r&8[&r &c&lInfinite Obsidian &8]_______");
        config.set("helpPageFormat", " &c* &7/{cmd}");
    }
}
