package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modeldata.CartItem;
import com.example.technologyactivity.CartManager;
import com.example.modeldata.Product;
import com.example.technologyactivity.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<CartItem> cartItemList;
    private OnProductClickListener listener;
    private Context context;

    public ProductAdapter(Context context, List<CartItem> cartItemList, OnProductClickListener listener) {
        this.context = context;
        this.cartItemList = cartItemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);
        if (cartItem != null) {
            Product product = cartItem.getProduct();
            if (product != null) {
                holder.productNameTextView.setText(product.getName());
                holder.productBrandTextView.setText(product.getBrand());
                holder.productPriceTextView.setText(String.valueOf(product.getPrice()));
                holder.productImageView.setImageResource(product.getImageResId());

                holder.productImageView.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onProductClick(cartItem);
                    }
                });

                holder.btnAdd.setOnClickListener(v -> {
                    CartManager.getInstance().addToCart(product, context);
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public interface OnProductClickListener {
        void onProductClick(@NonNull CartItem cartItem);
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productNameTextView, productBrandTextView,productDescriptionTV, productPriceTextView;
        ImageView productImageView;
        Button btnAdd;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.product_name);
            productBrandTextView = itemView.findViewById(R.id.product_brand);
            productDescriptionTV = itemView.findViewById(R.id.productDescription);
            productPriceTextView = itemView.findViewById(R.id.product_price);
            productImageView = itemView.findViewById(R.id.product_image);
            btnAdd = itemView.findViewById(R.id.btn_add);
        }
    }
}
