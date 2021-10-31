package com.example.memorycards.model;

import com.example.memorycards.R;

public class StandardDeck extends MemoryCardDeck {
    public static final int BACK_DRAWABLE_ID = R.drawable.card_back_blue;
    public static final int[] FRONT_DRAWABLE_IDS = {
            R.drawable.baseball,
            R.drawable.bat,
            R.drawable.heart,
            R.drawable.tree,
            R.drawable.castle,
            R.drawable.lightning,
            R.drawable.wave,
            R.drawable.pug,
            R.drawable.mountain,
            R.drawable.barbie
    };

    private StandardDeck(@MemoryCardDeckSize int numCards) {
        super(numCards, FRONT_DRAWABLE_IDS, BACK_DRAWABLE_ID);
    }

    public static MemoryCardDeck create(@MemoryCardDeckSize int numCards) {
        return new StandardDeck(numCards);
    }
}
