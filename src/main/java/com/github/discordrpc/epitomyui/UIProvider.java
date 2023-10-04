package com.github.discordrpc.epitomyui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

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

        final GuiBase gui = uis.get(player.getUniqueId());
        if (gui == null) return;

        final boolean cancel = gui.onClick(event);
        if (cancel) event.setCancelled(true);
        else if (event.getAction() == InventoryAction.UNKNOWN) event.setCancelled(true);

        GuiItem item = gui.getInteractables().get(event.getRawSlot());
        if (item == null) return;
        item.getClickEvent().accept(event);
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        final GuiBase gui = uis.get(player.getUniqueId());
        if (gui == null) return;

        final boolean cancel = gui.onDrag(event);
        event.setCancelled(cancel);

        for (final int slot : event.getRawSlots()) {
            GuiItem item = gui.getInteractables().get(slot);
            if (item == null) return;
            item.getDragEvent().accept(event);
        }
    }

    public static void registerUI(UUID uuid, GuiBase gui) {
        uis.put(uuid, gui);
    }
}
