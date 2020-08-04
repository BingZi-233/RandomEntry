package vip.bingzi.randomentry.util;

import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import vip.bingzi.randomentry.RandomEntry;

public class Command extends JavaPlugin {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        switch (args[0]){
            case "open":{
                if (sender instanceof Player){

                }else{
                    sender.sendMessage(RandomEntry.Message.getString("Backstage").replace("&","§"));
                }
            }
            case "help": // 与default进行了合并
            default:{
                for (String s : RandomEntry.Message.getStringList("Help")) {
                    sender.sendMessage(s.replace("&", "§"));
                }
                break;
            }
        }
        return true;
    }
}
