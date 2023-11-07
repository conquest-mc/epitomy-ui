package com.github.conquestmc.epitomyui.exceptions;

import javax.annotation.Nonnull;

public class NonUniqueStateException extends Exception {
    private final String stateId;

    public NonUniqueStateException(@Nonnull final String stateId) {
        super("A state with the ID \"" + stateId + "\" already exists on this item");
        this.stateId = stateId;
    }

    public String getStateId() {
        return this.stateId;
    }
}
