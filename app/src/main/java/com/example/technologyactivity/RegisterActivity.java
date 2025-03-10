package com.example.technologyactivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.database.DatabaseUser;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtHoten, edtEmail, edtUsername, edtPassword, edtSdt;
    private RadioGroup genderRadioGroup;
    private Button btnRegister,backBtn;
    private DatabaseUser databaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtHoten = findViewById(R.id.registerHoten);
        edtEmail = findViewById(R.id.registerEmail);
        edtUsername = findViewById(R.id.registerUsernameEditText);
        edtPassword = findViewById(R.id.registerPasswordEditText);
        edtSdt = findViewById(R.id.registersdt);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        btnRegister = findViewById(R.id.registerButton);
        backBtn = findViewById(R.id.backToLoginButton);

        databaseUser = new DatabaseUser(this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void registerUser() {
        // Lấy dữ liệu từ các trường nhập
        String hoten = edtHoten.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = ((EditText) findViewById(R.id.registerConfirmPasswordEditText)).getText().toString().trim();  // Lấy confirm password
        String sdt = edtSdt.getText().toString().trim();
        int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId();
        String gender = ((RadioButton) findViewById(selectedGenderId)).getText().toString();

        if (TextUtils.isEmpty(hoten) || TextUtils.isEmpty(email) || TextUtils.isEmpty(username) ||
                TextUtils.isEmpty(password) || TextUtils.isEmpty(sdt) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isValidEmail(email)) {
            edtEmail.setError("Email không hợp lệ, phải đúng định dạng!");
            return;
        }
        if (!isValidPhoneNumber(sdt)) {
            edtSdt.setError("Số điện thoại không hợp lệ, phải đúng định dạng!");
            return;
        }

        if (databaseUser.isEmailExists(email)) {
            Toast.makeText(this, "Email đã tồn tại!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (databaseUser.isPhoneExists(sdt)) {
            Toast.makeText(this, "Số điện thoại đã tồn tại!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (databaseUser.isUsernameExists(username)) {
            Toast.makeText(this, "Tên tài khoản đã tồn tại!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu và xác nhận mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = databaseUser.addUser(hoten, email, username, password, sdt, gender);

        if (success) {
            Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            intent.putExtra("username", edtUsername.getText().toString().trim());
            intent.putExtra("password", edtPassword.getText().toString().trim());
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this, "Đăng ký thất bại. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
        }
    }
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern emailPattern = Pattern.compile(emailRegex);
        return email != null && emailPattern.matcher(email).matches();
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        String phoneRegex = "^(?:\\+84|0)\\d{9}$";
        Pattern phonePattern = Pattern.compile(phoneRegex);
        return phoneNumber != null && phonePattern.matcher(phoneNumber).matches();
    }


}
