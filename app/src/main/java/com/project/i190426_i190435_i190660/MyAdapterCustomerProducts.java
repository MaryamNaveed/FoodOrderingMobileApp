package com.project.i190426_i190435_i190660;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapterCustomerProducts extends RecyclerView.Adapter<MyAdapterCustomerProducts.MyViewHolder>{

    List<Product> allProducts;
    Context c;

    public MyAdapterCustomerProducts(List<Product> allProducts, Context c) {
        this.allProducts = allProducts;
        this.c = c;
    }

    @NonNull
    @Override
    public MyAdapterCustomerProducts.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(c).inflate(R.layout.customer_product_row, parent, false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterCustomerProducts.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(allProducts.get(position).getName());
        holder.category.setText(allProducts.get(position).getCategory());
        holder.price.setText(String.valueOf(allProducts.get(position).getPrice()));


        int id=c.getResources().getIdentifier(allProducts.get(position).getImage(), "drawable", c.getPackageName());

        holder.image.setImageResource(id);

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(c, ProductDetail.class);
                intent.putExtra("id", allProducts.get(position).getId());
                intent.putExtra("name", allProducts.get(position).getName());
                intent.putExtra("quantity", allProducts.get(position).getQuantity());
                intent.putExtra("price", allProducts.get(position).getPrice());
                intent.putExtra("image", allProducts.get(position).getImage());
                intent.putExtra("description", allProducts.get(position).getDescription());
                intent.putExtra("category", allProducts.get(position).getCategory());
                c.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allProducts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, category;
        ImageView image;
        Button add;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.name);
            price=itemView.findViewById(R.id.price);
            category=itemView.findViewById(R.id.category);
            image=itemView.findViewById(R.id.image);
            add=itemView.findViewById(R.id.add);


        }
    }
}
