package com.example.storecomputer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.storecomputer.Model.Promotion;

import java.util.List;

public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.ViewHolder> {

    private List<Promotion> promotions;
    private Context context;

    public PromotionAdapter(List<Promotion> promotions, Context context) {
        this.promotions = promotions;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_promo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Promotion promotion = promotions.get(position);

        holder.tvTitle.setText(promotion.getTitle());
        holder.tvDescription.setText(promotion.getDescription());
        Glide.with(context).load(promotion.getImageUrl()).into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return promotions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvTitle, tvDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivPromotionImage);
            tvTitle = itemView.findViewById(R.id.tvPromotionTitle);
            tvDescription = itemView.findViewById(R.id.tvPromotionDescription);
        }
    }
}

