package com.example.manga;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import com.bumptech.glide.Glide;
public class MangaRepository {
    private static MangaRepository instance;
    private final List<MangaDataProvider.Manga> cachedMangaList = new ArrayList<>();

    private MangaRepository() {}

    public static MangaRepository getInstance() {
        if (instance == null) {
            instance = new MangaRepository();
        }
        return instance;
    }

    public void fetchMangaList(MangaCallback<List<MangaDataProvider.Manga>> callback) {
        RetrofitClient.fetchMangaList(new MangaCallback<List<MangaDataProvider.Manga>>() {
            @Override
            public void onSuccess(List<MangaDataProvider.Manga> mangaList) {
                cachedMangaList.clear();
                cachedMangaList.addAll(mangaList);
                MangaDataProvider.setMangaList(mangaList);
                callback.onSuccess(mangaList);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }


    public MangaDataProvider.Manga getMangaById(String id) {
        for (MangaDataProvider.Manga manga : cachedMangaList) {
            if (manga.id.equals(id)) return manga;
        }
        return null;
    }
}
