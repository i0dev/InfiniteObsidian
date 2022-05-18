package com.i0dev.plugin.infiniteobsidian.hook;

import com.i0dev.plugin.infiniteobsidian.template.AbstractHook;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
import org.bukkit.Location;
import org.bukkit.entity.Player;



public class FactionsMCoreHook extends AbstractHook {

    public boolean isWilderness(Location location) {
        return BoardColl.get().getFactionAt(PS.valueOf(location)).isNone();
    }

    public boolean isSystemFaction(Location location) {
        return BoardColl.get().getFactionAt(PS.valueOf(location)).isSystemFaction();
    }

    public boolean isPlayersLand(Location location, Player player) {
        return BoardColl.get().getFactionAt(PS.valueOf(location)).getId().equals(MPlayer.get(player).getFaction().getId());
    }

}
