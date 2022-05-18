package com.i0dev.plugin.infiniteobsidian.hook;

import com.i0dev.plugin.infiniteobsidian.InfiniteObsidianPlugin;
import com.i0dev.plugin.infiniteobsidian.template.AbstractHook;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class FactionsHook extends AbstractHook {

    public boolean isWilderness(Location location) {
        if (InfiniteObsidianPlugin.getPlugin().isHookEnabled("factions-mcore")) {
            return InfiniteObsidianPlugin.getPlugin().getHook(com.i0dev.plugin.infiniteobsidian.hook.FactionsMCoreHook.class).isWilderness(location);
        }
        if (InfiniteObsidianPlugin.getPlugin().isHookEnabled("factions-uuid")) {
            return InfiniteObsidianPlugin.getPlugin().getHook(FactionsUUIDHook.class).isWilderness(location);
        }

        return false;
    }


    public boolean isSystemFaction(Location location) {
        if (isWilderness(location)) return false;

        if (InfiniteObsidianPlugin.getPlugin().isHookEnabled("factions-mcore")) {
            return InfiniteObsidianPlugin.getPlugin().getHook(com.i0dev.plugin.infiniteobsidian.hook.FactionsMCoreHook.class).isSystemFaction(location);
        }
        if (InfiniteObsidianPlugin.getPlugin().isHookEnabled("factions-uuid")) {
            return InfiniteObsidianPlugin.getPlugin().getHook(FactionsUUIDHook.class).isSystemFaction(location);
        }

        return false;
    }

    public boolean isPlayersLand(Location location, Player player) {
        if (InfiniteObsidianPlugin.getPlugin().isHookEnabled("factions-mcore")) {
            return InfiniteObsidianPlugin.getPlugin().getHook(com.i0dev.plugin.infiniteobsidian.hook.FactionsMCoreHook.class).isPlayersLand(location, player);
        }
        if (InfiniteObsidianPlugin.getPlugin().isHookEnabled("factions-uuid")) {
            return InfiniteObsidianPlugin.getPlugin().getHook(FactionsUUIDHook.class).isPlayersLand(location, player);
        }

        return false;
    }


}
