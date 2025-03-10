package com.example.modeldata;

import android.content.Context;
import android.database.Cursor;

import com.example.database.DatabaseProduct;

import java.util.ArrayList;
import java.util.List;

public class ProductDataManager {

    private static ProductDataManager instance;
    private List<CartItem> cartItemList;
    private DatabaseProduct dbHelper;
    private Context context;

    private ProductDataManager(Context context) {
        this.context = context;
        this.cartItemList = new ArrayList<>();
        this.dbHelper = new DatabaseProduct(context);
        loadProducts();
    }

    public static ProductDataManager getInstance(Context context) {
        if (instance == null) {
            instance = new ProductDataManager(context);
        }
        return instance;
    }

    private void loadProducts() {
        cartItemList.clear();
        Cursor cursor = dbHelper.getAllProducts();

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String brand = cursor.getString(cursor.getColumnIndexOrThrow("brand"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                int imageResId = cursor.getInt(cursor.getColumnIndexOrThrow("image_res_id"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));

                Product product = new Product(name, brand, price, imageResId, description, quantity);
                cartItemList.add(new CartItem(product));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public List<CartItem> getProductList() {
        return new ArrayList<>(cartItemList);
    }
}
