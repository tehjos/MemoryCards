package com.example.memorycards.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.processors.PublishProcessor;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import kotlin.Pair;
import timber.log.Timber;

public abstract class MemoryCardDeck {
    private final List<MemoryCard> cards;
    private final BehaviorSubject<List<MemoryCard>> cardsSubject;
    private final PublishProcessor<Integer> selectedCardProcessor;
    private final CompositeDisposable disposables;
    private final Stack<Integer> openedCards;

    public MemoryCardDeck(@MemoryCardDeckSize int numCards, int[] frontResIds, int backResId) {
        cards = new ArrayList<>();
        openedCards = new Stack<>();
        for (int i = 0; i < numCards / 2; i++) {
            cards.add(new MemoryCard(frontResIds[i], backResId));
            cards.add(new MemoryCard(frontResIds[i], backResId));
        }
        shuffle();
        cardsSubject = BehaviorSubject.createDefault(cards);
        selectedCardProcessor = PublishProcessor.create();
        disposables = new CompositeDisposable();
        observeSelectedCard();
    }

    public Observable<List<MemoryCard>> observeCards() {
        return cardsSubject.hide();
    }

    public void resetCards() {
        Timber.d("resetCards");
        for (int i = 0; i < cards.size(); i++) {
            closeCardAt(i);
        }
        notifyCardsChange();
    }

    private void closeCardAt(int position) {
        cards.set(position, cards.get(position).copy(false));
    }

    public void newGame() {
        Timber.d("newGame");
        shuffle();
        resetCards();
    }

    public void openCard(int position) {
        selectedCardProcessor.onNext(position);
    }

    private void observeSelectedCard() {
        Disposable d = selectedCardProcessor
                .onBackpressureDrop()
                .flatMap(this::openCardFlowable, 1)
                .subscribeOn(Schedulers.io())
                .subscribe();
        disposables.add(d);
    }

    private Flowable<Boolean> openCardFlowable(int position) {
        return Flowable.just(new Pair<>(position, cards.get(position)))
                .filter(pair -> !pair.getSecond().isOpen())
                .map(pair -> new Pair<>(pair.getFirst(), pair.getSecond().copy(true)))
                .doOnNext(pair -> {
                    Timber.d("openCardFlowable: %d", pair.getFirst());
                    cards.set(pair.getFirst(), pair.getSecond());
                    notifyCardsChange();
                })
                .flatMap(pair -> {
                    if (openedCards.size() % 2 == 0) {
                        // leave card open
                        openedCards.push(pair.getFirst());
                        return Flowable.just(true);
                    } else {
                        if (pair.getSecond().isMatch(cards.get(openedCards.peek()))) {
                            // leave card open
                            openedCards.push(pair.getFirst());
                            return Flowable.just(true);
                        } else {
                            // close both cards after delay
                            int prev = openedCards.pop();
                            return Flowable.timer(1000, TimeUnit.MILLISECONDS)
                                    .map(ignore -> {
                                        closeCardAt(prev);
                                        closeCardAt(pair.getFirst());
                                        notifyCardsChange();
                                        return false;
                                    });
                        }
                    }
                });
    }

    private void shuffle() {
        Collections.shuffle(cards);
    }

    private void notifyCardsChange() {
        cardsSubject.onNext(cards);
    }

    public void close() {
        disposables.clear();
    }
}