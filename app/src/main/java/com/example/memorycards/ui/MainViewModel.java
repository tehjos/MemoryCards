package com.example.memorycards.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.memorycards.game.MemoryCardGameManager;
import com.example.memorycards.model.MemoryCard;
import com.example.memorycards.model.MemoryCardDeckSize;
import com.example.memorycards.model.StandardDeck;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import timber.log.Timber;

public class MainViewModel extends ViewModel {
    private final MemoryCardGameManager gameManager;
    private final CompositeDisposable disposables;
    private final MutableLiveData<List<MemoryCard>> cards;
    public final MutableLiveData<String> timeText;
    public final MutableLiveData<Integer> numMoves;

    public MainViewModel() {
        gameManager = new MemoryCardGameManager(StandardDeck.create(MemoryCardDeckSize.MEDIUM));
        disposables = new CompositeDisposable();
        cards = new MutableLiveData<>();
        timeText = new MutableLiveData<>("00:00");
        numMoves = new MutableLiveData<>(0);
        observeData();
    }

    public LiveData<List<MemoryCard>> getCards() {
        return cards;
    }

    public void setNewGame() {
        gameManager.setNewGame();
    }

    public void resetGame() {
        gameManager.resetGame();
    }

    public void clickCard(int position) {
        gameManager.openCardAt(position);
    }

    private void observeData() {
        observeCards();
        observeMoves();
        observeTime();
    }

    private void observeCards() {
        Timber.d("observeCards");
        Disposable d = gameManager.observeCards()
                .observeOn(AndroidSchedulers.mainThread())
                .map(ArrayList::new)
                .doOnNext(cards::setValue)
                .subscribe();
        disposables.add(d);
    }

    private void observeMoves() {
        Timber.d("observeMoves");
        Disposable d = gameManager.observeMoves()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(numMoves::setValue)
                .subscribe();
        disposables.add(d);
    }

    private void observeTime() {
        Timber.d("observeTime");
        Disposable d = gameManager.observeTime()
                .observeOn(AndroidSchedulers.mainThread())
                .map(this::formatTime)
                .doOnNext(timeText::setValue)
                .subscribe();
        disposables.add(d);
    }

    private String formatTime(Long timeInSeconds) {
        long seconds = timeInSeconds % 60;
        long minutes = (timeInSeconds / 60) % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        gameManager.close();
        disposables.dispose();
    }
}
