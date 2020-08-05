package vip.bingzi.randomentry;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import vip.bingzi.randomentry.util.RandomEntryCommand;
import vip.bingzi.randomentry.util.RandomEntryInv;

import java.io.File;

public final class RandomEntry extends JavaPlugin {
    public static YamlConfiguration Entry;
    public static YamlConfiguration Message;
    public static RandomEntry PluginMain;
    public static boolean Debug;

    public static RandomEntry getPluginMain() {
        return PluginMain;
    }

    @Override
    public void onLoad() {
        long startTime = System.currentTimeMillis();
        File fileConfig = new File(getDataFolder(), "config.yml");
        File fileEntry = new File(getDataFolder(), "Entry.yml");
        File fileMessage = new File(getDataFolder(), "Message.yml");
        onFileExamine(fileConfig, true, "配置文件");
        onFileExamine(fileEntry, false, "随机文件");
        onFileExamine(fileMessage, false, "语言文件");
        Entry = YamlConfiguration.loadConfiguration(fileEntry);
        Message = YamlConfiguration.loadConfiguration(fileMessage);
        Debug = getConfig().getBoolean("Debug");
        long endTime = System.currentTimeMillis();
        if (Debug) getLogger().info("程序执行加载耗时 "+(endTime-startTime)+" 毫秒");
    }

    @Override
    public void onEnable() {
        long startTime = System.currentTimeMillis();
        PluginMain = this;
        Bukkit.getPluginManager().registerEvents(new RandomEntryInv(),this);
        Bukkit.getPluginCommand("RandomEntry").setExecutor(new RandomEntryCommand());
        long endTime = System.currentTimeMillis();
        if (Debug) getLogger().info("程序执行启动耗时 "+(endTime-startTime)+" 毫秒");
    }

    @Override
    public void onDisable() {

    }

    private void onFileExamine(File file, Boolean isConfig, String fileName) {
        // 检查文件是否存在
        if (file.exists()) {
            getLogger().info("[ " + fileName + " ]已经存在，正在检查版本中...");
            // 只有配置文件才需要检查这个
            if (isConfig) {
                if (getDescription().getVersion().equalsIgnoreCase(getConfig().getString("Version"))) {
                    getLogger().info("[ " + fileName + " ]版本与插件版本一致！");
                } else {
                    getLogger().warning("[ " + fileName + " ]版本与插件版本不一致！");
                    if (getConfig().getBoolean("AutoUpDate")) {
                        getLogger().info("根据用户设定，正在进行自动更换为新版[ " + fileName + " ]");
                        // 抑制对delete()方法检查
                        //noinspection ResultOfMethodCallIgnored
                        file.delete();
                        saveDefaultConfig();
                        reloadConfig();
                        getLogger().info("[ " + fileName + " ]更换完毕");
                    } else {
                        getLogger().warning("根据用户设定，拒绝进行自动更换");
                        getLogger().warning("请到GitHub上查看并修改配置文件");
                        getLogger().warning("GitHub：https://github.com/BingZi-233/RandomEntry/blob/master/src/main/resources/config.yml");
                    }
                }
            }
        } else {
            getLogger().warning("未检测到[ " + fileName + " ]存在，正在释放默认配置文件。");
            if (isConfig) {
                saveDefaultConfig();
            } else {
                saveResource(file.getName(), false);
            }
            getLogger().info("[ " + fileName + " ]文件释放完成。");
        }
    }
}
