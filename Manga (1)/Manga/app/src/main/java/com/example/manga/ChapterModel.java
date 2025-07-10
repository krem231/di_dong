package com.example.manga;
public class ChapterModel {
    private String title;
    private String date;
    private int number;
    private String mangaId;
    private String chapterId;
    public ChapterModel(String title, String date, int number, String mangaId, String chapterID) {
        this.title = title;
        this.date = date;
        this.number = number;
        this.mangaId = mangaId;
        this.chapterId=chapterID;
    }


    public String getTitle() { return title; }
    public String getDate() { return date; }
    public String getChapterId() {return chapterId;}
    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }
    public int getNumber() { return number; }
    public String getMangaId() { return mangaId; }
    public static ChapterModel fromChapterData(ChapterData data, String mangaId) {
        int number = -1;
        try {
            number = data.attributes.chapter != null ? (int) Float.parseFloat(data.attributes.chapter) : -1;
        } catch (NumberFormatException e) {
            // ignore
        }

        String title = data.attributes.title != null ? data.attributes.title : "Chương " + data.attributes.chapter;
        String date = ""; // hoặc lấy từ API nếu có
        return new ChapterModel(title, date, number, mangaId, data.id);
    }


}
