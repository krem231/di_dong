package com.example.manga;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UserProfileActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_IMAGE = 1;

    private ImageView imgAvatar;
    private ImageButton btnHome, btnSearch, btnUser;
    private TextView tvUsername, tvEmail;
    private Button btnEditProfile;

    private SharedPreferences sharedPreferences;

    @SuppressLint("IntentReset")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user); // user.xml layout

        // Ánh xạ view
        imgAvatar = findViewById(R.id.imgAvatar);
        btnHome = findViewById(R.id.btnHome);
        btnSearch = findViewById(R.id.btnSearch);
        btnUser = findViewById(R.id.btnUser);
        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);
        btnEditProfile = findViewById(R.id.btnEditProfile);

        // Lấy trạng thái đăng nhập từ SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        String username = sharedPreferences.getString("username", "");
        String email = sharedPreferences.getString("email", "");

        if (isLoggedIn) {
            tvUsername.setText(username);
            tvEmail.setText(email);
            btnEditProfile.setText("Đăng xuất");
        } else {
            tvUsername.setText("Khách");
            tvEmail.setText("Chưa đăng nhập");
            btnEditProfile.setText("Đăng nhập");
        }

        // Avatar click
        imgAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
        });

        // Nút điều hướng
        btnHome.setOnClickListener(view -> {
            Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
            startActivity(intent);
        });

        btnSearch.setOnClickListener(view -> {
            Intent intent = new Intent(UserProfileActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        btnUser.setOnClickListener(view -> {
            Toast.makeText(this, "Bạn đang ở trang hồ sơ", Toast.LENGTH_SHORT).show();
        });

        // Xử lý nút đăng nhập / đăng xuất
        btnEditProfile.setOnClickListener(view -> {
            if (isLoggedIn) {
                // Nếu đang đăng nhập → đăng xuất
                sharedPreferences.edit().clear().apply();
                Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
                // Reload lại activity
                recreate();
            } else {
                // Nếu chưa đăng nhập → chuyển đến LoginActivity
                Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    // Xử lý kết quả chọn ảnh
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                imgAvatar.setImageURI(selectedImageUri);
            } else {
                Toast.makeText(this, "Không thể chọn ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
