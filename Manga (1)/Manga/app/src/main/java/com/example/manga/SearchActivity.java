package com.example.manga;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

public class SearchActivity extends AppCompatActivity {
    private EditText editSearch;
    private RecyclerView recyclerSearch;
    private Adapter_Item adapter;
    private List<Item> allItems, originalItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        editSearch = findViewById(R.id.searchEditText);
        recyclerSearch = findViewById(R.id.recyclerSearch);
        recyclerSearch.setLayoutManager(new LinearLayoutManager(this));

        allItems = new ArrayList<>();
        originalItems = new ArrayList<>();
        for (MangaDataProvider.Manga manga : MangaDataProvider.getAllManga()) {
            allItems.add(new Item(manga.title, manga.imageUrl, manga.id));
        }
        originalItems = new ArrayList<>(allItems);

      ;
        adapter = new Adapter_Item(this, allItems, item -> {
            Intent intent = new Intent(this, MangaDetailActivity.class);
            intent.putExtra("mangaId", item.getMangaId());
            intent.putExtra("imageUrl", item.getImage());
            intent.putExtra("heading", item.getMangaName());
            startActivity(intent);
        });
        recyclerSearch.setAdapter(adapter);
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        ImageButton btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(view -> {
            startActivity(new Intent(SearchActivity.this, MainActivity.class));
            finish();
        });

        ImageButton btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(view -> {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        });

        ImageButton btnUser = findViewById(R.id.btnUser);
        btnUser.setOnClickListener(view -> {
            startActivity(new Intent(SearchActivity.this, UserProfileActivity.class));
            finish();
        });
    }

    private void filter(String keyword) {
        List<Item> filteredList = new ArrayList<>();

        for (Item item : originalItems) {
            String name = item.getMangaName();
            if (name != null && name.toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(item);
            }
        }

        adapter.updateData(filteredList);

        if (filteredList.isEmpty() && !keyword.isEmpty()) {
            searchMangaOnline(keyword);
        }
    }

    private void searchMangaOnline(String keyword) {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                String url = "https://api.mangadex.org/manga?title=" + keyword;
                Request request = new Request.Builder()
                        .url(url)
                        .get()
                        .build();

                Response response = client.newCall(request).execute();
                String jsonData = response.body().string();

                JSONObject jsonObject = new JSONObject(jsonData);
                JSONArray dataArray = jsonObject.getJSONArray("data");

                List<MangaDataProvider.Manga> mangaList = new ArrayList<>();
                List<Item> onlineItems = new ArrayList<>();

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject mangaObj = dataArray.getJSONObject(i);
                    String mangaId = mangaObj.getString("id");

                    JSONObject attributes = mangaObj.getJSONObject("attributes");
                    JSONObject titleObj = attributes.getJSONObject("title");
                    String title = titleObj.has("en") ? titleObj.getString("en") : "No Title";

                    String overview = attributes.has("description") ?
                            attributes.getJSONObject("description").optString("en", "") : "";

                    String coverArtId = "";
                    JSONArray relationships = mangaObj.getJSONArray("relationships");
                    for (int j = 0; j < relationships.length(); j++) {
                        JSONObject relation = relationships.getJSONObject(j);
                        if (relation.getString("type").equals("cover_art")) {
                            coverArtId = relation.getString("id");
                            break;
                        }
                    }

                    // Lấy URL ảnh bìa
                    String imageUrl = getCoverArtUrl(mangaId, coverArtId);

                    // Tạo Manga và thêm vào danh sách
                    MangaDataProvider.Manga manga = new MangaDataProvider.Manga(
                            mangaId, title, overview, imageUrl
                    );
                    mangaList.add(manga);

                    // Map sang Item để hiển thị
                    onlineItems.add(new Item(title, imageUrl, mangaId));
                }
                MangaDataProvider.setMangaList(mangaList);

                runOnUiThread(() -> {
                    if (!onlineItems.isEmpty()) {
                        adapter.updateData(onlineItems);
                    } else {
                        Toast.makeText(SearchActivity.this, "Không tìm thấy truyện online!", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                Log.e("SearchAPI", "Error: " + e.getMessage());
                runOnUiThread(() -> Toast.makeText(SearchActivity.this, "Lỗi khi tìm kiếm online!", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private String getCoverArtUrl(String mangaId, String coverArtId) {
        String fileName = "";
        try {
            OkHttpClient client = new OkHttpClient();
            String url = "https://api.mangadex.org/cover/" + coverArtId;
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            Response response = client.newCall(request).execute();
            String jsonData = response.body().string();

            JSONObject jsonObject = new JSONObject(jsonData);
            JSONObject attributes = jsonObject.getJSONObject("data").getJSONObject("attributes");
            fileName = attributes.getString("fileName");

        } catch (Exception e) {
            Log.e("CoverArtAPI", "Error: " + e.getMessage());
        }

        if (!fileName.isEmpty()) {
            return "https://uploads.mangadex.org/covers/" + mangaId + "/" + fileName;
        } else {
            return "";
        }
    }
}
