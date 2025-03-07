package com.example.storecomputer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

        holder.tvId.setText(String.valueOf(product.getId()));
        holder.textPrice.setText("S/"+String.valueOf(product.getPrice()));
        holder.textname.setText(product.getName());
        Glide.with(context).load(product.getImage()).into(holder.imageView);

        holder.btnProduc.setOnClickListener(view -> {
            FragmentProduct fragment = FragmentProduct.newInstance(product.getId());
            if (context instanceof HomePage) {  // Verifica que el contexto sea HomePage
                HomePage homePage = (HomePage) context;
                homePage.updateFragments(new Header(), fragment); // Actualiza el header y contenido
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textname, textPrice, tvId;
        Button btnProduc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnProduc = itemView.findViewById(R.id.btnProduct);
            tvId = itemView.findViewById(R.id.textProductId);
            imageView = itemView.findViewById(R.id.imageProduct);
            textPrice = itemView.findViewById(R.id.textPrice);
            textname = itemView.findViewById(R.id.textProduct);
        }
    }
}
