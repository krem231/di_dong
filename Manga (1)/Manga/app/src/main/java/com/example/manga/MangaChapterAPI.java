package com.example.manga;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MangaChapterAPI {
    private static final String BASE_URL = "https://api.mangadex.org/";

    public static void fetchChapters(String mangaId, MangaCallback<List<ChapterData>> callback) {
        new Thread(() -> {
            try {
                String url = BASE_URL + "chapter?manga=" + mangaId + "&limit=100&translatedLanguage[]=en";

                Request request = new Request.Builder().url(url).build();
                Response response = new OkHttpClient().newCall(request).execute();

                String json = response.body().string();
                JSONObject obj = new JSONObject(json);

                // Check nếu không có trường "data"
                if (!obj.has("data")) {
                    callback.onError("API trả về lỗi: Không có trường 'data'");
                    return;
                }

                JSONArray data = obj.getJSONArray("data");
                List<ChapterData> chapterDataList = new ArrayList<>();

                for (int i = 0; i < data.length(); i++) {
                    JSONObject chap = data.getJSONObject(i);
                    JSONObject attr = chap.getJSONObject("attributes");

                    ChapterData chapterData = new ChapterData();
                    chapterData.id = chap.getString("id");

                    ChapterData.ChapterAttributes attributes = new ChapterData.ChapterAttributes();
                    attributes.title = attr.optString("title", null);
                    attributes.chapter = attr.optString("chapter", null);
                    chapterData.attributes = attributes;

                    chapterDataList.add(chapterData);
                }

                callback.onSuccess(chapterDataList);

            } catch (Exception e) {
                callback.onError("Lỗi tải chapter: " + e.getMessage());
            }
        }).start();
    }

    // Hàm tiện ích nếu cần convert ChapterData -> ChapterModel
    public static List<ChapterModel> convertToModels(List<ChapterData> chapterDataList, String mangaId) {
        List<ChapterModel> modelList = new ArrayList<>();
        for (ChapterData data : chapterDataList) {
            ChapterModel model = ChapterModel.fromChapterData(data, mangaId);
            if (model.getNumber() != -1) {
                modelList.add(model);
                ChapterImageProvider.saveChapterImages(mangaId, model.getNumber(), model.getChapterId(), new ArrayList<>());
            }
        }
        return modelList;
    }
}
