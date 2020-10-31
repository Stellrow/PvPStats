package ro.Stellrow.PvPStats.livestorage;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import ro.Stellrow.PvPStats.PVPStats;
import ro.Stellrow.PvPStats.storage.DataSet;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

public class LiveStorage {
    private final PVPStats pl;
    public ConcurrentHashMap<UUID, LiveDataSet> playerData = new ConcurrentHashMap<>();
    public LiveStorage(PVPStats pl) {
        this.pl = pl;
    }

    public void loadPlayer(UUID toLoad){
        CompletableFuture<Boolean> playerExistsFuture = pl.storage.checkPlayer(toLoad);
        playerExistsFuture.thenAccept(result->{
            if(!result){
                pl.storage.createPlayer(toLoad);
                playerExistsFuture.complete(false);
            }
        });
        if(playerExistsFuture.join()) {
            DataSet data = new DataSet();
            CompletableFuture<DataSet> future = pl.storage.getPlayerData(toLoad);
            while (!future.isDone()) {
            }
            try {
                data = future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if (data.isNull()) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[PVPStats] A player was improperly loaded into the database");
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[PVPStats] Caused by improperly creating or corrupted data for player " + Bukkit.getPlayer(toLoad).getName());
                Bukkit.getConsoleSender().sendMessage(data.level + "");
                return;
            }
            LiveDataSet liveDataSet = new LiveDataSet(pl.messageConfig, Bukkit.getPlayer(toLoad), pl.getConfig().getInt("Leveling.level-multiplier", 50)
                    , data.kills, data.deaths, data.topkillstreak
                    , data.level, data.experience, data.activeKillStreak);
            playerData.put(toLoad, liveDataSet);
        }else{
            LiveDataSet liveDataSet = new LiveDataSet(pl.messageConfig,Bukkit.getPlayer(toLoad),pl.getConfig().getInt("Leveling.level-multiplier", 50)
            ,0,0,0,1,0,0);
            playerData.put(toLoad,liveDataSet);
        }
    }
    public void removePlayer(UUID toRemove){
        if(!playerData.containsKey(toRemove)){
            return;
        }
        DataSet dataSet = new DataSet();
        LiveDataSet liveDataSet = playerData.get(toRemove);
        dataSet.kills=liveDataSet.kills;
        dataSet.activeKillStreak= liveDataSet.activeKillStreak;
        dataSet.deaths=liveDataSet.deaths;
        dataSet.experience= liveDataSet.experience;
        dataSet.level=liveDataSet.level;
        dataSet.topkillstreak= liveDataSet.topkillstreak;
        pl.storage.updatePlayer(toRemove,dataSet);
        playerData.remove(toRemove);
    }
    public void increaseKills(UUID player){
        if(playerData.containsKey(player)){
            playerData.get(player).incrementKills();
        }
    }
    public void increaseDeaths(UUID player){
        if(playerData.containsKey(player)){
            playerData.get(player).incrementDeaths();
        }
    }
    public void incrementKillStreak(UUID player){
        if(playerData.containsKey(player)){
            playerData.get(player).incrementKillStreak();
        }
    }
    public void resetKillStreak(UUID player) {
        if (playerData.containsKey(player)) {
            playerData.get(player).resetKillStreak();
        }
    }
    public void showProgress(UUID player){
        if (playerData.containsKey(player)) {
            playerData.get(player).showProgress(Bukkit.getPlayer(player));
        }
    }
    public void updateDataBase(){
        Bukkit.getScheduler().runTaskTimer(pl,()->{
            for(UUID uuid : playerData.keySet()){
                DataSet dataSet = new DataSet();
                LiveDataSet liveDataSet = playerData.get(uuid);
                dataSet.kills=liveDataSet.kills;
                dataSet.activeKillStreak= liveDataSet.activeKillStreak;
                dataSet.deaths=liveDataSet.deaths;
                dataSet.experience= liveDataSet.experience;
                dataSet.level=liveDataSet.level;
                dataSet.topkillstreak= liveDataSet.topkillstreak;
                pl.storage.updatePlayer(uuid,dataSet);
            }
        },0,5*60*60*20);
    }

}
