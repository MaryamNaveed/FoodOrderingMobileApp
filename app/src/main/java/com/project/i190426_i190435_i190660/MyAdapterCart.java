package com.project.i190426_i190435_i190660;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapterCart extends RecyclerView.Adapter<MyAdapterCart.MyViewHolder>{
    List<CartItems> list;
    Context c;

    public MyAdapterCart(List<CartItems> list, Context c) {
        this.list = list;
        this.c = c;
    }

    @NonNull
    @Override
    public MyAdapterCart.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(c).inflate(R.layout.cart_item_row, parent, false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterCart.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(list.get(position).getP().getName());
        holder.price.setText("Rs. "+String.valueOf(list.get(position).getP().getPrice()));
        holder.quantity.setText(String.valueOf(list.get(position).getQuantity()));
        holder.totalPrice.setText("Rs. "+String.valueOf(list.get(position).getP().getPrice()*list.get(position).getQuantity()));

        int id=c.getResources().getIdentifier(list.get(position).getP().getImage(), "drawable", c.getPackageName());

        holder.image.setImageResource(id);

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int q=Integer.parseInt(holder.quantity.getText().toString());
                q=q+1;
                list.get(position).setQuantity(q);
                notifyDataSetChanged();
                if (c instanceof Cart) {
                    ((Cart)c).calculatePrice();
                }


            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int q=Integer.parseInt(holder.quantity.getText().toString());
                if(q>1){
                    q=q-1;
                    list.get(position).setQuantity(q);
                    notifyDataSetChanged();
                    if (c instanceof Cart) {
                        ((Cart)c).calculatePrice();
                    }

                }

            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.remove(position);
                notifyDataSetChanged();
                if (c instanceof Cart) {
                    ((Cart)c).calculatePrice();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, totalPrice, quantity;
        ImageView image;
        ImageButton plus, minus, delete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.name);
            price=itemView.findViewById(R.id.price);
            totalPrice=itemView.findViewById(R.id.totalPrice);
            quantity=itemView.findViewById(R.id.quantity);
            image=itemView.findViewById(R.id.image);
            plus=itemView.findViewById(R.id.plus);
            minus=itemView.findViewById(R.id.minus);
            delete=itemView.findViewById(R.id.delete);
        }
    }
}
