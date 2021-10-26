package com.example.memorycards.model;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
        MemoryCardDeckSize.SMALL,
        MemoryCardDeckSize.MEDIUM,
        MemoryCardDeckSize.LARGE
})
public @interface MemoryCardDeckSize {
    public static final int SMALL = 12;
    public static final int MEDIUM = 16;
    public static final int LARGE = 20;
}
