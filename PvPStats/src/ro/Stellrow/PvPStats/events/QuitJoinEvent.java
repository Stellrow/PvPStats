package ro.Stellrow.PvPStats.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ro.Stellrow.PvPStats.PVPStats;

public class QuitJoinEvent implements Listener {
    private final PVPStats pl;
    public QuitJoinEvent(PVPStats pl) {
        this.pl = pl;
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Bukkit.getScheduler().runTaskLater(pl,()->{
            pl.liveStorage.loadPlayer(event.getPlayer().getUniqueId());
        },20);

    }
    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        pl.liveStorage.removePlayer(event.getPlayer().getUniqueId());
    }

}
