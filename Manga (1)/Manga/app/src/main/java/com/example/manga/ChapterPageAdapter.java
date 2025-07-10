// ChapterPageAdapter
package com.example.manga;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import java.util.List;


public class ChapterPageAdapter extends RecyclerView.Adapter<ChapterPageAdapter.PageViewHolder> {


    private List<String> imageUrls;


    public ChapterPageAdapter(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }


    @NonNull
    @Override
    public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chapter_page, parent, false);
        return new PageViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {
        String imageUrl = imageUrls.get(position);


        // Sử dụng Glide để load ảnh với hiệu ứng fade in
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.pageImage);
    }


    @Override
    public int getItemCount() {
        return imageUrls != null ? imageUrls.size() : 0;
    }


    public void updateImages(List<String> newImageUrls) {
        this.imageUrls = newImageUrls;
        notifyDataSetChanged();
    }


    static class PageViewHolder extends RecyclerView.ViewHolder {
        ImageView pageImage;


        public PageViewHolder(@NonNull View itemView) {
            super(itemView);
            pageImage = itemView.findViewById(R.id.page_image);
        }
    }
}

