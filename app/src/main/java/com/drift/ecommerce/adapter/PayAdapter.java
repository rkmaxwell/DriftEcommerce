package com.drift.ecommerce.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.drift.ecommerce.MainActivity;
import com.drift.ecommerce.R;
import com.drift.ecommerce.model.Pay;
import com.drift.ecommerce.model.Product;
import com.orhanobut.hawk.Hawk;

import java.util.List;

public class PayAdapter extends RecyclerView.Adapter<PayAdapter.MyViewHolder> {

    private List<Pay> prodList;
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


    public PayAdapter(List<Pay> payList) {
        this.prodList = payList;
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
            .inflate(R.layout.product_item_checkout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final Pay product = prodList.get(position);

        holder.name.setText(product.getName());
        holder.desc.setText(String.valueOf("Click to pay"));

        holder.itemView.setOnClickListener(v -> {
            Context context=holder.desc.getContext();
            Toast.makeText(context, "Thank you", Toast.LENGTH_SHORT).show();
            Hawk.delete("cart");
            ((Activity)context).finish();
            ((Activity)context).finish();
            ((Activity)context).startActivity(new Intent(context, MainActivity.class));
        });

    }

    @Override
    public int getItemCount() {
        return prodList.size();
    }
}
