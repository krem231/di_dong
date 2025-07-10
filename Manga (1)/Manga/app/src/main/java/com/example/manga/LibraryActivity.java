package com.example.manga;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LibraryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Adapter_Item adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library); // Đảm bảo layout có recyclerViewLibrary

        recyclerView = findViewById(R.id.recyclerViewLibrary);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        Set<String> savedIds = SavedMangaManager.getSavedMangaIds(this);
        List<MangaDataProvider.Manga> allMangas = MangaDataProvider.getAll();
        List<Item> savedItems = new ArrayList<>();

        for (MangaDataProvider.Manga m : allMangas) {
            if (savedIds.contains(m.id)) {
                savedItems.add(new Item(m.title, m.imageUrl, m.id));
            }
        }

        adapter = new Adapter_Item(this, savedItems, item -> {
            Intent intent = new Intent(this, MangaDetailActivity.class);
            intent.putExtra("mangaId", item.getMangaId());
            intent.putExtra("heading", item.getMangaName());
            intent.putExtra("imageUrl", item.getImage());
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
    }
}
