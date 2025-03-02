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
import com.example.storecomputer.Model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<Product> products;
    private Context context;

    public ProductAdapter(List<Product> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);

        holder.textPrice.setText("S/"+String.valueOf(product.getPrice()));
        holder.textname.setText(product.getName());
        Glide.with(context).load(product.getImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textname, textPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageProduct);
            textPrice = itemView.findViewById(R.id.textPrice);
            textname = itemView.findViewById(R.id.textProduct);
        }
    }
}
