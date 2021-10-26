package com.example.memorycards.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.memorycards.R;
import com.example.memorycards.model.MemoryCardDeck;
import com.example.memorycards.model.MemoryCardDeckSize;
import com.example.memorycards.model.MemoryCardGridLayoutManager;

public class MainActivity extends AppCompatActivity {
    private final MemoryCardAdapter memoryCardAdapter = new MemoryCardAdapter();
    private final MemoryCardDeck memoryCardDeck = new MemoryCardDeck(MemoryCardDeckSize.LARGE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        RecyclerView cardsRv = findViewById(R.id.cards_rv);
        cardsRv.setLayoutManager(new MemoryCardGridLayoutManager(this));
        cardsRv.setAdapter(memoryCardAdapter);
        memoryCardAdapter.submitList(memoryCardDeck.getCards());
    }
}