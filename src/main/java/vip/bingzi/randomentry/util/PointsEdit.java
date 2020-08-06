package vip.bingzi.randomentry.util;

import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.entity.Player;

public class PointsEdit {
    private PlayerPoints playerPoints;
    public boolean onPointsEdit(Player player,int Value){
        if (playerPoints.getAPI().take(player.getName(),Value)) {
            return true;
        }else{
            return false;
        }
    }
}
