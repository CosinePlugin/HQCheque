package kr.cosine.cheque.command;

import kr.cosine.cheque.config.SettingConfig;
import kr.cosine.cheque.enums.Notice;
import kr.cosine.cheque.service.ChequeService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class ChequeAdminCommand implements CommandExecutor, TabCompleter {

    private final SettingConfig settingConfig;
    private final ChequeService chequeService;

    public ChequeAdminCommand(SettingConfig settingConfig, ChequeService chequeService) {
        this.settingConfig = settingConfig;
        this.chequeService = chequeService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            printHelp(sender);
            return true;
        }
        checker(sender, args);
        return true;
    }

    private void printHelp(CommandSender sender) {
        sender.sendMessage("§6[ /수표관리 발행 §7[금액] ] §8- §f보유 중인 돈과 관계 없이 수표를 발행합니다.");
        sender.sendMessage("§6[ /수표관리 리로드 ] §8- §fconfig.yml을 리로드합니다.");
    }

    private void checker(CommandSender sender, String[] args) {
        switch (args[0]) {
            case "발행": {
                if (!(sender instanceof Player)) {
                    sender.sendMessage("§c콘솔에서 사용할 수 없는 명령어입니다.");
                    return;
                }
                issueCheque((Player) sender, args);
                return;
            }
            case "리로드": {
                reload(sender);
                return;
            }
            default: printHelp(sender);
        }
    }

    private void issueCheque(Player player, String[] args) {
        if (args.length == 1) {
            player.sendMessage(Notice.INPUT_MONEY.message);
            return;
        }
        String moneyText = args[1];
        String message = chequeService.issueCheque(player, moneyText, false);
        player.sendMessage(message);
    }

    private void reload(CommandSender sender) {
        settingConfig.reload();
        sender.sendMessage("§aconfig.yml을 리로드하였습니다.");
    }

    private final List<String> commandTabList = Arrays.asList("발행", "리로드");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length <= 1) {
            return commandTabList;
        }
        return Collections.emptyList();
    }
}
