package kr.cosine.cheque.data;

import kr.cosine.cheque.enums.Parse;
import kr.cosine.cheque.util.FormatUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;

public class Cheque {

    private static final String KEY = "HQCheque:";

    private final Parse parse;

    private final Material material;

    private final String displayName;

    private final List<String> lore;

    public Cheque(Parse parse, Material material, String displayName, List<String> lore) {
        this.parse = parse;
        this.material = material;
        this.displayName = displayName;
        this.lore = lore;
    }

    public ItemStack toItemStack(long money) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        String displayName = this.displayName;
        if (parse == Parse.DISPLAY) {
            displayName = parse(displayName, money);
        }
        List<String> lore = this.lore;
        if (parse == Parse.LORE) {
            lore = lore.stream()
                .map(line -> parse(line, money))
                .collect(Collectors.toList());
        }
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(lore);
        itemMeta.setLocalizedName(KEY + money);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private String parse(String text, long money) {
        return text.replace("%money%", FormatUtils.format(money));
    }

    public long getMoney(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        String text = itemMeta.getLocalizedName();
        if (text.isEmpty()) return 0L;
        if (!text.contains(KEY)) return 0L;
        try {
            String moneyText = text.replace(KEY, "");
            return Long.parseLong(moneyText);
        } catch (NumberFormatException e) {
            return 0L;
        }
    }
}
