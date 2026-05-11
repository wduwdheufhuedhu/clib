package com.conaxgames.libraries.message;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NavigableMap;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.regex.Pattern;

public final class FormatUtil {

    private static final char SECTION = '\u00a7';
    private static final Pattern FORMATTING = Pattern.compile("^.*(?<format>(" + SECTION + "[0-9a-fklmor])+).*");
    private static final Pattern FORMAT_CODES = Pattern.compile("(" + SECTION + "|&)[0-9a-fklmor]");

    private static final NavigableMap<Integer, String> ROMAN_NUMERALS = new TreeMap<>();
    static {
        ROMAN_NUMERALS.put(1000, "M"); ROMAN_NUMERALS.put(900, "CM");
        ROMAN_NUMERALS.put(500, "D");  ROMAN_NUMERALS.put(400, "CD");
        ROMAN_NUMERALS.put(100, "C");  ROMAN_NUMERALS.put(90, "XC");
        ROMAN_NUMERALS.put(50, "L");   ROMAN_NUMERALS.put(40, "XL");
        ROMAN_NUMERALS.put(10, "X");   ROMAN_NUMERALS.put(9, "IX");
        ROMAN_NUMERALS.put(5, "V");    ROMAN_NUMERALS.put(4, "IV");
        ROMAN_NUMERALS.put(1, "I");    ROMAN_NUMERALS.put(0, "");
    }

    private FormatUtil() {}

    public static String stripFormatting(String format) {
        if (format == null || format.isBlank()) return "";
        return FORMAT_CODES.matcher(format).replaceAll("");
    }

    public static String normalize(String format) {
        if (format == null || format.isBlank()) return "";
        return format.replaceAll("(" + SECTION + "|&)([0-9a-fklmor])", SECTION + "$2");
    }

    public static List<String> wordWrap(String s) {
        return wordWrap(s, 32, 32);
    }

    public static List<String> wordWrap(String s, int lineSize) {
        return wordWrap(s, lineSize, lineSize);
    }

    public static List<String> wordWrap(String s, int firstSegment, int lineSize) {
        var format = getFormatPrefix(s);
        var words = new ArrayList<String>();
        int numChars = firstSegment;
        int start = 0;
        int ix = 0;

        while (ix < s.length()) {
            ix = s.indexOf(' ', ix + 1);
            if (ix == -1) break;

            var sub = s.substring(start, ix).trim();
            int visibleLen = stripFormatting(sub).length() + 1;
            if (visibleLen >= numChars && !sub.isEmpty()) {
                var f = getFormatPrefix(sub);
                if (f != null && sub.startsWith(f)) format = f;
                words.add(applyFormat(format, sub));
                numChars = lineSize;
                start = ix + 1;
            }
        }
        words.add(applyFormat(format, s.substring(start).trim()));
        return words;
    }

    public static List<String> wordWrapStrict(String s, int lineLength) {
        var lines = new ArrayList<String>();
        var format = getFormatPrefix(s);
        if (format == null || !s.startsWith(format)) format = "";

        var sb = new StringBuilder();
        for (var word : s.split(" ")) {
            var testLine = stripFormatting(sb + " " + word).trim();
            if (testLine.length() <= lineLength) {
                if (!sb.isEmpty()) sb.append(' ');
                sb.append(word);
            } else if (sb.isEmpty() || stripFormatting(word).length() > lineLength) {
                var f = getFormatPrefix(word);
                var stripped = stripFormatting(word);
                while (!stripped.isEmpty()) {
                    int take = Math.min(stripped.length(), lineLength - (sb.isEmpty() ? 0 : sb.length() + 1));
                    if (!sb.isEmpty()) sb.append(' ');
                    sb.append(stripped, 0, take);
                    lines.add(applyFormat(format, sb.toString()));
                    sb.setLength(0);
                    stripped = stripped.substring(take);
                    if (f != null) format = f;
                }
            } else {
                lines.add(applyFormat(format, sb.toString()));
                var f = getFormatPrefix(sb.toString());
                if (f != null) format = f;
                sb.setLength(0);
                sb.append(word);
            }
        }
        if (!sb.isEmpty()) {
            lines.add(applyFormat(format, sb.toString()));
        }
        return lines;
    }

    public static String join(List<String> list, String separator) {
        return String.join(separator, list);
    }

    public static List<String> prefix(List<String> list, String prefix) {
        return list.stream().map(s -> prefix + s).toList();
    }

    public static String camelcase(String name) {
        if (name == null || name.isEmpty()) return "";
        var sb = new StringBuilder(name.length());
        for (var part : name.split("[ _]")) {
            if (part.isEmpty()) continue;
            sb.append(Character.toUpperCase(part.charAt(0)));
            if (part.length() > 1) sb.append(part.substring(1).toLowerCase());
        }
        return sb.toString();
    }

    public static String escape(String formatString) {
        return normalize(formatString).replace(SECTION, '&');
    }

    public static String possessiveString(String str) {
        return str + (str.endsWith("s") ? "'" : "'s");
    }

    public static String formatTps(double tps) {
        double clamped = Math.min(tps, 20.0);
        var color = tps > 18.0 ? ChatColor.GREEN : tps > 16.0 ? ChatColor.YELLOW : ChatColor.RED;
        return color + (tps > 20.0 ? "*" : "") + "%.2f".formatted(clamped);
    }

    public static String toRoman(int number) {
        if (number <= 0) return "";
        int key = ROMAN_NUMERALS.floorKey(number);
        if (number == key) return ROMAN_NUMERALS.get(key);
        return ROMAN_NUMERALS.get(key) + toRoman(number - key);
    }

    public static String getItemName(ItemStack item) {
        return item.getType().toString().replace("_", "");
    }

    public static String andJoin(Collection<String> collection, boolean delimiterBeforeAnd) {
        return andJoin(collection, delimiterBeforeAnd, ", ");
    }

    public static String andJoin(Collection<String> collection, boolean delimiterBeforeAnd, String delimiter) {
        if (collection == null || collection.isEmpty()) return "";
        var contents = new ArrayList<>(collection);
        var last = contents.removeLast();
        if (contents.isEmpty()) return last;

        var joiner = new StringJoiner(delimiter);
        contents.forEach(joiner::add);
        var sb = new StringBuilder(joiner.toString());
        if (delimiterBeforeAnd) sb.append(delimiter);
        return sb.append(" and ").append(last).toString();
    }

    private static String applyFormat(String format, String text) {
        return (format != null && !text.startsWith(String.valueOf(SECTION))) ? format + text : text;
    }

    private static String getFormatPrefix(String s) {
        if (s == null) return null;
        var m = FORMATTING.matcher(s);
        return m.matches() ? m.group("format") : null;
    }
}
