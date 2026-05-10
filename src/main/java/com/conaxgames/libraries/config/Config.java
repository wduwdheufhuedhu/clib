package com.conaxgames.libraries.config;

import com.conaxgames.libraries.LibraryPlugin;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;

public final class Config {

    private final YamlConfiguration config;
    private final JavaPlugin plugin;
    @Getter
    private final File configFile;
    @Getter
    private final boolean wasCreated;

    public Config(@NonNull String name, @NonNull JavaPlugin plugin) {
        this.plugin = plugin;
        Path path = resolveConfigPath(plugin, name);
        this.configFile = path.toFile();

        boolean created = false;
        if (!Files.exists(path)) {
            try {
                Path parent = path.getParent();
                if (parent != null) {
                    Files.createDirectories(parent);
                }
                Files.createFile(path);
                created = true;
            } catch (IOException e) {
                logIo("Could not create configuration file " + path, e);
            }
        }

        this.wasCreated = created;
        this.config = YamlConfiguration.loadConfiguration(this.configFile);
    }

    private static Path resolveConfigPath(JavaPlugin plugin, String name) {
        String trimmed = name.startsWith("/") ? name.substring(1) : name;
        return plugin.getDataFolder().toPath().resolve(trimmed + ".yml").normalize();
    }

    public void save() {
        try {
            this.config.save(configFile);
        } catch (IOException e) {
            logIo("Could not save configuration file " + configFile.getPath(), e);
        }
    }

    private void logIo(String message, IOException e) {
        try {
            LibraryPlugin.getInstance().getLibraryLogger().toConsole("Config", message, e);
        } catch (IllegalPluginAccessException ignored) {
            plugin.getLogger().log(Level.SEVERE, message, e);
        }
    }

    public FileConfiguration getConfig() {
        return this.config;
    }

    public void set(String path, Object value) {
        this.config.set(path, value);
        this.save();
    }
}
