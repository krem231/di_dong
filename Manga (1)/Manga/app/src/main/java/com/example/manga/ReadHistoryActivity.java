package com.example.manga;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ReadHistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Adapter_Item adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_history);

        recyclerView = findViewById(R.id.recyclerViewHistory);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        Set<String> readIds = ReadHistoryManager.getReadHistory(this);
        List<Item> readItems = new ArrayList<>();

        for (String id : readIds) {
            MangaDataProvider.Manga manga = MangaDataProvider.getMangaById(id);
            if (manga != null) {
                readItems.add(new Item(
                        manga.title != null ? manga.title : "No title",
                        manga.imageUrl,
                        manga.id
                ));
            }
        }

        adapter = new Adapter_Item(this, readItems, item -> {
            Intent intent = new Intent(this, MangaDetailActivity.class);
            intent.putExtra("mangaId", item.getMangaId());
            intent.putExtra("heading", item.getMangaName());
            intent.putExtra("imageUrl", item.getImage());
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
    }
}
