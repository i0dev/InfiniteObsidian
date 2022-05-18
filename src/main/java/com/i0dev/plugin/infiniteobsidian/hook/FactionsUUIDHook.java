package com.i0dev.plugin.infiniteobsidian.hook;

import com.i0dev.plugin.infiniteobsidian.template.AbstractHook;
import com.massivecraft.factions.*;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class FactionsUUIDHook extends AbstractHook {

    public boolean isWilderness(Location location) {
        Chunk chunk = location.getChunk();
        return Board.getInstance().getFactionAt(new FLocation(chunk.getWorld().getName(), chunk.getX(), chunk.getZ())).isWilderness();
    }

    public boolean isSystemFaction(Location location) {
        Chunk chunk = location.getChunk();
        Faction f = Board.getInstance().getFactionAt(new FLocation(chunk.getWorld().getName(), chunk.getX(), chunk.getZ()));
        return f.isSafeZone() || f.isWarZone();
    }


    public boolean isPlayersLand(Location location, Player player) {
        Chunk chunk = location.getChunk();
        Faction f = Board.getInstance().getFactionAt(new FLocation(chunk.getWorld().getName(), chunk.getX(), chunk.getZ()));
        return f.getFPlayers().stream().map(fPlayer -> fPlayer.getPlayer().getUniqueId()).findFirst().orElse(null) != null;
    }
}
