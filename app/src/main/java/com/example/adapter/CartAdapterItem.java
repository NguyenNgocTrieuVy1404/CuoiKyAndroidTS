package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.modeldata.Product;
import com.example.technologyactivity.CartFragment;
import com.example.modeldata.CartItem;
import com.example.technologyactivity.R;

import java.util.ArrayList;
import java.util.List;

public class CartAdapterItem extends ArrayAdapter<CartItem> {
    private final Context context;
    private final List<CartItem> cartItems;
    private final List<CartItem> selectedItems;
    private final CartFragment cartFragment;

    public CartAdapterItem(Context context, List<CartItem> cartItems, CartFragment cartFragment) {
        super(context, R.layout.cart_item, cartItems);
        this.context = context;
        this.cartItems = cartItems;
        this.selectedItems = new ArrayList<>();
        this.cartFragment = cartFragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.cart_item, parent, false);
        }

        CartItem currentItem = cartItems.get(position);
        Product product = currentItem.getProduct();

        TextView productNameTextView = convertView.findViewById(R.id.product_name);
        TextView productQuantityTextView = convertView.findViewById(R.id.product_quantity);
        TextView productPriceTextView = convertView.findViewById(R.id.product_price);
        ImageView productImageView = convertView.findViewById(R.id.product_image);
        CheckBox itemCheckBox = convertView.findViewById(R.id.item_checkbox);
        Button btnIncrease = convertView.findViewById(R.id.btn_increase);
        Button btnDecrease = convertView.findViewById(R.id.btn_decrease);
        Button btnRemove = convertView.findViewById(R.id.btn_remove);

        productNameTextView.setText(product.getName());
        productQuantityTextView.setText("Số lượng: " + currentItem.getQuantity());
        productPriceTextView.setText("Giá: $" + String.format("%.2f", product.getPrice() * currentItem.getQuantity()));
        productImageView.setImageResource(product.getImageResId());

        itemCheckBox.setChecked(selectedItems.contains(currentItem));
        itemCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedItems.add(currentItem);
            } else {
                selectedItems.remove(currentItem);
            }
            cartFragment.updateTotalPrice();
        });

        btnIncrease.setOnClickListener(v -> {
            int availableStock = product.getQuantity();
            int currentCartQuantity = currentItem.getQuantity();

            if (currentCartQuantity < availableStock) {
                currentItem.increaseQuantity(context);
                productQuantityTextView.setText("Số lượng: " + currentItem.getQuantity());
                productPriceTextView.setText("Giá: $" + String.format("%.2f", product.getPrice() * currentItem.getQuantity()));
                cartFragment.updateTotalPrice();
            } else {
                Toast.makeText(context, "Không đủ hàng trong kho!", Toast.LENGTH_SHORT).show();
            }
        });

        btnDecrease.setOnClickListener(v -> {
            if (currentItem.getQuantity() > 0) {
                currentItem.decreaseQuantity();
                productQuantityTextView.setText("Số lượng: " + currentItem.getQuantity());
                productPriceTextView.setText("Giá: $" + String.format("%.2f", product.getPrice() * currentItem.getQuantity()));
            }
            if (currentItem.getQuantity() == 0) {
                cartItems.remove(currentItem);
                selectedItems.remove(currentItem);
            }
            notifyDataSetChanged();
            cartFragment.updateTotalPrice();
        });

        btnRemove.setOnClickListener(v -> {
            cartItems.remove(currentItem);
            selectedItems.remove(currentItem);
            notifyDataSetChanged();
            cartFragment.updateTotalPrice();
        });

        return convertView;
    }

    public List<CartItem> getSelectedItems() {
        return selectedItems;
    }
}
