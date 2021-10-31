package com.example.memorycards.ui;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.memorycards.R;
import com.example.memorycards.databinding.MemoryCardItemBinding;

public class MemoryCardViewHolder extends RecyclerView.ViewHolder{
    private final MemoryCardItemBinding binding;
    private int position;

    public MemoryCardViewHolder(MemoryCardItemBinding binding, OnCardClickedListener listener) {
        super(binding.getRoot());
        this.binding = binding;
        binding.cardImage.setOnClickListener(v -> listener.onCardClicked(position));
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setCardImage(int resId) {
        binding.cardImage.setImageResource(resId);
    }
}
