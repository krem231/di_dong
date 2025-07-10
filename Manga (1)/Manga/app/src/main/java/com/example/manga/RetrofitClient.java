package com.example.manga;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RetrofitClient {
    private static final String BASE_URL = "https://api.mangadex.org/";
    private static final String COVER_BASE_URL = "https://uploads.mangadex.org/covers/";
    private static final OkHttpClient client = new OkHttpClient();

    public static void fetchMangaList(MangaCallback<List<MangaDataProvider.Manga>> callback) {
        new Thread(() -> {
            try {
                String token = AuthManager.getAccessToken();

                Request request = new Request.Builder()
                        .url(BASE_URL + "manga?limit=10")
                        .addHeader("Authorization", "Bearer " + token)
                        .build();

                Response response = client.newCall(request).execute();

                if (response.code() == 401) {
                    if (AuthManager.refreshToken()) {
                        fetchMangaList(callback);
                        return;
                    } else {
                        callback.onError("Token hết hạn và không làm mới được");
                        return;
                    }
                }

                String jsonString = response.body().string();
                JSONObject json = new JSONObject(jsonString);
                JSONArray dataArray = json.getJSONArray("data");

                List<MangaDataProvider.Manga> mangaList = new ArrayList<>();

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject item = dataArray.getJSONObject(i);
                    String id = item.getString("id");

                    JSONObject attributes = item.getJSONObject("attributes");
                    JSONObject titleObj = attributes.getJSONObject("title");
                    String lang = titleObj.keys().next();
                    String titleStr = titleObj.getString(lang);

                    String description = "";
                    if (attributes.has("description")) {
                        JSONObject descObj = attributes.getJSONObject("description");
                        if (descObj.length() > 0) {
                            String descKey = descObj.keys().next();
                            description = descObj.getString(descKey);
                        }
                    }


                    String imageUrl = "";
                    Request coverRequest = new Request.Builder()
                            .url(BASE_URL + "cover?manga[]=" + id + "&limit=1")
                            .build();

                    Response coverResponse = client.newCall(coverRequest).execute();
                    if (coverResponse.isSuccessful()) {
                        JSONObject coverJson = new JSONObject(coverResponse.body().string());
                        JSONArray coverData = coverJson.getJSONArray("data");
                        if (coverData.length() > 0) {
                            JSONObject coverItem = coverData.getJSONObject(0);
                            JSONObject coverAttr = coverItem.getJSONObject("attributes");
                            String fileName = coverAttr.getString("fileName");
                            imageUrl = COVER_BASE_URL + id + "/" + fileName;
                        }
                    }
                    MangaDataProvider.Manga manga = new MangaDataProvider.Manga(id, titleStr, description, imageUrl);
                    mangaList.add(manga);
                }

                callback.onSuccess(mangaList);

            } catch (IOException e) {
                callback.onError("Lỗi IO: " + e.getMessage());
            } catch (Exception e) {
                callback.onError("Lỗi phân tích dữ liệu: " + e.getMessage());
            }
        }).start();
    }
}
