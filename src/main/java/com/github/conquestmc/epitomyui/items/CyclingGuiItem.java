package com.github.conquestmc.epitomyui.items;

import com.github.conquestmc.epitomyui.exceptions.NonUniqueStateException;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CyclingGuiItem extends GuiInteractable {
    private static final Logger LOGGER = Logger.getLogger("epitome-ui");
    private final HashMap<String, State> states;
    private State state;

    public CyclingGuiItem() {
        this.states = new HashMap<>();
    }

    public CyclingGuiItem(@Nonnull final State... states) {
        this.states = new HashMap<>();
        for (State state : states) this.addState(state);
    }

    /**
     * Adds a state to the dynamic item.
     *
     * @param state The state to add
     */
    public void addState(@Nonnull final State state) {
        try {
            final String id = state.id;
            if (this.states.containsKey(id)) throw new NonUniqueStateException(id);
            if (this.states.isEmpty()) {
                this.states.put(id, state);
                this.setState(id);
            }
            else {
                this.states.put(id, state);
            }
        } catch (NonUniqueStateException e) {
            LOGGER.log(Level.SEVERE, "Could not add State to DynamicGuiItem", e);
        }
    }

    /**
     * Go to the next {@link State} in the list of available
     * {@link State}s
     */
    public void nextState() {
        boolean next = false;
        final List<State> states = new ArrayList<>(this.states.values());
        for (final State state : states) {
            if (next) {
                this.setState(state.getStateId());
                return;
            }
            else if (state.getStateId().equals(this.state.getStateId())) {
                next = true;
            }
        }
        if (!states.isEmpty()) this.setState(states.get(0).getStateId());
    }

    /**
     * Set the current state to the {@link State} with
     * the given ID.
     *
     * @param stateId The ID of the state to set to
     */
    public void setState(@Nonnull final String stateId) {
        if (!this.states.containsKey(stateId)) return;
        final State state = this.states.get(stateId);
        this.state = state;
        this.item = state.getItem();
        super.onClick(event -> {
            event.setCancelled(true);
            this.state.getClickEvent().accept(event);
            this.nextState();
        });
    }

    /**
     * @return The current {@link State}
     */
    public @Nonnull State getState() {
        return this.state;
    }

    /**
     * Gets a possible state of the dynamic item.
     *
     * @param id The ID of the state to get
     * @return A {@link State} if one with the given ID exists, otherwise null
     */
    public @Nullable State getState(@Nonnull final String id) {
        return this.states.get(id);
    }

    public static class State extends GuiItem {
        private final String id;

        public State(@Nonnull final String id, @Nonnull final Material material) {
            this(id, new ItemStack(material));
        }

        public State(@Nonnull final String id, @Nonnull final Material material, @Nonnegative final int amount) {
            this(id, new ItemStack(material, amount));
        }

        public State(@Nonnull final String id, @Nullable final ItemStack item) {
            super(item);
            this.id = id;
        }

        public State(@Nonnull final String id, @Nonnull final GuiItem item) {
            super(item.getItem());
            this.id = id;
            this.onClick(item.getClickEvent());
        }

        /**
         * @return The state's unique identifier
         */
        public String getStateId() {
            return this.id;
        }
    }

    @Override
    public void onClick(@Nonnull final Consumer<InventoryClickEvent> consumer) {}
}
