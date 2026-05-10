package com.conaxgames.libraries.menu;

import com.conaxgames.libraries.util.ItemBuilderUtil;
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class Button {

    public static Button placeholder(Material material) {
        return placeholder(material, (byte) 0, "");
    }

    public static Button placeholder(Material material, String title) {
        return placeholder(material, (byte) 0, title);
    }

    public static Button placeholder(Material material, byte data, String title) {
        return new Button() {
            @Override
            public String getName(Player player) {
                return title;
            }

            @Override
            public List<String> getDescription(Player player) {
                return List.of();
            }

            @Override
            public Material getMaterial(Player player) {
                return material;
            }

            @Override
            public int getDamageValue(Player player) {
                return data;
            }
        };
    }

    public static void playFail(Player player) {
        XSound.BLOCK_GRASS_BREAK.play(player);
    }

    public static void playSuccess(Player player) {
        XSound.BLOCK_NOTE_BLOCK_HARP.play(player);
    }

    public static void playNeutral(Player player) {
        XSound.UI_BUTTON_CLICK.play(player);
    }

    public abstract String getName(Player player);

    public abstract List<String> getDescription(Player player);

    public abstract Material getMaterial(Player player);

    public int getDamageValue(Player player) {
        return 0;
    }

    public void clicked(Player player, int slot, ClickType clickType) {
    }

    public boolean shouldCancel(Player player, int slot, ClickType clickType) {
        return true;
    }

    public boolean shinyItem(Player player) {
        return false;
    }

    public String skullOwner(Player player) {
        return null;
    }

    public int getAmount(Player player) {
        return 1;
    }

    public ItemStack getButtonItem(Player player) {
        Material material = this.getMaterial(player);
        if (material == null) {
            material = XMaterial.BEDROCK.get();
        }

        ItemBuilderUtil builder = new ItemBuilderUtil(material, this.getAmount(player), (byte) this.getDamageValue(player))
                .setName(this.getName(player));

        List<String> description = this.getDescription(player);
        if (description != null) {
            builder = builder.setLore(description);
        }

        String skull = skullOwner(player);
        if (skull != null) {
            builder = builder.setSkullOwner(skull);
        }

        if (shinyItem(player)) {
            builder = builder.setGlow();
        }

        return builder.toItemStack();
    }
}
