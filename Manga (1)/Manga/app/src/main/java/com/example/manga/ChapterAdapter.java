package com.example.manga;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.util.Log;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Toast;
import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder> {

    private List<ChapterModel> chapterList;

    public ChapterAdapter(List<ChapterModel> chapterList) {
        this.chapterList = chapterList;
    }

    public static class ChapterViewHolder extends RecyclerView.ViewHolder {
        TextView title, time;

        public ChapterViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            time = itemView.findViewById(R.id.time);
        }
    }

    @Override
    public ChapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chapter, parent, false);
        return new ChapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChapterViewHolder holder, int position) {
        ChapterModel chapter = chapterList.get(position);

        // Nếu title null → fallback về "Chapter [number]"
        String titleText = (chapter.getTitle() != null && !chapter.getTitle().isEmpty())
                ? chapter.getTitle()
                : "Chapter " + chapter.getNumber();

        holder.title.setText(titleText);
        holder.time.setText(chapter.getDate());

        // 🎨 Set màu chữ
        holder.title.setTextColor(Color.WHITE);   // tiêu đề màu trắng

        holder.itemView.setOnClickListener(v -> {
            String mangaId = chapter.getMangaId();
            int chapterNumber = chapter.getNumber();
            String chapterId = chapter.getChapterId();

            MangaPageAPI.fetchChapterImages(chapterId, mangaId, chapterNumber, new MangaCallback<List<String>>() {
                @Override
                public void onSuccess(List<String> imageUrls) {
                    ChapterImageProvider.saveChapterImages(mangaId, chapterNumber, chapterId, imageUrls);

                    ((android.app.Activity) v.getContext()).runOnUiThread(() -> {
                        Intent intent = new Intent(v.getContext(), ChapterDetailActivity.class);
                        intent.putExtra("chapter_number", chapterNumber);
                        intent.putExtra("manga_id", mangaId);
                        intent.putExtra("chapter_id", chapterId);
                        v.getContext().startActivity(intent);
                    });
                }

                @Override
                public void onError(String errorMessage) {
                    ((android.app.Activity) v.getContext()).runOnUiThread(() ->
                            Toast.makeText(v.getContext(), "Lỗi tải ảnh: " + errorMessage, Toast.LENGTH_SHORT).show()
                    );
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }
}
