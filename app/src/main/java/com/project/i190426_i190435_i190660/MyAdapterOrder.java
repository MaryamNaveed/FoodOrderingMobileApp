package com.project.i190426_i190435_i190660;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapterOrder extends RecyclerView.Adapter<MyAdapterOrder.MyViewHolder>{

    List<Order> orders;
    Context c;

    public MyAdapterOrder(List<Order> orders, Context c) {
        this.orders = orders;
        this.c = c;
    }

    @NonNull
    @Override
    public MyAdapterOrder.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(c).inflate(R.layout.order_row, parent, false);
        return new MyAdapterOrder.MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterOrder.MyViewHolder holder, int position) {
        holder.date.setText(orders.get(position).getDate());
        double total=0;
        for(int i=0; i<orders.get(position).getOrderItemList().size(); i++){
            total+=orders.get(position).getOrderItemList().get(i).getQuantity()*orders.get(position).getOrderItemList().get(i).getP().getPrice();
        }

        total+=orders.get(position).getTax();

        holder.total.setText("Rs. "+String.valueOf(total));

        MyAdapterOrderItem adapterOrderItem;
        List<OrderItem> orderedItems=new ArrayList<>();

        for(int i=0; i<orders.get(position).getOrderItemList().size(); i++){
           orderedItems.add(orders.get(position).getOrderItemList().get(i));
        }

        adapterOrderItem=new MyAdapterOrderItem(orderedItems, c);
        holder.rv.setAdapter(adapterOrderItem);
        RecyclerView.LayoutManager lm=new LinearLayoutManager(c);
        holder.rv.setLayoutManager(lm);

        holder.reorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(c, OrderedItems.class);
                intent.putExtra("order", orders.get(position));
                c.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView total, date;
        RecyclerView rv;
        Button reorder;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            total=itemView.findViewById(R.id.total);
            rv=itemView.findViewById(R.id.rv);
            date=itemView.findViewById(R.id.date);
            reorder=itemView.findViewById(R.id.reorder);

        }
    }
}
