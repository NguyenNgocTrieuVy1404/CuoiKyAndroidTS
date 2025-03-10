package com.example.technologyactivity;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.view.ContextThemeWrapper;

import com.example.database.DatabaseUser;
import com.example.modeldata.UserShop;

public class ProfileFragment extends Fragment {

    private EditText nameEditText, phoneEditText, emailEditText;
    private RadioGroup genderRadioGroup;
    private RadioButton maleRadioButton, femaleRadioButton;
    private Button editButton, saveButton, logoutButton, changePasswordButton;
    private DatabaseUser databaseHelper;
    private String currentUser;

    public static ProfileFragment newInstance(String currentUser) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString("current_user", currentUser);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        nameEditText = view.findViewById(R.id.name_edit_text);
        phoneEditText = view.findViewById(R.id.phone_edit_text);
        emailEditText = view.findViewById(R.id.email_edit_text);
        genderRadioGroup = view.findViewById(R.id.gender_group);
        maleRadioButton = view.findViewById(R.id.male_radio_button);
        femaleRadioButton = view.findViewById(R.id.female_radio_button);
        editButton = view.findViewById(R.id.edit_button);
        saveButton = view.findViewById(R.id.save_button);
        logoutButton = view.findViewById(R.id.dangxuat_button);
        changePasswordButton = view.findViewById(R.id.change_password_button);

        databaseHelper = new DatabaseUser(requireContext());

        if (getArguments() != null) {
            currentUser = getArguments().getString("username");
        }

        if (currentUser == null || currentUser.isEmpty()) {
            Toast.makeText(requireContext(), "Lỗi: Không có thông tin người dùng", Toast.LENGTH_SHORT).show();
            return view;
        }

        loadUserData();

        saveButton.setOnClickListener(v -> saveUserData());
        editButton.setOnClickListener(v -> setEditable(true));
        logoutButton.setOnClickListener(v -> showLogoutConfirmationDialog());
        changePasswordButton.setOnClickListener(v -> showChangePasswordDialog());

        return view;
    }
    private void setEditable(boolean editable) {
        nameEditText.setEnabled(editable);
        phoneEditText.setEnabled(editable);
        emailEditText.setEnabled(editable);
        genderRadioGroup.setEnabled(editable);
        maleRadioButton.setEnabled(editable);
        femaleRadioButton.setEnabled(editable);
        saveButton.setEnabled(editable);
    }
    private void setEnabled() {
        nameEditText.setEnabled(false);
        phoneEditText.setEnabled(false);
        emailEditText.setEnabled(false);
        genderRadioGroup.setEnabled(false);
        maleRadioButton.setEnabled(false);
        femaleRadioButton.setEnabled(false);
        saveButton.setEnabled(false);
    }
    private void loadUserData() {
        UserShop user = databaseHelper.getUser(currentUser);
        if (user != null) {
            nameEditText.setText(user.getFullName());
            phoneEditText.setText(user.getPhoneNumber());
            emailEditText.setText(user.getEmail());

            if ("Nam".equals(user.getGender())) {
                maleRadioButton.setChecked(true);
            } else {
                femaleRadioButton.setChecked(true);
            }
            Log.d("ProfileFragment", "Thông tin người dùng đã được tải.");
        } else {
            Toast.makeText(requireContext(), "Không thể tải thông tin người dùng", Toast.LENGTH_SHORT).show();
            Log.e("ProfileFragment", "Không tìm thấy thông tin người dùng.");
        }
    }

    private void saveUserData() {
        String newName = nameEditText.getText().toString().trim();
        String newPhone = phoneEditText.getText().toString().trim();
        String newEmail = emailEditText.getText().toString().trim();
        String newGender = getSelectedGender();

        if (TextUtils.isEmpty(newName) || TextUtils.isEmpty(newPhone) || TextUtils.isEmpty(newEmail) || newGender == null) {
            Toast.makeText(requireContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isUpdated = databaseHelper.updateUser(currentUser, newName, newPhone, newEmail, newGender);
        if (isUpdated) {
            Toast.makeText(requireContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
            setEnabled();
        } else {
            Toast.makeText(requireContext(), "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
        }
    }

    private String getSelectedGender() {
        int selectedId = genderRadioGroup.getCheckedRadioButtonId();
        if (selectedId == maleRadioButton.getId()) {
            return "Nam";
        } else if (selectedId == femaleRadioButton.getId()) {
            return "Nữ";
        } else {
            return null;
        }
    }

    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xác nhận đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất không?")
                .setPositiveButton("Có", (dialog, which) -> {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    requireActivity().finish();
                })
                .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void showChangePasswordDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_change_password, null);

        EditText currentPasswordEt = dialogView.findViewById(R.id.current_password);
        EditText newPasswordEt = dialogView.findViewById(R.id.new_password);
        EditText confirmPasswordEt = dialogView.findViewById(R.id.confirm_password);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        Button btnConfirm = dialogView.findViewById(R.id.btn_confirm);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
            .setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        
        btnConfirm.setOnClickListener(v -> {
            String currentPassword = currentPasswordEt.getText().toString().trim();
            String newPassword = newPasswordEt.getText().toString().trim();
            String confirmPassword = confirmPasswordEt.getText().toString().trim();

            if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(getContext(), "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            if (databaseHelper.checkPassword(currentUser, currentPassword)) {
                if (databaseHelper.updatePassword(currentUser, newPassword)) {
                    Toast.makeText(getContext(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(getContext(), "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Mật khẩu hiện tại không đúng", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
}
