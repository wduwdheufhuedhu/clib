package com.conaxgames.libraries.config;

import com.conaxgames.libraries.LibraryPlugin;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class Config {

    private final YamlConfiguration config;
    private final JavaPlugin plugin;
    @Getter
    private final File configFile;
    @Getter
    protected boolean wasCreated;

    public Config(String name, JavaPlugin plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder() + "/" + name + ".yml");
        if (!this.configFile.exists()) {
            try {
                this.configFile.getParentFile().mkdirs();
                this.configFile.createNewFile();
                this.wasCreated = true;
            } catch (IOException e) {
                logIo("Could not create configuration file " + this.configFile.getPath(), e);
            }
        }
        this.config = YamlConfiguration.loadConfiguration(this.configFile);
    }

    public void save() {
        try {
            this.config.save(configFile);
        } catch (IOException e) {
            logIo("Could not save configuration file " + this.configFile.getPath(), e);
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
