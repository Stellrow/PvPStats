package ro.Stellrow.PvPStats.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ro.Stellrow.PvPStats.PVPStats;

public class StatsCommands implements CommandExecutor {
    private final PVPStats pl;

    public StatsCommands(PVPStats pl) {
        this.pl = pl;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            pl.liveStorage.showProgress(p.getUniqueId());
        }
        return true;
    }
}
