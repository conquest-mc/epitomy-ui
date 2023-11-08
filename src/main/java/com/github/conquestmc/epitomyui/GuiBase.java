package com.github.conquestmc.epitomyui;

import com.github.conquestmc.epitomyui.items.GuiInteractable;
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
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public abstract class GuiBase implements InventoryHolder {
    protected final Inventory inventory;
    protected final Map<Integer, GuiInteractable> interactables;

    @SuppressWarnings("deprecation")
    public GuiBase(@Nullable final String title, @Nonnegative int rows) {
        if (title == null) this.inventory = Bukkit.createInventory(this, rows * 9);
        else this.inventory = Bukkit.createInventory(this, rows * 9, title);
        this.interactables = new HashMap<>(rows * 9);
    }

    @SuppressWarnings("deprecation")
    public GuiBase(@Nonnull final InventoryType type, @Nullable final String title) {
        if (title == null) this.inventory = Bukkit.createInventory(this, type);
        else this.inventory = Bukkit.createInventory(this, type, title);
        this.interactables = new HashMap<>(type.getDefaultSize());
    }

    /**
     * Opens the GUI for the given player.
     *
     * @param player The player to open the GUI for
     */
    public void open(@Nonnull final Player player) {
        UIProvider.registerUI(player.getUniqueId(), this);
        player.openInventory(this.inventory);
    }

    /**
     * Updates all interactable items in the inventory.
     */
    public void update() {
        for (final Map.Entry<Integer, GuiInteractable> entry : this.interactables.entrySet()) {
            final int slot = entry.getKey();
            final ItemStack item = entry.getValue().getItem();
            this.inventory.setItem(slot, item);
        }
    }

    public abstract void setItem(@Nonnegative final int slot, @Nonnull final GuiInteractable item);

    /**
     * Called when an item in the inventory is clicked.
     *
     * @param event The {@link InventoryClickEvent}
     * @return True if the event should be cancelled, otherwise false
     */
    public boolean onClick(@Nonnull final InventoryClickEvent event) {
        return true;
    }

    /**
     * Called when an item in the inventory is dragged.
     *
     * @param event The {@link InventoryDragEvent}
     * @return True if the event should be cancelled, otherwise false
     */
    public boolean onDrag(@Nonnull final InventoryDragEvent event) {
        return true;
    }

    /**
     * @return The {@link Inventory} contained in the {@link InventoryHolder}
     */
    @Override
    public @Nonnull Inventory getInventory() {
        return this.inventory;
    }

    /**
     * @return The interactable {@link GuiInteractable}s in the Gui
     */
    public @Nonnull Map<Integer, GuiInteractable> getInteractables() {
        return this.interactables;
    }

    /**
     * @return All {@link ItemStack}s contained in the {@link Inventory}
     */
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
