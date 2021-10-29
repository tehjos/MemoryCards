package com.example.memorycards.model;

import com.example.memorycards.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class MemoryCardDeck {
    private static final int[] DRAWABLE_IDS = {
            R.drawable.baseball,
            R.drawable.bat,
            R.drawable.heart,
            R.drawable.tree,
            R.drawable.castle,
            R.drawable.lightning
    };
    private final List<MemoryCard> cards;
    private final BehaviorSubject<List<MemoryCard>> cardsSubject;

    public MemoryCardDeck(@MemoryCardDeckSize int numCards) {
        cards = new ArrayList<>();
        cardsSubject = BehaviorSubject.create();
        for (int i = 0; i < numCards/2; i++) {
            cards.add(new MemoryCard(DRAWABLE_IDS[i]));
            cards.add(new MemoryCard(DRAWABLE_IDS[i]));
        }
        shuffle();
    }

    public Observable<List<MemoryCard>> observeCards() {
        return cardsSubject.hide();
    }

    public void shuffle() {
        Collections.shuffle(cards);
        for (MemoryCard card : cards) {
            card.setOpen(false);
        }
        cardsSubject.onNext(cards);
    }

    public void openCard(int position) {
        MemoryCard oldCard = cards.get(position);
        if (!oldCard.isOpen()) {
            MemoryCard newCard = oldCard.copy();
            newCard.setOpen(true);
            cards.set(position, newCard);
            cardsSubject.onNext(cards);
        }
    }
}