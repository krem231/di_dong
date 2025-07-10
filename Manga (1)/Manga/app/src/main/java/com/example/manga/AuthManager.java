package com.example.manga;

import android.util.Log;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.RequestBody;
import org.json.JSONObject;

public class AuthManager {
    private static final String TOKEN_URL = "https://auth.mangadex.org/realms/mangadex/protocol/openid-connect/token";
    private static final String CLIENT_ID = "personal-client-fba454f0-e547-4038-9568-7dd2988ab8b0-c51fe868";
    private static final String CLIENT_SECRET = "4HVgSGa9nfQCseUaumWIgBNRZZ8QSPRK";
    private static final String USERNAME = "KaTanMye";
    private static final String PASSWORD = "katanmy2345";

    private static String accessToken;
    private static String refreshToken;

    public static String getAccessToken() {
        return accessToken;
    }

    public static boolean authenticate() {
        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("grant_type", "password")
                    .add("username", USERNAME)
                    .add("password", PASSWORD)
                    .add("client_id", CLIENT_ID)
                    .add("client_secret", CLIENT_SECRET)
                    .build();

            Request request = new Request.Builder()
                    .url(TOKEN_URL)
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            String jsonData = response.body().string();
            JSONObject jsonObject = new JSONObject(jsonData);
            accessToken = jsonObject.getString("access_token");
            refreshToken = jsonObject.getString("refresh_token");
            return true;
        } catch (Exception e) {
            Log.e("AuthManager", "Authentication failed: " + e.getMessage());
            return false;
        }
    }

    public static boolean refreshToken() {
        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("grant_type", "refresh_token")
                    .add("refresh_token", refreshToken)
                    .add("client_id", CLIENT_ID)
                    .add("client_secret", CLIENT_SECRET)
                    .build();

            Request request = new Request.Builder()
                    .url(TOKEN_URL)
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            String jsonData = response.body().string();
            JSONObject jsonObject = new JSONObject(jsonData);
            accessToken = jsonObject.getString("access_token");
            refreshToken = jsonObject.getString("refresh_token");
            return true;
        } catch (Exception e) {
            Log.e("AuthManager", "Refresh failed: " + e.getMessage());
            return false;
        }
    }
}
