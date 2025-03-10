package com.example.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.technologyactivity.R;

import java.util.List;

public class AdPagerAdapter extends RecyclerView.Adapter<AdPagerAdapter.AdViewHolder> {

    private List<Integer> adImages;

    public AdPagerAdapter(List<Integer> adImages) {
        this.adImages = adImages;
    }

    @NonNull
    @Override
    public AdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ad, parent, false);
        return new AdViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdViewHolder holder, int position) {
        holder.imageView.setImageResource(adImages.get(position));
    }

    @Override
    public int getItemCount() {
        return adImages.size();
    }

    static class AdViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        AdViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
        }
    }
}
