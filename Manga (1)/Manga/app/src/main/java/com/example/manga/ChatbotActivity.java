package com.example.manga;

import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatbotActivity extends AppCompatActivity {
    private RecyclerView recyclerChat;
    private EditText messageInput;
    private Button sendButton;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatHistory = new ArrayList<>();
    private OkHttpClient client = new OkHttpClient();

    private Map<String, String> tagMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        recyclerChat = findViewById(R.id.recyclerChat);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);

        recyclerChat.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new ChatAdapter(chatHistory);
        recyclerChat.setAdapter(chatAdapter);

        sendButton.setOnClickListener(v -> {
            String userMessage = messageInput.getText().toString().trim().toLowerCase();
            if (!userMessage.isEmpty()) {
                addMessageOnUiThread(userMessage, true);
                handleUserRequest(userMessage);
                messageInput.setText("");
            }
        });

        fetchTags();
    }

    private void addMessage(String text, boolean isUser) {
        chatHistory.add(new ChatMessage(text, isUser));
        chatAdapter.notifyDataSetChanged();
        recyclerChat.scrollToPosition(chatHistory.size() - 1);
    }

    private void addMessageOnUiThread(String text, boolean isUser) {
        runOnUiThread(() -> addMessage(text, isUser));
    }

    private void fetchTags() {
        String url = "https://api.mangadex.org/manga/tag";

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                addMessageOnUiThread("Lỗi khi tải danh sách tag: " + e.getMessage(), false);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonResponse = response.body().string();
                try {
                    JSONObject obj = new JSONObject(jsonResponse);
                    JSONArray tagsArray = obj.getJSONArray("data");
                    for (int i = 0; i < tagsArray.length(); i++) {
                        JSONObject tagObj = tagsArray.getJSONObject(i);
                        String id = tagObj.getString("id");
                        JSONObject attributes = tagObj.getJSONObject("attributes");
                        JSONObject nameObj = attributes.getJSONObject("name");
                        String name = nameObj.optString("en", "").toLowerCase();

                        if (name.equals("erotica") || name.equals("pornographic")) continue;
                        tagMap.put(name, id);
                        Log.d("TagList", "Loaded tags: " + tagMap.keySet());

                    }
                    addMessageOnUiThread("Đã tải xong danh sách tag (" + tagMap.size() + " tag).", false);
                } catch (Exception e) {
                    addMessageOnUiThread("Lỗi phân tích danh sách tag: " + e.getMessage(), false);
                }
            }

        });
    }

    private void handleUserRequest(String userMessage) {
        if (tagMap.isEmpty()) {
            addMessageOnUiThread("Vui lòng đợi, đang tải danh sách thể loại...", false);
            return;
        }

        // Tìm tất cả tag từ câu người dùng
        List<String> genres = extractGenresFromMessage(userMessage);
        if (genres.isEmpty()) {
            addMessageOnUiThread("Không tìm thấy thể loại nào trong câu của bạn.", false);
            return;
        }

        List<String> tagIds = new ArrayList<>();
        for (String genre : genres) {
            String tagId = getTagIdForGenre(genre);
            if (!tagId.isEmpty()) {
                tagIds.add(tagId);
            } else {
                addMessageOnUiThread("Thể loại \"" + genre + "\" chưa được hỗ trợ.", false);
            }
        }

        if (tagIds.isEmpty()) {
            addMessageOnUiThread("Không có thể loại hợp lệ để tìm kiếm.", false);
            return;
        }

        String tagNames = String.join(", ", genres);
        addMessageOnUiThread("Đang tìm truyện thuộc thể loại: " + tagNames, false);
        fetchMangaRecommendations(tagIds, tagNames);
    }

    private List<String> extractGenresFromMessage(String message) {
        List<String> genres = new ArrayList<>();

        // Loại bỏ dấu câu và normalize về chữ thường
        String normalizedMsg = message.replaceAll("[^a-zA-Z\\s]", "").toLowerCase();

        // Tách câu thành từ
        String[] words = normalizedMsg.split("\\s+");

        for (String word : words) {
            for (String tagName : tagMap.keySet()) {
                if (tagName.equalsIgnoreCase(word)) {
                    genres.add(tagName);
                }
            }
        }

        return genres;
    }



    private String getTagIdForGenre(String genre) {
        return tagMap.getOrDefault(genre.toLowerCase(), "");
    }

    private void fetchMangaRecommendations(List<String> tagIds, String genreNames) {
        StringBuilder urlBuilder = new StringBuilder("https://api.mangadex.org/manga?");
        for (String tagId : tagIds) {
            urlBuilder.append("includedTags[]=").append(tagId).append("&");
        }
        urlBuilder.append("limit=5");
        String url = urlBuilder.toString();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                addMessageOnUiThread("Lỗi MangaDex: " + e.getMessage(), false);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonResponse = response.body().string();
                List<String> mangas = parseMangaList(jsonResponse);
                if (mangas.isEmpty()) {
                    addMessageOnUiThread("Không tìm thấy truyện thuộc thể loại: " + genreNames, false);
                } else {
                    for (String manga : mangas) {
                        addMessageOnUiThread( manga, false);
                    }
                }
            }
        });
    }

    private List<String> parseMangaList(String json) {
        List<String> mangaTitles = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray dataArray = obj.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject manga = dataArray.getJSONObject(i);
                JSONObject attributes = manga.getJSONObject("attributes");
                JSONObject titleObj = attributes.getJSONObject("title");
                String titleKey = titleObj.keys().next();
                mangaTitles.add(titleObj.getString(titleKey));
            }
        } catch (Exception e) {
            mangaTitles.add("Lỗi phân tích danh sách truyện");
        }
        return mangaTitles;
    }
}
