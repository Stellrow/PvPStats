package ro.Stellrow.PvPStats.commands;

import ro.Stellrow.PvPStats.PVPStats;

public class CommandsManager {
    private final PVPStats pl;
    public CommandsManager(PVPStats pl) {
        this.pl = pl;
    }
    public void registerCommands(){
        pl.getCommand("stats").setExecutor(new StatsCommands(pl));
    }

}
