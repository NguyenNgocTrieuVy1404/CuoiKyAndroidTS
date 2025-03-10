package com.example.modeldata;

import android.content.Context;
import android.widget.Toast;

public class CartItem {
    private final Product product;
    private int quantity;

    public CartItem(Product product) {
        this.product = product;
        this.quantity = 1;
    }

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void increaseQuantity(Context context) {
        this.quantity++;
    }

    public void decreaseQuantity() {
        if (quantity > 0) {
            this.quantity--;
        }
    }
}
