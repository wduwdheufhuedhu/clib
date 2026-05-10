package com.conaxgames.libraries.config;

import com.conaxgames.libraries.LibraryPlugin;
import com.conaxgames.libraries.util.Pair;
import lombok.NonNull;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * {@link YamlConfiguration} that records {@code #} comment blocks preceding keys and re-inserts them on save.
 * <p>
 * Requires {@link LibraryPlugin} to be initialized before static loaders run so messages use {@link com.conaxgames.libraries.debug.LibraryLogger}.
 */
public final class CommentedConfiguration extends YamlConfiguration {

    private static final String LOG_LABEL = "CommentedConfiguration";

    private final Map<String, String> configComments = new HashMap<>();
    private boolean creationFailure;

    public CommentedConfiguration() {
        try {
            this.options().parseComments(false);
        } catch (Throwable ignored) {
            // Older Paper/Bukkit API: YamlConfigurationOptions.parseComments(boolean) absent
        }
    }

    /**
     * Loads from a UTF-8 file. On missing file or read failure, returns a failed instance ({@link #hasFailed()}).
     */
    public static CommentedConfiguration loadConfiguration(@NonNull File file) {
        try (BufferedReader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
            return loadConfiguration(reader);
        } catch (NoSuchFileException e) {
            LibraryPlugin.getInstance().getLibraryLogger().toConsole(LOG_LABEL,
                    "File " + file.getName() + " doesn't exist.");
            return new CommentedConfiguration().flagAsFailed();
        } catch (IOException e) {
            LibraryPlugin.getInstance().getLibraryLogger().toConsole(LOG_LABEL,
                    "Could not read file " + file.getName(), e);
            return new CommentedConfiguration().flagAsFailed();
        }
    }

    /**
     * Loads from a resource stream (caller-owned lifecycle). Null stream yields a failed instance.
     */
    public static CommentedConfiguration loadConfiguration(InputStream inputStream) {
        if (inputStream == null) {
            LibraryPlugin.getInstance().getLibraryLogger().toConsole(LOG_LABEL,
                    "InputStream cannot be null!");
            return new CommentedConfiguration().flagAsFailed();
        }
        return loadConfiguration(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }

    /**
     * Loads YAML text from a reader (UTF-8 should be ensured by the caller when wrapping byte streams).
     */
    public static CommentedConfiguration loadConfiguration(Reader reader) {
        CommentedConfiguration config = new CommentedConfiguration();
        try (BufferedReader bufferedReader = reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader)) {
            StringBuilder contents = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                contents.append(line).append('\n');
            }
            config.loadFromString(contents.toString());
        } catch (IOException | InvalidConfigurationException ex) {
            config.flagAsFailed();
            LibraryPlugin.getInstance().getLibraryLogger().toConsole(LOG_LABEL,
                    "Failed to load YAML configuration from reader.", ex);
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
                    && !commentedConfig.contains((parentSection = getParentPath(parentSection)) + "." + newSection))
                ;

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
            correctOrder.add(Pair.of(entry.getKey(), target.get(entry.getKey())));
        }

        clearConfiguration(target);

        for (Pair<String, Object> entry : correctOrder) {
            target.set(entry.getKey(), entry.getValue());
        }
    }

    private static List<Pair<String, Object>> getSectionMap(ConfigurationSection section) {
        List<Pair<String, Object>> list = new ArrayList<>();
        for (String key : section.getKeys(false)) {
            list.add(Pair.of(key, section.get(key)));
        }
        return list;
    }

    private static void clearConfiguration(ConfigurationSection section) {
        for (String key : section.getKeys(false)) {
            section.set(key, null);
        }
    }

    /**
     * Merges missing keys and comment metadata from {@code resource} into this configuration, then saves to {@code file}.
     *
     * @param ignoredSections subtree paths to skip when traversing defaults (substring match)
     */
    public void syncWithConfig(File file, InputStream resource, String... ignoredSections) throws IOException {
        if (creationFailure) {
            return;
        }

        if (file == null) {
            LibraryPlugin.getInstance().getLibraryLogger().toConsole(LOG_LABEL,
                    "File cannot be null when using syncWithConfig");
            return;
        }

        if (resource == null) {
            LibraryPlugin.getInstance().getLibraryLogger().toConsole(LOG_LABEL,
                    "Input stream cannot be null when using syncWithConfig");
            return;
        }

        CommentedConfiguration defaults = loadConfiguration(resource);
        if (syncConfigurationSection(defaults, defaults.getConfigurationSection(""), Arrays.asList(ignoredSections))) {
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

    /**
     * Whether loading failed; callers should avoid using values from this instance.
     */
    public boolean hasFailed() {
        return creationFailure;
    }

    @Override
    public void loadFromString(String contents) throws InvalidConfigurationException {
        super.loadFromString(contents);

        String[] lines = contents.split("\n");
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

        List<String> lines = new ArrayList<>(Arrays.asList(super.saveToString().split("\n")));

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

        StringBuilder contents = new StringBuilder();
        for (String line : lines) {
            contents.append("\n").append(line);
        }

        return contents.length() == 0 ? "" : contents.substring(1);
    }

    private boolean syncConfigurationSection(CommentedConfiguration commentedConfig, ConfigurationSection section, List<String> ignoredSections) {
        boolean changed = false;

        for (String key : section.getKeys(false)) {
            String path = section.getCurrentPath().isEmpty() ? key : section.getCurrentPath() + "." + key;

            if (section.isConfigurationSection(key)) {
                boolean isIgnored = ignoredSections.stream().anyMatch(path::contains);
                boolean containsSection = contains(path);
                if (!containsSection || !isIgnored) {
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

    private CommentedConfiguration flagAsFailed() {
        creationFailure = true;
        return this;
    }

}
