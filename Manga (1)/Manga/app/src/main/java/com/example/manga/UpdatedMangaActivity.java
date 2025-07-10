package com.example.manga;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UpdatedMangaActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Adapter_Item adapter;
private TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updated_manga); // Tạo layout với id: recyclerViewUpdated

        recyclerView = findViewById(R.id.recyclerViewUpdated);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        MangaUpdatedAPI.fetchRecentlyUpdatedManga(new MangaCallback<List<MangaDataProvider.Manga>>() {
            @Override
            public void onSuccess(List<MangaDataProvider.Manga> mangaList) {
                runOnUiThread(() -> {
                    List<Item> items = new ArrayList<>();
                    for (MangaDataProvider.Manga manga : mangaList) {
                        items.add(new Item(manga.title, manga.imageUrl, manga.id));
                        Log.d("DEBUG_IMAGE", "Manga: " + manga.title + " - " + manga.imageUrl);
                    }

                    adapter = new Adapter_Item(UpdatedMangaActivity.this, items, item -> {
                        Intent intent = new Intent(UpdatedMangaActivity.this, MangaDetailActivity.class);
                        intent.putExtra("mangaId", item.getMangaId());
                        intent.putExtra("heading", item.getMangaName());
                        intent.putExtra("imageUrl", item.getImage());
                        startActivity(intent);
                    });

                    recyclerView.setAdapter(adapter);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() ->
                        Toast.makeText(UpdatedMangaActivity.this, "Lỗi: " + error, Toast.LENGTH_SHORT).show()
                );
            }
        });
    }
}
