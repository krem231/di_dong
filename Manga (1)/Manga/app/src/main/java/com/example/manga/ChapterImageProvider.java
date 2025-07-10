package com.example.manga;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.Comparator;

public class ChapterImageProvider {

    public static class ChapterInfo {
        public int chapterNumber;
        public String chapterId;

        public ChapterInfo(int number, String id) {
            this.chapterNumber = number;
            this.chapterId = id;
        }
    }

    private static final Map<String, List<ChapterInfo>> mangaChaptersMap = new HashMap<>();
    private static final Map<String, List<String>> chapterImageUrlMap = new HashMap<>();

    public static void saveChapterImages(String mangaId, int chapterNumber, String chapterId, List<String> imageUrls) {
        chapterImageUrlMap.put(chapterId, imageUrls);

        List<ChapterInfo> list = mangaChaptersMap.getOrDefault(mangaId, new ArrayList<>());
        boolean exists = false;
        for (ChapterInfo info : list) {
            if (info.chapterNumber == chapterNumber) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            list.add(new ChapterInfo(chapterNumber, chapterId));
            list.sort(Comparator.comparingInt(ci -> ci.chapterNumber)); // đảm bảo thứ tự tăng dần
        }
        mangaChaptersMap.put(mangaId, list);
    }

    public static String getChapterId(String mangaId, int chapterNumber) {
        List<ChapterInfo> list = mangaChaptersMap.getOrDefault(mangaId, new ArrayList<>());
        for (ChapterInfo info : list) {
            if (info.chapterNumber == chapterNumber) return info.chapterId;
        }
        return null;
    }

    public static int getTotalChapters(String mangaId) {
        List<ChapterInfo> list = mangaChaptersMap.getOrDefault(mangaId, new ArrayList<>());
        return list.size();
    }

    public static List<ChapterInfo> getAllChapters(String mangaId) {
        return mangaChaptersMap.getOrDefault(mangaId, new ArrayList<>());
    }

    public static List<String> getChapterImages(String chapterId) {
        return chapterImageUrlMap.getOrDefault(chapterId, new ArrayList<>());
    }

    public static boolean hasImagesForChapter(String chapterId) {
        return chapterImageUrlMap.containsKey(chapterId);
    }

    public static void clearCache() {
        mangaChaptersMap.clear();
        chapterImageUrlMap.clear();
    }
}