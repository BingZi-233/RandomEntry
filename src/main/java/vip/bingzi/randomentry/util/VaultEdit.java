package vip.bingzi.randomentry.util;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;

public class VaultEdit {
    private static Economy econ;
    public boolean onVaultEdit(Player player ,int Value){
        EconomyResponse response = econ.depositPlayer(player,-1);
        return response.transactionSuccess();
    }
    public static Economy getEconomy() {
        return econ;
    }
}
