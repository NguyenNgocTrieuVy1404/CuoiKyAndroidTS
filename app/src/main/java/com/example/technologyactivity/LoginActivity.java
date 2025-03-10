package com.example.technologyactivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.database.DatabaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameOrEmailOrPhoneEditText, passwordEditText;
    private Button loginButton, faceBookBtn, googleBtn;
    private TextView signUpTextView;
    private DatabaseUser databaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseUser = new DatabaseUser(this);
        SQLiteDatabase db = databaseUser.getWritableDatabase();

        initializeViews();

        setupClickListeners();
    }

    private void initializeViews() {
        usernameOrEmailOrPhoneEditText = findViewById(R.id.usernameOrEmailOrPhoneEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signUpTextView = findViewById(R.id.signUpTextView);
        faceBookBtn = findViewById(R.id.facebookButton);
        googleBtn = findViewById(R.id.googleButton);

    }

    private void setupClickListeners() {
        loginButton.setOnClickListener(v -> handleLogin());
        signUpTextView.setOnClickListener(v -> handleSignUp());

        faceBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/login"));
                startActivity(facebookIntent);
            }
        });

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent googleIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://accounts.google.com/signin"));
                startActivity(googleIntent);
            }
        });
    }

    private void handleLogin() {
        String input = usernameOrEmailOrPhoneEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (input.isEmpty()) {
            usernameOrEmailOrPhoneEditText.setError("Vui lòng nhập tên người dùng, email hoặc số điện thoại");
            return;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Vui lòng nhập mật khẩu");
            return;
        }

        // Kiểm tra thông tin đăng nhập
        if (databaseUser.checkUser(input, password)) {
            Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, FormMain.class);
            intent.putExtra("username", input);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Sai thông tin đăng nhập", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleSignUp() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            String username = data.getStringExtra("username");
            String password = data.getStringExtra("password");
            usernameOrEmailOrPhoneEditText.setText(username);
            passwordEditText.setText(password);
        }
    }
}
