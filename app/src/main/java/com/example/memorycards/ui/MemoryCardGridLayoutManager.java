package com.example.memorycards.ui;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;

public class MemoryCardGridLayoutManager extends GridLayoutManager {
    private static final int SPAN_COUNT = 4;

    public MemoryCardGridLayoutManager(Context context) {
        super(context, SPAN_COUNT);
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }
}
