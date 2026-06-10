package com.conaxgames.libraries.menu;

import com.conaxgames.libraries.util.ItemBuilderUtil;
import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XItemFlag;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public final class Button {

    @FunctionalInterface
    public interface Click {
        void on(Player player, ClickType type);
    }

    private final ItemStack icon;
    private final Click click;
    private final boolean editable;

    private Button(ItemStack icon, Click click) {
        this(icon, click, false);
    }

    private Button(ItemStack icon, Click click, boolean editable) {
        this.icon = icon;
        this.click = click;
        this.editable = editable;
    }

    public static Button of(ItemStack icon) {
        return new Button(icon, null);
    }

    public static Button editable(ItemStack initial) {
        return new Button(initial, null, true);
    }

    public static Builder builder(XMaterial material) {
        return new Builder(new ItemBuilderUtil(material));
    }

    public static Builder builder(ItemStack icon) {
        return new Builder(new ItemBuilderUtil(icon));
    }

    public ItemStack icon() {
        return icon;
    }

    public boolean editable() {
        return editable;
    }

    public void click(Player player, ClickType type) {
        if (click != null) {
            click.on(player, type);
        }
    }

    public static final class Builder {

        private final ItemBuilderUtil item;
        private Click click;

        private Builder(ItemBuilderUtil item) {
            this.item = item;
        }

        public Builder name(String name) {
            item.setName(name);
            return this;
        }

        public Builder lore(String... lore) {
            item.setLore(lore);
            return this;
        }

        public Builder lore(List<String> lore) {
            item.setLore(lore);
            return this;
        }

        public Builder amount(int amount) {
            item.setAmount(amount);
            return this;
        }

        public Builder data(int data) {
            item.setDurability((short) data);
            return this;
        }

        public Builder glow() {
            return glow(true);
        }

        public Builder glow(boolean glow) {
            item.setGlow(glow);
            return this;
        }

        public Builder skull(String owner) {
            item.setSkullOwner(owner);
            return this;
        }

        public Builder skull(UUID owner) {
            item.setSkullOwner(owner);
            return this;
        }

        public Builder enchant(XEnchantment enchantment, int level) {
            item.addEnchant(enchantment, level);
            return this;
        }

        public Builder flags(XItemFlag... flags) {
            item.setFlags(flags);
            return this;
        }

        public Builder modelData(int modelData) {
            item.setCustomModelData(modelData);
            return this;
        }

        public Builder onClick(Click click) {
            this.click = click;
            return this;
        }

        public Button build() {
            return new Button(item.toItemStack(), click);
        }
    }
}
