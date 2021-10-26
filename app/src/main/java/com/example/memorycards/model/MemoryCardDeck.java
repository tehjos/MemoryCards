package com.example.memorycards.model;

import com.example.memorycards.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemoryCardDeck {
    private static final int[] DRAWABLE_IDS = {

    };
    private final List<MemoryCard> cards;

    public MemoryCardDeck(@MemoryCardDeckSize int numCards) {
        cards = new ArrayList<>();
        for (int i = 0; i < numCards; i++) {
            cards.add(new MemoryCard(0, R.drawable.playing_card));
        }
    }

    public List<MemoryCard> getCards() {
        return cards;
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }
}