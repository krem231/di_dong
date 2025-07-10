package com.example.manga;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter_Item extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<Item> items;
    private OnMangaClickListener clickListener;


    public Adapter_Item(Context context, List<Item> items, OnMangaClickListener listener) {
        this.context = context;
        this.items = items;
        this.clickListener = listener;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_truyen, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item currentItem = items.get(position);

        holder.nameView.setText(currentItem.getMangaName());
        Glide.with(context)
                .load(currentItem.getImage())
                .placeholder(R.drawable.default_image)
                .into(holder.imageView);

        holder.imageView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onMangaClick(currentItem);
            }
        });
    }
    public void updateData(List<Item> newItems) {
        this.items.clear();
        this.items.addAll(newItems);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
}
