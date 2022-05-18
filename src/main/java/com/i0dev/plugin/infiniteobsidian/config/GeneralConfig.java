package com.i0dev.plugin.infiniteobsidian.config;

import com.i0dev.plugin.infiniteobsidian.template.AbstractConfiguration;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Getter
@Setter
public class GeneralConfig extends AbstractConfiguration {
    public GeneralConfig(String path, String... header) {
        super(path, header);
    }

    protected void setValues() {
        config.set("itemDisplayName", "&c&lInfinite Obsidian", "The display name of the Infinite Obsidian block.");
        config.set("itemLore", Arrays.asList(
                "",
                "&7Place this block to never run out of obsidian",
                "&7It will charge &a${price} &7each time its placed"
        ), "The lore of the infinite obsidian block");
        config.set("itemMaterial", "BEDROCK", "This is the material for the item that the player will hold and use to click with, this is NOT what will be placed.");
        config.set("itemGlow", true, "Weather or not the item in the players hand should glow.");

        config.set("pricePerPlace", 1000, "How much it costs to place an Infinite Obsidian.");
        config.set("placeMaterial", "OBSIDIAN", "This is the material for the block that ACTUALLY gets placed down.");
        config.set("messagePriceEveryXSeconds", 45, "How often to send message about how much money they recently spent.");
        config.set("disablePlacingInSystemFactions", true, "Should placing Infinite Obsidian blocks be disabled in system factions (WarZone and SafeZone)");

    }
}
