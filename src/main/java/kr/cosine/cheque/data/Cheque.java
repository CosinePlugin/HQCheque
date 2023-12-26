package kr.cosine.cheque.data;

import kr.cosine.cheque.enums.Parse;
import kr.cosine.cheque.util.FormatUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;

public class Cheque {

    private static final String KEY = "HQCheque:";

    private final int version;

    private final Parse parse;

    private final Material material;

    private final short durability;

    private final String displayName;

    private final List<String> lore;

    private final boolean unbreakable;

    private final ItemFlag[] itemFlags;

    private final int customModelData;

    public Cheque(
        int version,
        Parse parse,
        Material material,
        short durability,
        String displayName,
        List<String> lore,
        boolean unbreakable,
        ItemFlag[] itemFlags,
        int customModelData
    ) {
        this.version = version;
        this.parse = parse;
        this.material = material;
        this.durability = durability;
        this.displayName = displayName;
        this.lore = lore;
        this.unbreakable = unbreakable;
        this.itemFlags = itemFlags;
        this.customModelData = customModelData;
    }

    public ItemStack toItemStack(long money) {
        ItemStack itemStack = new ItemStack(material);
        if (durability != 0) {
            itemStack.setDurability(durability);
        }
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
        itemMeta.setUnbreakable(unbreakable);
        itemMeta.addItemFlags(itemFlags);
        if (version >= 14 && customModelData != 0) {
            itemMeta.setCustomModelData(customModelData);
        }
        itemMeta.setLocalizedName(KEY + money);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private String parse(String text, long money) {
        return text.replace("%money%", FormatUtils.format(money));
    }

    public long getMoney(ItemStack itemStack) {
        if (!itemStack.hasItemMeta()) return 0L;
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!itemMeta.hasLocalizedName()) return 0L;
        String text = itemMeta.getLocalizedName();
        if (text.isEmpty()) return 0L;
        if (!text.contains(KEY)) return 0L;
        try {
            String moneyText = text.replace(KEY, "");
            return Long.parseLong(moneyText);
        } catch (Exception e) {
            return 0L;
        }
    }
}
