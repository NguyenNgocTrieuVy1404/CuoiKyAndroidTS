package com.example.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.modeldata.UserShop;

import org.mindrot.jbcrypt.BCrypt;

public class DatabaseUser extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "VyShop.db";
    private static final int DATABASE_VERSION = 5;
    private static final String TABLE_NAME = "userAccounts";

    public DatabaseUser(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
        Log.d("DatabaseShop", "Cơ sở dữ liệu đã được tạo thành công.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DatabaseShop", "Nâng cấp cơ sở dữ liệu từ phiên bản " + oldVersion + " lên " + newVersion);
        try {
            if (oldVersion < 5) {
                db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN gender TEXT");
                Log.d("DatabaseShop", "Đã thêm cột 'gender' vào bảng " + TABLE_NAME);
            }
        } catch (SQLiteException e) {
            Log.e("DatabaseShop", "Lỗi khi nâng cấp cơ sở dữ liệu: " + e.getMessage());
        }
    }

    private void createTable(SQLiteDatabase db) {
        String createTableSQL = "CREATE TABLE " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "full_name TEXT NOT NULL, " +
                "email TEXT NOT NULL, " +
                "username TEXT NOT NULL UNIQUE, " +
                "password TEXT NOT NULL, " +
                "phone_number TEXT, " +
                "gender TEXT)";
        db.execSQL(createTableSQL);
    }

    public boolean checkUser(String input, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE (username = ? OR email = ? OR phone_number = ?) AND password IS NOT NULL";
            cursor = db.rawQuery(query, new String[]{input, input, input});
            if (cursor.moveToFirst()) {

                String storedHashedPassword = cursor.getString(cursor.getColumnIndexOrThrow("password"));

                return BCrypt.checkpw(password, storedHashedPassword);
            }
            return false;
        } catch (Exception e) {
            Log.e("DatabaseShop", "Lỗi kiểm tra thông tin đăng nhập: " + e.getMessage());
            return false;
        } finally {
            if (cursor != null) cursor.close();
        }
    }


    public boolean addUser(String fullName, String email, String username, String password, String phoneNumber, String gender) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            String insertSQL = "INSERT INTO " + TABLE_NAME +
                    " (full_name, email, username, password, phone_number, gender) VALUES (?, ?, ?, ?, ?, ?)";
            db.execSQL(insertSQL, new String[]{fullName, email, username, hashedPassword, phoneNumber, gender});
            Log.d("DatabaseShop", "Thêm người dùng thành công: " + username);
            return true;
        } catch (SQLiteException e) {
            Log.e("DatabaseShop", "Lỗi thêm người dùng: " + e.getMessage());
            return false;
        }
    }

    public UserShop getUser(String input) {
        if (input == null || input.isEmpty()) {
            Log.e("DatabaseShop", "Tham số đầu vào là null hoặc rỗng.");
            return null;
        }

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM userAccounts WHERE username = ? OR email = ? OR phone_number = ?";
            cursor = db.rawQuery(query, new String[]{input, input, input});
            if (cursor.moveToFirst()) {
                return new UserShop(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("full_name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("email")),
                        cursor.getString(cursor.getColumnIndexOrThrow("username")),
                        cursor.getString(cursor.getColumnIndexOrThrow("phone_number")),
                        cursor.getString(cursor.getColumnIndexOrThrow("gender"))
                );
            }
            return null;
        } catch (SQLiteException e) {
            Log.e("DatabaseShop", "Lỗi khi lấy thông tin người dùng: " + e.getMessage());
            return null;
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    public boolean updateUser(String input, String fullName, String phone, String email, String gender) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            String updateSQL = "UPDATE " + TABLE_NAME +
                    " SET full_name = ?, phone_number = ?, email = ?, gender = ? WHERE username = ? OR email = ? OR phone_number = ?";
            db.execSQL(updateSQL, new String[]{fullName, phone, email, gender, input, input, input});
            return true;
        } catch (SQLiteException e) {
            Log.e("DatabaseShop", "Lỗi cập nhật dữ liệu: " + e.getMessage());
            return false;
        }
    }

    public void deleteAllData() {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL("DELETE FROM " + TABLE_NAME);
            Log.d("DatabaseShop", "Tất cả dữ liệu trong bảng '" + TABLE_NAME + "' đã bị xóa.");
        } catch (SQLiteException e) {
            Log.e("DatabaseShop", "Lỗi khi xóa dữ liệu trong bảng: " + e.getMessage());
        }
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE email = ?";
            cursor = db.rawQuery(query, new String[]{email});
            return cursor.getCount() > 0;
        } catch (SQLiteException e) {
            Log.e("DatabaseShop", "Lỗi khi kiểm tra email: " + e.getMessage());
            return false;
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    public boolean isPhoneExists(String phoneNumber) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE phone_number = ?";
            cursor = db.rawQuery(query, new String[]{phoneNumber});
            return cursor.getCount() > 0;
        } catch (SQLiteException e) {
            Log.e("DatabaseShop", "Lỗi khi kiểm tra số điện thoại: " + e.getMessage());
            return false;
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    public boolean isUsernameExists(String username) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE username = ?";
            cursor = db.rawQuery(query, new String[]{username});
            return cursor.getCount() > 0;
        } catch (SQLiteException e) {
            Log.e("DatabaseShop", "Lỗi khi kiểm tra tên tài khoản: " + e.getMessage());
            return false;
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    public boolean checkPassword(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_NAME, new String[]{"password"},
                    "username = ?", new String[]{username},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                String storedHashedPassword = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                return BCrypt.checkpw(password, storedHashedPassword);
            }
            return false;
        } catch (Exception e) {
            Log.e("DatabaseShop", "Lỗi kiểm tra mật khẩu: " + e.getMessage());
            return false;
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    public boolean updatePassword(String username, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            
            ContentValues values = new ContentValues();
            values.put("password", hashedPassword);

            int rowsAffected = db.update(TABLE_NAME, values,
                    "username = ?", new String[]{username});
                
            Log.d("DatabaseShop", "Số dòng được cập nhật: " + rowsAffected);
            return rowsAffected > 0;
        } catch (Exception e) {
            Log.e("DatabaseShop", "Lỗi cập nhật mật khẩu: " + e.getMessage());
            return false;
        }
    }
}