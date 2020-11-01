package ro.Stellrow.PvPStats.storage;

import io.netty.util.concurrent.CompleteFuture;
import org.bukkit.Bukkit;
import ro.Stellrow.PvPStats.PVPStats;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class DataStorage {
    private final PVPStats pl;
    private SQLiteHandler sqLiteHandler;
    public DataStorage(PVPStats pl) {
        this.pl = pl;

    }
    public void setup(){
        sqLiteHandler = new SQLiteHandler(pl.getDataFolder(),"playerData","playerData",
                "CREATE TABLE IF NOT EXISTS playerData (" +
                        "`uuid` varchar(100) PRIMARY KEY NOT NULL," +
                        "`name` varchar(100) NOT NULL," +
                        "`kills` INT NOT NULL," +
                        "`deaths` INT NOT NULL," +
                        "`topkillstreak` INT NOT NULL," +
                        "`level` INT NOT NULL," +
                        "`experience` INT NOT NULL," +
                        "`activekillstreak` INT NOT NULL" +
                        ");"
        );
        sqLiteHandler.load();
    }
    public CompletableFuture<Void> createPlayer(UUID player,String name){
        return CompletableFuture.runAsync(() -> {
            sqLiteHandler.addPlayer(player,name);
        });
    }
    public CompletableFuture<Boolean> checkPlayer(UUID player){
       return CompletableFuture.supplyAsync(()-> sqLiteHandler.playerExists(player));
    }
    public CompletableFuture<DataSet> getPlayerData(UUID toGet){
        return CompletableFuture.supplyAsync(()->sqLiteHandler.getPlayerData(toGet));
    }
    public CompletableFuture<Void> updatePlayer(UUID toUpdate,DataSet data){
        return CompletableFuture.runAsync(()-> sqLiteHandler.updatePlayerData(toUpdate,data));
    }
    public void saveUUIDNonAsync(UUID uuid,DataSet dataSet){
        sqLiteHandler.updatePlayerData(uuid, dataSet);
    }




}
