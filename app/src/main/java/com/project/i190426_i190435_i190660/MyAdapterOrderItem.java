package com.project.i190426_i190435_i190660;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapterOrderItem extends RecyclerView.Adapter<MyAdapterOrderItem.MyViewHolder> {

    List<OrderItem> orderedItems;
    Context c;

    public MyAdapterOrderItem(List<OrderItem> order, Context c) {
        this.orderedItems = order;
        this.c = c;
    }

    @NonNull
    @Override
    public MyAdapterOrderItem.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(c).inflate(R.layout.order_item_row, parent, false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterOrderItem.MyViewHolder holder, int position) {
        holder.name.setText(orderedItems.get(position).getP().getName());
        holder.price.setText("Rs. "+String.valueOf(orderedItems.get(position).getP().getPrice()));
        holder.quantity.setText("Qty: "+String.valueOf(orderedItems.get(position).getQuantity()));

        int id=c.getResources().getIdentifier(orderedItems.get(position).getP().getImage(), "drawable", c.getPackageName());

        holder.image.setImageResource(id);
    }

    @Override
    public int getItemCount() {
        return orderedItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, quantity;
        ImageView image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            price=itemView.findViewById(R.id.price);
            quantity=itemView.findViewById(R.id.quantity);
            image=itemView.findViewById(R.id.image);

        }
    }
}
