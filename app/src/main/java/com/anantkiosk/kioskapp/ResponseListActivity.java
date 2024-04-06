package com.anantkiosk.kioskapp;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anantkiosk.kioskapp.Adapter.ResponseListAdapter;
import com.anantkiosk.kioskapp.Api.ApiClient;
import com.anantkiosk.kioskapp.Api.ApiInterface;
import com.anantkiosk.kioskapp.Model.AdvRefreshCalls;
import com.anantkiosk.kioskapp.Home.HomeFragments.AdvertisementFragment;
import com.anantkiosk.kioskapp.Utils.UtilsGlobal;
import com.anantkiosk.kioskapp.databinding.ActivityResponseListBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResponseListActivity extends AppCompatActivity {

    ActivityResponseListBinding binding;
    ApiInterface apiForCall;
    ResponseListActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_response_list);
        getSupportActionBar().hide();
        apiForCall = ApiClient.getClient().create(ApiInterface.class);
        context = ResponseListActivity.this;
        callLogsListAPI(getIntent().getExtras().getString("androidID"));
        binding.btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.btnrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (UtilsGlobal.getTimeToRefresh(ResponseListActivity.this, false)) {
//                        Edited by Varun for QT
//                        AdvertisementFragment.context.fetchAdvFromBlip();
                        if (UtilsGlobal.isFromQT) {
                            AdvertisementFragment.context.Auth_QT();
                        }else{
                            AdvertisementFragment.context.callImagesWs();
                        }
//                        END
                        new Handler().postDelayed(() -> {
                            callLogsListAPI(getIntent().getExtras().getString("androidID"));
                        }, 5000);

                    } else {
                        try {
                            Double timeis = Double.parseDouble(String.valueOf(UtilsGlobal.getTimeToDisplay(ResponseListActivity.this)));
                            Toast.makeText(ResponseListActivity.this, "Refresh already requested, try after " + (timeis) + " minutes! ", Toast.LENGTH_LONG).show();
                        } catch (Exception E) {
                            Toast.makeText(ResponseListActivity.this, "Refresh already requested, try after few minutes!", Toast.LENGTH_LONG).show();
                        }
                    }

            }
        });
    }

    private void callLogsListAPI(String androidID) {
        UtilsGlobal.showProgressBar(ResponseListActivity.this);
        Call<ArrayList<AdvRefreshCalls>> call1 = apiForCall.getResponseLogs(UtilsGlobal.store.getId(), androidID);
        call1.enqueue(new Callback<ArrayList<AdvRefreshCalls>>() {
            @Override
            public void onResponse(Call<ArrayList<AdvRefreshCalls>> call, Response<ArrayList<AdvRefreshCalls>> response) {
                if (response.body() != null) {
                    if (response.body() != null) {
                        if (response.body().size() > 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ResponseListAdapter adapter = new ResponseListAdapter(context, response.body());
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                                    binding.rvItemList.setLayoutManager(layoutManager);
                                    binding.rvItemList.setAdapter(adapter);
                                    binding.rvItemList.setNestedScrollingEnabled(false);
                                    binding.rvItemList.setVisibility(View.VISIBLE);
                                    UtilsGlobal.dismissProgressBar();
                                }
                            });
                        } else {
                            UtilsGlobal.dismissProgressBar();
                        }
                    } else {
                        UtilsGlobal.dismissProgressBar();
                    }
                } else {
                    UtilsGlobal.dismissProgressBar();
                }

            }

            @Override
            public void onFailure(Call<ArrayList<AdvRefreshCalls>> call, Throwable t) {
                call.cancel();
                UtilsGlobal.dismissProgressBar();

            }
        });
    }
}