package com.example.memorycards.model;

import java.util.Objects;

public class MemoryCard {
    private final int frontDrawableId;
    private final int backDrawableId;

    private boolean isOpen;

    public MemoryCard(int frontDrawableId, int backDrawableId) {
        this.frontDrawableId = frontDrawableId;
        this.backDrawableId = backDrawableId;
        this.isOpen = false;
    }

    public int getFrontDrawableId() {
        return frontDrawableId;
    }

    public int getBackDrawableId() {
        return backDrawableId;
    }

    public boolean isOpen() {
        return this.isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemoryCard that = (MemoryCard) o;
        return frontDrawableId == that.frontDrawableId && backDrawableId == that.backDrawableId && isOpen == that.isOpen;
    }

    @Override
    public int hashCode() {
        return Objects.hash(frontDrawableId, backDrawableId, isOpen);
    }
}
