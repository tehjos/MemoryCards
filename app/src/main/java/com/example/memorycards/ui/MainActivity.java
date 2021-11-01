package com.example.memorycards.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.memorycards.R;
import com.example.memorycards.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity{
    private MainViewModel viewModel;
    private ActivityMainBinding binding;
    private MemoryCardAdapter memoryCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("onCreate");
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());
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
            viewModel.setNewGame();
            return true;
        } else if (id == R.id.replay) {
            viewModel.resetGame();
            return true;
        }
        return false;
    }

    private void init() {
        Timber.d("init");
        setSupportActionBar(binding.toolbar);
        setUpRecyclerView();
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding.setViewModel(viewModel);
        observeCards();
    }

    private void setUpRecyclerView() {
        Timber.d("setUpRecyclerView");
        memoryCardAdapter = new MemoryCardAdapter(position -> {
            Timber.d("onCardClicked: %d", position);
            viewModel.clickCard(position);
        });
        binding.cardsRv.setLayoutManager(
                MemoryCardGridLayoutManager.create(
                        this, getResources().getConfiguration().orientation
        ));
        binding.cardsRv.setAdapter(memoryCardAdapter);
    }

    private void observeCards() {
        viewModel.getCards().observe(this, memoryCardAdapter::submitList);
    }
}