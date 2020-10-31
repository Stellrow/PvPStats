package ro.Stellrow.PvPStats;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ro.Stellrow.PvPStats.commands.CommandsManager;
import ro.Stellrow.PvPStats.events.EventsManager;
import ro.Stellrow.PvPStats.livestorage.LiveStorage;
import ro.Stellrow.PvPStats.storage.DataStorage;
import ro.Stellrow.PvPStats.utils.CustomConfig;

public class PVPStats extends JavaPlugin {
    //Storage
    public DataStorage storage = new DataStorage(this);
    public LiveStorage liveStorage = new LiveStorage(this);
    //Managers
    public EventsManager eventsManager = new EventsManager(this);
    public CommandsManager commandsManager = new CommandsManager(this);
    //Configs
    public CustomConfig messageConfig = new CustomConfig("messages",this);


    public void onEnable(){
    loadConfig();
    storage.setup();
    liveStorage.updateDataBase();
    eventsManager.registerEvents();
    commandsManager.registerCommands();

    }
    public void onDisable(){
        for(Player p : getServer().getOnlinePlayers()){
            liveStorage.removePlayer(p.getUniqueId());
        }
    }
    private void loadConfig(){
        getConfig().options().copyDefaults(true);
        saveConfig();
    }
    private void possibleReload(){
        for(Player p : getServer().getOnlinePlayers()){
            liveStorage.loadPlayer(p.getUniqueId());
        }
    }
}
