package com.example.manga;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Lớp cấu hình Retrofit client để gọi API từ server PHP/XAMPP
 */
public class ApiClient {

    // Địa chỉ server nội bộ Android emulator (trỏ tới localhost của máy thật)
    private static final String BASE_URL = "http://10.0.2.2/android_api/";

    // Biến giữ đối tượng Retrofit
    private static Retrofit retrofit = null;
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    /**
     * Trả về một thể hiện của Retrofit client
     */
    public static Retrofit getClient() {
        if (retrofit == null) {
            // Log Interceptor
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
