package com.i0dev.InfiniteObsidian.config;

import com.i0dev.InfiniteObsidian.Heart;
import com.i0dev.InfiniteObsidian.templates.AbstractConfiguration;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class GeneralConfig extends AbstractConfiguration {

    String displayName = "&c&lInfinite Obsidian";
    List<String> lore = Arrays.asList(
            "",
            "&7Place this block to never run out of obsidian",
            "&7It will charge &a${price} &7each time its placed"
    );
    String material = "BEDROCK";
    boolean glow = true;

    long pricePer = 1000;
    String infiniteBlockMaterial = "OBSIDIAN";
    long messageEveryXSeconds = 45;

    boolean disablePlacingInSystemFactions = true;


    public GeneralConfig(Heart heart, String path) {
        this.path = path;
        this.heart = heart;
    }

}
