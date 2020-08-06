package vip.bingzi.randomentry.util;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import vip.bingzi.randomentry.RandomEntry;

public class VaultEdit {
    private static Economy econ = null;
    public static boolean onVaultEdit(Player player ,int Value){
        if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("被扣除的玩家ID:"+player.getName());
        if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("即将扣除的金币为:"+Value);
        EconomyResponse response = econ.withdrawPlayer(player, Value);
        if (RandomEntry.Debug) RandomEntry.getPluginMain().getLogger().info("执行返回状态:"+response.transactionSuccess());
        return response.transactionSuccess();
    }
    public static Economy getEconomy() {
        return econ;
    }
    public static void setupEconomy(){
        RegisteredServiceProvider< Economy > rsp = RandomEntry.getPluginMain().getServer().getServicesManager().getRegistration(Economy.class);
        econ = rsp.getProvider();
    }
}
