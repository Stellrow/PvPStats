package ro.Stellrow.PvPStats.events;

import ro.Stellrow.PvPStats.PVPStats;

public class EventsManager {
    private final PVPStats pl;

    public EventsManager(PVPStats pl) {
        this.pl = pl;
    }
    public void registerEvents(){
        pl.getServer().getPluginManager().registerEvents(new QuitJoinEvent(pl),pl);
        pl.getServer().getPluginManager().registerEvents(new CombatEvents(pl),pl);
    }
}
