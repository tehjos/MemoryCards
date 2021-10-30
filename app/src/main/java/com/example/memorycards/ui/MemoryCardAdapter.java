package com.example.memorycards.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.memorycards.R;
import com.example.memorycards.model.MemoryCard;

public class MemoryCardAdapter extends ListAdapter<MemoryCard, MemoryCardViewHolder> {
    private final OnCardClickedListener listener;

    protected MemoryCardAdapter(OnCardClickedListener listener) {
        super(new MEMORY_CARD_DIFF());
        this.listener = listener;
    }

    @NonNull
    @Override
    public MemoryCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MemoryCardViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.memory_card_item, parent, false),
                listener
        );
    }

    @Override
    public void onBindViewHolder(@NonNull MemoryCardViewHolder holder, int position) {
        MemoryCard card = getCurrentList().get(position);
        holder.setPosition(position);
        if (card.isOpen()) {
            holder.setCardImage(card.getFrontDrawableId());
        } else {
            holder.setCardImage(card.getBackDrawableId());
        }
    }

    private static class MEMORY_CARD_DIFF extends DiffUtil.ItemCallback<MemoryCard> {
        @Override
        public boolean areItemsTheSame(@NonNull MemoryCard oldItem, @NonNull MemoryCard newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull MemoryCard oldItem, @NonNull MemoryCard newItem) {
            return oldItem.equals(newItem);
        }
    }
}
