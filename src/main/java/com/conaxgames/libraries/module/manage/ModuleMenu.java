package com.conaxgames.libraries.module.manage;

import com.conaxgames.libraries.menu.Button;
import com.conaxgames.libraries.menu.pagination.PaginatedMenu;
import com.conaxgames.libraries.module.ModuleManager;
import com.conaxgames.libraries.module.Module;
import com.conaxgames.libraries.message.CC;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleMenu extends PaginatedMenu {

    private final ModuleManager moduleManager;

    public ModuleMenu(ModuleManager moduleManager) {
        this.moduleManager = moduleManager;
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Modules";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        int index = 0;
        for (Module module : moduleManager.getModules().values()) {
            buttons.put(index++, new ModuleButton(moduleManager, module));
        }
        return buttons;
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        Map<String, Module> registered = moduleManager.getModules();
        int total = registered.size();
        long enabled = registered.values().stream()
                .filter(moduleManager::isModuleEnabled)
                .count();

        Button infoButton = new Button() {
            @Override
            public String getName(Player player) {
                return CC.translate("&6Module Statistics");
            }

            @Override
            public List<String> getDescription(Player player) {
                List<String> lore = new ArrayList<>();
                lore.add(CC.translate("&8Module overview"));
                lore.add(" ");
                lore.add(CC.translate("&7Total Modules: &f" + total));
                lore.add(CC.translate("&7Enabled: &a" + enabled));
                lore.add(CC.translate("&7Disabled: &c" + (total - enabled)));
                lore.add(" ");
                return lore;
            }

            @Override
            public Material getMaterial(Player player) {
                return XMaterial.BOOK.get();
            }
        };
        buttons.put(49, infoButton);
        return buttons;
    }

    @Override
    public int previousPageSlot(Player player) {
        return 48;
    }

    @Override
    public int nextPageSlot(Player player) {
        return 50;
    }

    @Override
    public int getMaxItemsPerPage(Player player) {
        return 45;
    }
}
