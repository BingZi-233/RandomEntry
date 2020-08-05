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

public class RandomEntryCommand implements CommandExecutor {
    private static Economy econ = null;
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        long startTime = System.currentTimeMillis();
//        if ("help".equalsIgnoreCase(args[0])){
//            for (String s : RandomEntry.Message.getStringList("Help")) {
//                sender.sendMessage(s.replace("&", "§"));
//            }
//        }
        switch (args[0]){
            case "open":{
                if (sender instanceof Player){
                    Player player = (Player) sender;
                    ViewGUi(player);
                    // 扣除操作
//                    EconomyResponse response = econ.depositPlayer(player,-1);
//                    if (response.transactionSuccess()){
//
//                    }

                }else{
                    sender.sendMessage(RandomEntry.Message.getString("Backstage").replace("&","§"));
                }
                break;
            }
            case "reload":{
                RandomEntry.Message = YamlConfiguration.loadConfiguration(new File(RandomEntry.getPluginMain().getDataFolder(),"Message.yml"));
                RandomEntry.Entry = YamlConfiguration.loadConfiguration(new File(RandomEntry.getPluginMain().getDataFolder(),"Entry.yml"));
                RandomEntry.getPluginMain().reloadConfig();
                RandomEntry.Debug = RandomEntry.getPluginMain().getConfig().getBoolean("Debug");
                break;
            }
            default:{
                for (String s : RandomEntry.Message.getStringList("Help")) {
                    sender.sendMessage(s.replace("&", "§"));
                }
            }
        }
        long endTime = System.currentTimeMillis();
        if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("程序执行命令耗时 "+(endTime-startTime)+" 毫秒");
        return true;
    }
    public static Economy getEconomy() {
        return econ;
    }
    public static void ViewGUi(Player p){
        Inventory inventory = Bukkit.createInventory(p,45,RandomEntry.getPluginMain().getConfig().getString("ViewTitle"));
        ItemStack itemStack = new ItemStack(Material.valueOf(RandomEntry.getPluginMain().getConfig().getString("Item.Mats")));
        ItemMeta meta = itemStack.getItemMeta();
        meta.setLore(RandomEntry.getPluginMain().getConfig().getStringList("Item.Lore"));
        meta.setDisplayName(RandomEntry.getPluginMain().getConfig().getString("Item.Name"));
        itemStack.setItemMeta(meta);
        for (int i = 0; i < 45; i++){
            if (i<=9||i==17||i==18||i==26||i==27||i==35||i>=36){
                inventory.setItem(i,itemStack);
            }
        }
        p.openInventory(inventory);
    }

}
