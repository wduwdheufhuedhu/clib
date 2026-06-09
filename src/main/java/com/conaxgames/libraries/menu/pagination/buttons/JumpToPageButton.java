package com.conaxgames.libraries.menu.pagination.buttons;

import com.conaxgames.libraries.event.impl.menu.MenuButtonJumpToEvent;
import com.conaxgames.libraries.menu.Button;
import com.conaxgames.libraries.menu.pagination.PaginatedMenu;
import com.conaxgames.libraries.message.CC;
import com.cryptomorin.xseries.XMaterial;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;

public class JumpToPageButton extends Button {

    private final int page;
    private final PaginatedMenu menu;
    @Setter
    private boolean glowing = false;

    public JumpToPageButton(int page, PaginatedMenu menu) {
        this.page = page;
        this.menu = menu;
    }

    @Override
    public String getName(Player player) {
        String prefix = this.glowing ? "&6► " : "&6";
        return CC.translate(prefix + "Page " + this.page);
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> description = new ArrayList<>();

        int currentPage = this.menu.getPage();
        int totalPages = this.menu.getPages(player);

        if (this.page == currentPage) {
            description.add(CC.translate("&7This is your current page"));
        } else {
            description.add(CC.translate("&7Current Page: " + currentPage));
            description.add(CC.translate("&7Total Pages: " + totalPages));
            description.add(CC.translate("&7Target Page: " + this.page));
        }

        description.add(" ");
        description.add(CC.translate("&eClick to jump to this page!"));

        return description;
    }

    @Override
    public Material getMaterial(Player player) {
        return this.glowing ? XMaterial.WRITABLE_BOOK.get() : XMaterial.BOOK.get();
    }

    @Override
    public int getAmount(Player player) {
        return this.page;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        new MenuButtonJumpToEvent(player, menu, this).call();
        this.menu.modPage(player, this.page - this.menu.getPage());
    }
}
