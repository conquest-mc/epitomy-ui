package com.github.discordrpc.epitomyui.pagination;

import com.github.discordrpc.epitomyui.GuiItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ScrollItem extends GuiItem {
    private final ScrollDirection direction;

    public ScrollItem(final ScrollDirection direction, final Material material) {
        this(direction, new ItemStack(material));
    }

    public ScrollItem(final ScrollDirection direction, final Material material, final int amount) {
        this(direction, new ItemStack(material, amount));
    }

    public ScrollItem(final ScrollDirection direction, final ItemStack item) {
        super(item);
        this.direction = direction;
    }

    public ScrollDirection getScrollDirection() {
        return this.direction;
    }
}
