//ChapterDetailActivity.java


package com.example.manga;


import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.view.View;
import java.util.Map;
import java.util.Set;
import java.util.Collections;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import android.util.Log;
import java.util.List;
import java.util.ArrayList;
import com.example.manga.SavedMangaManager;


public class ChapterDetailActivity extends AppCompatActivity {


    private ViewPager2 viewPager;
    private Button prevButton, nextButton, listButton;
    private ChapterPageAdapter pageAdapter;


    private String mangaId;
    private int chapterNumber;
    private String chapterId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chapter_detail);


        mangaId = getIntent().getStringExtra("manga_id");
        chapterNumber = getIntent().getIntExtra("chapter_number", 1);
        chapterId = getIntent().getStringExtra("chapter_id");


        viewPager = findViewById(R.id.chapter_detail_viewpager);
        prevButton = findViewById(R.id.prev_button);
        nextButton = findViewById(R.id.next_button);
        listButton = findViewById(R.id.list_button);


        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);


        //  hiệu ứng lật
        viewPager.setPageTransformer(new DepthPageTransformer());


        loadChapterImages();


        prevButton.setOnClickListener(v -> {
            int prevChapter = chapterNumber - 1;
            if (prevChapter >= 1) {
                String prevChapterId = ChapterImageProvider.getChapterId(mangaId, prevChapter);
                if (prevChapterId != null) {
                    chapterNumber = prevChapter;
                    chapterId = prevChapterId;
                    loadChapterImages();
                } else {
                    Toast.makeText(this, "Không có chương trước!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Đây là chương đầu tiên!", Toast.LENGTH_SHORT).show();
            }
        });


        nextButton.setOnClickListener(v -> {
            int nextChapter = chapterNumber + 1;
            String nextChapterId = ChapterImageProvider.getChapterId(mangaId, nextChapter);
            if (nextChapterId != null) {
                chapterNumber = nextChapter;
                chapterId = nextChapterId;
                loadChapterImages();
            } else {
                Toast.makeText(this, "Không có chương tiếp theo!", Toast.LENGTH_SHORT).show();
            }
        });


        listButton.setOnClickListener(v -> {
            List<ChapterImageProvider.ChapterInfo> chapterList = ChapterImageProvider.getAllChapters(mangaId);


            if (chapterList == null || chapterList.isEmpty()) {
                Toast.makeText(this, "Chưa có dữ liệu chương!", Toast.LENGTH_SHORT).show();
                return;
            }


            String[] chapterTitles = new String[chapterList.size()];
            for (int i = 0; i < chapterList.size(); i++) {
                chapterTitles[i] = "Chương " + chapterList.get(i).chapterNumber;
            }


            new AlertDialog.Builder(this)
                    .setTitle("Chọn chương")
                    .setItems(chapterTitles, (dialog, which) -> {
                        ChapterImageProvider.ChapterInfo selected = chapterList.get(which);
                        chapterNumber = selected.chapterNumber;
                        chapterId = selected.chapterId;
                        loadChapterImages();
                    })
                    .show();
        });
    }


    private void loadChapterImages() {
        Log.d("DEBUG", "loadChapterImages() - chapterId: " + chapterId + ", chapterNumber: " + chapterNumber);


        if (chapterId == null || chapterId.trim().isEmpty()) {
            Toast.makeText(this, "Không tìm thấy chapterId!", Toast.LENGTH_SHORT).show();
            return;
        }


        List<String> imageUrls = ChapterImageProvider.getChapterImages(chapterId);


        if (imageUrls == null || imageUrls.isEmpty()) {
            Log.d("DEBUG", "Không có ảnh trong cache, gọi API");


            MangaPageAPI.fetchChapterImages(chapterId, mangaId, chapterNumber, new MangaCallback<List<String>>() {
                @Override
                public void onSuccess(List<String> urls) {
                    runOnUiThread(() -> {
                        Log.d("DEBUG", "API trả về: " + (urls != null ? urls.size() : 0) + " ảnh");
                        if (urls == null || urls.isEmpty()) {
                            Toast.makeText(ChapterDetailActivity.this, "Không có ảnh cho chương này!", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("CHAPTER_UI", "Set adapter với " + urls.size() + " ảnh");
                            pageAdapter = new ChapterPageAdapter(urls);
                            viewPager.setAdapter(pageAdapter);
                            setTitle("Chương " + chapterNumber);
                            Log.d("DEBUG", "Adapter được gán với số ảnh: " + urls.size());
                        }
                    });
                }


                @Override
                public void onError(String errorMessage) {
                    runOnUiThread(() ->
                            Toast.makeText(ChapterDetailActivity.this, "Lỗi khi tải ảnh: " + errorMessage, Toast.LENGTH_SHORT).show()
                    );
                }
            });


        } else {
            Log.d("DEBUG", "Đang dùng ảnh trong cache - Số ảnh: " + imageUrls.size());
            pageAdapter = new ChapterPageAdapter(imageUrls);
            viewPager.setAdapter(pageAdapter);
            setTitle("Chương " + chapterNumber);
        }
    }


    // Lớp tạo hiệu ứng lật trang 3D
    public class DepthPageTransformer implements ViewPager2.PageTransformer {
        private static final float MIN_SCALE = 0.75f;


        @Override
        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();


            if (position < -1) { // [-Infinity,-1)
                view.setAlpha(0f);


            } else if (position <= 0) { // [-1,0]
                view.setAlpha(1f);
                view.setTranslationX(0f);
                view.setScaleX(1f);
                view.setScaleY(1f);


            } else if (position <= 1) { // (0,1]
                view.setAlpha(1 - position);


                view.setTranslationX(pageWidth * -position);


                float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);


            } else {


                view.setAlpha(0f);
            }
        }
    }
}

