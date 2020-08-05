package vip.bingzi.randomentry.util;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import vip.bingzi.randomentry.RandomEntry;

public class RandomEntryInv implements Listener {
    private static boolean OpenInfo = false;
    @EventHandler
    public void onOpenGUI(InventoryOpenEvent event){
        if (event.getView().getTitle().equalsIgnoreCase(RandomEntry.getPluginMain().getConfig().getString("ViewTitle"))){
            if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("检测到打开了鉴定GUI界面，状态已经设置为true");
            OpenInfo = true;
        }
    }
    @EventHandler
    public void onCloseGUI(InventoryCloseEvent event){
        if (event.getView().getTitle().equalsIgnoreCase(RandomEntry.getPluginMain().getConfig().getString("ViewTitle"))){
            if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("检测到关闭了鉴定GUI界面，状态已经设置为false");
            OpenInfo = false;
        }
    }
    @EventHandler
    public void onPlayerOpenGui(InventoryClickEvent event){
        // 检查是不是鉴定界面
        // 这里使用了OpenInfo全局参数进行了简化判断的过程
        if (OpenInfo){
            if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("玩家"+event.getView().getPlayer().getName()+"点击了鉴定界面");
            event.setCancelled(true);
            if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("已经取消点击");
        }
    }
}
