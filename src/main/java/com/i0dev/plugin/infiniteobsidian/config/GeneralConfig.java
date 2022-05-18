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
        config.set("itemDisplayName", "&c&lInfinite Obsidian");
        config.set("itemLore", Arrays.asList(
                "",
                "&7Place this block to never run out of obsidian",
                "&7It will charge &a${price} &7each time its placed"
        ));
        config.set("itemMaterial", "BEDROCK");
        config.set("itemGlow", true);

        config.set("pricePerPlace", 1000);
        config.set("placeMaterial", "OBSIDIAN");
        config.set("messagePriceEveryXSeconds", 45);
        config.set("disablePlacingInSystemFactions", true);

    }
}
