package com.github.conquestmc.epitomyui.events;

import com.github.conquestmc.epitomyui.items.CyclingGuiItem;
import com.github.conquestmc.epitomyui.utils.Logging;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.logging.Level;

public class CyclingGuiItemStateChangeEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled;
    private final CyclingGuiItem cycler;
    private final CyclingGuiItem.State oldState;
    private CyclingGuiItem.State newState;

    public CyclingGuiItemStateChangeEvent(@Nonnull final CyclingGuiItem cycler, @Nonnull final CyclingGuiItem.State oldState, @Nonnull final CyclingGuiItem.State newState) {
        this.cancelled = false;
        this.cycler = cycler;
        this.oldState = oldState;
        this.newState = newState;
    }

    @Override
    public @Nonnull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static @Nonnull HandlerList getHandlerList() {
        return HANDLERS;
    }

    public @Nonnull CyclingGuiItem getCycler() {
        return this.cycler;
    }

    public @Nonnull CyclingGuiItem.State getOldState() {
        return this.oldState;
    }

    public @Nonnull CyclingGuiItem.State getNewState() {
        return this.newState;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setNewState(@Nonnull final CyclingGuiItem.State newState) {
        this.newState = newState;
    }

    public void setNewState(@Nonnull final String stateId) {
        try {
            final CyclingGuiItem.State state = this.cycler.getState(stateId);
            if (state == null) throw new NullPointerException("Could not find State with ID \"" + stateId + "\"");
            else this.newState = state;
        } catch (NullPointerException e) {
            Logging.LOGGER.log(Level.SEVERE, "A state with the ID \"" + stateId + "\" does not exist on the CyclingGuiItem", e);
        }
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
