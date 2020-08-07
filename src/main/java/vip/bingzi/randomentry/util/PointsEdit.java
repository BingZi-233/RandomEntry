package vip.bingzi.randomentry.util;

import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.entity.Player;

public class PointsEdit {
    private PlayerPoints playerPoints;
    public boolean onPointsEdit(Player player,int Value){
        // 这个部分被进行了简化，如果出现问题请重新使用IF进行处理。（貌似这个就是返回true和false，淦
        return playerPoints.getAPI().take(player.getName(), Value);
    }
}
