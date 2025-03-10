package com.example.technologyactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;  // Dùng ImageView để hiển thị hình ảnh động
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.adapter.ProductAdapter;
import com.example.modeldata.CartItem;
import com.example.modeldata.Product;
import com.example.modeldata.ProductDataManager;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements ProductAdapter.OnProductClickListener {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<CartItem> cartItemList;
    private List<CartItem> filteredCartItemList;
    private CartManager cartManager;
    private ImageView noProductsGif;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        initializeViews(view);
        setupRecyclerView(view);
        loadProducts();
        setupSearchView(view);
        cartManager = CartManager.getInstance();

        return view;
    }

    private void initializeViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        noProductsGif = view.findViewById(R.id.no_products_gif);
    }

    private void setupRecyclerView(View view) {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        cartItemList = ProductDataManager.getInstance(getContext()).getProductList();
        filteredCartItemList = new ArrayList<>(cartItemList);
        productAdapter = new ProductAdapter(getContext(), filteredCartItemList, this);
        recyclerView.setAdapter(productAdapter);
    }

    private void loadProducts() {
        filteredCartItemList.addAll(cartItemList);
        productAdapter.notifyDataSetChanged();
        updateNoProductsGifVisibility();
    }

    private void setupSearchView(View view) {
        SearchView searchView = view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterProducts(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterProducts(newText);
                return true;
            }
        });
    }

    private void filterProducts(String query) {
        filteredCartItemList.clear();
        for (CartItem item : cartItemList) {
            if (item.getProduct().getName().toLowerCase().contains(query.toLowerCase())) {
                filteredCartItemList.add(item);
            }
        }
        productAdapter.notifyDataSetChanged();
        updateNoProductsGifVisibility();
    }

    private void updateNoProductsGifVisibility() {
        if (filteredCartItemList.isEmpty()) {
            noProductsGif.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(R.drawable.seatchgif)
                    .into(noProductsGif);
        } else {
            noProductsGif.setVisibility(View.GONE);
        }
    }

    @Override
    public void onProductClick(@NonNull CartItem cartItem) {
        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
        Product product = cartItem.getProduct();
        intent.putExtra("product_name", product.getName());
        intent.putExtra("product_brand", product.getBrand());
        intent.putExtra("product_description", product.getDescription());
        intent.putExtra("product_price", product.getPrice());
        intent.putExtra("product_image_res_id", product.getImageResId());
        startActivity(intent);
    }
}
