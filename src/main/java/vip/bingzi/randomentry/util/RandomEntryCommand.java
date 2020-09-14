package vip.bingzi.randomentry.util;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import vip.bingzi.randomentry.RandomEntry;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static vip.bingzi.randomentry.RandomEntry.Debug;
import static vip.bingzi.randomentry.RandomEntry.PluginMain;

public class RandomEntryCommand implements CommandExecutor {
    private static Economy econ = null;
    private static Inventory ViewGUi;
    //开服时加载gui不必每次都加载
    public static void loadGUI(){
        Inventory inventory = Bukkit.createInventory(null,45,RandomEntry.getPluginMain().getConfig().getString("ViewTitle"));
        boolean Version = isVersion();
        if (Debug) PluginMain.getLogger().info("当前正在构建GUI第一个所需物品");
        ItemStack itemStack = onItemStack(
                PluginMain.getConfig().getString("Item.Mats"),
                PluginMain.getConfig().getStringList("Item.Lore"),
                PluginMain.getConfig().getString("Item.Name"),
                Version
        );
        ItemMeta meta = itemStack.getItemMeta();
        meta.setLore(RandomEntry.getPluginMain().getConfig().getStringList("Item.Lore"));
        meta.setDisplayName(RandomEntry.getPluginMain().getConfig().getString("Item.Name"));

        itemStack.setItemMeta(meta);
        for (int i = 0; i < 45; i++){
            if (i<=9||i==17||i==18||i==26||i==27||i==35||i>=36){
                inventory.setItem(i,itemStack);
            }
        }
        if (Debug) PluginMain.getLogger().info("当前正在构建GUI第二个所需物品");
        ItemStack VaultButton = onItemStack(
                RandomEntry.getPluginMain().getConfig().getString("VaultButton.Mats"),
                RandomEntry.getPluginMain().getConfig().getStringList("VaultButton.Lore"),
                RandomEntry.getPluginMain().getConfig().getString("VaultButton.Name"),
                Version
        );
        if (Debug) PluginMain.getLogger().info("当前正在构建GUI第三个所需物品");
        ItemStack PointsButton = onItemStack(
                RandomEntry.getPluginMain().getConfig().getString("PointsButton.Mats"),
                RandomEntry.getPluginMain().getConfig().getStringList("PointsButton.Lore"),
                RandomEntry.getPluginMain().getConfig().getString("PointsButton.Name"),
                Version
        );
        inventory.setItem(20,VaultButton);
        inventory.setItem(24,PointsButton);
        ViewGUi= inventory;
    }

    private static boolean isVersion() {
        ArrayList<String> listVersion = new ArrayList<String>(){{
            add("1.12");
            add("1.11");
            add("1.10");
            add("1.9");
            add("1.8");
            add("1.7");
        }};
        // true 表示旧版，false表示新版
        boolean Version = false;
        for (String s: listVersion){
            if (RandomEntry.getPluginMain().getServer().getBukkitVersion().contains(s)){
                Version = true;
            }
        }
        return Version;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        long startTime = System.currentTimeMillis();
        if (args.length == 0){
            for (String s : RandomEntry.Message.getStringList("Help")) {
                sender.sendMessage(s.replace("&", "§"));
            }
            return true;
        }
        switch (args[0]){
            case "open":{
                if (sender instanceof Player){
                    Player player = (Player) sender;
                    ViewGUi(player);

                }else{
                    sender.sendMessage(RandomEntry.Message.getString("Backstage").replace("&","§"));
                }
                break;
            }
            case "test":{
                LoreProcess.onStringProcess((Player)sender,false);
            }
            case "reload":{
                sender.sendMessage("§a§l[ RandomEntry ] >> 重载完成");
                RandomEntry.Message = YamlConfiguration.loadConfiguration(new File(RandomEntry.getPluginMain().getDataFolder(),"Message.yml"));
                RandomEntry.VaultEntry = YamlConfiguration.loadConfiguration(new File(RandomEntry.getPluginMain().getDataFolder(), "VaultEntry.yml"));
                RandomEntry.getPluginMain().reloadConfig();
                RandomEntryCommand.loadGUI();
                Debug = RandomEntry.getPluginMain().getConfig().getBoolean("Debug");
                break;
            }
            default:{
                for (String s : RandomEntry.Message.getStringList("Help")) {
                    sender.sendMessage(s.replace("&", "§"));
                }
            }
        }
        long endTime = System.currentTimeMillis();
        if (Debug) RandomEntry.getPluginMain().getLogger().info("程序执行命令耗时 "+(endTime-startTime)+" 毫秒");
        return true;
    }
    public static Economy getEconomy() {
        return econ;
    }
    public static void ViewGUi(Player p){
        p.openInventory(ViewGUi);
    }
    private static ItemStack onItemStack(String material, List<String> Lore, String DisplayName,boolean Version){
        if (Debug) PluginMain.getLogger().info("第一个参数："+material);
        if (Debug) PluginMain.getLogger().info("第二个参数："+Lore);
        if (Debug) PluginMain.getLogger().info("第三个参数："+DisplayName);
        ItemStack itemStack;
        if (Version){
            itemStack = new ItemStack(RandomEntry.getPluginMain().getConfig().getInt(material));
        }else {
            itemStack = new ItemStack(Material.valueOf(material));
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(Lore);
        itemMeta.setDisplayName(DisplayName);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
