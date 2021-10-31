package com.example.memorycards.game;

import com.example.memorycards.model.MemoryCard;
import com.example.memorycards.model.MemoryCardDeck;

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

public class MemoryCardGameManager {
    private final List<MemoryCard> cards;
    private final StopWatch stopWatch;
    private final BehaviorSubject<List<MemoryCard>> cardsSubject;
    private final BehaviorSubject<Integer> movesSubject;
    private final PublishProcessor<Integer> selectedCardProcessor;
    private final CompositeDisposable disposables;
    private final Stack<Integer> openedCards;

    public MemoryCardGameManager(MemoryCardDeck cardDeck) {
        cards = cardDeck.getCards();
        stopWatch = new StopWatch();
        openedCards = new Stack<>();
        shuffle();
        cardsSubject = BehaviorSubject.createDefault(cards);
        movesSubject = BehaviorSubject.createDefault(0);
        selectedCardProcessor = PublishProcessor.create();
        disposables = new CompositeDisposable();
        observeSelectedCard();
    }

    public Observable<List<MemoryCard>> observeCards() {
        return cardsSubject.hide();
    }

    public Observable<Integer> observeMoves() {
        return movesSubject.hide();
    }

    public Observable<Long> observeTime() {
        return stopWatch.observeTime();
    }

    public void resetGame() {
        Timber.d("resetGame");
        for (int i = 0; i < cards.size(); i++) {
            closeCardAt(i);
        }
        stopWatch.reset();
        notifyCardsChange();
        notifyMovesReset();
    }

    private void closeCardAt(int position) {
        cards.set(position, cards.get(position).copy(false));
    }

    public void setNewGame() {
        Timber.d("newGame");
        shuffle();
        resetGame();
    }

    public void openCardAt(int position) {
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
                    stopWatch.start();
                    Timber.d("openCardFlowable: %d", pair.getFirst());
                    cards.set(pair.getFirst(), pair.getSecond());
                    notifyCardsChange();
                    notifyMovesIncrease();
                })
                .flatMap(pair -> {
                    if (openedCards.size() % 2 == 0) {
                        // no prev card - leave card open
                        openedCards.push(pair.getFirst());
                        return Flowable.just(openedCards.size() == cards.size());
                    } else {
                        if (pair.getSecond().isMatch(cards.get(openedCards.peek()))) {
                            // cards match - leave card open
                            openedCards.push(pair.getFirst());
                            return Flowable.just(openedCards.size() == cards.size());
                        } else {
                            // cards do not match - close both cards after delay
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
                })
                .filter(gameOver -> gameOver)
                .doOnNext(gameOver -> stopWatch.stop());
    }

    private void shuffle() {
        Collections.shuffle(cards);
    }

    private void notifyCardsChange() {
        cardsSubject.onNext(cards);
    }

    private void notifyMovesIncrease() {
        int currentMoves = movesSubject.getValue() == null ? 0 : movesSubject.getValue();
        movesSubject.onNext(currentMoves + 1);
    }

    private void notifyMovesReset() {
        movesSubject.onNext(0);
    }

    public void close() {
        disposables.clear();
    }
}