package com.example.manga;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VPAdapter extends RecyclerView.Adapter<VPAdapter.ViewHolder> {

    private ArrayList<ViewPagerItem> viewPagerItemArrayList;

    public VPAdapter(ArrayList<ViewPagerItem> viewPagerItemArrayList) {
        this.viewPagerItemArrayList = viewPagerItemArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewpager_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ViewPagerItem item = viewPagerItemArrayList.get(position);

        holder.imageTruyen.setImageResource(item.getImageID());
        holder.titleTruyen.setText(item.getHeading());
        holder.ratingInfo.setText(item.getRatingText());

        holder.btnViewDetail.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, MangaDetailActivity.class);
            intent.putExtra("mangaId", item.getMangaId()); // bạn cần có getMangaId() trong ViewPagerItem
            intent.putExtra("heading", item.getHeading());
            intent.putExtra("imageUrl", item.getImageUrl()); // dòng quan trọng
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return viewPagerItemArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageTruyen;
        TextView titleTruyen;
        TextView ratingInfo;
        Button btnViewDetail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageTruyen = itemView.findViewById(R.id.imageTruyen1);
            titleTruyen = itemView.findViewById(R.id.titleTruyen1);
            ratingInfo = itemView.findViewById(R.id.ratingInfo);
            btnViewDetail = itemView.findViewById(R.id.btnViewDetail);
        }
    }
}
