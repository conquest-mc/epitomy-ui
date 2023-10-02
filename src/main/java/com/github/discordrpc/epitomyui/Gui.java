package com.github.discordrpc.epitomyui;

import com.github.discordrpc.epitomyui.utils.GuiSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Gui implements InventoryHolder {
    private final Inventory inventory;
    private final Map<Integer, GuiItem> interactables;

    @SuppressWarnings("deprecation")
    public Gui(String title, @Nonnegative int rows) {
        this.inventory = Bukkit.createInventory(this, rows * 9, title);
        this.interactables = new HashMap<>(rows * 9);
    }

    @SuppressWarnings("deprecation")
    public Gui(@Nonnull InventoryType type, String title) {
        this.inventory = Bukkit.createInventory(this, type, title);
        this.interactables = new HashMap<>(type.getDefaultSize());
    }

    /**
     * Opens the GUI for the given player.
     *
     * @param player The player to open the GUI for
     */
    public void open(Player player) {
        UIProvider.registerUI(player.getUniqueId(), this);
        player.openInventory(this.inventory);
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

    /**
     * Called when an item in the inventory is clicked.
     *
     * @param event The {@link InventoryClickEvent}
     * @return True if the event should be cancelled, otherwise false
     */
    public abstract boolean onClick(InventoryClickEvent event);

    /**
     * Called when an item in the inventory is dragged.
     *
     * @param event The {@link InventoryDragEvent}
     * @return True if the event should be cancelled, otherwise false
     */
    public abstract boolean onDrag(InventoryDragEvent event);

    @Override
    public @Nonnull Inventory getInventory() {
        return this.inventory;
    }

    public @Nonnull Map<Integer, GuiItem> getInteractables() {
        return this.interactables;
    }

    public @Nonnull Map<Integer, ItemStack> getItems() {
        final int size = this.inventory.getSize();
        final Map<Integer, ItemStack> items = new HashMap<>(size);
        for (int slot = 0; slot < size; slot++) {
            ItemStack item = this.inventory.getItem(slot);
            if (item == null) continue;
            items.put(slot, item);
        }
        return items;
    }
}
