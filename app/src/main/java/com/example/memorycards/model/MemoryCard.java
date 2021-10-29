package com.example.memorycards.model;

import com.example.memorycards.R;

import java.util.Objects;

public class MemoryCard {
    private final int frontDrawableId;
    public static final int BACK_DRAWABLE_ID = R.drawable.playing_card;

    private boolean isOpen;

    public MemoryCard(int frontDrawableId) {
        this.frontDrawableId = frontDrawableId;
        this.isOpen = false;
    }

    private MemoryCard(int frontDrawableId, boolean isOpen) {
        this.frontDrawableId = frontDrawableId;
        this.isOpen = isOpen;
    }

    public int getFrontDrawableId() {
        return frontDrawableId;
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
