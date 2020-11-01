package ro.Stellrow.PvPStats.storage;

public class DataSet {
    public String name;
    public int kills;
    public int deaths;
    public int topkillstreak;
    public int level;
    public int experience;
    public int activeKillStreak;
    public boolean isNull(){
        if(level==0){
            return true;
        }
        return false;
    }
}
