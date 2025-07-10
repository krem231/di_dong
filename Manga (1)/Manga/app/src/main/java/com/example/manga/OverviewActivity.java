package com.example.manga;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class OverviewActivity extends AppCompatActivity {

    private TextView overviewText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overview);

        overviewText = findViewById(R.id.overview_text);

        String mangaId = getIntent().getStringExtra("manga_id");

        MangaDataProvider.Manga manga = MangaDataProvider.getMangaById(mangaId);
        if (manga != null) {
            setTitle(manga.title + " - Tóm tắt");
            overviewText.setText(manga.overview);
        } else {
            overviewText.setText("Không tìm thấy thông tin.");
        }
    }
}
