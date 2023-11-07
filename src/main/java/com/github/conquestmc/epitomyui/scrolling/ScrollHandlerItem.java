package com.github.conquestmc.epitomyui.scrolling;

import com.github.conquestmc.epitomyui.items.GuiItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public class ScrollHandlerItem extends GuiItem {
    private final ScrollDirection direction;

    public ScrollHandlerItem(@Nonnull final ScrollDirection direction, @Nonnull final Material material) {
        this(direction, new ItemStack(material));
    }

    public ScrollHandlerItem(@Nonnull final ScrollDirection direction, @Nonnull final Material material, @Nonnegative final int amount) {
        this(direction, new ItemStack(material, amount));
    }

    public ScrollHandlerItem(@Nonnull final ScrollDirection direction, @Nonnull final ItemStack item) {
        super(item);
        this.direction = direction;
    }

    /**
     * @return The {@link ScrollDirection}
     */
    public @Nonnull ScrollDirection getScrollDirection() {
        return this.direction;
    }
}
