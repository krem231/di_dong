package com.example.manga;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.util.Log;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.request.target.Target;

import com.bumptech.glide.Glide;

import java.util.List;

public class ChapterDetailAdapter extends RecyclerView.Adapter<ChapterDetailAdapter.ImageViewHolder> {

    private final List<String> imageUrls;

    public ChapterDetailAdapter(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.image_view);
        }
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chapter_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        try {
            if (imageUrls == null || position >= imageUrls.size()) {
                Log.e("ChapterAdapter", "Invalid image index: " + position + ", size: " + (imageUrls != null ? imageUrls.size() : 0));
                return;
            }

            Glide.with(holder.imageView.getContext())
                    .load(imageUrls.get(position))
                    .override(Target.SIZE_ORIGINAL)
                    .into(holder.imageView);
        } catch (Exception e) {
            Log.e("ChapterAdapter", "Lỗi khi hiển thị ảnh: " + e.getMessage(), e);
        }
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }
}
