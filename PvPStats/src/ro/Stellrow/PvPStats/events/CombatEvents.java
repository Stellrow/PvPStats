package ro.Stellrow.PvPStats.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import ro.Stellrow.PvPStats.PVPStats;

public class CombatEvents implements Listener {
    private final PVPStats pl;

    public CombatEvents(PVPStats pl) {
        this.pl = pl;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        pl.liveStorage.increaseDeaths(event.getEntity().getUniqueId());
        if(event.getEntity().getKiller()!=null){
            pl.liveStorage.increaseKills(event.getEntity().getKiller().getUniqueId());
        }
    }
}
