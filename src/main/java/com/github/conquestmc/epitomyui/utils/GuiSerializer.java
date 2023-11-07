package com.github.conquestmc.epitomyui.utils;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class GuiSerializer {
    /**
     * Parses a slot string, e.g. "0-8,9,17,18-26", to a {@link List}
     * of integers.
     *
     * @param str The slot string to parse
     * @return A {@link List} of all slots
     *
     * @author <a href="https://github.com/hamza-cskn/">Hamza Coşkun</a>
     * @author <a href="https://github.com/discordrpc">Lukas Hammer</a>
     */
    public static @Nonnull List<Integer> parseSlotString(@Nonnull String str) {
        str = str.replaceAll(" ", "");
        if (str.contains(",")) {
            return parseStringAsIntegerList(str);
        } else if (str.contains("-")) {
            return parseStringAsIntegerRange(str);
        }
        return new ArrayList<>();
    }

    /**
     * @author <a href="https://github.com/hamza-cskn/">Hamza Coşkun</a>
     */
    private static @Nonnull List<Integer> parseStringAsIntegerRange(@Nonnull final String str) {
        final String[] slots = str.split("-");
        if (slots.length != 2) return new ArrayList<>();
        int from, to;

        try {
            from = Integer.parseInt(slots[0]);
            to = Integer.parseInt(slots[1]);
        } catch (NumberFormatException ignore) {
            return new ArrayList<>();
        }

        if (from > to) return new ArrayList<>();

        final List<Integer> result = new ArrayList<>();
        while (from <= to) result.add(from++);
        return result;
    }

    /**
     * @author <a href="https://github.com/hamza-cskn/">Hamza Coşkun</a>
     * @author <a href="https://github.com/discordrpc">Lukas Hammer</a>
     */
    private static @Nonnull List<Integer> parseStringAsIntegerList(@Nonnull final String str) {
        final List<Integer> pageSlots = new ArrayList<>();
        final String[] slotStrings = str.split(",");

        for (final String slotText : slotStrings) {
            try {
                if (slotText.contains("-")) {
                    pageSlots.addAll(parseStringAsIntegerRange(slotText));
                } else {
                    pageSlots.add(Integer.parseInt(slotText));
                }
            } catch (NumberFormatException ignore) {
            }
        }
        return pageSlots;
    }
}
