package com.example.manga;

public class ViewPagerItem {

    private int imageID;
    private String heading;
    private String ratingText;
    private String mangaId;
    private String imageUrl;

    public ViewPagerItem(int imageID, String heading, String ratingText, String mangaId, String imageUrl) {
        this.imageID = imageID;
        this.heading = heading;
        this.ratingText = ratingText;
        this.mangaId = mangaId;
        this.imageUrl = imageUrl;
    }

    public int getImageID() {
        return imageID;
    }

    public String getHeading() {
        return heading;
    }

    public String getRatingText() {
        return ratingText;
    }

    public String getMangaId() {
        return mangaId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setRatingText(String ratingText) {
        this.ratingText = ratingText;
    }
}
