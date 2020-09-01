package vip.bingzi.randomentry.util;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import vip.bingzi.randomentry.RandomEntry;

public class RandomEntryEvent implements Listener {
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
                if (!LoreProcess.getKeyInfo(player, true)) {
                    if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("未检测到指定Lore，进行终止操作。");
                    return;
                }
                // 进行了金币扣除操作
                if (VaultEdit.onVaultEdit(player, RandomEntry.getPluginMain().getConfig().getInt("VaultButton.Consume"))) {
                    if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("金币扣除成功，正在进行后续操作");
                    LoreProcess.onStringProcess(player, true);
                } else {
                    if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("金币扣除失败，进行玩家提示");
                    player.sendRawMessage(RandomEntry.Message.getString("Failure"));
                }
                return;
            }
            // 点券鉴定逻辑
            if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(RandomEntry.getPluginMain().getConfig().getString("PointsButton.Name"))) {
                if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("点击了点券按钮");
                // 先进行物品是否有需求的Lore检查
                if (!LoreProcess.getKeyInfo(player, false)) {
                    if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("未检测到指定Lore，进行终止操作。");
                    return;
                }
                // 进行了金币扣除操作
                if (PointsEdit.onPointsEdit(player, RandomEntry.getPluginMain().getConfig().getInt("PointsButton.Consume"))) {
                    if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("点券扣除成功，正在进行后续操作");
                    LoreProcess.onStringProcess(player, false);
                } else {
                    if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("点券扣除失败，进行玩家提示");
                    player.sendRawMessage(RandomEntry.Message.getString("Failure"));
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        // RIGHT_CLICK_AIR 右键空气
        // HAND 主手 / OFF_HAND 非主手
        // TODO 虽然这部分已经做了异步处理，但是并没有运行。等待完成逻辑编写。
        Thread thread = new Thread(() -> {
            Player player = event.getPlayer();
            if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                String PointsLore = RandomEntry.getPluginMain().getConfig().getString("ItemLore.Points");
                String VaultLore = RandomEntry.getPluginMain().getConfig().getString("ItemLore.Vault");
                int i = 0;
                for (String s : event.getItem().getItemMeta().getLore()) {
                    if (s.equalsIgnoreCase(VaultLore)) {
                        i = 1;
                    }
                    if (s.equalsIgnoreCase(PointsLore)) {
                        i = 2;
                    }
                    switch (i) {
                        // 执行金币鉴定流程
                        case 1: {
                            if (!LoreProcess.getKeyInfo(player, true)) {
                                if (RandomEntry.Debug)
                                    RandomEntry.getPluginMain().getLogger().info("未检测到指定Lore，进行终止操作。");
                                return;
                            }
                            if (VaultEdit.onVaultEdit(player, RandomEntry.getPluginMain().getConfig().getInt("VaultButton.Consume"))) {
                                if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("金币扣除成功，正在进行后续操作");
                                LoreProcess.onStringProcess(player, true);
                            } else {
                                if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("金币扣除失败，进行玩家提示");
                                player.sendRawMessage(RandomEntry.Message.getString("Failure"));
                            }
                            return;
                        }
                        // 执行点券鉴定流程
                        case 2: {
                            if (!LoreProcess.getKeyInfo(player, false)) {
                                if (RandomEntry.Debug)
                                    RandomEntry.getPluginMain().getLogger().info("未检测到指定Lore，进行终止操作。");
                                return;
                            }
                            // 进行了金币扣除操作
                            if (PointsEdit.onPointsEdit(player, RandomEntry.getPluginMain().getConfig().getInt("VaultButton.Consume"))) {
                                if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("点券扣除成功，正在进行后续操作");
                                LoreProcess.onStringProcess(player, false);
                            } else {
                                if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("点券扣除失败，进行玩家提示");
                                player.sendRawMessage(RandomEntry.Message.getString("Failure"));
                            }
                            return;
                        }
                    }
                }
                event.getPlayer().sendRawMessage("没有可以进行鉴定的物品");
            }
        });
    }
}
