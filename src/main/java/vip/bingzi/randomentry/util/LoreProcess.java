package vip.bingzi.randomentry.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import vip.bingzi.randomentry.RandomEntry;

import java.text.MessageFormat;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class LoreProcess {
    private static Random random = new Random();
    public static void onStringProcess(Player player,boolean VoP){
        String VaultEntryKey = null;
        // 对玩家背包进行刷新操作
        player.updateInventory();
        // 获取玩家主手物品Stack
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        // 获取Meta信息
        ItemMeta itemMeta = itemInMainHand.getItemMeta();
        // 获取物品Lore
        List<String> lore = itemMeta.getLore();
        // 获取金币鉴定目录下的所有Key，之后进行读取操作
        Set<String> keys;
        if (VoP){
            keys = RandomEntry.VaultEntry.getKeys(false);
            if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("金币随机"); // 调试输出
        }else {
            keys = RandomEntry.PointsEntry.getKeys(false);
            if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("点券随机"); // 调试输出
        }
        if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("获取到的Keys："+keys); // 调试输出
        int a = -1;
        for (String s : keys){
            a = lore.indexOf(RandomEntry.VaultEntry.getString(s+".IdentifyLore"));
            if (a != -1){
                VaultEntryKey = s;
                if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("获取到了Key:"+VaultEntryKey+"位置信息："+a);
                break;
            }
        }
        // 如果在这里VaultEntryKey依旧为null，则直接中断处理流程并且给玩家发送一条消息。
        if (VaultEntryKey == null){
            player.sendRawMessage(RandomEntry.Message.getString("NoKey").replace("&","§"));
            return;
        }
        List<String> stringList;
        if (VoP){
            stringList = RandomEntry.VaultEntry.getStringList(VaultEntryKey + ".RandomList");
        }else{
            stringList = RandomEntry.PointsEntry.getStringList(VaultEntryKey + ".RandomList");
        }
        // 随机出来一个数字，在RandomList的范围内
        int i = random.nextInt(stringList.size());
        // 获取到需要进行进一步处理的词条
        String RandomLore = stringList.get(i);
        // 在指定位置进行替换
        lore.set(a,Split(RandomLore));
        // 设置Lore
        itemMeta.setLore(lore);
        // 设置物品
        itemInMainHand.setItemMeta(itemMeta);
        // 给予玩家物品
        player.getInventory().setItemInMainHand(itemInMainHand);
    }
    private static String Split(String RandomLore){
        String[] s = RandomLore.split("!");
        if (RandomEntry.Debug){ // 调试输出
            for (int e = 0; e < s.length; e++){
                RandomEntry.getPluginMain().getLogger().info(MessageFormat.format("第{0}个 - {1}", e,s[e]));
            }
        }
        // 获取两个需要随机的范围
        String[] Min = s[1].split("~");
        String[] Max = s[3].split("~");
        if (Max.length != 0){
            if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info(MessageFormat.format("{0}{1}{2}{3}",s[0],RandomValue(Min,Max)[0],s[2],RandomValue(Min,Max)[1]));
            return MessageFormat.format("{0}{1}{2}{3}",s[0],RandomValue(Min,Max)[0],s[2],RandomValue(Min,Max)[1]);
        } if (Min.length != 0){
            if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info(MessageFormat.format("{0}{1}",s[0],RandomValue(Min)[0]));
            return MessageFormat.format("{0}{1}",s[0],RandomValue(Min,Max)[0]);
        } else{
            if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info(MessageFormat.format("{0}",s[0]));
            return MessageFormat.format("{0}",s[0]);
        }
    }
    private static int[] RandomValue(String[] Min, String[] Max) {
        int[] Value = new int[2];
        Value[0] = random.nextInt(Integer.parseInt(Min[0])) % (Integer.parseInt(Min[1]) - Integer.parseInt(Min[0]) + 1) + Integer.parseInt(Min[0]);
        Value[1] = random.nextInt(Integer.parseInt(Max[0])) % (Integer.parseInt(Max[1]) - Integer.parseInt(Max[0]) + 1) + Integer.parseInt(Max[0]);
        return Value;
    }
    private static int[] RandomValue(String[] Min) {
        int[] Value = new int[1];
        Value[0] = random.nextInt(Integer.parseInt(Min[0])) % (Integer.parseInt(Min[1]) - Integer.parseInt(Min[0]) + 1) + Integer.parseInt(Min[0]);
        return Value;
    }

    public static boolean getKeyInfo(Player player,boolean VoP){
        String VaultEntryKey;
        // 对玩家背包进行刷新操作
        player.updateInventory();
        // 获取玩家主手物品Stack
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        // 获取Meta信息
        ItemMeta itemMeta = itemInMainHand.getItemMeta();
        // 获取lore
        List<String> lore = itemMeta.getLore();
        // 获取金币鉴定目录下的所有Key，之后进行读取操作
        Set<String> keys;
        if (VoP){
            keys = RandomEntry.VaultEntry.getKeys(false);
            if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("金币随机"); // 调试输出
        }else {
            keys = RandomEntry.PointsEntry.getKeys(false);
            if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("点券随机"); // 调试输出
        }
        if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("获取到的Keys："+keys); // 调试输出
        int a = -1;
        for (String s : keys){
            a = lore.indexOf(RandomEntry.VaultEntry.getString(s+".IdentifyLore"));
            if (a != -1){
                VaultEntryKey = s;
                if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("获取到了Key:"+VaultEntryKey+"位置信息："+a);
                return true;
            }
        }
        if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("未在所有文件中找到指定项目");
        return false;
    }
}
