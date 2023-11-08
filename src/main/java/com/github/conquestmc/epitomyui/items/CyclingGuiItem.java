package com.github.conquestmc.epitomyui.items;

import com.github.conquestmc.epitomyui.events.CyclingGuiItemStateChangeEvent;
import com.github.conquestmc.epitomyui.exceptions.NonUniqueStateException;
import com.github.conquestmc.epitomyui.utils.Logging;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;


public class CyclingGuiItem extends GuiInteractable {
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
            Logging.LOGGER.log(Level.SEVERE, "Could not add State to CyclingGuiItem", e);
        }
    }

    /**
     * Go to the next {@link State} in the list of available
     * {@link State}s
     */
    public void nextState() {
        final State nextState = this.getNextState();
        if (nextState != null) this.setState(nextState);
    }

    /**
     * Sets the current state to the given {@link State}.
     * If the state does not exist on the current
     * {@link CyclingGuiItem}, it is added first.
     *
     * @param state The state to set
     */
    public void setState(@Nonnull final State state) {
        if (!this.states.containsKey(state.getStateId())) this.addState(state);
        this.setState(state.getStateId());
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

    /**
     * @return The next {@link State} in the list of available {@link State}s
     */
    public @Nullable State getNextState() {
        boolean next = false;
        final List<State> states = new ArrayList<>(this.states.values());
        for (final State state : states) {
            if (next) {
                return state;
            }
            else if (state.getStateId().equals(this.state.getStateId())) {
                next = true;
            }
        }
        if (!states.isEmpty()) return states.get(0);
        else return null;
    }

    public static class State extends GuiItem {
        private final String id;
        private Consumer<CyclingGuiItemStateChangeEvent> changeEvent;

        public State(@Nonnull final String id, @Nonnull final Material material) {
            this(id, new ItemStack(material));
        }

        public State(@Nonnull final String id, @Nonnull final Material material, @Nonnegative final int amount) {
            this(id, new ItemStack(material, amount));
        }

        public State(@Nonnull final String id, @Nullable final ItemStack item) {
            super(item);
            this.id = id;
            this.changeEvent = event -> {};
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

        public void onChange(@Nonnull final Consumer<CyclingGuiItemStateChangeEvent> consumer) {
            this.changeEvent = consumer;
        }

        public Consumer<CyclingGuiItemStateChangeEvent> getChangeEvent() {
            return this.changeEvent;
        }
    }

    @Override
    public void onClick(@Nonnull final Consumer<InventoryClickEvent> consumer) {
        this.clickEvent = event -> {
            State newState = this.getNextState();
            if (newState == null) newState = this.state;

            final CyclingGuiItemStateChangeEvent changeEvent = new CyclingGuiItemStateChangeEvent(this, this.state, newState);
            this.state.clickEvent.accept(event);
            this.state.changeEvent.accept(changeEvent);
            Bukkit.getPluginManager().callEvent(changeEvent);

            if (!changeEvent.isCancelled()) this.setState(changeEvent.getNewState());

            consumer.accept(event);
        };
    }
}
