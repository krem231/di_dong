package com.example.manga;
import java.util.ArrayList;
import java.util.List;

public class ChapterRespond {
    public List<ChapterData> data;

    public List<String> toChapterList() {
        List<String> chapters = new ArrayList<>();
        for (ChapterData item : data) {
            String title = item.attributes.title != null ? item.attributes.title : "Chapter " + item.attributes.chapter;
            chapters.add(title);
        }
        return chapters;
    }
}
