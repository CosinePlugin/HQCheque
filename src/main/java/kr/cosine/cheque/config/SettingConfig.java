package kr.cosine.cheque.config;

import kr.cosine.cheque.HQCheque;
import kr.cosine.cheque.data.Cheque;
import kr.cosine.cheque.enums.Notice;
import kr.cosine.cheque.enums.Parse;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemFlag;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SettingConfig {

    private static final String PATH = "config.yml";

    private final int version;
    private final Logger logger;

    private final File file;
    private final YamlConfiguration config;

    public SettingConfig(HQCheque plugin) {
        version = getVersion(plugin.getServer());
        logger = plugin.getLogger();
        File newFile = new File(plugin.getDataFolder(), PATH);
        if (!newFile.exists() && plugin.getResource(PATH) != null) {
            plugin.saveResource(PATH, false);
        }
        file = newFile;
        config = YamlConfiguration.loadConfiguration(newFile);
    }

    private Cheque cheque;

    public void load() {
        loadChequeSection();
        loadMessageSection();
    }

    private void loadChequeSection() {
        ConfigurationSection chequeSection = config.getConfigurationSection("cheque");
        if (chequeSection == null) return;
        Parse parse;
        try {
            String parserText = chequeSection.getString("parse", "DISPLAY");
            parse = Parse.getParse(parserText);
        } catch (IllegalArgumentException e) {
            parse = Parse.DISPLAY;
        }
        String materialText = chequeSection.getString("material", "PAPER");
        Material material = Material.getMaterial(materialText);
        if (material == null) {
            material = Material.PAPER;
        }
        short durability = (short) chequeSection.getInt("durability", 0);
        String displayName = applyColor(chequeSection.getString("display-name", "&e[수표] &f%money%원"));
        List<String> lore = chequeSection.getStringList("lore")
            .stream()
            .map(this::applyColor)
            .collect(Collectors.toList());
        boolean unbreakable = chequeSection.getBoolean("unbreakable", false);
        ItemFlag[] itemFlags = chequeSection.getStringList("item-flags")
            .stream()
            .map(ItemFlag::valueOf)
            .toArray(ItemFlag[]::new);
        int customModelData = chequeSection.getInt("custom-model-data", 0);
        cheque = new Cheque(version, parse, material, durability, displayName, lore, unbreakable, itemFlags, customModelData);
    }

    private int getVersion(Server server) {
        String version = server.getClass().getName().replace(".", ",").split(",")[3];
        Pattern pattern = Pattern.compile("v\\d+_(\\d+)_R\\d+");
        Matcher matcher = pattern.matcher(version);
        if (!matcher.find()) throw new NullPointerException();
        return Integer.parseInt(matcher.group(1));
    }

    private void loadMessageSection() {
        String messageSectionKey = "message";
        ConfigurationSection messageSection = config.getConfigurationSection(messageSectionKey);
        if (messageSection == null) return;
        for (String messageText : messageSection.getKeys(false)) {
            try {
                Notice notice = Notice.getNotice(messageText);
                notice.message = applyColor(messageSection.getString(messageText));
            } catch (IllegalArgumentException e) {
                logger.warning(messageSectionKey + "섹션에 " + messageText + "은(는) 존재하지 않는 Message입니다.");
            }
        }
    }

    private String applyColor(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public void reload() {
        try {
            config.load(file);
            load();
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public Cheque getCheque() {
        return cheque;
    }
}
