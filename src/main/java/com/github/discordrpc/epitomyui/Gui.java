package com.github.discordrpc.epitomyui;

import com.github.discordrpc.epitomyui.utils.GuiSerializer;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.List;

public abstract class Gui extends GuiBase {

    @SuppressWarnings("deprecation")
    public Gui(String title, @Nonnegative int rows) {
        super(title, rows);
    }

    @SuppressWarnings("deprecation")
    public Gui(@Nonnull InventoryType type, String title) {
        super(type, title);
    }

    /**
     * Fills the entire inventory with the given item.
     *
     * @param item The item to fill the inventory with
     */
    public void fill(@Nonnull GuiItem item) {
        for (int slot = 0; slot < this.inventory.getSize(); slot++) {
            this.interactables.put(slot, item);
            this.inventory.setItem(slot, item.getItem());
        }
    }

    /**
     * Fills the entire inventory with the given item.
     *
     * @param item The item to fill the inventory with
     */
    public void fill(ItemStack item) {
        for (int slot = 0; slot < this.inventory.getSize(); slot++) {
            this.inventory.setItem(slot, item);
        }
    }

    /**
     * Fills all empty slots in the inventory with the given item.
     *
     * @param item The item to fill the empty slots with
     */
    public void fillEmpty(@Nonnull GuiItem item) {
        for (int slot = 0; slot < this.inventory.getSize(); slot++) {
            if (this.inventory.getItem(slot) != null) continue;
            this.interactables.put(slot, item);
            this.inventory.setItem(slot, item.getItem());
        }
    }

    /**
     * Fills all empty slots in the inventory with the given item.
     *
     * @param item The item to fill the empty slots with
     */
    public void fillEmpty(@Nonnull ItemStack item) {
        for (int slot = 0; slot < this.inventory.getSize(); slot++) {
            if (this.inventory.getItem(slot) != null) continue;
            this.inventory.setItem(slot, item);
        }
    }

    /**
     * Sets the given slots to the given item.
     *
     * @param slotStr The string of slots to set
     * @param item The item to set
     */
    public void setItem(final String slotStr, @Nonnull GuiItem item) {
        final List<Integer> slots = GuiSerializer.parseSlotString(slotStr);
        for (final int slot : slots) {
            setItem(slot, item);
        }
    }

    /**
     * Sets the given slot to the given item.
     *
     * @param slot The slot to set
     * @param item The item to set
     */
    public void setItem(@Nonnegative final int slot, @Nonnull GuiItem item) {
        if (slot >= inventory.getSize()) return;
        this.interactables.put(slot, item);
        this.inventory.setItem(slot, item.getItem());
    }

    /**
     * Sets the given slots to the given item.
     *
     * @param slotStr The string of slots to set
     * @param item The item to set
     */
    public void setItem(final String slotStr, ItemStack item) {
        final List<Integer> slots = GuiSerializer.parseSlotString(slotStr);
        for (final int slot : slots) {
            setItem(slot, item);
        }
    }

    /**
     * Sets the given slot to the given item.
     *
     * @param slot The slot to set
     * @param item The item to set
     */
    public void setItem(@Nonnegative final int slot, ItemStack item) {
        if (slot >= inventory.getSize()) return;
        this.inventory.setItem(slot, item);
    }
}
