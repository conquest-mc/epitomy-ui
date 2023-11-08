package com.github.conquestmc.epitomyui;

import com.github.conquestmc.epitomyui.events.CyclingGuiItemStateChangeEvent;
import com.github.conquestmc.epitomyui.items.GuiInteractable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UIProvider implements Listener {
    private static final Map<UUID, GuiBase> uis = new HashMap<>();

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;
        uis.remove(player.getUniqueId());
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getAction() == InventoryAction.UNKNOWN) {
            event.setCancelled(true);
            return;
        }

        final GuiBase gui = uis.get(player.getUniqueId());
        if (gui == null) return;

        final boolean cancel = gui.onClick(event);
        if (cancel) event.setCancelled(true);

        GuiInteractable item = gui.getInteractables().get(event.getRawSlot());
        if (item == null) return;
        item.getClickEvent().accept(event);

        final int slot = event.getRawSlot();
        final ItemStack newStack = item.getItem();
        final ItemStack currentStack = gui.getInventory().getItem(slot);
        if (currentStack == null || currentStack.equals(newStack)) return;
        gui.getInventory().setItem(slot, newStack);
    }

    public static void registerUI(UUID uuid, GuiBase gui) {
        uis.put(uuid, gui);
    }
}
