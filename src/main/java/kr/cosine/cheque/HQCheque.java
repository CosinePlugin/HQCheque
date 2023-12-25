package kr.cosine.cheque;

import kr.cosine.cheque.command.ChequeAdminCommand;
import kr.cosine.cheque.command.ChequeUserCommand;
import kr.cosine.cheque.config.SettingConfig;
import kr.cosine.cheque.listener.ChequeListener;
import kr.cosine.cheque.service.ChequeService;
import kr.cosine.cheque.service.EconomyService;
import org.bukkit.plugin.java.JavaPlugin;

public class HQCheque extends JavaPlugin {

    @Override
    public void onEnable() {
        SettingConfig settingConfig = new SettingConfig(this);
        settingConfig.load();

        EconomyService economyService = new EconomyService(getServer());
        ChequeService chequeService = new ChequeService(settingConfig, economyService);

        getServer().getPluginManager().registerEvents(new ChequeListener(chequeService), this);

        getCommand("수표").setExecutor(new ChequeUserCommand(chequeService));
        getCommand("수표관리").setExecutor(new ChequeAdminCommand(settingConfig, chequeService));
    }
}
