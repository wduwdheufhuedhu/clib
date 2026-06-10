package com.conaxgames.libraries.menu.listener;

import com.conaxgames.libraries.LibraryPlugin;
import com.conaxgames.libraries.menu.Button;
import com.conaxgames.libraries.menu.Menu;
import com.conaxgames.libraries.menu.MenuInventoryHolder;
import com.cryptomorin.xseries.inventory.XInventoryView;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public final class ButtonListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }
        Inventory top = XInventoryView.of(event.getView()).getTopInventory();
        if (!(top.getHolder() instanceof MenuInventoryHolder holder) || !holder.getViewerId().equals(player.getUniqueId())) {
            return;
        }
        event.setCancelled(true);

        if (event.getRawSlot() != event.getSlot()) {
            return;
        }

        Button button = holder.getButton(event.getSlot());
        if (button == null) {
            return;
        }

        Menu menu = holder.getMenu();
        button.click(player, event.getClick());
        if (Menu.opened(player) == menu && menu.updateAfterClick()) {
            menu.update(player);
        }
        LibraryPlugin.getInstance().getScheduler().runTaskLater(
                LibraryPlugin.getInstance().getPlugin(),
                player::updateInventory,
                1L
        );
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) {
            return;
        }
        if (!(event.getInventory().getHolder() instanceof MenuInventoryHolder holder)) {
            return;
        }
        UUID id = player.getUniqueId();
        if (!holder.getViewerId().equals(id)) {
            return;
        }
        Menu closed = holder.getMenu();
        closed.closed(player);
        Menu.cancelCheck(player);
        Menu.currentlyOpenedMenus.remove(id);

        Menu previous = closed.previous();
        if (previous != null) {
            LibraryPlugin.getInstance().getScheduler().runTaskLater(
                    LibraryPlugin.getInstance().getPlugin(),
                    () -> {
                        if (Menu.opened(player) == null) {
                            previous.open(player);
                        }
                    },
                    2L
            );
        }
    }
}
