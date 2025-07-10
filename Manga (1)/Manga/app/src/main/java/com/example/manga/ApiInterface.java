package com.example.manga;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
public interface ApiInterface {
    @GET("manga")
    Call<MangaRespond> getMangaList();
    @FormUrlEncoded
    @POST("login.php")
    Call<ApiResponse> login(
            @Field("username") String username,
            @Field("password") String password
    );

    /**
     * API đăng ký tài khoản
     * Gửi POST request đến register.php với 3 field: username, password, email
     */
    @FormUrlEncoded
    @POST("register.php")
    Call<ApiResponse> register(
            @Field("username") String username,
            @Field("password") String password,
            @Field("email") String emai,
            @Field("dob") String dob
    );
}
