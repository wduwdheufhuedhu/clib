package com.conaxgames.libraries.util;

import com.conaxgames.libraries.message.CC;
import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XItemFlag;
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.profiles.builder.XSkull;
import com.cryptomorin.xseries.profiles.objects.Profileable;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ItemBuilderUtil {

    private final ItemStack itemStack;

    public ItemBuilderUtil(Material material) {
        this(material, 1);
    }

    public ItemBuilderUtil(XMaterial material) {
        this(material, 1);
    }

    public ItemBuilderUtil(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemBuilderUtil(Material material, int amount) {
        ItemStack parsed = XMaterial.matchXMaterial(material).parseItem();
        if (parsed != null) {
            parsed.setAmount(amount);
            this.itemStack = parsed;
        } else {
            this.itemStack = new ItemStack(material, amount);
        }
    }

    public ItemBuilderUtil(XMaterial material, int amount) {
        ItemStack parsed = material.parseItem();
        if (parsed == null) {
            throw new IllegalArgumentException("Unsupported material: " + material);
        }
        parsed.setAmount(amount);
        this.itemStack = parsed;
    }

    @SuppressWarnings("deprecation")
    public ItemBuilderUtil(Material material, int amount, byte durability) {
        if (durability == 0) {
            ItemStack parsed = XMaterial.matchXMaterial(material).parseItem();
            if (parsed != null) {
                parsed.setAmount(amount);
                this.itemStack = parsed;
            } else {
                this.itemStack = new ItemStack(material, amount);
            }
            return;
        }
        ItemStack parsed = XMaterial.matchXMaterial(material.name() + ':' + durability)
                .map(XMaterial::parseItem)
                .orElse(null);
        if (parsed != null) {
            parsed.setAmount(amount);
            this.itemStack = parsed;
        } else {
            this.itemStack = new ItemStack(material, amount, durability);
        }
    }

    @SuppressWarnings("deprecation")
    public ItemBuilderUtil(XMaterial material, int amount, byte durability) {
        if (durability == 0) {
            ItemStack parsed = material.parseItem();
            if (parsed == null) {
                throw new IllegalArgumentException("Unsupported material: " + material);
            }
            parsed.setAmount(amount);
            this.itemStack = parsed;
            return;
        }
        ItemStack parsed = XMaterial.matchXMaterial(material.name() + ':' + durability)
                .map(XMaterial::parseItem)
                .orElseGet(() -> {
                    Material legacy = material.get();
                    if (legacy == null) {
                        throw new IllegalArgumentException("Unsupported material: " + material);
                    }
                    return new ItemStack(legacy, amount, durability);
                });
        parsed.setAmount(amount);
        this.itemStack = parsed;
    }

    public ItemBuilderUtil clone() {
        return new ItemBuilderUtil(itemStack.clone());
    }

    @SuppressWarnings("deprecation")
    public ItemBuilderUtil setDurability(short durability) {
        if (!itemStack.editMeta(Damageable.class, meta -> meta.setDamage(durability))) {
            itemStack.setDurability(durability);
        }
        return this;
    }

    public ItemBuilderUtil setName(String name) {
        itemStack.editMeta(meta -> meta.setDisplayName(CC.translate(name)));
        return this;
    }

    public ItemBuilderUtil addUnsafeEnchantment(Enchantment enchantment, int level) {
        itemStack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilderUtil addUnsafeEnchantment(XEnchantment enchantment, int level) {
        Enchantment resolved = enchantment.get();
        return resolved == null ? this : addUnsafeEnchantment(resolved, level);
    }

    public ItemBuilderUtil addUnsafeEnchantmentIf(boolean condition, Enchantment enchantment, int level) {
        return condition ? addUnsafeEnchantment(enchantment, level) : this;
    }

    public ItemBuilderUtil addUnsafeEnchantmentIf(boolean condition, XEnchantment enchantment, int level) {
        return condition ? addUnsafeEnchantment(enchantment, level) : this;
    }

    public ItemBuilderUtil removeEnchantment(Enchantment enchantment) {
        itemStack.removeEnchantment(enchantment);
        return this;
    }

    public ItemBuilderUtil removeEnchantment(XEnchantment enchantment) {
        Enchantment resolved = enchantment.get();
        return resolved == null ? this : removeEnchantment(resolved);
    }

    public ItemBuilderUtil setSkullOwner(String name) {
        if (!XMaterial.PLAYER_HEAD.isSimilar(itemStack)) {
            return this;
        }
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
        Profileable profile = offlinePlayer.hasPlayedBefore()
                ? Profileable.of(offlinePlayer)
                : Profileable.username(name);
        XSkull.of(itemStack).profile(profile).lenient().apply();
        return this;
    }

    public ItemBuilderUtil setSkullOwner(OfflinePlayer offlinePlayer) {
        if (!XMaterial.PLAYER_HEAD.isSimilar(itemStack)) {
            return this;
        }
        XSkull.of(itemStack).profile(Profileable.of(offlinePlayer)).lenient().apply();
        return this;
    }

    public ItemBuilderUtil setSkullOwner(UUID uuid) {
        if (!XMaterial.PLAYER_HEAD.isSimilar(itemStack)) {
            return this;
        }
        XSkull.of(itemStack).profile(Profileable.of(uuid)).lenient().apply();
        return this;
    }

    public ItemBuilderUtil setSkullProfile(String texture) {
        if (!XMaterial.PLAYER_HEAD.isSimilar(itemStack)) {
            return this;
        }
        XSkull.of(itemStack).profile(Profileable.detect(texture)).lenient().apply();
        return this;
    }

    public ItemBuilderUtil addEnchant(Enchantment enchantment, int level) {
        itemStack.editMeta(meta -> meta.addEnchant(enchantment, level, true));
        return this;
    }

    public ItemBuilderUtil addEnchant(XEnchantment enchantment, int level) {
        Enchantment resolved = enchantment.get();
        return resolved == null ? this : addEnchant(resolved, level);
    }

    public ItemBuilderUtil addEnchantments(Map<Enchantment, Integer> enchantments) {
        itemStack.addEnchantments(enchantments);
        return this;
    }

    public ItemBuilderUtil setInfinityDurability() {
        if (!itemStack.editMeta(Damageable.class, meta -> meta.setDamage(Short.MAX_VALUE))) {
            itemStack.setDurability(Short.MAX_VALUE);
        }
        return this;
    }

    public ItemBuilderUtil setLore(String... lore) {
        itemStack.editMeta(meta -> meta.setLore(CC.translate(Arrays.asList(lore))));
        return this;
    }

    public ItemBuilderUtil setLore(List<String> lore) {
        itemStack.editMeta(meta -> meta.setLore(CC.translate(lore)));
        return this;
    }

    public ItemBuilderUtil removeLoreLine(String line) {
        itemStack.editMeta(meta -> {
            List<String> lore = meta.getLore();
            if (lore == null) {
                return;
            }
            lore = new ArrayList<>(lore);
            if (lore.remove(CC.translate(line))) {
                meta.setLore(lore);
            }
        });
        return this;
    }

    public ItemBuilderUtil removeLoreLine(int index) {
        itemStack.editMeta(meta -> {
            List<String> lore = meta.getLore();
            if (lore == null) {
                return;
            }
            lore = new ArrayList<>(lore);
            if (index >= 0 && index < lore.size()) {
                lore.remove(index);
                meta.setLore(lore);
            }
        });
        return this;
    }

    public ItemBuilderUtil addLoreLine(String line) {
        itemStack.editMeta(meta -> {
            List<String> lore = meta.getLore();
            lore = lore == null ? new ArrayList<>() : new ArrayList<>(lore);
            lore.add(CC.translate(line));
            meta.setLore(lore);
        });
        return this;
    }

    public ItemBuilderUtil addLoreLineIf(boolean condition, String line) {
        return condition ? addLoreLine(line) : this;
    }

    public ItemBuilderUtil addLoreLineList(List<String> lines) {
        itemStack.editMeta(meta -> {
            List<String> lore = meta.getLore();
            lore = lore == null ? new ArrayList<>() : new ArrayList<>(lore);
            lore.addAll(CC.translate(lines));
            meta.setLore(lore);
        });
        return this;
    }

    public ItemBuilderUtil addLoreLine(String line, int pos) {
        itemStack.editMeta(meta -> {
            List<String> lore = meta.getLore();
            if (lore == null) {
                return;
            }
            lore = new ArrayList<>(lore);
            if (pos >= 0 && pos < lore.size()) {
                lore.set(pos, CC.translate(line));
                meta.setLore(lore);
            }
        });
        return this;
    }

    public ItemBuilderUtil setDyeColor(DyeColor color) {
        XMaterial.matchXMaterial(color.name() + "_WOOL")
                .ifPresent(xm -> xm.setType(itemStack));
        return this;
    }

    public ItemBuilderUtil setLeatherArmorColor(Color color) {
        itemStack.editMeta(meta -> {
            if (meta instanceof LeatherArmorMeta leather) {
                leather.setColor(color);
            }
        });
        return this;
    }

    public ItemBuilderUtil setUnbreakable() {
        itemStack.editMeta(meta -> meta.setUnbreakable(true));
        return this;
    }

    public ItemBuilderUtil setFlags(ItemFlag... flags) {
        itemStack.editMeta(meta -> {
            for (ItemFlag flag : flags) {
                XItemFlag.of(flag).set(meta);
            }
        });
        return this;
    }

    public ItemBuilderUtil setFlags(XItemFlag... flags) {
        itemStack.editMeta(meta -> {
            for (XItemFlag flag : flags) {
                flag.set(meta);
            }
        });
        return this;
    }

    public ItemBuilderUtil removeFlags(ItemFlag... flags) {
        itemStack.editMeta(meta -> {
            for (ItemFlag flag : flags) {
                XItemFlag.of(flag).removeFrom(meta);
            }
        });
        return this;
    }

    public ItemBuilderUtil removeFlags(XItemFlag... flags) {
        itemStack.editMeta(meta -> {
            for (XItemFlag flag : flags) {
                flag.removeFrom(meta);
            }
        });
        return this;
    }

    public ItemBuilderUtil hideAttributes() {
        itemStack.editMeta(meta -> XItemFlag.HIDE_ATTRIBUTES.set(meta));
        return this;
    }

    public ItemBuilderUtil hideEnchants() {
        itemStack.editMeta(meta -> XItemFlag.HIDE_ENCHANTS.set(meta));
        return this;
    }

    public ItemBuilderUtil hideTooltipExtras() {
        itemStack.editMeta(meta -> {
            XItemFlag.HIDE_ATTRIBUTES.set(meta);
            XItemFlag.HIDE_ENCHANTS.set(meta);
            XItemFlag.HIDE_ADDITIONAL_TOOLTIP.set(meta);
        });
        return this;
    }

    public ItemBuilderUtil showAttributes() {
        itemStack.editMeta(meta -> XItemFlag.HIDE_ATTRIBUTES.removeFrom(meta));
        return this;
    }

    public ItemBuilderUtil setAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilderUtil setCustomModelData(int modelData) {
        itemStack.editMeta(meta -> meta.setCustomModelData(modelData));
        return this;
    }

    public ItemBuilderUtil setGlow() {
        return setGlow(true);
    }

    public ItemBuilderUtil setGlow(boolean glow) {
        itemStack.editMeta(meta -> {
            Enchantment unbreaking = XEnchantment.UNBREAKING.get();
            if (glow) {
                if (unbreaking != null) {
                    meta.addEnchant(unbreaking, 1, true);
                }
                XItemFlag.HIDE_ENCHANTS.set(meta);
                return;
            }
            if (unbreaking != null) {
                meta.removeEnchant(unbreaking);
            }
            if (!meta.hasEnchants()) {
                XItemFlag.HIDE_ENCHANTS.removeFrom(meta);
            }
        });
        return this;
    }

    public ItemBuilderUtil setUnstackable() {
        itemStack.editMeta(meta -> {
            PersistentDataContainer container = meta.getPersistentDataContainer();
            NamespacedKey key = new NamespacedKey("conaxgames", "unstackable");
            container.set(key, PersistentDataType.STRING, UUID.randomUUID().toString());
        });
        return this;
    }

    public ItemStack toItemStack() {
        return itemStack;
    }
}
