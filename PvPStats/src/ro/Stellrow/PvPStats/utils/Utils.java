package ro.Stellrow.PvPStats.utils;

import com.google.common.base.Strings;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static String asColor(String toTranslate){
        return ChatColor.translateAlternateColorCodes('&',toTranslate);
    }
    public static List<String> loreAsColor(List<String> toTranslate){
        List<String> toRet = new ArrayList<>();
        for(String s : toTranslate){
            toRet.add(asColor(s));
        }
        return toRet;
    }
    public static String getProgressBar(int current, int max, int totalBars, char symbol, ChatColor completedColor,
                                 ChatColor notCompletedColor) {
        float percent = (float) current / max;
        int progressBars = (int) (totalBars * percent);

        return Strings.repeat("" + completedColor + symbol, progressBars)
                + Strings.repeat("" + notCompletedColor + symbol, totalBars - progressBars);
    }
    public static String getPercentage(int current,int max){
        int percent = current * 100 / max;
        double b = Math.round(percent * 10.0) / 10.0 ;
        return (int)b+"";
    }
}
