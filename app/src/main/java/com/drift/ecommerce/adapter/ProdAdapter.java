package com.drift.ecommerce.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.drift.ecommerce.R;
import com.drift.ecommerce.model.Product;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

public class ProdAdapter extends RecyclerView.Adapter<ProdAdapter.MyViewHolder> {

    private List<Product> prodList;
    private OnClickListener onClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView prod_image;
        TextView name, desc;
        AppCompatButton appCompatButton;


        public MyViewHolder(View view) {
            super(view);
            prod_image = view.findViewById(R.id.prod_image);
            appCompatButton = view.findViewById(R.id.addCart);
            name = (TextView) view.findViewById(R.id.prodName);
            desc = (TextView) view.findViewById(R.id.desc);

        }
    }


    public ProdAdapter(List<Product> userList) {
        this.prodList = userList;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, Product model);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final Product product = prodList.get(position);

        holder.name.setText(product.getProductName());
        holder.desc.setText(String.valueOf(product.getProductPrice()));

        //Loading the image using Glide
        Context context = holder.prod_image.getContext();
        Glide.with(context).load(product.getImageUrl())
                .placeholder(R.drawable.placeholder)
                .into(holder.prod_image);

        holder.appCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onClick(position, product);
                    holder.appCompatButton.setText("Added Cart");
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return prodList.size();
    }
}
