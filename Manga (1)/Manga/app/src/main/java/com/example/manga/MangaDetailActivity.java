package com.example.manga;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

public class MangaDetailActivity extends AppCompatActivity {

    ImageView imageManga;
    TextView titleManga;
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    Button saveButton;

    String mangaId;
    String heading;
    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manga_detail);

        imageManga = findViewById(R.id.imageManga);
        titleManga = findViewById(R.id.titleManga_detail);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.ViewpagerDetail);
        saveButton = findViewById(R.id.btnLibrary);

        mangaId = getIntent().getStringExtra("mangaId");
        heading = getIntent().getStringExtra("heading");
        imageUrl = getIntent().getStringExtra("imageUrl");

        ReadHistoryManager.markAsRead(this, mangaId);
        Glide.with(this).load(imageUrl).into(imageManga);
        titleManga.setText(heading);
        ViewPagerDetailAdapter adapter = new ViewPagerDetailAdapter(this, mangaId);
        viewPager2.setAdapter(adapter);

        MangaChapterAPI.fetchChapters(mangaId, new MangaCallback<List<ChapterData>>() {
            @Override
            public void onSuccess(List<ChapterData> chapterDataList) {
                List<ChapterModel> modelList = MangaChapterAPI.convertToModels(chapterDataList, mangaId);
                runOnUiThread(() ->
                        Toast.makeText(MangaDetailActivity.this, "Đã tải " + modelList.size() + " chương", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() ->
                        Toast.makeText(MangaDetailActivity.this, "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show()
                );
            }
        });


        // Gán trạng thái nút lưu
        if (SavedMangaManager.isSaved(this, mangaId)) {
            saveButton.setText("Đã lưu");
        } else {
            saveButton.setText("Lưu truyện");
        }

        // Bắt sự kiện nút lưu
        saveButton.setOnClickListener(v -> {
            if (SavedMangaManager.isSaved(this, mangaId)) {
                SavedMangaManager.removeManga(this, mangaId);
                saveButton.setText("Lưu truyện");
                Toast.makeText(this, "Đã xóa khỏi thư viện", Toast.LENGTH_SHORT).show();
            } else {
                SavedMangaManager.saveManga(this, mangaId, heading, imageUrl);
                saveButton.setText("Đã lưu");
                Toast.makeText(this, "Đã lưu vào thư viện", Toast.LENGTH_SHORT).show();
            }
        });

        // Gán tab "Overview" và "Chapters"
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            if (position == 0) {
                tab.setText("Overview");
            } else if (position == 1) {
                tab.setText("Chapters");
            }
        }).attach();
        ReadHistoryManager.markAsRead(this, mangaId);
    }
}
