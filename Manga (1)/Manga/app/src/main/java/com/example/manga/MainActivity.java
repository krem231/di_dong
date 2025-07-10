package com.example.manga;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageButton btnHome, btnSearch, btnUser, btnMenu;
    private Button  btnLibrary, btnReadHistory,btnChatbot;
    private LinearLayout menuBox;
    private ViewPager2 imagePager;
    private boolean isMenuVisible = false;
    private RecyclerView recyclerViewTruyen, recyclerViewTruyen2;

    private Adapter_Item adapter1, adapter2;
private EditText editSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupViewPager();
        setupRecyclerViews();
        setupClickListeners();
        initMenuState();
        fetchMangaData();
    }

    private void initViews() {
        btnHome = findViewById(R.id.btnHome);
        btnSearch = findViewById(R.id.btnSearch);
        btnUser = findViewById(R.id.btnUser);
        btnMenu = findViewById(R.id.btn1);
        menuBox = findViewById(R.id.menuBox);
        imagePager = findViewById(R.id.viewpager);
        recyclerViewTruyen = findViewById(R.id.recyclerViewTruyen);
        recyclerViewTruyen2 = findViewById(R.id.recyclerViewTruyen2);
        btnLibrary = findViewById(R.id.btn1_1);
        btnReadHistory = findViewById(R.id.btn1_2);
        btnChatbot = findViewById(R.id.btnChatbot);
    }

    private void setupViewPager() {
        ArrayList<ViewPagerItem> viewPagerItems = new ArrayList<>();
        viewPagerItems.add(new ViewPagerItem(R.drawable.cat, "Cat", "⭐ 8.3 • Completed", "manga1", "https://example.com/cat.jpg"));
        viewPagerItems.add(new ViewPagerItem(R.drawable.kiss, "Tai and Bel", "⭐ 9.1 • Ongoing", "manga2", "https://example.com/kiss.jpg"));
        viewPagerItems.add(new ViewPagerItem(R.drawable.gun, "Hakamori", "⭐ 9.0 • Completed", "manga3", "https://example.com/gun.jpg"));

        VPAdapter vpAdapter = new VPAdapter(viewPagerItems);
        imagePager.setAdapter(vpAdapter);
    }

    private void setupRecyclerViews() {
        recyclerViewTruyen.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewTruyen2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void setupClickListeners() {
        btnHome.setOnClickListener(v -> recreate());

        btnSearch.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        btnUser.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
            startActivity(intent);
        });

        btnMenu.setOnClickListener(v -> toggleMenu());

        btnLibrary.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LibraryActivity.class);
            startActivity(intent);
        });
        btnSearch.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });
        btnReadHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ReadHistoryActivity.class);
            startActivity(intent);
        });
        Button btnUpdated = findViewById(R.id.btn1_3);
        btnUpdated.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UpdatedMangaActivity.class);
            startActivity(intent);
        });
        btnChatbot.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ChatbotActivity.class);
            startActivity(intent);
        });

    }

    private void initMenuState() {
        menuBox.setVisibility(View.GONE);
        imagePager.setVisibility(View.VISIBLE);
        isMenuVisible = false;
    }

    private void toggleMenu() {
        if (isMenuVisible) {
            menuBox.setVisibility(View.GONE);
            imagePager.setVisibility(View.VISIBLE);
        } else {
            menuBox.setVisibility(View.VISIBLE);
            imagePager.setVisibility(View.GONE);
            Animation slideDown = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_down);
            menuBox.startAnimation(slideDown);
        }
        isMenuVisible = !isMenuVisible;
    }

    private void fetchMangaData() {
        MangaRepository.getInstance().fetchMangaList(new MangaCallback<List<MangaDataProvider.Manga>>() {
            @Override
            public void onSuccess(List<MangaDataProvider.Manga> mangaList) {
                runOnUiThread(() -> {
                    List<Item> items1 = new ArrayList<>();
                    List<Item> items2 = new ArrayList<>();
                    for (int i = 0; i < mangaList.size(); i++) {
                        MangaDataProvider.Manga manga = mangaList.get(i);
                        Item item = new Item(manga.title, manga.imageUrl, manga.id);

                        if (i % 2 == 0) {
                            items1.add(item);
                        } else {
                            items2.add(item);
                        }
                    }

                    adapter1 = new Adapter_Item(MainActivity.this, items1, item -> {
                        Intent intent = new Intent(MainActivity.this, MangaDetailActivity.class);
                        intent.putExtra("mangaId", item.getMangaId());
                        intent.putExtra("imageUrl", item.getImage());
                        startActivity(intent);
                    });

                    adapter2 = new Adapter_Item(MainActivity.this, items2, item -> {
                        Intent intent = new Intent(MainActivity.this, MangaDetailActivity.class);
                        intent.putExtra("mangaId", item.getMangaId());
                        intent.putExtra("imageUrl", item.getImage());
                        startActivity(intent);
                    });
                    recyclerViewTruyen.setAdapter(adapter1);
                    recyclerViewTruyen2.setAdapter(adapter2);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() ->
                        Toast.makeText(MainActivity.this, "Lỗi tải manga: " + error, Toast.LENGTH_SHORT).show()
                );
            }
        });
    }
}
