package com.anantkiosk.kioskapp.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Paint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anantkiosk.kioskapp.Api.ApiClient;
import com.anantkiosk.kioskapp.Api.ApiInterface;
import com.anantkiosk.kioskapp.Model.ProductResponseMain;
import com.anantkiosk.kioskapp.Model.Product;
import com.anantkiosk.kioskapp.R;
import com.anantkiosk.kioskapp.Home.HomeFragments.ItemLookupFragment;
import com.anantkiosk.kioskapp.Utils.UtilsGlobal;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder> {
    private ArrayList<Product> progress;
    private Activity activity;

    boolean isRelated;

    public ProductListAdapter(ArrayList<Product> progress, Activity activity, boolean isRelated) {
        this.progress = progress;
        this.activity = activity;
        this.isRelated = isRelated;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView;
        if (isRelated) {
            itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.row_item_related_product, viewGroup, false);
        } else {
            itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.row_item_product_list, viewGroup, false);
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
        holder.txtdiscountedprice.setVisibility(View.GONE);
        holder.txtprice.setText("$" + progress1.getPrice());
        holder.txtprice.setTextColor(activity.getResources().getColor(R.color.clr_itemlook));
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
                    }
                }

            }
        }
        if (!isRelated)
            holder.tvdateupto.setVisibility(View.GONE);
        else
            holder.tvdateupto.setVisibility(View.INVISIBLE);
        holder.tvexpectedate.setVisibility(View.GONE);
        if (progress1.getQuantityonHand() != null) {
            if (progress1.getQuantityonHand().trim().length() > 0) {
                if (Double.parseDouble(progress1.getQuantityonHand().trim()) <= 0) {
                    holder.tvdateupto.setText(Html.fromHtml("Out Of Stock!"));
                    holder.tvdateupto.setVisibility(View.VISIBLE);
                    if (ItemLookupFragment.activity.needToDisplayDate) {
                        if (progress1.getExpectedDate() != null) {
                            if (progress1.getExpectedDate().trim().length() > 0) {
                                try {
                                    holder.tvexpectedate.setVisibility(View.VISIBLE);
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                                    if (UtilsGlobal.isToday(format.parse(progress1.getExpectedDate()))) {
                                        holder.tvexpectedate.setText("Available Today!");
                                    } else if (UtilsGlobal.isTomorrow(format.parse(progress1.getExpectedDate()))) {
                                        holder.tvexpectedate.setText("Available Tomorrow!");
                                    } else {
                                        Date formatdate = format.parse(progress1.getExpectedDate());
                                        //YYYY
                                        if (formatdate.after(new Date())) {
                                            SimpleDateFormat format2 = new SimpleDateFormat("yyyy", Locale.getDefault());
                                            String strCurYear = format2.format(formatdate);
                                            String strNow = format2.format(new Date());
                                            SimpleDateFormat format1;
                                            if (strCurYear.equalsIgnoreCase(strNow)) {
                                                format1 = new SimpleDateFormat("EEEE, MMM dd", Locale.getDefault());
                                            } else {
                                                format1 = new SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.getDefault());
                                            }
                                            String dateIs = format1.format(formatdate);
                                            holder.tvexpectedate.setText("Available On " + dateIs);
                                        } else {
                                            holder.tvexpectedate.setText("Available Soon!");
                                        }
                                    }
                                } catch (Exception e) {
                                    holder.tvexpectedate.setVisibility(View.VISIBLE);
                                    holder.tvexpectedate.setText("" + progress1.getExpectedDate());
                                    holder.tvexpectedate.setVisibility(View.VISIBLE);
                                }

                            }
                        }
                    }
                }
            }
        }


//        Edited by Varun for Centralized Image
//        if (progress1.getInvSmallImage() != null) {
//            if (progress1.getInvSmallImage().trim().length() > 0) {
//                Glide.with(activity)
//                        .load(ApiClient.IMG_BASE + UtilsGlobal.IMAGEURL + UtilsGlobal.store.getId() + "/" + progress1.getInvSmallImage())
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .error(R.drawable.no_image)
//                        .into(holder.ic_product);
//            } else {
//                holder.ic_product.setImageResource(R.drawable.no_image);
//            }
//        } else {
//            holder.ic_product.setImageResource(R.drawable.no_image);
//        }
        if (progress1.getInvSmallImageFullPath() != null) {
            if (progress1.getInvSmallImageFullPath().trim().length() > 0) {
                Glide.with(activity)
                        .load( progress1.getInvSmallImageFullPath())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.no_image)
                        .into(holder.ic_product);
            } else {
                holder.ic_product.setImageResource(R.drawable.no_image);
            }
        } else {
            holder.ic_product.setImageResource(R.drawable.no_image);
        }

