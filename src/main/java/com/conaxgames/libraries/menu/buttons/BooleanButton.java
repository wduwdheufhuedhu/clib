package com.conaxgames.libraries.menu.buttons;

import com.conaxgames.libraries.menu.Button;
import com.conaxgames.libraries.message.FormatUtil;
import com.conaxgames.libraries.util.CC;
import com.conaxgames.libraries.util.ColorMaterialUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BooleanButton extends Button {

    private final boolean confirm;
    private final Consumer<Boolean> callback;
    private final String details;

    public BooleanButton(boolean confirm, Consumer<Boolean> callback, String details) {
        this.confirm = confirm;
        this.callback = callback;
        this.details = details;
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        player.closeInventory();
        this.callback.accept(this.confirm);
    }

    @Override
    public String getName(Player player) {
        return this.confirm ? CC.GREEN + "Confirm" : CC.RED + "Cancel";
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> description = new ArrayList<>();

        if (this.confirm) {
            description.addAll(FormatUtil.wordWrap(CC.GRAY + details));
        } else {
            description.add(CC.GRAY + "Cancel this action.");
        }

        return description;
    }

    @Override
    public int getDamageValue(Player player) {
        return this.confirm ? 5 : 14;
    }

    @Override
    public Material getMaterial(Player player) {
        return ColorMaterialUtil.convertCCToXClay(this.confirm ? CC.GREEN : CC.RED).get();
    }
}
