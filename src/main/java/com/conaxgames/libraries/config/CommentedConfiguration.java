package com.conaxgames.libraries.config;

import com.conaxgames.libraries.util.Pair;
import lombok.NonNull;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class CommentedConfiguration extends YamlConfiguration {

    private final Map<String, String> configComments = new HashMap<>();

    public CommentedConfiguration() {
        this.options().parseComments(false);
    }

    public static CommentedConfiguration loadConfiguration(@NonNull File file) throws IOException, InvalidConfigurationException {
        try (FileInputStream stream = new FileInputStream(file)) {
            return loadConfiguration(new InputStreamReader(stream, StandardCharsets.UTF_8));
        }
    }

    public static CommentedConfiguration loadConfiguration(@NonNull InputStream inputStream) throws IOException, InvalidConfigurationException {
        Objects.requireNonNull(inputStream, "inputStream");
        return loadConfiguration(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }

    public static CommentedConfiguration loadConfiguration(Reader reader) throws IOException, InvalidConfigurationException {
        CommentedConfiguration config = new CommentedConfiguration();
        try (BufferedReader bufferedReader = reader instanceof BufferedReader br ? br : new BufferedReader(reader)) {
            StringBuilder contents = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                contents.append(line).append('\n');
            }
            config.loadFromString(contents.toString());
        }
        return config;
    }

    private static boolean isNewSection(String line) {
        String trimLine = line.trim();
        return trimLine.contains(": ") || trimLine.endsWith(":");
    }

    private static String getSectionPath(CommentedConfiguration commentedConfig, String line, String currentSection) {
        String newSection = line.trim().split(": ")[0];

        if (newSection.endsWith(":")) {
            newSection = newSection.substring(0, newSection.length() - 1);
        }
        if (newSection.startsWith(".")) {
            newSection = newSection.substring(1);
        }
        if (newSection.startsWith("'") && newSection.endsWith("'")) {
            newSection = newSection.substring(1, newSection.length() - 1);
        }

        if (!currentSection.isEmpty() && commentedConfig.contains(currentSection + "." + newSection)) {
            newSection = currentSection + "." + newSection;
        } else {
            String parentSection = currentSection;

            while (!parentSection.isEmpty()
                    && !commentedConfig.contains((parentSection = getParentPath(parentSection)) + "." + newSection)) {
                // Condition walks parentSection toward the root.
            }

            newSection = parentSection.trim().isEmpty() ? newSection : parentSection + "." + newSection;
        }

        return newSection;
    }

    private static boolean isComment(String line) {
        String trimLine = line.trim();
        return trimLine.startsWith("#") || trimLine.isEmpty();
    }

    private static String getParentPath(String path) {
        return path.contains(".") ? path.substring(0, path.lastIndexOf('.')) : "";
    }

    private static void correctIndexes(ConfigurationSection section, ConfigurationSection target) {
        List<Pair<String, Object>> sectionMap = getSectionMap(section);
        List<Pair<String, Object>> correctOrder = new ArrayList<>();

        for (Pair<String, Object> entry : sectionMap) {
            correctOrder.add(new Pair<>(entry.getKey(), target.get(entry.getKey())));
        }

        clearConfiguration(target);

        for (Pair<String, Object> entry : correctOrder) {
            target.set(entry.getKey(), entry.getValue());
        }
    }

    private static List<Pair<String, Object>> getSectionMap(ConfigurationSection section) {
        List<Pair<String, Object>> list = new ArrayList<>();

        for (String key : section.getKeys(false)) {
            list.add(new Pair<>(key, section.get(key)));
        }

        return list;
    }

    private static void clearConfiguration(ConfigurationSection section) {
        for (String key : section.getKeys(false)) {
            section.set(key, null);
        }
    }

    private static boolean isIgnoredPath(String path, String[] ignoredSections) {
        for (String ignored : ignoredSections) {
            if (path.contains(ignored)) {
                return true;
            }
        }
        return false;
    }

    public void syncWithConfig(@NonNull File file, @NonNull InputStream resource, String... ignoredSections)
            throws IOException, InvalidConfigurationException {
        Objects.requireNonNull(file, "file");
        Objects.requireNonNull(resource, "resource");
        CommentedConfiguration cfg = loadConfiguration(resource);
        if (syncConfigurationSection(cfg, cfg.getConfigurationSection(""), ignoredSections)) {
            save(file);
        }
    }

    public void setComment(String path, String comment) {
        if (comment == null) {
            configComments.remove(path);
        } else {
            configComments.put(path, comment);
        }
    }

    public String getComment(String path) {
        return getComment(path, null);
    }

    public String getComment(String path, String def) {
        return configComments.getOrDefault(path, def);
    }

    public boolean containsComment(String path) {
        return getComment(path) != null;
    }

    @Override
    public void loadFromString(String contents) throws InvalidConfigurationException {
        super.loadFromString(contents);

        String[] lines = contents.split("\n", -1);
        int currentIndex = 0;

        StringBuilder comments = new StringBuilder();
        String currentSection = "";

        while (currentIndex < lines.length) {
            if (isComment(lines[currentIndex])) {
                comments.append(lines[currentIndex]).append("\n");
            } else if (isNewSection(lines[currentIndex])) {
                currentSection = getSectionPath(this, lines[currentIndex], currentSection);

                if (comments.length() > 1) {
                    setComment(currentSection, comments.substring(0, comments.length() - 1));
                }

                comments = new StringBuilder();
            }

            currentIndex++;
        }
    }

    @Override
    public String saveToString() {
        this.options().header(null);

        List<String> lines = new ArrayList<>(Arrays.asList(super.saveToString().split("\n", -1)));

        int currentIndex = 0;
        String currentSection = "";

        while (currentIndex < lines.size()) {
            String line = lines.get(currentIndex);

            if (isNewSection(line)) {
                currentSection = getSectionPath(this, line, currentSection);
                if (containsComment(currentSection)) {
                    lines.add(currentIndex, getComment(currentSection));
                    currentIndex++;
                }
            }

            currentIndex++;
        }

        return String.join("\n", lines);
    }

    private boolean syncConfigurationSection(CommentedConfiguration commentedConfig, ConfigurationSection section, String[] ignoredSections) {
        boolean changed = false;

        for (String key : section.getKeys(false)) {
            String path = section.getCurrentPath().isEmpty() ? key : section.getCurrentPath() + "." + key;

            if (section.isConfigurationSection(key)) {
                boolean ignored = isIgnoredPath(path, ignoredSections);
                boolean containsSection = contains(path);
                if (!containsSection || !ignored) {
                    changed = syncConfigurationSection(commentedConfig, section.getConfigurationSection(key), ignoredSections) || changed;
                }
            } else if (!contains(path)) {
                set(path, section.get(key));
                changed = true;
            }

            if (commentedConfig.containsComment(path)
                    && !Objects.equals(commentedConfig.getComment(path), getComment(path))) {
                setComment(path, commentedConfig.getComment(path));
                changed = true;
            }

        }

        if (changed) {
            correctIndexes(section, getConfigurationSection(section.getCurrentPath()));
        }

        return changed;
    }

}
