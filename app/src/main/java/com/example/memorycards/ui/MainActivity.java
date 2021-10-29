package com.example.memorycards.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.memorycards.R;
import com.example.memorycards.model.MemoryCardDeck;
import com.example.memorycards.model.MemoryCardDeckSize;
import com.example.memorycards.model.MemoryCardGridLayoutManager;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity{
    private MemoryCardAdapter memoryCardAdapter;
    private MemoryCardDeck memoryCardDeck;
    private CompositeDisposable disposables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("onCreate");
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        Timber.d("init");
        RecyclerView cardsRv = findViewById(R.id.cards_rv);
        cardsRv.setHasFixedSize(true);
        disposables = new CompositeDisposable();
        memoryCardDeck = new MemoryCardDeck(MemoryCardDeckSize.SMALL);
        setUpAdapter();
        cardsRv.setLayoutManager(new MemoryCardGridLayoutManager(this));
        cardsRv.setAdapter(memoryCardAdapter);
        observeCards();
    }

    private void setUpAdapter() {
        Timber.d("setUpAdapter");
        memoryCardAdapter = new MemoryCardAdapter(position -> {
            Timber.d("onCardClicked: %d", position);
            memoryCardDeck.openCard(position);
        });
    }

    private void observeCards() {
        Timber.d("observeCards");
        Disposable d = memoryCardDeck.observeCards()
                .observeOn(AndroidSchedulers.mainThread())
                .map(ArrayList::new)
                .doOnNext(memoryCardAdapter::submitList)
                .subscribe();
        disposables.add(d);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Timber.d("onDestroy");
        disposables.dispose();
    }
}