package com.example.memorycards.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorycards.R;
import com.example.memorycards.StopWatch;
import com.example.memorycards.model.MemoryCardDeck;
import com.example.memorycards.model.MemoryCardDeckSize;
import com.example.memorycards.model.StandardDeck;

import java.util.ArrayList;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity{
    private MemoryCardAdapter memoryCardAdapter;
    private MemoryCardDeck memoryCardDeck;
    private StopWatch stopWatch;
    private CompositeDisposable disposables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("onCreate");
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.overflow_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.new_game) {
            memoryCardDeck.newGame();
            stopWatch.reset();
            return true;
        } else if (id == R.id.replay) {
            memoryCardDeck.resetCards();
            stopWatch.reset();
            return true;
        }
        return false;
    }

    private void init() {
        Timber.d("init");
        disposables = new CompositeDisposable();
        memoryCardDeck = new StandardDeck(MemoryCardDeckSize.MEDIUM);
        stopWatch = new StopWatch();
        setUpAppBar();
        setUpRecyclerView();
        observeCards();
        observeTime();
    }

    private void setUpAppBar() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
    }

    private void setUpRecyclerView() {
        memoryCardAdapter = new MemoryCardAdapter(position -> {
            Timber.d("onCardClicked: %d", position);
            memoryCardDeck.openCard(position);
            stopWatch.start();
        });
        RecyclerView cardsRv = findViewById(R.id.cards_rv);
        cardsRv.setHasFixedSize(true);
        cardsRv.setLayoutManager(new MemoryCardGridLayoutManager(this));
        cardsRv.setAdapter(memoryCardAdapter);
    }

    private void observeCards() {
        Disposable d = memoryCardDeck.observeCards()
                .observeOn(AndroidSchedulers.mainThread())
                .map(ArrayList::new)
                .doOnNext(memoryCardAdapter::submitList)
                .subscribe();
        disposables.add(d);
    }

    private void observeTime() {
        TextView timeText = findViewById(R.id.time_text);
        Disposable d = stopWatch.observeTime()
                .filter(time -> time >= 0)
                .observeOn(AndroidSchedulers.mainThread())
                .map(this::formatTime)
                .doOnNext(timeText::setText)
                .subscribe();
        disposables.add(d);
    }

    private String formatTime(Long timeInSeconds) {
        long seconds = timeInSeconds % 60;
        long minutes = (timeInSeconds / 60) % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Timber.d("onDestroy");
        memoryCardDeck.close();
        disposables.dispose();
    }
}