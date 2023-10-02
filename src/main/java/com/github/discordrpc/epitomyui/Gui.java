package com.github.discordrpc.epitomyui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.HashMap;
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

    public void open(Player player) {
        UIProvider.registerUI(player.getUniqueId(), this);
        player.openInventory(this.inventory);
    }

    public abstract boolean onClick(InventoryClickEvent event);

    public abstract boolean onDrag(InventoryDragEvent event);

    @Override
    public @Nonnull Inventory getInventory() {
        return this.inventory;
    }

    public @Nonnull Map<Integer, GuiItem> getInteractables() {
        return this.interactables;
    }
}
