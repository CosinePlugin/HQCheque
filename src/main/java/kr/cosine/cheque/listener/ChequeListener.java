package kr.cosine.cheque.listener;

import kr.cosine.cheque.service.ChequeService;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class ChequeListener implements Listener {

    private static final String RIGHT_CLICK = "RIGHT_CLICK";

    private final ChequeService chequeService;

    public ChequeListener(ChequeService chequeService) {
        this.chequeService = chequeService;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (!event.getAction().name().contains(RIGHT_CLICK)) return;
        ItemStack itemStack = event.getItem();
        if (itemStack == null) return;
        if (itemStack.getType() == Material.AIR) return;
        Player player = event.getPlayer();
        if (chequeService.useCheque(player, itemStack)) {
            event.setCancelled(true);
        }
    }
}
