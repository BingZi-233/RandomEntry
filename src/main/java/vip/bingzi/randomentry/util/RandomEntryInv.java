package vip.bingzi.randomentry.util;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import vip.bingzi.randomentry.RandomEntry;

import java.text.MessageFormat;
import java.util.List;
import java.util.Random;

public class RandomEntryInv implements Listener {
    private static int[] RandomValue(String[] Min, String[] Max) {
        Random random = new Random();
        int[] Value = new int[2];
        Value[0] = random.nextInt(Integer.parseInt(Min[0])) % (Integer.parseInt(Min[1]) - Integer.parseInt(Min[0]) + 1) + Integer.parseInt(Min[0]);
        Value[1] = random.nextInt(Integer.parseInt(Max[0])) % (Integer.parseInt(Max[1]) - Integer.parseInt(Max[0]) + 1) + Integer.parseInt(Max[0]);
        return Value;
    }

    private static int[] RandomValue(String[] Min) {
        Random random = new Random();
        int[] Value = new int[1];
        Value[0] = random.nextInt(Integer.parseInt(Min[0])) % (Integer.parseInt(Min[1]) - Integer.parseInt(Min[0]) + 1) + Integer.parseInt(Min[0]);
        return Value;
    }

    @EventHandler
    public void onPlayerOpenGui(InventoryClickEvent event) {
        // 检查是不是鉴定界面
        // 这里使用了OpenInfo全局参数进行了简化判断的过程
        if (RandomEntry.getPluginMain().getConfig().getString("ViewTitle").equalsIgnoreCase(event.getView().getTitle())) {
            Player player = (Player) event.getView().getPlayer();
            if (RandomEntry.Debug) {
                RandomEntry.getPluginMain().getLogger().info("玩家" + event.getView().getPlayer().getName() + "点击了鉴定界面");
            }
            event.setCancelled(true);
            if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("已经取消点击");
            if (event.getCurrentItem() == null) return;
            if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(RandomEntry.getPluginMain().getConfig().getString("VaultButton.Name"))) {
                if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("点击了金币按钮，进行金币扣除操作");
                if (VaultEdit.onVaultEdit(player, RandomEntry.getPluginMain().getConfig().getInt("VaultButton.Consume"))) {
                    if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("金币扣除成功，正在进行后续操作");
                    // 进行玩家背包刷新
                    player.updateInventory();
                    // 获取玩家主手物品的lore
                    ItemStack itemStack = player.getInventory().getItemInMainHand();
//                    List<String> listLore = player.getInventory().getItemInMainHand().getItemMeta().getLore();
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    List<String> listLore = itemMeta.getLore();
                    int a = listLore.indexOf(RandomEntry.VaultEntry.getString("AttackAffix.IdentifyLore"));
                    // 查看该物品是否有指定的lore需要被替换
                    if (listLore.contains(RandomEntry.VaultEntry.getString("AttackAffix.IdentifyLore"))) {
                        if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("找到了要被替换的Lore,位置"+a);
                    } else {
                        if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("没有要被替换的Lore");
                    }

                    Random random = new Random();
                    List<String> s = RandomEntry.getPluginMain().VaultEntry.getStringList("AttackAffix.RandomList");
                    if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("获取到的列表为：" + s);
                    int i = random.nextInt(s.size());
                    String s1 = s.get(i);
                    if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("获取到的Lore为" + s1);
                    String[] s2 = s1.split("!");
                    if (RandomEntry.Debug) {
                        for (int e = 0; e < s2.length; e++) {
                            RandomEntry.getPluginMain().getLogger().info(MessageFormat.format("第{0}个被分割出来的字符为:{1}", e, s2[e]));
                        }
                    }
                    String[] Min = s2[1].split("~");
                    String[] Max = s2[3].split("~");
                    if (Max.length == 0) {
                        RandomEntry.getPluginMain().getLogger().info(MessageFormat.format("{0}{1}", s2[0], RandomValue(Min)[0]));
                        listLore.set(a,MessageFormat.format("{0}{1}", s2[0], RandomValue(Min)[0]));
                    } else {
                        RandomEntry.getPluginMain().getLogger().info(MessageFormat.format("{0}{1}{2}{3}", s2[0], RandomValue(Min, Max)[0], s2[2], RandomValue(Min, Max)[1]));
                        listLore.set(a,MessageFormat.format("{0}{1}{2}{3}", s2[0], RandomValue(Min, Max)[0], s2[2], RandomValue(Min, Max)[1]));
                    }
                    // 完成替换
                    itemMeta.setLore(listLore);
                    itemStack.setItemMeta(itemMeta);
                    player.getInventory().setItemInMainHand(itemStack);
                } else {
                    if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("金币扣除失败，进行玩家提示");
                    player.sendRawMessage(RandomEntry.Message.getString("Failure"));
                }
                return;
            }
            if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(RandomEntry.getPluginMain().getConfig().getString("PointsButton.Name"))) {
                if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("点击了点券按钮");
            }
        }
    }

}
