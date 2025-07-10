package com.example.manga;

import org.json.JSONArray;
import org.json.JSONObject;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MangaPageAPI {
    public static void fetchChapterImages(String chapterId, String mangaId, int chapterNumber, MangaCallback<List<String>> callback) {
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            String baseUrl = "https://api.mangadex.org/at-home/server/" + chapterId;

            try {
                Request request = new Request.Builder()
                        .url(baseUrl)
                        .build();

                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) {
                    callback.onError("API lỗi: " + response.code());
                    return;
                }

                String body = response.body().string();
                JSONObject json = new JSONObject(body);
                String baseUrlImg = json.getString("baseUrl");
                JSONObject chapter = json.getJSONObject("chapter");
                JSONArray dataArray = chapter.getJSONArray("data");
                String hash = chapter.getString("hash");
                List<String> imageUrls = new ArrayList<>();
                for (int i = 0; i < dataArray.length(); i++) {
                    String filename = dataArray.getString(i);
                    String fullUrl = baseUrlImg + "/data/" + hash + "/" + filename;
                    Log.d("IMAGE_URL", "Ảnh chương: " + fullUrl);
                    imageUrls.add(fullUrl);
                }

                ChapterImageProvider.saveChapterImages(mangaId, chapterNumber, chapterId, imageUrls);

                callback.onSuccess(imageUrls);

            } catch (IOException e) {
                callback.onError("IO Exception: " + e.getMessage());
            } catch (Exception e) {
                callback.onError("Exception: " + e.getMessage());
            }
        }).start();
    }

}
