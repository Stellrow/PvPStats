package ro.Stellrow.PvPStats.livestorage;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import ro.Stellrow.PvPStats.utils.CustomConfig;
import ro.Stellrow.PvPStats.utils.Utils;

public class LiveDataSet {
    public Player player;
    public int kills;
    public int deaths;
    public int topkillstreak;
    public int level;
    public int experience;
    public int activeKillStreak;
    public final CustomConfig messageConfig;
    public int levelMultiplier = 50;

    public LiveDataSet(CustomConfig messageConfig,Player player,int levelMultiplier,int kills, int deaths, int topkillstreak, int level, int experience, int activeKillStreak){
        this.messageConfig = messageConfig;
        this.levelMultiplier = levelMultiplier;
        this.kills = kills;
        this.deaths = deaths;
        this.topkillstreak = topkillstreak;
        this.level = level;
        this.experience = experience;
        this.activeKillStreak = activeKillStreak;
    }

    public void incrementKills(){
        kills++;
        experience++;
        checkLevelUp();
    }
    public void incrementDeaths(){
        deaths++;
    }
    private void checkLevelUp(){
        if(experience>level*levelMultiplier){
            experience=experience-(level*levelMultiplier);
            player.sendMessage(Utils.asColor(messageConfig.getString("Messages.level-up").replaceAll("%currentLevel%",level+"")));
            level++;
        }
    }
    public void incrementKillStreak(){
        activeKillStreak++;
        if(activeKillStreak>topkillstreak){
            topkillstreak=activeKillStreak;
            player.sendMessage(Utils.asColor(messageConfig.getString("Messages.new-top-kill-streak")));
        }
    }
    public void resetKillStreak(){
        activeKillStreak=0;
    }
    public void showProgress(Player p){
        for(String s : messageConfig.getConfig().getStringList("Messages.stats")){
            String s2 = s;
            s2 = s2.replaceAll("%player%",messageConfig.getString("Messages.me"));
            s2 = s2.replaceAll("%kills%",kills+"");
            s2 = s2.replaceAll("%deaths%",deaths+"");
            s2 = s2.replaceAll("%killstreak%",activeKillStreak+"");
            s2 = s2.replaceAll("%topkillstreak%",topkillstreak+"");
            s2 = s2.replaceAll("%level%",level+"");
            s2 = s2.replaceAll("%percentage_bar%",Utils.getProgressBar(kills,level*levelMultiplier,20,':', ChatColor.GREEN,ChatColor.GRAY));
            s2 = s2.replaceAll("%percentage%",Utils.getPercentage(kills,remainingKills()));
            s2 = s2.replaceAll("%killstonextlevel%",remainingKills()+"");
            p.sendMessage(Utils.asColor(s2));
        }
    }
    private int remainingKills(){
        if(experience==0){
            return level*levelMultiplier;
        }
        return experience-(level*levelMultiplier);
    }
}
