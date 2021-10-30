package com.example.memorycards.model;

import com.example.memorycards.R;

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

    private MemoryCard(int frontDrawableId, int backDrawableId, boolean isOpen) {
        this.frontDrawableId = frontDrawableId;
        this.backDrawableId = backDrawableId;
        this.isOpen = isOpen;
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

    public MemoryCard copy() {
        return new MemoryCard(
                frontDrawableId,
                backDrawableId,
                isOpen
        );
    }

    public MemoryCard copy(boolean isOpen) {
        return new MemoryCard(
                frontDrawableId,
                backDrawableId,
                isOpen
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemoryCard that = (MemoryCard) o;
        return frontDrawableId == that.frontDrawableId && isOpen == that.isOpen;
    }

    @Override
    public int hashCode() {
        return Objects.hash(frontDrawableId, isOpen);
    }
}
