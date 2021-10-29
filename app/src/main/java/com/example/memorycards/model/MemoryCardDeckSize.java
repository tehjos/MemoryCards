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
    int SMALL = 12;
    int MEDIUM = 16;
    int LARGE = 20;
}
