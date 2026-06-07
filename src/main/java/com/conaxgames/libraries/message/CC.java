package com.conaxgames.libraries.message;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CC {

    private CC() {}

    public static final String U = ChatColor.UNDERLINE.toString();
    public static final String BLUE = ChatColor.BLUE.toString();
    public static final String AQUA = ChatColor.AQUA.toString();
    public static final String YELLOW = ChatColor.YELLOW.toString();
    public static final String RED = ChatColor.RED.toString();
    public static final String GRAY = ChatColor.GRAY.toString();
    public static final String GOLD = ChatColor.GOLD.toString();
    public static final String GREEN = ChatColor.GREEN.toString();
    public static final String WHITE = ChatColor.WHITE.toString();
    public static final String BLACK = ChatColor.BLACK.toString();
    public static final String BOLD = ChatColor.BOLD.toString();
    public static final String ITALIC = ChatColor.ITALIC.toString();
    public static final String STRIKE_THROUGH = ChatColor.STRIKETHROUGH.toString();
    public static final String RESET = ChatColor.RESET.toString();
    public static final String MAGIC = ChatColor.MAGIC.toString();
    public static final String DARK_BLUE = ChatColor.DARK_BLUE.toString();
    public static final String DARK_AQUA = ChatColor.DARK_AQUA.toString();
    public static final String DARK_GRAY = ChatColor.DARK_GRAY.toString();
    public static final String DARK_GREEN = ChatColor.DARK_GREEN.toString();
    public static final String DARK_PURPLE = ChatColor.DARK_PURPLE.toString();
    public static final String DARK_RED = ChatColor.DARK_RED.toString();
    public static final String LIGHT_PURPLE = ChatColor.LIGHT_PURPLE.toString();

    private static final char SECTION = ChatColor.COLOR_CHAR;
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");
    private static final Pattern LEGACY_HEX_STRIP = Pattern.compile("(?i)§x(?:§[0-9a-f]){6}");
    private static final Pattern LEGACY_CODES_STRIP = Pattern.compile("(?i)[&§][0-9a-fk-orx]");
    private static final Pattern INLINE_HEX_STRIP = Pattern.compile("(?i)&#[0-9a-f]{6}");

    public static String PRIMARY = YELLOW;
    public static String SECONDARY = GOLD;
    public static String TERTIARY = GRAY;

    public static String translate(String string) {
        if (string == null) return null;

        var result = string
                .replace("&p", PRIMARY).replace("&P", PRIMARY)
                .replace("&s", SECONDARY).replace("&S", SECONDARY)
                .replace("&t", TERTIARY).replace("&T", TERTIARY);

        return ChatColor.translateAlternateColorCodes('&', translateHex(result));
    }

    public static List<String> translate(List<String> text) {
        if (text == null) return null;
        return text.stream().map(CC::translate).toList();
    }

    public static List<String> translate(String... text) {
        return Arrays.stream(text).map(CC::translate).toList();
    }

    public static String stripAllColor(String input) {
        if (input == null) return null;
        var stripped = INLINE_HEX_STRIP.matcher(input).replaceAll("");
        stripped = LEGACY_HEX_STRIP.matcher(stripped).replaceAll("");
        stripped = LEGACY_CODES_STRIP.matcher(stripped).replaceAll("");
        return ChatColor.stripColor(stripped);
    }

    public static String getLastColors(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        String colors = "";
        String formats = "";
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) != SECTION || i + 1 >= input.length()) {
                continue;
            }
            char code = Character.toLowerCase(input.charAt(i + 1));
            if (code == 'x' && isHexSequence(input, i)) {
                colors = input.substring(i, i + 14);
                formats = "";
                i += 13;
                continue;
            }
            if ("0123456789abcdef".indexOf(code) >= 0) {
                colors = input.substring(i, i + 2);
                formats = "";
                i++;
                continue;
            }
            if ("klmor".indexOf(code) >= 0) {
                formats += input.substring(i, i + 2);
                i++;
            }
        }
        return colors + formats;
    }

    public static int safeSplitIndex(String translated, int max) {
        int at = Math.min(max, translated.length());
        while (at > 0 && isInsideFormatCode(translated, at)) {
            at--;
        }
        return at;
    }

    private static boolean isInsideFormatCode(String text, int index) {
        if (index <= 0 || index > text.length()) {
            return false;
        }
        for (int i = index - 1; i >= Math.max(0, index - 13); i--) {
            if (text.charAt(i) != SECTION) {
                continue;
            }
            if (i + 1 < text.length() && text.charAt(i + 1) == 'x' && isHexSequence(text, i)) {
                return index > i && index < i + 14;
            }
            if (index == i + 1 && i + 1 < text.length()) {
                char code = Character.toLowerCase(text.charAt(i + 1));
                if ("0123456789abcdefklmor".indexOf(code) >= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isHexSequence(String text, int start) {
        if (start + 13 >= text.length()) {
            return false;
        }
        if (text.charAt(start) != SECTION || text.charAt(start + 1) != 'x') {
            return false;
        }
        for (int i = start + 2; i <= start + 13; i += 2) {
            if (text.charAt(i) != SECTION) {
                return false;
            }
        }
        return true;
    }

    private static String translateHex(String message) {
        var matcher = HEX_PATTERN.matcher(message);
        var out = new StringBuilder(message.length() + 16);
        while (matcher.find()) {
            var rgb = matcher.group(1);
            var hex = new StringBuilder(14).append(SECTION).append('x');
            for (int i = 0; i < 6; i++) {
                hex.append(SECTION).append(rgb.charAt(i));
            }
            matcher.appendReplacement(out, Matcher.quoteReplacement(hex.toString()));
        }
        matcher.appendTail(out);
        return out.toString();
    }
}
