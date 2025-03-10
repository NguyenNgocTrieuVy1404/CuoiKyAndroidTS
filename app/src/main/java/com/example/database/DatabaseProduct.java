package com.example.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.technologyactivity.R;

public class DatabaseProduct extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ProductDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "products";

    public DatabaseProduct(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "brand TEXT NOT NULL, " +
                "price REAL NOT NULL, " +
                "image_res_id INTEGER NOT NULL, " +
                "description TEXT NOT NULL, " +
                "quantity INTEGER NOT NULL)";
        db.execSQL(createTable);

        insertInitialData(db);
    }

    private void insertInitialData(SQLiteDatabase db) {
        // Smartphones
        insertProduct(db, "Pixel 7", "Google", 599.99, R.drawable.pixel7, 
            "Google Pixel 7 Smartphone - 128GB, 8GB RAM", 4);
        insertProduct(db, "IPhone 14", "Apple", 799.99, R.drawable.iphone14, 
            "Apple iPhone 14 Pro - 128GB, 6GB RAM", 2);
        insertProduct(db, "Galaxy S21", "Samsung", 699.99, R.drawable.galaxys21, 
            "Samsung Galaxy S21 Ultra - 128GB, 12GB RAM", 3);
        insertProduct(db, "Iphone16", "Apple", 120.00, R.drawable.iphone16, 
            "Nike Air Max Running Shoes - Men's, Size 42", 1);
        insertProduct(db, "Iphone 15", "Apple", 10.00, R.drawable.iphone15, 
            "Apple iPhone 15 Accessories - Socks (Colorful) for iPhone Lovers", 10);

        // Laptops
        insertProduct(db, "HP Laptop Pro F5", "HP Laptop", 30.00, R.drawable.hplaptop, 
            "HP Laptop - Intel Core i7, 16GB RAM, 512GB SSD - High Performance for Work & Play", 5);
        insertProduct(db, "Dell XPS 13", "Dell", 1200.00, R.drawable.dell_xps13, 
            "Dell XPS 13 Laptop - Intel Core i7, 16GB RAM, 512GB SSD", 3);
        insertProduct(db, "MacBook Pro 16", "Apple", 2399.99, R.drawable.macbook_pro, 
            "Apple MacBook Pro 16-inch - M1 Pro Chip, 512GB SSD", 1);
        insertProduct(db, "HP Spectre x360", "HP", 1450.00, R.drawable.hp_spectrex360, 
            "HP Spectre x360 Laptop - 13.3-inch Touchscreen, Intel Core i7", 4);
        insertProduct(db, "Microsoft Surface Pro 7", "Microsoft", 999.99, R.drawable.surface_pro7, 
            "Microsoft Surface Pro 7 - 12.3-inch Tablet, Intel Core i5", 5);

        // TVs
        insertProduct(db, "TV LG UHD", "LG", 110.00, R.drawable.tivilg, 
            "Smart Tivi LG UHD 55UQ7050 4K 55 inch", 1);
        insertProduct(db, "LG OLED TV", "LG", 1499.99, R.drawable.lg_oled_tv, 
            "LG 55-inch OLED 4K TV - Self-lighting OLED Display", 2);

        // Audio Devices
        insertProduct(db, "Sony WH-1000XM4", "Sony", 350.00, R.drawable.sonywh, 
            "Sony WH-1000XM4 Wireless Noise-Canceling Headphones", 2);
        insertProduct(db, "Samsung Galaxy Buds", "Samsung", 130.00, R.drawable.samsung_galaxy_buds, 
            "Samsung Galaxy Buds+ True Wireless Earbuds", 5);
        insertProduct(db, "Bose SoundLink", "Bose", 199.99, R.drawable.bose_soundlink, 
            "Bose SoundLink Flex Bluetooth Speaker - Portable and Waterproof", 3);
        insertProduct(db, "AirPods Pro", "Apple", 249.99, R.drawable.airpods_pro, 
            "Apple AirPods Pro 2nd Gen - Active Noise Cancellation, In-Ear", 6);

        // Accessories & Wearables
        insertProduct(db, "Fitbit Charge 5", "Fitbit", 179.95, R.drawable.fitbit_charge5, 
            "Fitbit Charge 5 Fitness & Health Tracker - Black/Gold", 4);
        insertProduct(db, "GoPro HERO10", "GoPro", 499.99, R.drawable.gopro_hero10, 
            "GoPro HERO10 Black - Waterproof Action Camera with 5.3K Video", 2);
    }

    private void insertProduct(SQLiteDatabase db, String name, String brand, double price, 
                             int imageResId, String description, int quantity) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("brand", brand);
        values.put("price", price);
        values.put("image_res_id", imageResId);
        values.put("description", description);
        values.put("quantity", quantity);
        db.insert(TABLE_NAME, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }
} 