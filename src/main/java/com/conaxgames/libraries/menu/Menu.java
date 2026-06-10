package com.conaxgames.libraries.menu;

import com.conaxgames.libraries.LibraryPlugin;
import com.conaxgames.libraries.menu.listener.ButtonListener;
import com.conaxgames.libraries.message.CC;
import com.conaxgames.libraries.util.scheduler.Scheduler;
import com.cryptomorin.xseries.inventory.XInventoryView;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public final class Menu {

    public static final Map<UUID, Menu> currentlyOpenedMenus = new ConcurrentHashMap<>();
    private static final Map<UUID, Scheduler.CancellableTask> CHECK_TASKS = new ConcurrentHashMap<>();

    static {
        Bukkit.getServer().getPluginManager().registerEvents(new ButtonListener(), LibraryPlugin.getInstance().getPlugin());
    }

    @FunctionalInterface
    public interface Renderer {
        void render(Player player, Layout layout);
    }

    private final Function<Player, String> title;
    private final int rows;
    private final Map<Integer, Button> staticButtons;
    private final Renderer renderer;
    private final Button filler;
    private final boolean autoUpdate;
    private final long updateTicks;
    private final boolean updateAfterClick;
    private final boolean refreshInPlace;
    private final Consumer<Player> onOpen;
    private final Consumer<Player> onClose;
    private final Menu previous;

    private Menu(Builder builder) {
        this.title = builder.title;
        this.rows = builder.rows;
        this.staticButtons = builder.buttons;
        this.renderer = builder.renderer;
        this.filler = builder.filler;
        this.autoUpdate = builder.autoUpdate;
        this.updateTicks = builder.updateTicks;
        this.updateAfterClick = builder.updateAfterClick;
        this.refreshInPlace = builder.refreshInPlace;
        this.onOpen = builder.onOpen;
        this.onClose = builder.onClose;
        this.previous = builder.previous;
    }

    public static Builder builder(String title) {
        return new Builder(player -> CC.translate(title));
    }

    public static Builder builder(Function<Player, String> title) {
        return new Builder(player -> CC.translate(title.apply(player)));
    }

    public static Menu opened(Player player) {
        return currentlyOpenedMenus.get(player.getUniqueId());
    }

    public static void cancelCheck(Player player) {
        Scheduler.CancellableTask task = CHECK_TASKS.remove(player.getUniqueId());
        if (task != null) {
            task.cancel();
        }
    }

    public Menu previous() {
        return previous;
    }

    public boolean updateAfterClick() {
        return updateAfterClick;
    }

    public void closed(Player player) {
        if (onClose != null) {
            onClose.accept(player);
        }
    }

    public void open(Player player) {
        if (Bukkit.isPrimaryThread()) {
            open0(player);
        } else {
            LibraryPlugin.getInstance().getScheduler().runTask(LibraryPlugin.getInstance().getPlugin(), () -> open0(player));
        }
    }

    private void open0(Player player) {
        UUID id = player.getUniqueId();
        Map<Integer, Button> layout = render(player);
        int size = resolveSize(layout);
        applyFiller(layout, size);

        Inventory top = XInventoryView.of(player.getOpenInventory()).getTopInventory();
        if (refreshInPlace
                && top.getHolder() instanceof MenuInventoryHolder existing
                && existing.getMenu() == this
                && existing.getViewerId().equals(id)
                && top.getSize() == size) {
            fill(existing, layout, size);
            beginSession(player);
            return;
        }

        cancelCheck(player);
        MenuInventoryHolder holder = new MenuInventoryHolder(this, id);
        Inventory inv = Bukkit.createInventory(holder, size, title.apply(player));
        holder.attachInventory(inv);
        fill(holder, layout, size);
        player.openInventory(inv);
        beginSession(player);
    }

    public void update(Player player) {
        Inventory top = XInventoryView.of(player.getOpenInventory()).getTopInventory();
        if (!(top.getHolder() instanceof MenuInventoryHolder holder)) {
            return;
        }
        if (holder.getMenu() != this || !holder.getViewerId().equals(player.getUniqueId())) {
            return;
        }
        Map<Integer, Button> layout = render(player);
        int size = resolveSize(layout);
        applyFiller(layout, size);
        if (top.getSize() != size) {
            open(player);
            return;
        }
        fill(holder, layout, size);
    }

    private Map<Integer, Button> render(Player player) {
        Map<Integer, Button> layout = new HashMap<>(staticButtons);
        if (renderer != null) {
            renderer.render(player, new Layout(layout));
        }
        return layout;
    }

    private int resolveSize(Map<Integer, Button> layout) {
        return rows > 0 ? rows * 9 : autoSize(layout);
    }

    private static int autoSize(Map<Integer, Button> layout) {
        int highest = -1;
        for (int slot : layout.keySet()) {
            if (slot > highest) {
                highest = slot;
            }
        }
        if (highest < 0) {
            return 9;
        }
        return Math.min(54, ((highest + 9) / 9) * 9);
    }

    private void applyFiller(Map<Integer, Button> layout, int size) {
        if (filler == null) {
            return;
        }
        for (int slot = 0; slot < size; slot++) {
            layout.putIfAbsent(slot, filler);
        }
    }

    private void fill(MenuInventoryHolder holder, Map<Integer, Button> layout, int size) {
        holder.setSlotButtons(layout);
        Inventory inv = holder.getInventory();
        for (int slot = 0; slot < size; slot++) {
            Button button = layout.get(slot);
            inv.setItem(slot, button != null ? button.icon() : null);
        }
    }

    private void beginSession(Player player) {
        UUID id = player.getUniqueId();
        cancelCheck(player);
        currentlyOpenedMenus.put(id, this);
        if (onOpen != null) {
            onOpen.accept(player);
        }
        if (!autoUpdate) {
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
                    update(player);
                },
                updateTicks,
                updateTicks
        );
        CHECK_TASKS.put(id, task);
    }

    public static final class Layout {

        private final Map<Integer, Button> buttons;

        private Layout(Map<Integer, Button> buttons) {
            this.buttons = buttons;
        }

        public Layout set(int slot, Button button) {
            if (button != null) {
                buttons.put(slot, button);
            }
            return this;
        }

        public Layout set(int row, int col, Button button) {
            return set(row * 9 + col, button);
        }
    }

    public static final class Builder {

        private final Function<Player, String> title;
        private final Map<Integer, Button> buttons = new HashMap<>();
        private int rows = 0;
        private Renderer renderer;
        private Button filler;
        private boolean autoUpdate = false;
        private long updateTicks = 20L;
        private boolean updateAfterClick = true;
        private boolean refreshInPlace = true;
        private Consumer<Player> onOpen;
        private Consumer<Player> onClose;
        private Menu previous;

        private Builder(Function<Player, String> title) {
            this.title = title;
        }

        public Builder rows(int rows) {
            this.rows = rows;
            return this;
        }

        public Builder set(int slot, Button button) {
            if (button != null) {
                buttons.put(slot, button);
            }
            return this;
        }

        public Builder set(int row, int col, Button button) {
            return set(row * 9 + col, button);
        }

        public Builder fill(Button filler) {
            this.filler = filler;
            return this;
        }

        public Builder render(Renderer renderer) {
            this.renderer = renderer;
            return this;
        }

        public Builder autoUpdate() {
            return autoUpdate(20L);
        }

        public Builder autoUpdate(long updateTicks) {
            this.autoUpdate = true;
            this.updateTicks = updateTicks;
            return this;
        }

        public Builder updateAfterClick(boolean updateAfterClick) {
            this.updateAfterClick = updateAfterClick;
            return this;
        }

        public Builder refreshInPlace(boolean refreshInPlace) {
            this.refreshInPlace = refreshInPlace;
            return this;
        }

        public Builder onOpen(Consumer<Player> onOpen) {
            this.onOpen = onOpen;
            return this;
        }

        public Builder onClose(Consumer<Player> onClose) {
            this.onClose = onClose;
            return this;
        }

        public Builder previous(Menu previous) {
            this.previous = previous;
            return this;
        }

        public Menu build() {
            return new Menu(this);
        }
    }
}
