package com.conaxgames.libraries.menu;

import com.conaxgames.libraries.LibraryPlugin;
import com.conaxgames.libraries.event.impl.menu.MenuOpenEvent;
import com.conaxgames.libraries.menu.listener.ButtonListener;
import com.conaxgames.libraries.message.CC;
import com.conaxgames.libraries.util.scheduler.Scheduler;
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.inventory.XInventoryView;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public abstract class Menu {

    public static final Map<UUID, Menu> currentlyOpenedMenus = new ConcurrentHashMap<>();
    public static final Map<UUID, Scheduler.CancellableTask> checkTasks = new ConcurrentHashMap<>();
    private static final long MENU_UPDATE_DELAY_TICKS = 10L;
    private static final long MENU_UPDATE_PERIOD_TICKS = 20L;

    static {
        Bukkit.getServer().getPluginManager().registerEvents(new ButtonListener(), LibraryPlugin.getInstance().getPlugin());
    }

    private boolean autoUpdate = false;
    private boolean updateAfterClick = true;
    private boolean placeholder = false;
    private boolean noncancellingInventory = false;
    private String staticTitle = null;
    private Menu previous;

    protected Menu() {
    }

    protected Menu(String staticTitle) {
        this.staticTitle = Objects.requireNonNull(staticTitle, "staticTitle");
    }

    public static Menu getOpenMenu(Player player) {
        return currentlyOpenedMenus.get(player.getUniqueId());
    }

    public static void cancelCheck(Player player) {
        Scheduler.CancellableTask task = checkTasks.remove(player.getUniqueId());
        if (task != null) {
            task.cancel();
        }
    }

    public void openMenu(Player player) {
        openMenu(player, true);
    }

    public void openMenu(Player player, boolean firstOpen) {
        if (firstOpen) {
            MenuOpenEvent openEvent = new MenuOpenEvent(player, this);
            if (openEvent.call()) {
                return;
            }
        }

        if (Bukkit.isPrimaryThread()) {
            open(player);
        } else {
            LibraryPlugin.getInstance().getScheduler().runTask(LibraryPlugin.getInstance().getPlugin(), () -> open(player));
        }
    }

    private void open(Player player) {
        UUID id = player.getUniqueId();
        Inventory top = XInventoryView.of(player.getOpenInventory()).getTopInventory();

        if (refreshInPlaceWhenPossible() && top.getHolder() instanceof MenuInventoryHolder existing) {
            if (existing.getMenu() == this && existing.getViewerId().equals(id)) {
                Map<Integer, Button> defined = getButtons(player);
                int invSize = size(defined);
                if (top.getSize() == invSize) {
                    fillInventory(existing, player, defined, invSize);
                    beginSession(player, top);
                    return;
                }
            }
        }

        cancelCheck(player);
        Map<Integer, Button> defined = getButtons(player);
        int invSize = size(defined);
        MenuInventoryHolder holder = new MenuInventoryHolder(this, id);
        Inventory inv = Bukkit.createInventory(holder, invSize, getTitle(player));
        holder.attachInventory(inv);
        fillInventory(holder, player, defined, invSize);
        player.openInventory(inv);
        beginSession(player, inv);
    }

    protected boolean refreshInPlaceWhenPossible() {
        return true;
    }

    private void fillInventory(MenuInventoryHolder holder, Player player, Map<Integer, Button> defined, int invSize) {
        Map<Integer, Button> layout = new HashMap<>();
        for (Map.Entry<Integer, Button> e : defined.entrySet()) {
            int slot = e.getKey();
            if (slot >= 0 && slot < invSize) {
                layout.put(slot, e.getValue());
            }
        }
        if (this.placeholder) {
            Button filler = Button.placeholder(XMaterial.GRAY_STAINED_GLASS_PANE.get(), (byte) 7, CC.DARK_GRAY);
            for (int slot = 0; slot < invSize; slot++) {
                layout.putIfAbsent(slot, filler);
            }
        }
        holder.setSlotButtons(layout);
        Inventory inv = holder.getInventory();
        for (int slot = 0; slot < invSize; slot++) {
            Button b = layout.get(slot);
            inv.setItem(slot, b != null ? b.getButtonItem(player) : null);
        }
    }

    private void beginSession(Player player, Inventory inv) {
        UUID id = player.getUniqueId();
        cancelCheck(player);
        currentlyOpenedMenus.put(id, this);
        onOpen(player);

        if (!this.autoUpdate) {
            return;
        }

        Scheduler.CancellableTask task = LibraryPlugin.getInstance().getScheduler().runTaskTimerCancellable(
                LibraryPlugin.getInstance().getPlugin(),
                () -> {
                    if (!player.isOnline()) {
                        cancelCheck(player);
                        currentlyOpenedMenus.remove(id);
                        return;
                    }
                    if (!autoUpdate) {
                        return;
                    }
                    if (!(inv.getHolder() instanceof MenuInventoryHolder h)) {
                        cancelCheck(player);
                        return;
                    }
                    if (h.getMenu() != this || !h.getViewerId().equals(id)) {
                        return;
                    }
                    Map<Integer, Button> defined = getButtons(player);
                    int invSize = size(defined);
                    if (inv.getSize() != invSize) {
                        open(player);
                        return;
                    }
                    fillInventory(h, player, defined, invSize);
                },
                MENU_UPDATE_DELAY_TICKS,
                MENU_UPDATE_PERIOD_TICKS
        );
        checkTasks.put(id, task);
    }

    public void buttonUpdate(Player player) {
        Inventory top = XInventoryView.of(player.getOpenInventory()).getTopInventory();
        if (!(top.getHolder() instanceof MenuInventoryHolder h)) {
            return;
        }
        if (h.getMenu() != this || !h.getViewerId().equals(player.getUniqueId())) {
            return;
        }
        Map<Integer, Button> defined = getButtons(player);
        int invSize = size(defined);
        if (top.getSize() != invSize) {
            open(player);
            return;
        }
        fillInventory(h, player, defined, invSize);
    }

    public int size(Map<Integer, Button> buttons) {
        int highest = -1;
        for (int slot : buttons.keySet()) {
            if (slot > highest) {
                highest = slot;
            }
        }
        if (highest < 0) {
            return 9;
        }
        int rows = (highest + 9) / 9;
        return Math.min(54, rows * 9);
    }

    public String getTitle(Player player) {
        return this.staticTitle;
    }

    public abstract Map<Integer, Button> getButtons(Player player);

    public void onOpen(Player player) {
    }

    public void onClose(Player player) {
    }
}
