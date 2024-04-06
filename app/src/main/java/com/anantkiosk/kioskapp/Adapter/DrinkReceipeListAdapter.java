package com.anantkiosk.kioskapp.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anantkiosk.kioskapp.Model.Drinks;
import com.anantkiosk.kioskapp.R;
import com.anantkiosk.kioskapp.Home.HomeFragments.DrinkReceipesFragment;
import com.anantkiosk.kioskapp.Utils.UtilsGlobal;

import java.util.ArrayList;

public class DrinkReceipeListAdapter extends RecyclerView.Adapter<DrinkReceipeListAdapter.MyViewHolder> {
    private ArrayList<Drinks> progress;
    private Activity activity;
    boolean selected = false;
    int type;

    public DrinkReceipeListAdapter(ArrayList<Drinks> progress, Activity activity, int type) {
        this.progress = progress;
        this.activity = activity;
        this.type = type;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView;
        itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_item_drinks_list, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final Drinks progress1 = progress.get(position);
        holder.title.setText(progress1.getTitle());
        holder.llmain.setVisibility(View.VISIBLE);
        if (selected) {
            holder.title.setTypeface(UtilsGlobal.setFontSemiBold(activity));

        } else {
            if(position==DrinkReceipesFragment.activity.selectedpos){
                holder.title.setTypeface(UtilsGlobal.setFontSemiBold(activity));

            }else {
                holder.title.setTypeface(UtilsGlobal.setFontRegular(activity));

            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected = true;
                DrinkReceipesFragment.activity.selectedpos = position;
                ArrayList<Drinks> tempprogress = new ArrayList<>();
                tempprogress.add(progress.get(position));
                updateList(tempprogress,true);
                UtilsGlobal.hideKeyboard(activity);
                DrinkReceipesFragment.activity.showContent(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (progress == null)
            return 0;
        return progress.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void updateList(ArrayList<Drinks> temp,boolean isSelected) {
        this.selected=isSelected;
        progress = temp;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        LinearLayout llmain;

        public MyViewHolder(@NonNull View view) {
            super(view);
            title = view.findViewById(R.id.tv_product_name);
            llmain = view.findViewById(R.id.llmain);

            title.setTypeface(UtilsGlobal.setFontSemiBold(activity));
        }
    }

}
