package kr.cosine.cheque.command;

import kr.cosine.cheque.enums.Notice;
import kr.cosine.cheque.service.ChequeService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChequeUserCommand implements CommandExecutor {

    private final ChequeService chequeService;

    public ChequeUserCommand(ChequeService chequeService) {
        this.chequeService = chequeService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c콘솔에서 사용할 수 없는 명령어입니다.");
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            player.sendMessage(Notice.INPUT_MONEY.message);
            return true;
        }
        String moneyText = args[0];
        String message = chequeService.issueCheque(player, moneyText, true);
        player.sendMessage(message);
        return true;
    }
}
