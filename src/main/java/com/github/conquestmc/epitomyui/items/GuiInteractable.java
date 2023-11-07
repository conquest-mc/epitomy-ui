package com.github.conquestmc.epitomyui.items;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

public class GuiInteractable {
    protected ItemStack item;
    private Consumer<InventoryClickEvent> clickEvent;
    private Consumer<InventoryDragEvent> dragEvent;

    public GuiInteractable() {
        this.clickEvent = event -> {};
        this.dragEvent = event -> {};
    }

    public GuiInteractable(@Nullable final ItemStack item) {
        this();
        this.item = item;
    }

    /**
     * @return The {@link ItemStack} belonging to the {@link GuiInteractable}
     */
    public @Nullable ItemStack getItem() {
        return this.item;
    }

    /**
     * Called when the item is clicked on.
     *
     * @param consumer The method to execute when clicked
     */
    public void onClick(@Nonnull final Consumer<InventoryClickEvent> consumer) {
        this.clickEvent = consumer;
    }

    /**
     * @return The {@link Consumer} to run on an {@link InventoryClickEvent}
     */
    public @Nonnull Consumer<InventoryClickEvent> getClickEvent() {
        return this.clickEvent;
    }

    /**
     * Called when the item is dragged.
     *
     * @param consumer The method to execute when dragged
     */
    public void onDrag(@Nonnull final Consumer<InventoryDragEvent> consumer) {
        this.dragEvent = consumer;
    }

    /**
     * @return The {@link Consumer} to run on an {@link InventoryDragEvent}
     */
    public @Nonnull Consumer<InventoryDragEvent> getDragEvent() {
        return this.dragEvent;
    }
}
