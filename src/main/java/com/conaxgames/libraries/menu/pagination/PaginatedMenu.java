package com.conaxgames.libraries.menu.pagination;

import com.conaxgames.libraries.menu.Button;
import com.conaxgames.libraries.menu.Menu;
import com.conaxgames.libraries.menu.pagination.buttons.PageButton;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class PaginatedMenu extends Menu {
    private int page = 1;

    @Override
    public String getTitle(Player player) {
        return this.getPrePaginatedTitle(player) + " (" + this.page + "/" + this.getPages(player) + ")";
    }

    @Override
    protected boolean refreshInPlaceWhenPossible() {
        return false;
    }

    public final void modPage(Player player, int mod) {
        this.page += mod;
        this.openMenu(player, false);
    }

    private boolean hasNext(Player player) {
        int pg = this.page + 1;
        return pg > 0 && this.getPages(player) >= pg;
    }

    private boolean hasPrevious(Player player) {
        int pg = this.page - 1;
        return pg > 0 && this.getPages(player) >= pg;
    }

    public final int getPages(Player player) {
        int buttonAmount = this.getAllPagesButtons(player).size();
        if (buttonAmount == 0) {
            return 1;
        }
        return (int) Math.ceil((double) buttonAmount / this.getMaxItemsPerPage(player));
    }

    @Override
    public final Map<Integer, Button> getButtons(Player player) {
        int maxItems = this.getMaxItemsPerPage(player);
        int minIndex = (this.page - 1) * maxItems;
        int maxIndex = this.page * maxItems;

        int previousSlot = this.previousPageSlot(player);
        int nextSlot = this.nextPageSlot(player);

        HashMap<Integer, Button> buttons = new HashMap<>();

        if (hasPrevious(player)) {
            buttons.put(previousSlot, new PageButton(-1, this));
        }
        if (hasNext(player)) {
            buttons.put(nextSlot, new PageButton(1, this));
        }

        for (Map.Entry<Integer, Button> entry : this.getAllPagesButtons(player).entrySet()) {
            int ind = entry.getKey();
            if (ind < minIndex || ind >= maxIndex) continue;

            int targetSlot = ind - (maxItems * (this.page - 1));
            buttons.put(targetSlot, entry.getValue());
        }

        Map<Integer, Button> global = this.getGlobalButtons(player);
        if (global != null) {
            buttons.putAll(global);
        }

        return buttons;
    }

    public int getMaxItemsPerPage(Player player) {
        return 54;
    }

    public Map<Integer, Button> getGlobalButtons(Player player) {
        return null;
    }

    public abstract String getPrePaginatedTitle(Player player);

    public abstract Map<Integer, Button> getAllPagesButtons(Player player);

    public abstract int previousPageSlot(Player player);

    public abstract int nextPageSlot(Player player);
}
