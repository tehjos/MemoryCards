package com.example.memorycards.ui;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.memorycards.R;

public class MemoryCardViewHolder extends RecyclerView.ViewHolder{
    private final ImageView cardImage;
    private int position;

    public MemoryCardViewHolder(View view, OnCardClickedListener listener) {
        super(view);
        cardImage = view.findViewById(R.id.card_image);
        cardImage.setOnClickListener(v -> listener.onCardClicked(position));
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setCardImage(int resId) {
        cardImage.setImageResource(resId);
    }
}
