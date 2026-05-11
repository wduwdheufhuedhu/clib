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

    public static final String B = BOLD;
    public static final String B_BLUE = BLUE + B;
    public static final String B_AQUA = AQUA + B;
    public static final String B_YELLOW = YELLOW + B;
    public static final String B_RED = RED + B;
    public static final String B_GRAY = GRAY + B;
    public static final String B_GOLD = GOLD + B;
    public static final String B_GREEN = GREEN + B;
    public static final String B_WHITE = WHITE + B;
    public static final String B_BLACK = BLACK + B;
    public static final String B_DARK_AQUA = DARK_AQUA + B;
    public static final String B_DARK_GREEN = DARK_GREEN + B;
    public static final String B_DARK_PURPLE = DARK_PURPLE + B;
    public static final String B_DARK_RED = DARK_RED + B;
    public static final String B_LIGHT_PURPLE = LIGHT_PURPLE + B;

    public static final String M = MAGIC;
    public static final String O = MAGIC;
    public static final String I = ITALIC;
    public static final String I_BLUE = BLUE + I;
    public static final String I_AQUA = AQUA + I;
    public static final String I_YELLOW = YELLOW + I;
    public static final String I_RED = RED + I;
    public static final String I_GRAY = GRAY + I;
    public static final String I_GOLD = GOLD + I;
    public static final String I_GREEN = GREEN + I;
    public static final String I_WHITE = WHITE + I;
    public static final String I_BLACK = BLACK + I;

    public static final String S = STRIKE_THROUGH;
    public static final String R = RESET;

    public static final String D_BLUE = DARK_BLUE;
    public static final String BD_BLUE = D_BLUE + B;
    public static final String ID_BLUE = D_BLUE + I;
    public static final String D_AQUA = DARK_AQUA;
    public static final String BD_AQUA = D_AQUA + B;
    public static final String ID_AQUA = D_AQUA + I;
    public static final String D_GRAY = DARK_GRAY;
    public static final String BD_GRAY = D_GRAY + B;
    public static final String ID_GRAY = D_GRAY + I;
    public static final String D_GREEN = DARK_GREEN;
    public static final String BD_GREEN = D_GREEN + B;
    public static final String ID_GREEN = D_GREEN + I;
    public static final String D_PURPLE = DARK_PURPLE;
    public static final String BD_PURPLE = D_PURPLE + B;
    public static final String ID_PURPLE = D_PURPLE + I;
    public static final String D_RED = DARK_RED;
    public static final String BD_RED = D_RED + B;
    public static final String ID_RED = D_RED + I;
    public static final String L_PURPLE = LIGHT_PURPLE;
    public static final String BL_PURPLE = L_PURPLE + B;
    public static final String IL_PURPLE = L_PURPLE + I;

    public static final String U_GREEN = U + GREEN;
    public static final String U_GRAY = U + GRAY;
    public static final String U_WHITE = U + WHITE;
    public static final String U_GOLD = U + GOLD;
    public static final String U_YELLOW = U + YELLOW;
    public static final String U_LIGHT_PURPLE = U + LIGHT_PURPLE;
    public static final String U_AQUA = U + AQUA;
    public static final String U_DARK_AQUA = U + DARK_AQUA;

    private static final char SECTION = ChatColor.COLOR_CHAR;
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");
    private static final Pattern LEGACY_HEX_STRIP = Pattern.compile("(?i)§x(?:§[0-9a-f]){6}");
    private static final Pattern LEGACY_CODES_STRIP = Pattern.compile("(?i)[&§][0-9a-fk-orx]");
    private static final Pattern INLINE_HEX_STRIP = Pattern.compile("(?i)&#[0-9a-f]{6}");

    public static String PRIMARY = YELLOW;
    public static String B_PRIMARY = PRIMARY + B;
    public static String SECONDARY = GOLD;
    public static String B_SECONDARY = SECONDARY + B;
    public static String TERTIARY = GRAY;
    public static String B_TERTIARY = TERTIARY + B;

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
        return ChatColor.getLastColors(input);
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
