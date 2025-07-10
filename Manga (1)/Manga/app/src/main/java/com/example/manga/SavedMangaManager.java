package com.example.manga;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class SavedMangaManager {
    private static final String PREF_NAME = "SavedMangaPrefs";
    private static final String KEY_SAVED_IDS = "saved_manga_ids";

    public static void saveManga(Context context, String mangaId, String title, String imageUrl)
 {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Set<String> savedIds = getSavedMangaIds(context);
        savedIds.add(mangaId);
        prefs.edit().putStringSet(KEY_SAVED_IDS, savedIds).apply();
    }

    public static void removeManga(Context context, String mangaId) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Set<String> savedIds = getSavedMangaIds(context);
        savedIds.remove(mangaId);
        prefs.edit().putStringSet(KEY_SAVED_IDS, savedIds).apply();
    }

    public static Set<String> getSavedMangaIds(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return new HashSet<>(prefs.getStringSet(KEY_SAVED_IDS, new HashSet<>()));
    }

    public static boolean isSaved(Context context, String mangaId) {
        return getSavedMangaIds(context).contains(mangaId);
    }
}
