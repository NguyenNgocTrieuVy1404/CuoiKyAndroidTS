package com.example.technologyactivity;

import android.content.Context;
import android.widget.Toast;

import com.example.modeldata.CartItem;
import com.example.modeldata.Product;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<CartItem> cartItems;

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addToCart(Product product, Context context) {
        Product productInCart = new Product(product.getName(), product.getBrand(), product.getPrice(),
                product.getImageResId(), product.getDescription(), product.getQuantity());

        for (CartItem item : cartItems) {
            if (item.getProduct().getName().equals(product.getName())) {
                if (item.getQuantity() < product.getQuantity()) {
                    item.increaseQuantity(context);
                    Toast.makeText(context, "Sản phẩm đã được thêm!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Không thể thêm sản phẩm. Sản phẩm hết hàng!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }

        cartItems.add(new CartItem(productInCart, 1));
        Toast.makeText(context, "Sản phẩm đã được thêm vào giỏ hàng.", Toast.LENGTH_SHORT).show();
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public double getTotalPrice() {
        double totalPrice = 0;
        for (CartItem cartItem : cartItems) {
            totalPrice += cartItem.getProduct().getPrice() * cartItem.getQuantity();
        }
        return totalPrice;
    }

    public void removeItems(List<CartItem> itemsToRemove) {
        cartItems.removeAll(itemsToRemove);
    }
}
