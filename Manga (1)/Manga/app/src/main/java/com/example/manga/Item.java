package com.example.manga;

public class Item {
    private String mangaName;
    private String image;
    private String mangaId;

    public Item(String mangaName, String image, String mangaId) {
        this.mangaName = mangaName;
        this.image = image;
        this.mangaId = mangaId;
    }

    public String getMangaName() {
        return mangaName;
    }

    public void setMangaName(String mangaName) {
        this.mangaName = mangaName;
    }

    public String getImage() {
        return image;
    }

    public String getMangaId() {
        return mangaId;
    }
}
