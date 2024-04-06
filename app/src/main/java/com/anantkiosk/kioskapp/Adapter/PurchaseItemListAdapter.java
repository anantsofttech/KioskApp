package com.anantkiosk.kioskapp.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.anantkiosk.kioskapp.Model.Product;
import com.anantkiosk.kioskapp.Model.Purchase;
import com.anantkiosk.kioskapp.R;
import com.anantkiosk.kioskapp.Utils.UtilsGlobal;

import java.util.ArrayList;

public class PurchaseItemListAdapter extends RecyclerView.Adapter<PurchaseItemListAdapter.MyViewHolder> {
    private ArrayList<Product> progress;
    int type;
    Activity activity;
    public PurchaseItemListAdapter(ArrayList<Product> progress, int type,Activity activity) {
        this.progress = progress;
        this.type=type;
        this.activity=activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView;
        itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_item_product, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //holder.title.setText(progress.get(position).getProduct_name());

        if(type==0){
            holder.img.setVisibility(View.VISIBLE);
            holder.img1.setVisibility(View.GONE);
        }else{
            holder.img.setVisibility(View.GONE);
            holder.img1.setVisibility(View.VISIBLE);
        }
        holder.tvQty.setText(progress.get(position).getQty());
        holder.title.setText(Html.fromHtml(progress.get(position).getItemName()));
        if(progress.get(position).getProduct_unit()!=null) {
            if(progress.get(position).getProduct_unit().trim().length()>0) {
                if(!progress.get(position).getProduct_unit().trim().equalsIgnoreCase("N/A")) {

                    holder.title.setText(Html.fromHtml(progress.get(position).getItemName())+" ("+progress.get(position).getProduct_unit().replace(" ","").replace(" ","")+")");
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if(progress == null)
            return 0;
        return progress.size();
    }

    public void updateList(ArrayList<Product> temp) {
        progress = temp;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,tvQty;
        ImageView img,img1;

        public MyViewHolder(@NonNull View view) {
            super(view);
            title = view.findViewById(R.id.tv_product_name);
            title.setTypeface(UtilsGlobal.setFontRegular(activity));
            tvQty=view.findViewById(R.id.tvQty);
            tvQty.setTypeface(UtilsGlobal.setFontSemiBold(activity));
            img=view.findViewById(R.id.img);
            img1=view.findViewById(R.id.img1);
        }
    }
}
