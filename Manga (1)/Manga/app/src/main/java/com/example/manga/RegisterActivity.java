package com.example.manga;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword, etConfirmPassword,etDob;
    private Button btnRegister, btnToLogin;

    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        // Ánh xạ view
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnToLogin = findViewById(R.id.btnToLogin);
etDob=findViewById(R.id.etDob);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        btnToLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
        etDob.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String dob =selectedYear + "-" + String.format("%02d", selectedMonth + 1) + "-" + String.format("%02d", selectedDay);
                        etDob.setText(dob);
                    },
                    year, month, day);

            datePickerDialog.show();
        });

        btnRegister.setOnClickListener(v -> handleRegister());
    }

    private void handleRegister() {
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String dob=etDob.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gửi yêu cầu đăng ký
        Call<ApiResponse> call = apiInterface.register(username, password, email,dob);
        Log.d("REGISTER_DEBUG", "Gửi đăng ký: username=" + username + ", email=" + email + ", password=" + password);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.d("REGISTER_DEBUG", "Response code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    Log.d("REGISTER_DEBUG", "Response body: " + apiResponse.getStatus() + ", message: " + apiResponse.getMessage());

                    switch (apiResponse.getStatus()) {
                        case "success":
                            Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                            break;
                        case "exists":
                            Toast.makeText(RegisterActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(RegisterActivity.this, "Lỗi: " + apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            break;
                    }
                } else {
                    try {
                        Log.e("REGISTER_DEBUG", "Lỗi response: " + response.errorBody().string());
                    } catch (Exception e) {
                        Log.e("REGISTER_DEBUG", "Lỗi khi đọc errorBody: " + e.getMessage());
                    }
                    Toast.makeText(RegisterActivity.this, "Lỗi phản hồi từ máy chủ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("REGISTER_DEBUG", "Lỗi kết nối: " + t.getMessage(), t);
                Toast.makeText(RegisterActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
