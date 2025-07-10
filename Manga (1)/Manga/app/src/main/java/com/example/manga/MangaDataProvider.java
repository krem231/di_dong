package com.example.manga;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class MangaDataProvider {

    public static class Manga {
        @SerializedName("id")
        public String id;
        @SerializedName("title")
        public String title;
        @SerializedName("overview")
        public String overview;
        @SerializedName("imageUrl")
        public String imageUrl;

        public Manga() {

        }
        public Manga(String id, String title, String overview, String imageUrl) {
            this.id = id;
            this.title = title;
            this.overview = overview;
            this.imageUrl = imageUrl;

        }
    }
    public static void setMangaList(List<Manga> list) {
        mangaList.clear();
        mangaList.addAll(list);
    }

    private static final List<Manga> mangaList = new ArrayList<>();



    public static Manga getMangaById(String id) {
        for (Manga manga : mangaList) {
            if (manga.id.equals(id)) {
                return manga;
            }
        }
        return null;
    }
    public static List<Manga> getAll() {
        return mangaList;
    }

    public static List<Manga> getAllManga() {
        return new ArrayList<>(mangaList);
    }
}