//        END
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
        holder.rvMultiOptions.setVisibility(View.GONE);
        if (progress1.getMultipackArray() != null) {
            if (progress1.getMultipackArray().trim().length() > 0) {
                ArrayList<Product> arrMultiOption = new ArrayList<>();
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(progress1.getMultipackArray());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject explrObject = jsonArray.getJSONObject(i);
                        Product productVals = new Product();
                        productVals.setProduct_name(explrObject.getString("Description"));
                        productVals.setPrice(explrObject.getString("Price"));
                        try {
                            productVals.setDiscountedamount(explrObject.getString("Promotion_Price"));
                            productVals.setPromoStart(explrObject.getString("promotionstart_date"));
                            productVals.setPromoEnd(explrObject.getString("promotionend_date"));
                            productVals.setInvSmallImage(explrObject.getString("InvSmallImage"));
//                            Edited by Varun for Centralized Image
                            productVals.setInvSmallImageFullPath(explrObject.getString("InvSmallImageFullPath"));
                            productVals.setInvLargeImageFullPath(explrObject.getString("InvLargeImageFullPath"));
//                            END
                        }catch (Exception e){

                        }
                        arrMultiOption.add(productVals);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //DISPLAY
                if (arrMultiOption.size() > 0) {
                    MultiPackProductListAdapter relatedAdapter = new MultiPackProductListAdapter(arrMultiOption, activity,true,progress.get(position));
                    LinearLayoutManager layoutManager = new LinearLayoutManager(activity, RecyclerView.VERTICAL, false);
                    holder.rvMultiOptions.setLayoutManager(layoutManager);
                    holder.rvMultiOptions.setAdapter(relatedAdapter);
                    holder.rvMultiOptions.setNestedScrollingEnabled(false);
                    holder.rvMultiOptions.setVisibility(View.VISIBLE);
                    try {
                        if (Double.parseDouble(progress1.getPrice()) <= 0) {
                            holder.txtprice.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        holder.txtprice.setVisibility(View.GONE);
                    }
                }

            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRelated) {
                    //fetch detail and display
                    ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                    Call<ArrayList<ProductResponseMain>> call1 = apiService.GetPosInventory(UtilsGlobal.store.getId(), "I", "1", "1", progress.get(position).getSKU(), "true", "false", "desc1", "asc");
                    call1.enqueue(new Callback<ArrayList<ProductResponseMain>>() {
                        @Override
                        public void onResponse(Call<ArrayList<ProductResponseMain>> call, Response<ArrayList<ProductResponseMain>> response) {
                            try {
                                ItemLookupFragment.activity.showDialog(activity, response.body().get(0).getUserInfo().get(0), isRelated);
                            } catch (Exception e) {
                                ItemLookupFragment.activity.showDialog(activity, progress.get(position), isRelated);
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<ProductResponseMain>> call, Throwable t) {
                            call.cancel();

                        }
                    });
                } else {
                    UtilsGlobal.hideKeyboard(activity);
                    ItemLookupFragment.activity.showDialog(activity, progress.get(position), isRelated);
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
        public TextView title, tvunit, txtdiscountedprice, txtprice, tvdateupto, tvexpectedate;
        ImageView ic_product;
        RecyclerView rvMultiOptions;

        public MyViewHolder(@NonNull View view) {
            super(view);
            title = view.findViewById(R.id.tv_product_name);
            title.setTypeface(UtilsGlobal.setFontSemiBold(activity));
            ic_product = view.findViewById(R.id.ic_product);
            tvdateupto = view.findViewById(R.id.tvdateupto);
            tvdateupto.setTypeface(UtilsGlobal.setFontRegular(activity));
            tvunit = view.findViewById(R.id.tvunit1);
            tvunit.setTypeface(UtilsGlobal.setFontRegular(activity));
            txtdiscountedprice = view.findViewById(R.id.txtdiscountedprice);
            txtdiscountedprice.setTypeface(UtilsGlobal.setFontSemiBold(activity));
            txtprice = view.findViewById(R.id.txtprice);
            txtprice.setTypeface(UtilsGlobal.setFontSemiBold(activity));
            tvexpectedate = view.findViewById(R.id.tvexpectedate);
            tvexpectedate.setTypeface(UtilsGlobal.setFontRegular(activity));
            rvMultiOptions = view.findViewById(R.id.rvmultiitem);
        }
    }

}
