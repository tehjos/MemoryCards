package com.example.memorycards.model;

import java.util.ArrayList;
import java.util.List;

public abstract class MemoryCardDeck {
    private final List<MemoryCard> cards;

    public MemoryCardDeck(@MemoryCardDeckSize int numCards, int[] frontResIds, int backResId) {
        cards = new ArrayList<>();
        for (int i = 0; i < numCards / 2; i++) {
            cards.add(new MemoryCard(frontResIds[i], backResId));
            cards.add(new MemoryCard(frontResIds[i], backResId));
        }
    }

    public List<MemoryCard> getCards() {
        return cards;
    }
}
