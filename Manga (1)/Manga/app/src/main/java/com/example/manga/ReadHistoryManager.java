// ReadHistoryManager.java
package com.example.manga;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.LinkedHashSet;
import java.util.Set;

public class ReadHistoryManager {
    private static final String PREF_NAME = "ReadHistory";
    private static final String KEY_READ_MANGA = "read_manga";

    public static void markAsRead(Context context, String mangaId) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Set<String> readSet = new LinkedHashSet<>(prefs.getStringSet(KEY_READ_MANGA, new LinkedHashSet<>()));
        readSet.remove(mangaId);
        readSet.add(mangaId);

        prefs.edit().putStringSet(KEY_READ_MANGA, readSet).apply();
    }

    public static Set<String> getReadHistory(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getStringSet(KEY_READ_MANGA, new LinkedHashSet<>());
    }
}
