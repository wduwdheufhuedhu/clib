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
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class ItemBuilderUtil {

    private final ItemStack is;

    public ItemBuilderUtil(Material m) {
        this(m, 1);
    }

    public ItemBuilderUtil(XMaterial material) {
        this(material, 1);
    }

    public ItemBuilderUtil(ItemStack is) {
        this.is = is;
    }

    public ItemBuilderUtil(Material m, int amount) {
        is = new ItemStack(m, amount);
    }

    public ItemBuilderUtil(XMaterial material, int amount) {
        ItemStack stack = material.parseItem();
        if (stack == null) {
            throw new IllegalArgumentException("Unsupported material: " + material);
        }
        stack.setAmount(amount);
        is = stack;
    }

    @SuppressWarnings("deprecation")
    public ItemBuilderUtil(Material m, int amount, byte durability) {
        if (durability == 0) {
            is = new ItemStack(m, amount);
        } else {
            ItemStack matched = XMaterial.matchXMaterial(m.name() + ':' + durability)
                    .map(XMaterial::parseItem)
                    .orElse(null);
            if (matched != null) {
                matched.setAmount(amount);
                is = matched;
            } else {
                is = new ItemStack(m, amount, durability);
            }
        }
    }

    @SuppressWarnings("deprecation")
    public ItemBuilderUtil(XMaterial material, int amount, byte durability) {
        Material parsed = material.get();
        if (parsed == null) {
            throw new IllegalArgumentException("Unsupported material: " + material);
        }
        if (durability == 0) {
            is = new ItemStack(parsed, amount);
        } else {
            ItemStack matched = XMaterial.matchXMaterial(material.name() + ':' + durability)
                    .map(XMaterial::parseItem)
                    .orElse(null);
            if (matched != null) {
                matched.setAmount(amount);
                is = matched;
            } else {
                is = new ItemStack(parsed, amount, durability);
            }
        }
    }

    private ItemBuilderUtil edit(Consumer<ItemMeta> action) {
        is.editMeta(action);
        return this;
    }

    public ItemBuilderUtil clone() {
        return new ItemBuilderUtil(is.clone());
    }

    @SuppressWarnings("deprecation")
    public ItemBuilderUtil setDurability(short durability) {
        if (!is.editMeta(Damageable.class, meta -> meta.setDamage(durability))) {
            is.setDurability(durability);
        }
        return this;
    }

    public ItemBuilderUtil setName(String name) {
        return edit(meta -> meta.setDisplayName(CC.translate(name)));
    }

    public ItemBuilderUtil addUnsafeEnchantment(Enchantment ench, int level) {
        is.addUnsafeEnchantment(ench, level);
        return this;
    }

    public ItemBuilderUtil addUnsafeEnchantmentIf(boolean condition, Enchantment ench, int level) {
        if (condition) {
            return addUnsafeEnchantment(ench, level);
        }
        return this;
    }

    public ItemBuilderUtil removeEnchantment(Enchantment ench) {
        is.removeEnchantment(ench);
        return this;
    }

    public ItemBuilderUtil setSkullOwner(String name) {
        if (!XMaterial.PLAYER_HEAD.isSimilar(is)) {
            return this;
        }
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
        Profileable profile = offlinePlayer.hasPlayedBefore()
                ? Profileable.of(offlinePlayer)
                : Profileable.username(name);
        XSkull.of(is).profile(profile).lenient().apply();
        return this;
    }

    public ItemBuilderUtil setSkullOwner(OfflinePlayer offlinePlayer) {
        if (!XMaterial.PLAYER_HEAD.isSimilar(is)) {
            return this;
        }
        XSkull.of(is).profile(Profileable.of(offlinePlayer)).lenient().apply();
        return this;
    }

    public ItemBuilderUtil setSkullOwner(UUID uuid) {
        if (!XMaterial.PLAYER_HEAD.isSimilar(is)) {
            return this;
        }
        XSkull.of(is).profile(Profileable.of(uuid)).lenient().apply();
        return this;
    }

    public ItemBuilderUtil setSkullProfile(String texture) {
        if (!XMaterial.PLAYER_HEAD.isSimilar(is)) {
            return this;
        }
        XSkull.of(is).profile(Profileable.detect(texture)).lenient().apply();
        return this;
    }

    public ItemBuilderUtil addEnchant(Enchantment ench, int level) {
        return edit(meta -> meta.addEnchant(ench, level, true));
    }

    public ItemBuilderUtil addEnchant(XEnchantment enchantment, int level) {
        Enchantment ench = enchantment.get();
        return ench == null ? this : addEnchant(ench, level);
    }

    public ItemBuilderUtil addEnchantments(Map<Enchantment, Integer> enchantments) {
        is.addEnchantments(enchantments);
        return this;
    }

    public ItemBuilderUtil setInfinityDurability() {
        return setDurability(Short.MAX_VALUE);
    }

    public ItemBuilderUtil setLore(String... lore) {
        return edit(meta -> meta.setLore(CC.translate(lore)));
    }

    public ItemBuilderUtil setLore(List<String> lore) {
        return edit(meta -> meta.setLore(CC.translate(lore)));
    }

    public ItemBuilderUtil removeLoreLine(String line) {
        return edit(meta -> {
            List<String> lore = meta.getLore();
            if (lore == null) {
                return;
            }
            lore = new ArrayList<>(lore);
            if (lore.remove(line)) {
                meta.setLore(lore);
            }
        });
    }

    public ItemBuilderUtil removeLoreLine(int index) {
        return edit(meta -> {
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
    }

    public ItemBuilderUtil addLoreLine(String line) {
        return edit(meta -> {
            List<String> lore = meta.getLore();
            lore = lore == null ? new ArrayList<>() : new ArrayList<>(lore);
            lore.add(CC.translate(line));
            meta.setLore(lore);
        });
    }

    public ItemBuilderUtil addLoreLineIf(boolean condition, String line) {
        return condition ? addLoreLine(line) : this;
    }

    public ItemBuilderUtil addLoreLineList(List<String> lines) {
        return edit(meta -> {
            List<String> lore = meta.getLore();
            lore = lore == null ? new ArrayList<>() : new ArrayList<>(lore);
            lore.addAll(CC.translate(lines));
            meta.setLore(lore);
        });
    }

    public ItemBuilderUtil addLoreLine(String line, int pos) {
        return edit(meta -> {
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
    }

    public ItemBuilderUtil setDyeColor(DyeColor color) {
        XMaterial.matchXMaterial(color.name() + "_WOOL")
                .ifPresent(xm -> xm.setType(is));
        return this;
    }

    public ItemBuilderUtil setLeatherArmorColor(Color color) {
        return edit(meta -> {
            if (meta instanceof LeatherArmorMeta leather) {
                leather.setColor(color);
            }
        });
    }

    public ItemBuilderUtil setUnbreakable() {
        return edit(meta -> meta.setUnbreakable(true));
    }

    public ItemBuilderUtil setFlags(ItemFlag... flags) {
        return edit(meta -> {
            for (ItemFlag flag : flags) {
                XItemFlag.of(flag).set(meta);
            }
        });
    }

    public ItemBuilderUtil setFlags(XItemFlag... flags) {
        return edit(meta -> {
            for (XItemFlag flag : flags) {
                flag.set(meta);
            }
        });
    }

    public ItemBuilderUtil removeFlags(ItemFlag... flags) {
        return edit(meta -> {
            for (ItemFlag flag : flags) {
                XItemFlag.of(flag).removeFrom(meta);
            }
        });
    }

    public ItemBuilderUtil removeFlags(XItemFlag... flags) {
        return edit(meta -> {
            for (XItemFlag flag : flags) {
                flag.removeFrom(meta);
            }
        });
    }

    public ItemBuilderUtil hideAttributes() {
        return edit(meta -> XItemFlag.HIDE_ATTRIBUTES.set(meta));
    }

    public ItemBuilderUtil hideEnchants() {
        return edit(meta -> XItemFlag.HIDE_ENCHANTS.set(meta));
    }

    public ItemBuilderUtil showAttributes() {
        return edit(meta -> XItemFlag.HIDE_ATTRIBUTES.removeFrom(meta));
    }

    public ItemBuilderUtil setAmount(int amount) {
        is.setAmount(amount);
        return this;
    }

    public ItemBuilderUtil setCustomModelData(int modelData) {
        return edit(meta -> meta.setCustomModelData(modelData));
    }

    public ItemBuilderUtil setGlow() {
        return setGlow(true);
    }

    public ItemBuilderUtil setGlow(boolean glow) {
        return edit(meta -> {
            if (glow) {
                meta.setEnchantmentGlintOverride(true);
                return;
            }
            meta.setEnchantmentGlintOverride(null);
            Enchantment unbreaking = XEnchantment.UNBREAKING.get();
            if (unbreaking != null) {
                meta.removeEnchant(unbreaking);
            }
            if (!meta.hasEnchants()) {
                XItemFlag.HIDE_ENCHANTS.removeFrom(meta);
            }
        });
    }

    public ItemBuilderUtil setUnstackable() {
        return edit(meta -> {
            PersistentDataContainer container = meta.getPersistentDataContainer();
            NamespacedKey key = new NamespacedKey("conaxgames", "unstackable");
            container.set(key, PersistentDataType.STRING, UUID.randomUUID().toString());
        });
    }

    public ItemStack toItemStack() {
        return is;
    }

    public ItemStack build() {
        return is;
    }
}
