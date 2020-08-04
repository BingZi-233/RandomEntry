package vip.bingzi.randomentry.util;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import vip.bingzi.randomentry.RandomEntry;

import java.io.File;

public class Command extends JavaPlugin {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        long startTime = System.currentTimeMillis();
        switch (args[0]){
            case "open":{
                if (sender instanceof Player){

                }else{
                    sender.sendMessage(RandomEntry.Message.getString("Backstage").replace("&","§"));
                }
                break;
            }
            case "reload":{
                RandomEntry.Message = YamlConfiguration.loadConfiguration(new File(getDataFolder(),"Message.yml"));
                RandomEntry.Entry = YamlConfiguration.loadConfiguration(new File(getDataFolder(),"Entry.yml"));
                RandomEntry.getPluginMain().reloadConfig();
                RandomEntry.Debug = RandomEntry.getPluginMain().getConfig().getBoolean("Debug");
                break;
            }
            default:{
                for (String s : RandomEntry.Message.getStringList("Help")) {
                    sender.sendMessage(s.replace("&", "§"));
                }
                break;
            }
        }
        long endTime = System.currentTimeMillis();
        if (RandomEntry.Debug) getLogger().info("程序执行命令耗时 "+(endTime-startTime)+" 毫秒");
        return true;
    }
}
