package com.i0dev.plugin.infiniteobsidian;

import com.i0dev.plugin.infiniteobsidian.command.CmdInfiniteObsidian;
import com.i0dev.plugin.infiniteobsidian.config.GeneralConfig;
import com.i0dev.plugin.infiniteobsidian.config.MessageConfig;
import com.i0dev.plugin.infiniteobsidian.hook.FactionsHook;
import com.i0dev.plugin.infiniteobsidian.hook.FactionsMCoreHook;
import com.i0dev.plugin.infiniteobsidian.hook.PlaceholderAPIHook;
import com.i0dev.plugin.infiniteobsidian.hook.VaultHook;
import com.i0dev.plugin.infiniteobsidian.manager.ConfigManager;
import com.i0dev.plugin.infiniteobsidian.manager.PlaceManager;
import com.i0dev.plugin.infiniteobsidian.object.CorePlugin;
import com.i0dev.plugin.infiniteobsidian.utility.Utility;
import lombok.Getter;

@Getter
public class InfiniteObsidianPlugin extends CorePlugin {
    @Getter
    private static InfiniteObsidianPlugin plugin;
    public static final String PERMISSION_PREFIX = "infiniteobsidian";

    @Override
    public void startup() {
        plugin = this;

        // Managers
        registerManager(ConfigManager.getInstance());
        registerManager(PlaceManager.getInstance());

        // Hooks
        registerHook(new VaultHook(), "vault");
        if (isPluginEnabled("PlaceholderAPI"))
            registerHook(new PlaceholderAPIHook(), "papi");
        if (isPluginEnabled("Factions"))
            registerHook(new FactionsHook(), "factions");
        if (Utility.isClassInProject("com.massivecraft.factions.entity.MPlayer"))
            registerHook(new FactionsMCoreHook(), "factions-mcore");
        if (Utility.isClassInProject("com.massivecraft.factions.FactionsAPI"))
            registerHook(new FactionsMCoreHook(), "factions-uuid");

        // Configs
        registerConfig(new GeneralConfig("config.yml", "Main configuration file", "Plugin created by i0dev"));
        registerConfig(new MessageConfig("messages.yml", "Main messaging configuration"));

        // Commands
        registerCommand(CmdInfiniteObsidian.getInstance(), "infiniteobsidian");
    }

    @Override
    public void shutdown() {

    }

}
