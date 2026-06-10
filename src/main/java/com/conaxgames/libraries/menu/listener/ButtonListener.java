package com.conaxgames.libraries.menu.listener;

import com.conaxgames.libraries.menu.Menu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public final class ButtonListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onClick(InventoryClickEvent event) {
        Menu.handleClick(event);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Menu.handleClose(event);
    }
}
