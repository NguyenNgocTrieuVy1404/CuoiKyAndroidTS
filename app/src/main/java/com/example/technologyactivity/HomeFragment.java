package com.example.technologyactivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.adapter.AdPagerAdapter;
import com.example.adapter.ProductAdapter;
import com.example.modeldata.CartItem;
import com.example.modeldata.Product;
import com.example.modeldata.ProductDataManager;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements ProductAdapter.OnProductClickListener {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<CartItem> cartItemList;
    private ViewPager2 viewPager;
    private Handler handler = new Handler();
    private Runnable runnable;
    private int currentPage = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        viewPager = view.findViewById(R.id.viewPager);
        recyclerView = view.findViewById(R.id.recycler_view);

        setupViewPager();
        setupRecyclerView(view);
        loadProducts();

        runnable = new Runnable() {
            @Override
            public void run() {
                if (currentPage == viewPager.getAdapter().getItemCount()) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
                handler.postDelayed(this, 3000);
            }
        };
        handler.postDelayed(runnable, 2000);

        return view;
    }

    private void setupViewPager() {
        List<Integer> adImages = new ArrayList<>();
        adImages.add(R.drawable.adveriphone);
        adImages.add(R.drawable.adverasus);
        adImages.add(R.drawable.adveropple);
        adImages.add(R.drawable.adveriphone16);
        adImages.add(R.drawable.adverlaptop);

        AdPagerAdapter adPagerAdapter = new AdPagerAdapter(adImages);
        viewPager.setAdapter(adPagerAdapter);
    }

    private void setupRecyclerView(View view) {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        cartItemList = ProductDataManager.getInstance(getContext()).getProductList();
        productAdapter = new ProductAdapter(getContext(), cartItemList, this);
        recyclerView.setAdapter(productAdapter);
    }

    private void loadProducts() {
        productAdapter.notifyDataSetChanged();
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

