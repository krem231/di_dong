package com.example.manga;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MangaUpdatedAPI {

    private static final String TAG = "MangaUpdatedAPI";
    private static final String API_URL = "https://api.mangadex.org/manga?order[latestUploadedChapter]=desc&limit=20&includes[]=cover_art";

    public static void fetchRecentlyUpdatedManga(MangaCallback<List<MangaDataProvider.Manga>> callback) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(API_URL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "API call failed: " + e.getMessage());
                callback.onError("Lỗi kết nối: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onError("Lỗi khi gọi API: " + response.message());
                    return;
                }

                try {
                    String jsonData = response.body().string();
                    JSONObject json = new JSONObject(jsonData);
                    JSONArray dataArray = json.getJSONArray("data");

                    List<MangaDataProvider.Manga> updatedMangaList = new ArrayList<>();

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject mangaJson = dataArray.getJSONObject(i);

                        String id = mangaJson.getString("id");
                        JSONObject attributes = mangaJson.getJSONObject("attributes");
                        JSONObject titleObj = attributes.getJSONObject("title");

                        // Lấy tiêu đề tiếng Anh nếu có, không thì lấy bất kỳ
                        String title = titleObj.has("en") ? titleObj.getString("en") :
                                (titleObj.keys().hasNext() ? titleObj.getString(titleObj.keys().next()) : "No title");

                        String imageUrl = null;
                        JSONArray relationships = mangaJson.getJSONArray("relationships");

                        for (int j = 0; j < relationships.length(); j++) {
                            JSONObject rel = relationships.getJSONObject(j);
                            if (rel.getString("type").equals("cover_art")) {
                                JSONObject relAttributes = rel.getJSONObject("attributes");
                                String fileName = relAttributes.getString("fileName");
                                imageUrl = "https://uploads.mangadex.org/covers/" + id + "/" + fileName;
                                break;
                            }
                        }

                        // Tạo đối tượng Manga
                        MangaDataProvider.Manga manga = new MangaDataProvider.Manga(id, title, "", imageUrl);
                        updatedMangaList.add(manga);

                        Log.d("DEBUG_IMAGE", "Manga: " + title + " - " + imageUrl);
                    }

                    callback.onSuccess(updatedMangaList);

                } catch (JSONException e) {
                    Log.e(TAG, "JSON parsing error: " + e.getMessage());
                    callback.onError("Lỗi xử lý dữ liệu.");
                }
            }
        });
    }
}
