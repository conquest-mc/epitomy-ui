package com.github.discordrpc.epitomyui;

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
import java.util.Map;

public abstract class GuiBase implements InventoryHolder {
    protected final Inventory inventory;
    protected final Map<Integer, GuiItem> interactables;

    @SuppressWarnings("deprecation")
    public GuiBase(String title, @Nonnegative int rows) {
        this.inventory = Bukkit.createInventory(this, rows * 9, title);
        this.interactables = new HashMap<>(rows * 9);
    }

    @SuppressWarnings("deprecation")
    public GuiBase(@Nonnull InventoryType type, String title) {
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
    public Inventory getInventory() {
        return this.inventory;
    }

    public Map<Integer, GuiItem> getInteractables() {
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
