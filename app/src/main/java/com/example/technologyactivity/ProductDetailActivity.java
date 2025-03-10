package com.example.technologyactivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.modeldata.Product;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView productImageView;
    private TextView productNameTextView, productBrandTextView,productDescriptionTextView, productPriceTextView;
    private Button addToCartButton, exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        productImageView = findViewById(R.id.productImageView);
        productNameTextView = findViewById(R.id.productNameTextView);
        productBrandTextView = findViewById(R.id.productBrandTextView);
        productDescriptionTextView = findViewById(R.id.productDescription);
        productPriceTextView = findViewById(R.id.productPriceTextView);
        addToCartButton = findViewById(R.id.addToCartButton);
        exitButton = findViewById(R.id.exitButton);

        Intent intent = getIntent();
        String productName = intent.getStringExtra("product_name");
        String productBrand = intent.getStringExtra("product_brand");
        String productDescription = intent.getStringExtra("product_description");
        double productPrice = intent.getDoubleExtra("product_price", 0.0);
        int productImageResId = intent.getIntExtra("product_image_res_id", 0);

        productNameTextView.setText(productName);
        productBrandTextView.setText(productBrand);
        productDescriptionTextView.setText(productDescription);
        productPriceTextView.setText(String.format("%.2f", productPrice));
        productImageView.setImageResource(productImageResId);

        addToCartButton.setOnClickListener(v -> {
            Product product = new Product(productName, productBrand, productPrice, productImageResId, "", 1);
            CartManager.getInstance().addToCart(product, this);
        });

        exitButton.setOnClickListener(v -> {
            finish();
        });
    }
}
