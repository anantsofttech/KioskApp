package com.anantkiosk.kioskapp.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anantkiosk.kioskapp.Model.AdvRefreshCalls;
import com.anantkiosk.kioskapp.Model.AdvSign;
import com.anantkiosk.kioskapp.R;
import com.anantkiosk.kioskapp.Utils.UtilsGlobal;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ResponseListAdapter extends RecyclerView.Adapter<ResponseListAdapter.MyViewHolder> {

    List<AdvRefreshCalls> advList;
    Context context;

    public ResponseListAdapter(Context context, List<AdvRefreshCalls> advList) {
        this.context = context;
        this.advList = advList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_response_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        //setup
        holder.tv_status.setText("Failed");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.tv_status.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.red)));
            holder.viewpassfail.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.red)));
        }
        if (advList.get(position).getErrorMessage() != null) {
            if (advList.get(position).getErrorMessage().trim().length() > 0) {
                if (advList.get(position).getErrorMessage().toLowerCase().contains("pass")) {
                    holder.tv_status.setText("Success");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.tv_status.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green)));
                        holder.viewpassfail.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green)));

                    }

                }
            }
        }
        if (advList.get(position).getResponse() != null) {
            if (advList.get(position).getResponse().trim().length() > 0) {
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<AdvSign>>() {
                    }.getType();
                    List<AdvSign> contactList = gson.fromJson(advList.get(position).getResponse(), type);
                    holder.tv_response_vals.setText(contactList.size() + " ads retrived!");
                }catch (Exception e){
                    holder.tv_response_vals.setText(Html.fromHtml("Error retriving advertisement!<br> "+advList.get(position).getResponse()+"</br>"));
                }

            } else {
                holder.tv_response_vals.setText(Html.fromHtml("Error retriving advertisement!<br> "+advList.get(position).getResponse()+"</br>"));
            }
        } else {
            holder.tv_response_vals.setText(Html.fromHtml("Error retriving advertisement!<br> "+advList.get(position).getResponse()+"</br>"));
        }
        try {
            holder.tv_time.setText(advList.get(position).getSignid() + "/ " + advList.get(position).getDeviceID());
            holder.tv_time_list_vals.setText(Html.fromHtml("<b>Requested On </b> " + advList.get(position).getCreateDate()));

        } catch (Exception e) {
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
// ...Irrelevant code for customizing the buttons and title
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alert_blipboard_details, null);
                dialogBuilder.setView(dialogView);
                AlertDialog alertDialog = dialogBuilder.create();
                Button btnclose = (Button) dialogView.findViewById(R.id.btnclose);
                TextView tv_status = dialogView.findViewById(R.id.tv_status);
                TextView tv_time = dialogView.findViewById(R.id.tv_time);
                TextView tv_time_list_vals = dialogView.findViewById(R.id.tv_time_list_vals);
                TextView tv_time_list_list = dialogView.findViewById(R.id.tv_time_list_list);
                TextView tv_response_vals = dialogView.findViewById(R.id.tv_response_vals);
                TextView tv_response_vals_all = dialogView.findViewById(R.id.tv_response_vals_all);
                tv_status.setText("Failed");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tv_status.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.red)));

                }
                if (advList.get(position).getErrorMessage() != null) {
                    if (advList.get(position).getErrorMessage().trim().length() > 0) {
                        if (advList.get(position).getErrorMessage().toLowerCase().contains("pass")) {
                            tv_status.setText("Success");
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                tv_status.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green)));

                            }

                        }
                    }
                }
                if (advList.get(position).getResponse() != null) {
                    if (advList.get(position).getResponse().trim().length() > 0) {
                        try {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<AdvSign>>() {
                            }.getType();
                            List<AdvSign> contactList = gson.fromJson(advList.get(position).getResponse(), type);
                            tv_response_vals.setText(contactList.size() + " ads retrived!");
                            String strVals = "";
                            for (int i = 0; i < contactList.size(); i++) {
                                if (strVals.trim().length() <= 0) {
                                    strVals = strVals + "<b>Advertisement ID: </b>" + contactList.get(i).getId() + "<br><b>ADV URL:</b> " + contactList.get(i).getUrl() + "</br>";
                                } else {
                                    strVals = strVals + "<br></br><br><b>Advertisement ID: </b>" + contactList.get(i).getId() + "</br><br><b>Advertisement URL:</b> " + contactList.get(i).getUrl() + "</br>";

                                }

                            }
                            tv_response_vals_all.setText(Html.fromHtml(strVals));
                        }catch (Exception e){
                            tv_response_vals.setText("Error retriving advertise!");
                            tv_response_vals_all.setText(advList.get(position).getResponse());
                        }

                    } else {
                        tv_response_vals.setText("Error retriving advertise!");
                        tv_response_vals_all.setText(advList.get(position).getResponse());
                    }
                } else {
                    tv_response_vals.setText("Error retriving advertise!");
                    tv_response_vals_all.setText(advList.get(position).getResponse());
                }
                try {
                    tv_time.setText(advList.get(position).getSignid() + "/ " + advList.get(position).getDeviceID());
                    tv_time_list_vals.setText(Html.fromHtml("<b>Requested On: </b> " + advList.get(position).getCreateDate()));
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<String>>() {
                    }.getType();
                    List<String> contactList = gson.fromJson(advList.get(position).getRequested(), type);
                    String strVals = "";
                    for (int i = 0; i < contactList.size(); i++) {
                        strVals = strVals + "<br>" + contactList.get(i) + "</br>";
                    }
                    tv_time_list_list.setText(Html.fromHtml("<b>UTC TIME REQUEST TO BLIPBOARD:</b>" + strVals));

                } catch (Exception e) {
                }
                btnclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return advList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_status, tv_time, tv_time_list_vals, tv_response_vals;
        View viewpassfail;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_status = itemView.findViewById(R.id.tv_status);
            tv_status.setTypeface(UtilsGlobal.setFontSemiBold(context));
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_time.setTypeface(UtilsGlobal.setFontRegular(context));
            tv_time_list_vals = itemView.findViewById(R.id.tv_time_list);
            tv_time_list_vals.setTypeface(UtilsGlobal.setFontRegular(context));
            tv_response_vals = itemView.findViewById(R.id.tv_response_vals);
            tv_response_vals.setTypeface(UtilsGlobal.setFontRegular(context));
            viewpassfail = itemView.findViewById(R.id.viewPass);

        }
    }

}
