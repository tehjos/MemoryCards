package com.example.memorycards.ui;

import android.content.Context;
import android.content.res.Configuration;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MemoryCardGridLayoutManager extends GridLayoutManager {
    private static final int PORTRAIT_NUM_COLUMNS = 4;
    private static final int LANDSCAPE_NUM_ROWS = 2;

    private MemoryCardGridLayoutManager(Context context, int spanCount, int orientation) {
        super(context, spanCount, orientation, false);
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }

    public static MemoryCardGridLayoutManager create(Context context, int screenOrientation) {
        if (screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            return new MemoryCardGridLayoutManager(context, LANDSCAPE_NUM_ROWS, RecyclerView.HORIZONTAL);
        } else {
            return new MemoryCardGridLayoutManager(context, PORTRAIT_NUM_COLUMNS, RecyclerView.VERTICAL);
        }
    }
}
