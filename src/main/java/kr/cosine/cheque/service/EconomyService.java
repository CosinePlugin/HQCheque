package kr.cosine.cheque.service;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.logging.Logger;

public class EconomyService {

    private Economy economy;

    public EconomyService(Server server) {
        Logger logger = server.getLogger();
        if (server.getPluginManager().getPlugin("Vault") == null) {
            logger.warning("Vault를 찾을 수 없습니다.");
            return;
        }
        RegisteredServiceProvider<Economy> rsp = server.getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            logger.warning("Vault의 Provider를 찾을 수 없습니다.");
            return;
        }
        economy = rsp.getProvider();
    }

    private double getMoney(Player player) {
        return economy.getBalance(player);
    }

    public boolean hasMoney(Player player, long money) {
        return getMoney(player) >= money;
    }

    public void deposit(Player player, long money) {
        economy.depositPlayer(player, money);
    }

    public void withdraw(Player player, long money) {
        economy.withdrawPlayer(player, money);
    }
}
