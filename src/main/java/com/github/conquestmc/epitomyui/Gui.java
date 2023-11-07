package com.github.conquestmc.epitomyui;

import com.github.conquestmc.epitomyui.items.GuiItem;
import com.github.conquestmc.epitomyui.utils.GuiSerializer;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class Gui extends GuiBase {

    public Gui(@Nullable final String title, @Nonnegative final int rows) {
        super(title, rows);
    }

    public Gui(@Nonnull final InventoryType type, @Nullable final String title) {
        super(type, title);
    }

    /**
     * Fills the entire inventory with the given item.
     *
     * @param item The item to fill the inventory with
     */
    public void fill(@Nonnull final GuiItem item) {
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
    public void fill(@Nullable final ItemStack item) {
        for (int slot = 0; slot < this.inventory.getSize(); slot++) {
            this.inventory.setItem(slot, item);
        }
    }

    /**
     * Fills all empty slots in the inventory with the given item.
     *
     * @param item The item to fill the empty slots with
     */
    public void fillEmpty(@Nonnull final GuiItem item) {
        for (int slot = 0; slot < this.inventory.getSize(); slot++) {
            if (this.inventory.getItem(slot) != null) continue;
            this.interactables.put(slot, item);
            this.inventory.setItem(slot, item.getItem());
        }
    }

    /**
     * Fills all empty slots in the inventory with the given material.
     *
     * @param material The material to fill the empty slots with
     */
    public void fillEmpty(@Nonnull final Material material) {
        fillEmpty(new ItemStack(material));
    }

    /**
     * Fills all empty slots in the inventory with the given item.
     *
     * @param item The item to fill the empty slots with
     */
    public void fillEmpty(@Nonnull final ItemStack item) {
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
    public void setItem(@Nonnull final String slotStr, @Nonnull final GuiItem item) {
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
    public void setItem(@Nonnegative final int slot, @Nonnull final GuiItem item) {
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
    public void setItem(@Nonnull final String slotStr, @Nullable final ItemStack item) {
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
    public void setItem(@Nonnegative final int slot, @Nullable final ItemStack item) {
        if (slot >= inventory.getSize()) return;
        this.interactables.remove(slot);
        this.inventory.setItem(slot, item);
    }
}
