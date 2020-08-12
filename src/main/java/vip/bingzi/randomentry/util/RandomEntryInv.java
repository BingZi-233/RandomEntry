package vip.bingzi.randomentry.util;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import vip.bingzi.randomentry.RandomEntry;

public class RandomEntryInv implements Listener {

    @EventHandler
    public void onPlayerOpenGui(InventoryClickEvent event) {
        // 检查是不是鉴定界面
        if (RandomEntry.getPluginMain().getConfig().getString("ViewTitle").equalsIgnoreCase(event.getView().getTitle())) {
            // 获取到执行玩家的实例对象
            Player player = (Player) event.getView().getPlayer();
            // 调试信息
            if (RandomEntry.Debug)
                RandomEntry.getPluginMain().getLogger().info("玩家" + event.getView().getPlayer().getName() + "点击了鉴定界面");
            // 取消点击事件，以防玩家拿取物品
            event.setCancelled(true);
            // 调试信息
            if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("已经取消点击");
            // 如果玩家点击的是空白地方，则直接进行返回。（这里是为了防止出现报错信息，获取物品得到了空指针）
            if (event.getCurrentItem() == null) return;
            // 这里获取玩家点击的是否是鉴定按钮
            if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(RandomEntry.getPluginMain().getConfig().getString("VaultButton.Name"))) {
                if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("点击了金币按钮，进行金币扣除操作");
                // 先进行物品是否有需求的Lore检查
                if (!LoreProcess.getKeyInfo(player,true)){
                    if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("未检测到指定Lore，进行终止操作。");
                    return;
                }
                // 进行了金币扣除操作
                if (VaultEdit.onVaultEdit(player, RandomEntry.getPluginMain().getConfig().getInt("VaultButton.Consume"))) {
                    if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("金币扣除成功，正在进行后续操作");
                    LoreProcess.onStringProcess(player,true);
                } else {
                    if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("金币扣除失败，进行玩家提示");
                    player.sendRawMessage(RandomEntry.Message.getString("Failure"));
                }
                return;
            }
            if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(RandomEntry.getPluginMain().getConfig().getString("PointsButton.Name"))) {
                if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("点击了点券按钮");
                // 先进行物品是否有需求的Lore检查
                if (!LoreProcess.getKeyInfo(player,false)){
                    if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("未检测到指定Lore，进行终止操作。");
                    return;
                }
                // 进行了金币扣除操作
                if (PointsEdit.onPointsEdit(player, RandomEntry.getPluginMain().getConfig().getInt("VaultButton.Consume"))) {
                    if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("点券扣除成功，正在进行后续操作");
                    LoreProcess.onStringProcess(player,false);
                } else {
                    if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("点券扣除失败，进行玩家提示");
                    player.sendRawMessage(RandomEntry.Message.getString("Failure"));
                }
                return;
            }
        }
    }
}
