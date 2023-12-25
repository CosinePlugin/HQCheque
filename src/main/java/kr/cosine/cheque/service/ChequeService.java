package kr.cosine.cheque.service;

import kr.cosine.cheque.config.SettingConfig;
import kr.cosine.cheque.data.Cheque;
import kr.cosine.cheque.enums.Notice;
import kr.cosine.cheque.util.FormatUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ChequeService {

    private final SettingConfig settingConfig;
    private final EconomyService economyService;

    public ChequeService(SettingConfig settingConfig, EconomyService economyService) {
        this.settingConfig = settingConfig;
        this.economyService = economyService;
    }

    public boolean useCheque(Player player, ItemStack itemStack) {
        long money = getCheque().getMoney(itemStack);
        if (money <= 0.0) return false;
        economyService.deposit(player, money);
        itemStack.setAmount(itemStack.getAmount() - 1);
        player.sendMessage(Notice.USE_CHEQUE.replace("%money%", FormatUtils.format(money)));
        return true;
    }

    public String issueCheque(Player player, String moneyText, boolean moneyCheck) {
        try {
            long money = Long.parseLong(moneyText);
            return issueCheque(player, money, moneyCheck);
        } catch (NumberFormatException e) {
            return Notice.INPUT_LONG.message;
        }
    }

    public String issueCheque(Player player, long money, boolean moneyCheck) {
        if (money <= 0L) {
            return Notice.INPUT_POSITIVE_LONG.message;
        }
        if (moneyCheck && !economyService.hasMoney(player, money)) {
            return Notice.LACK_MONEY.message;
        }
        PlayerInventory playerInventory = player.getInventory();
        ItemStack itemStack = getCheque().toItemStack(money);
        if (!isAddable(playerInventory, itemStack)) {
            return Notice.INVENTORY_FULL.message;
        }
        if (moneyCheck) {
            economyService.withdraw(player, money);
        }
        playerInventory.addItem(itemStack);
        return Notice.ISSUE_CHEQUE.replace("%money%", FormatUtils.format(money));
    }

    private boolean isAddable(PlayerInventory inventory, ItemStack itemStack) {
        for (ItemStack loopItemStack : inventory.getStorageContents()) {
            if (loopItemStack == null ||
                loopItemStack.getType() == Material.AIR ||
                (loopItemStack.isSimilar(itemStack) && loopItemStack.getAmount() + itemStack.getAmount() <= loopItemStack.getMaxStackSize())
            ) {
                return true;
            }
        }
        return false;
    }

    private Cheque getCheque() {
        return settingConfig.getCheque();
    }
}
