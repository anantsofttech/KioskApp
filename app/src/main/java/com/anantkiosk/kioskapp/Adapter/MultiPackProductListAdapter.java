package com.anantkiosk.kioskapp.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.anantkiosk.kioskapp.Api.ApiClient;
import com.anantkiosk.kioskapp.Home.HomeFragments.ItemLookupFragment;
import com.anantkiosk.kioskapp.Model.Product;
import com.anantkiosk.kioskapp.R;
import com.anantkiosk.kioskapp.Utils.UtilsGlobal;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MultiPackProductListAdapter extends RecyclerView.Adapter<MultiPackProductListAdapter.MyViewHolder> {
    private ArrayList<Product> progress;
    private Activity activity;
    boolean isInList;
    Product mainProduct;

    public MultiPackProductListAdapter(ArrayList<Product> progress, Activity activity, boolean isInList, Product mainProduct) {
        this.progress = progress;
        this.activity = activity;
        this.isInList = isInList;
        this.mainProduct = mainProduct;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = null;
        if (isInList) {
            itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.row_item_product_options_list, viewGroup, false);
        } else {
            itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.row_item_product_options, viewGroup, false);
        }
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final Product progress1 = progress.get(position);
        try {
            String pNAME = progress1.getProduct_name().trim();
            if (progress1.getProduct_name().contains("()")) {
                pNAME = pNAME.replace("()", "");
            }
            holder.title.setText(Html.fromHtml(pNAME));
        } catch (Exception e) {
            holder.title.setText(progress1.getProduct_name());
        }
        if (progress1.getDiscountedamount() != null) {
            if (Double.parseDouble(progress1.getDiscountedamount()) > 0) {
                //check date is yet on or not
                //progress1.setPromoEnd("2021-11-04T03:00:00");
                Date promoStart = null, promoEnd = null;
                if (progress1.getPromoStart() != null) {
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        promoStart = df.parse(progress1.getPromoStart());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (progress1.getPromoEnd() != null) {
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        promoEnd = df.parse(progress1.getPromoEnd());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                Date today = new Date();
                if (promoStart != null && promoEnd != null) {
                    if (promoStart.compareTo(today) == 1 || promoEnd.compareTo(today) == 1 || (today.after(promoStart) && today.before(promoEnd))) {
                        holder.txtdiscountedprice.setVisibility(View.VISIBLE);
                        holder.txtdiscountedprice.setText("$" + progress1.getPrice());
                        holder.txtdiscountedprice.setPaintFlags(holder.txtdiscountedprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        holder.txtprice.setTextColor(activity.getResources().getColor(R.color.green));
                        holder.txtprice.setText("$" + progress1.getDiscountedamount());
                    } else {
                        holder.txtdiscountedprice.setVisibility(View.GONE);
                        holder.txtprice.setText("$" + progress1.getPrice());
                        holder.txtprice.setTextColor(activity.getResources().getColor(R.color.clr_itemlook));
                    }
                } else {
                    holder.txtdiscountedprice.setVisibility(View.GONE);
                    holder.txtprice.setText("$" + progress1.getPrice());
                    holder.txtprice.setTextColor(activity.getResources().getColor(R.color.clr_itemlook));
                }

            } else {
                holder.txtdiscountedprice.setVisibility(View.GONE);
                holder.txtprice.setText("$" + progress1.getPrice());
                holder.txtprice.setTextColor(activity.getResources().getColor(R.color.clr_itemlook));
            }
        } else {
            holder.txtdiscountedprice.setVisibility(View.GONE);
            holder.txtprice.setText("$" + progress1.getPrice());
            holder.txtprice.setTextColor(activity.getResources().getColor(R.color.clr_itemlook));
        }
        holder.tvunit.setVisibility(View.GONE);
        if (progress1.getSizeFlag() != null) {
            if (progress1.getSizeFlag().equalsIgnoreCase("y")) {
                if (progress1.getProduct_unit() != null) {
                    if (progress1.getProduct_unit().trim().length() > 0) {
                        //holder.title.setText(Html.fromHtml(progress1.getProduct_name() + " <font color='#6F7072'>(" + progress1.getProduct_unit().trim() + ")</font>"));
                        if (!progress1.getProduct_name().trim().contains(progress1.getProduct_unit())) {
                            holder.tvunit.setVisibility(View.VISIBLE);
                            holder.tvunit.setText("(" + progress1.getProduct_unit().trim() + ")");
                        }

                    }
                }
            }
        }
        holder.imgproduct.setVisibility(View.GONE);

//        Edited by Varun for Centralized Image

//        if (progress1.getInvSmallImage() != null) {
//            if (progress1.getInvSmallImage().trim().length() > 0) {
//                Glide.with(activity)
//                        .asBitmap()
//                        .load(ApiClient.IMG_BASE + UtilsGlobal.IMAGEURL + UtilsGlobal.store.getId() + "/" + progress1.getInvSmallImage())
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .error(R.drawable.no_image)
//                        .into(new BitmapImageViewTarget(holder.imgproduct) {
//                            @Override
//                            protected void setResource(Bitmap resource) {
//                                RoundedBitmapDrawable circularBitmapDrawable =
//                                        RoundedBitmapDrawableFactory.create(activity.getResources(), resource);
//                                circularBitmapDrawable.setCornerRadius(16);
//                                holder.imgproduct.setImageDrawable(circularBitmapDrawable);
//                                holder.imgproduct.setVisibility(View.VISIBLE);
//                                holder.title.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                            }
//                        });
//            }
//        }
        if (progress1.getInvSmallImageFullPath() != null) {
            if (progress1.getInvSmallImageFullPath().trim().length() > 0) {
                Glide.with(activity)
                        .asBitmap()
                        .load( progress1.getInvSmallImageFullPath())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.no_image)
                        .into(new BitmapImageViewTarget(holder.imgproduct) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(activity.getResources(), resource);
                                circularBitmapDrawable.setCornerRadius(16);
                                holder.imgproduct.setImageDrawable(circularBitmapDrawable);
                                holder.imgproduct.setVisibility(View.VISIBLE);
                                holder.title.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            }
                        });
            }
        }

//        END
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInList) {
                    UtilsGlobal.hideKeyboard(activity);
                    ItemLookupFragment.activity.showDialog(activity, mainProduct, false);
                }
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

    public void updateList(ArrayList<Product> temp) {
        progress = temp;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, tvunit, txtdiscountedprice, txtprice;
        ImageView imgproduct;

        public MyViewHolder(@NonNull View view) {
            super(view);
            title = view.findViewById(R.id.tv_product_name);
            tvunit = view.findViewById(R.id.tvunit1);
            txtdiscountedprice = view.findViewById(R.id.txtdiscountedprice);
            txtprice = view.findViewById(R.id.txtprice);
            imgproduct = view.findViewById(R.id.imgproduct);
            try {
                if(!isInList)
                    title.setTypeface(UtilsGlobal.setFontSemiBold(activity));
                else
                    title.setTypeface(UtilsGlobal.setFontRegular(activity));
                tvunit.setTypeface(UtilsGlobal.setFontRegular(activity));
                txtdiscountedprice.setTypeface(UtilsGlobal.setFontSemiBold(activity));
                txtprice.setTypeface(UtilsGlobal.setFontSemiBold(activity));
            } catch (Exception e) {
            }
        }
    }

}
