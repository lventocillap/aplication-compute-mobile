package com.example.storecomputer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.storecomputer.Model.Product;
import com.example.storecomputer.Model.Promotion;

import java.sql.SQLOutput;
import java.util.List;

public class ListWishAdapter extends RecyclerView.Adapter<ListWishAdapter.ViewHolder> {

    private List<Product> products;
    private Context context;

    public ListWishAdapter(List<Product> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_wish, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);
        System.out.println(product.getName());
        holder.textPrice.setText("S/" + product.getPrice());
        holder.textname.setText(product.getName());
        holder.textAmount.setText("Cantidad seleccionada: "+ String.valueOf(product.getAmount()));
        Glide.with(context).load(product.getImage()).into(holder.imageView);
        //Glide.with(context).load(product.getImage()).into(holder.imageView);

//        holder.btnProduc.setOnClickListener(view -> {
//            FragmentProduct fragment = FragmentProduct.newInstance(product.getId());
//
//            if (context instanceof HomePage) {  // Verifica que el contexto sea HomePage
//                HomePage homePage = (HomePage) context;
//                homePage.updateFragments(new Header(), fragment); // Actualiza el header y contenido
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textname, textPrice, textAmount, textId;
        ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textId = itemView.findViewById(R.id.txtProductId);
            btnDelete = itemView.findViewById(R.id.btnDeleteProduct);
            imageView = itemView.findViewById(R.id.imgProductListWish);
            textPrice = itemView.findViewById(R.id.txtPrice);
            textname = itemView.findViewById(R.id.txtName);
            textAmount = itemView.findViewById(R.id.txtAmount);
        }
    }
}
