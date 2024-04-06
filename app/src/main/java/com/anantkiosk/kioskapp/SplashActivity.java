package com.anantkiosk.kioskapp;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.anantkiosk.kioskapp.Api.ApiClient;
import com.anantkiosk.kioskapp.Api.ApiClientBillBoard;
import com.anantkiosk.kioskapp.Api.ApiInterface;
import com.anantkiosk.kioskapp.Eventbus.GlobalBus;
import com.anantkiosk.kioskapp.Eventbus.InternetCheck;
import com.anantkiosk.kioskapp.Model.Result;
import com.anantkiosk.kioskapp.Model.Sign;
import com.anantkiosk.kioskapp.Model.Store;
import com.anantkiosk.kioskapp.NetworkReceiver.NetworkReceiver;
import com.anantkiosk.kioskapp.Utils.UtilsGlobal;
import com.anantkiosk.kioskapp.databinding.ActivitySplashBinding;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    ActivitySplashBinding binding;
    ApiInterface apiServiceBillboard;
    SplashActivity context;
    int SPLASH_TIME = 2500;
    Store savedStore = null;
    int retryCount = 0;
    boolean isApiCalled=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        getSupportActionBar().hide();
        apiServiceBillboard = ApiClientBillBoard.getClient().create(ApiInterface.class);
        context = SplashActivity.this;
        savedStore = UtilsGlobal.getStoreList(SplashActivity.this);

        try {
            GlobalBus.getBus().register(this);
        } catch (Exception e) {
        }
        if (UtilsGlobal.isNetworkAvailable(SplashActivity.this)) {
            // funForWorkingInternet();
            Log.d("TEST", "CHECK INTERNET");
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        try{

        }catch (Exception e){
            unregisterReceiver(new NetworkReceiver());
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new NetworkReceiver(), intentFilter);
    }

    private void funForWorkingPingInternet() {
        {
            //popup update
            binding.errorpopup.ivprogress.setVisibility(View.VISIBLE);
            ScheduledExecutorService exec3 = Executors.newSingleThreadScheduledExecutor();
            exec3.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    // do stuff
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!UtilsGlobal.isNetworkAvailable(SplashActivity.this)) {
                                retryCount++;
                                if (retryCount == 1) {
                                    binding.llpopup.setVisibility(View.VISIBLE);
                                    binding.errorpopup.txtmessage.setText("There appears to be an internet connection issue as we are not able to get a response from the Lightning or Google servers");
                                    binding.errorpopup.btnnext.setVisibility(View.GONE);
                                    binding.errorpopup.btnnext.setText("Close App");
                                    binding.errorpopup.btnnext.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            finish();
                                        }
                                    });
                                } else if (retryCount == 2) {
                                    binding.llpopup.setVisibility(View.VISIBLE);
                                    binding.errorpopup.txtmessage.setText("Still working on obtaining a connection, please stand by as we try to connect to other servers to ensure an internet connection is available.");
                                    binding.errorpopup.btnnext.setText("Close App");
                                    binding.errorpopup.btnnext.setVisibility(View.GONE);
                                    binding.errorpopup.btnnext.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            finish();
                                        }
                                    });
                                } else {
                                    binding.errorpopup.ivprogress.setVisibility(View.GONE);
                                    binding.llpopup.setVisibility(View.VISIBLE);
                                    binding.errorpopup.txtmessage.setText("There appears to be an internet connection issue as we are not able to get a response from the Lightning or Google servers.");
                                    binding.errorpopup.btnnext.setText("Close App");
                                    binding.errorpopup.btnnext.setVisibility(View.VISIBLE);
                                    binding.errorpopup.btnnext.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            finish();
                                        }
                                    });
                                }
                            }
                        }
                    });

                }
            }, 0, 10, TimeUnit.SECONDS);

        }
    }

    private void funForWorkingInternet() {
        try {
//            Sign sign = UtilsGlobal.getSign(SplashActivity.this);
//            if (sign != null) {
//                if (sign.getId() != null) {
//                    if (sign.getId().trim().length() > 0) {
//                        ApiClientBillBoard.signId = sign.getId();
//                    }
//                }
//            }
            if (savedStore == null) {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                callStoreVerificationAPI(UtilsGlobal.store.getId());
                UtilsGlobal.AppPref = SplashActivity.this.getSharedPreferences(UtilsGlobal.PrefName, Context.MODE_PRIVATE);
                UtilsGlobal.type = UtilsGlobal.AppPref.getString("storeType", "");
                UtilsGlobal.Industry_Type = UtilsGlobal.AppPref.getString("storeIndustryType", "");
                if (UtilsGlobal.type.equals("QT")){
                    UtilsGlobal.isFromQT=true;
                }else{
                    UtilsGlobal.isFromQT=false;
                }
//                callAndroidTabletValidation(UtilsGlobal.store.getId());
            }
        } catch (Exception e) {
        }
    }



    private void callAndroidTabletValidation(String storeNum) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<Result>> call1 = apiService.AndroidTabletValidation(storeNum);
        call1.enqueue(new Callback<ArrayList<Result>>() {
            @Override
            public void onResponse(Call<ArrayList<Result>> call, Response<ArrayList<Result>> response) {
                if (response.body() != null) {
                    //version web
                    if (response.body().get(0).getWebstore() != null) {
                        if (response.body().get(0).getWebstore().trim().length() > 0) {
                            UtilsGlobal.store.setWebserver(response.body().get(0).getWebstore());
                            UtilsGlobal.getVersionName(binding.txtversion, SplashActivity.this);
                            if (response.body().get(0).getKioskAvailableStors() != null) {
                                if (response.body().get(0).getKioskAvailableStors().size() > 0) {
                                    UtilsGlobal.address = response.body().get(0).getKioskAvailableStors().get(0).getStoreNickName();
                                }
                            }
                            callStoreLoyaltyPointCheckAPI(UtilsGlobal.store.getId());

                        } else {
                            ApiClientBillBoard.signId = "";
                            UtilsGlobal.saveSign(SplashActivity.this, null);
                            new Handler().postDelayed(() -> {
                                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }, SPLASH_TIME);
                        }

                    } else {
                        ApiClientBillBoard.signId = "";
                        UtilsGlobal.saveSign(SplashActivity.this, null);
                        new Handler().postDelayed(() -> {
                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }, SPLASH_TIME);
                    }
                } else {
                    ApiClientBillBoard.signId = "";
                    UtilsGlobal.saveSign(SplashActivity.this, null);
                    new Handler().postDelayed(() -> {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }, SPLASH_TIME);
                }

            }

            @Override
            public void onFailure(Call<ArrayList<Result>> call, Throwable t) {
                call.cancel();
                new Handler().postDelayed(() -> {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }, SPLASH_TIME);

            }
        });
    }

    private void callStoreLoyaltyPointCheckAPI(String storeNum) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<Store>> call1 = apiService.CheckLoyaltyProgram(storeNum);
        call1.enqueue(new Callback<ArrayList<Store>>() {
            @Override
            public void onResponse(Call<ArrayList<Store>> call, Response<ArrayList<Store>> response) {
                if (response.body() != null) {
                    if (response.body().size() > 0) {
                        UtilsGlobal.store.setLoyaltyEnable(false);
                        if (response.body().get(0).getLoyaltyRewardName() != null) {
                            if (response.body().get(0).getLoyaltyRewardName().trim().length() > 0) {
                                if (response.body().get(0).getFrequentBuyer().trim().toLowerCase().contains("y")) {
//                                    Edited by Varun for to check whisch program type is selected from the POS
//                                    if (response.body().get(0).getLoyaltyRewardName().trim().toLowerCase().contains("internal")) {
                                    if (response.body().get(0).getProgramtype().trim().toLowerCase().contains("internal")) {
//                                        END
                                        UtilsGlobal.store.setLoyaltyEnable(true);
                                        UtilsGlobal.store.setLoyaltyRewardName(response.body().get(0).getLoyaltyRewardName());
                                        if (response.body().get(0).getIsLoyaltyPointAllowCheckPoint() != null) {
                                            if (response.body().get(0).getIsLoyaltyPointAllowCheckPoint().trim().length() > 0)
                                                if (response.body().get(0).getIsLoyaltyPointAllowCheckPoint().equalsIgnoreCase("y")) {
                                                    UtilsGlobal.store.setIsLoyaltyPointAllowCheckPoint("true");
                                                    if (response.body().get(0).getAllowCustmLoyalltyCheckProgram() != null) {
                                                        if (response.body().get(0).getAllowCustmLoyalltyCheckProgram().equalsIgnoreCase("y")) {
                                                            UtilsGlobal.store.setAllowToJoinUserForLoyaltyProgram(true);
                                                        } else {
                                                            UtilsGlobal.store.setAllowToJoinUserForLoyaltyProgram(false);
                                                        }
                                                    }
                                                } else {
                                                    UtilsGlobal.store.setIsLoyaltyPointAllowCheckPoint("false");
                                                    UtilsGlobal.store.setAllowToJoinUserForLoyaltyProgram(false);
                                                }
                                        }
                                    }
                                }
                                if (response.body().get(0).getIsLoyaltyPointAllowAllCustomers() != null) {
                                    if (response.body().get(0).getIsLoyaltyPointAllowAllCustomers().trim().length() > 0)
                                        if (response.body().get(0).getIsLoyaltyPointAllowAllCustomers().equalsIgnoreCase("y")) {
                                            UtilsGlobal.store.setIsLoyaltyPointAllowAllCustomers("true");
                                        } else {
                                            UtilsGlobal.store.setIsLoyaltyPointAllowAllCustomers("false");
                                        }
                                }

                            } else {
                                UtilsGlobal.store.setLoyaltyEnable(false);

                            }
                        } else {
                            UtilsGlobal.store.setLoyaltyEnable(false);

                        }
                        if (response.body().get(0).getAuthAllowCustomer() != null) {
                            if (response.body().get(0).getAuthAllowCustomer().trim().length() > 0)
                                if (response.body().get(0).getAuthAllowCustomer().equalsIgnoreCase("y")) {
                                    UtilsGlobal.store.setIsAllowAuthenticationWithCode("true");
                                } else {
                                    UtilsGlobal.store.setIsAllowAuthenticationWithCode("false");
                                }
                        } else {
                            UtilsGlobal.store.setIsAllowAuthenticationWithCode("false");
                        }
                        if (response.body().get(0).getAllowCustmGiftcardCheck() != null) {
                            if (response.body().get(0).getAllowCustmGiftcardCheck().trim().length() > 0) {
                                if (response.body().get(0).getAllowCustmGiftcardCheck().equalsIgnoreCase("y")) {
                                    callGiftCardCheckAPI(UtilsGlobal.store.getId());
                                } else {
                                    UtilsGlobal.store.setIsAllowGiftCardBalanceCheck("false");
                                    UtilsGlobal.store.setGiftCardEnable(true);
                                }
                            }
                        } else {
                            UtilsGlobal.store.setIsAllowGiftCardBalanceCheck("false");
                            UtilsGlobal.store.setGiftCardEnable(false);
                        }
                        if (response.body().get(0).getIsInStockCheck_flag() != null) {
                            if (response.body().get(0).getIsInStockCheck_flag().trim().length() > 0)
                                if (response.body().get(0).getIsInStockCheck_flag().equalsIgnoreCase("Y")) {
                                    UtilsGlobal.store.setIsInStockCheck(true);
                                } else {
                                    UtilsGlobal.store.setIsInStockCheck(false);
                                }
                        } else {
                            UtilsGlobal.store.setIsInStockCheck(false);
                        }
                        if (response.body().get(0).getShowRelatedProducts_flag() != null) {
                            if (response.body().get(0).getShowRelatedProducts_flag().trim().length() > 0)
                                if (response.body().get(0).getShowRelatedProducts_flag().equalsIgnoreCase("y")) {
                                    UtilsGlobal.store.setShowRelatedProducts(true);
                                } else {
                                    UtilsGlobal.store.setShowRelatedProducts(false);
                                }
                        } else {
                            UtilsGlobal.store.setShowRelatedProducts(false);
                        }
                        if (response.body().get(0).getIsExpacteddateforkiosk() != null) {
                            if (response.body().get(0).getIsExpacteddateforkiosk().trim().length() > 0)
                                if (response.body().get(0).getIsExpacteddateforkiosk().equalsIgnoreCase("y")) {
                                    UtilsGlobal.store.setIsNeedToDisplayDate(true);
                                } else {
                                    UtilsGlobal.store.setIsNeedToDisplayDate(false);
                                }
                        } else {
                            UtilsGlobal.store.setIsNeedToDisplayDate(false);
                        }
                        if (response.body().get(0).getStoreType() != null) {
                            if (response.body().get(0).getStoreType().contains("Wine & Spirits Retailers")) {
                                UtilsGlobal.store.setHasWineSpirit(true);
                                if (response.body().get(0).getIsFoodPairingForKiosk() != null) {
                                    if (response.body().get(0).getIsFoodPairingForKiosk().trim().length() > 0) {
                                        if (response.body().get(0).getIsFoodPairingForKiosk().equalsIgnoreCase("Y")) {
                                            UtilsGlobal.store.setHasFoodPairing(true);

                                        } else {
                                            UtilsGlobal.store.setHasFoodPairing(false);

                                        }
                                    } else {
                                        UtilsGlobal.store.setHasFoodPairing(false);

                                    }
                                } else {
                                    UtilsGlobal.store.setHasFoodPairing(false);

                                }
                                if (response.body().get(0).getIsDrinkRecipesForKiosk() != null) {
                                    if (response.body().get(0).getIsDrinkRecipesForKiosk().trim().length() > 0) {
                                        if (response.body().get(0).getIsDrinkRecipesForKiosk().equalsIgnoreCase("Y")) {
                                            UtilsGlobal.store.setHasDrinkReceipes(true);

                                        } else {
                                            UtilsGlobal.store.setHasDrinkReceipes(false);

                                        }
                                    } else {
                                        UtilsGlobal.store.setHasDrinkReceipes(false);

                                    }
                                } else {
                                    UtilsGlobal.store.setHasDrinkReceipes(false);

                                }

                            } else {
                                UtilsGlobal.store.setHasWineSpirit(false);
                            }
                        } else {
                            UtilsGlobal.store.setHasWineSpirit(false);
                        }
                    } else {
                        UtilsGlobal.store.setLoyaltyEnable(false);
                        UtilsGlobal.store.setIsAllowAuthenticationWithCode("false");
                        UtilsGlobal.store.setIsAllowGiftCardBalanceCheck("false");

                    }
                } else {
                    UtilsGlobal.store.setLoyaltyEnable(false);
                    UtilsGlobal.store.setIsAllowAuthenticationWithCode("false");
                    UtilsGlobal.store.setIsAllowGiftCardBalanceCheck("false");

                }
                UtilsGlobal.saveStore(SplashActivity.this, UtilsGlobal.store);
                new Handler().postDelayed(() -> {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }, SPLASH_TIME);
            }

            @Override
            public void onFailure(Call<ArrayList<Store>> call, Throwable t) {
                call.cancel();
                {
                    UtilsGlobal.store.setLoyaltyEnable(false);
                    UtilsGlobal.store.setIsAllowAuthenticationWithCode("false");
                    UtilsGlobal.store.setIsAllowGiftCardBalanceCheck("false");
                    UtilsGlobal.saveStore(SplashActivity.this, UtilsGlobal.store);
                    new Handler().postDelayed(() -> {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }, SPLASH_TIME);
                }

            }
        });
    }

    private void callGiftCardCheckAPI(String storeNum) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Result> call1 = apiService.CheckGiftCards(storeNum);
        call1.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.body() != null) {
                    if (!(response.body().getResult().toLowerCase().contains("data not found") || response.body().getResult().toLowerCase().contains("not activated"))) {
                        if (!response.body().getResult().toLowerCase().contains("internal")) {
                            UtilsGlobal.store.setGiftCardEnable(false);
                            UtilsGlobal.store.setIsGiftCardAvail("false");
                        } else {
                            UtilsGlobal.store.setGiftCardEnable(true);
                        }

                    } else {
                        UtilsGlobal.store.setGiftCardEnable(false);
                        UtilsGlobal.store.setIsGiftCardAvail("false");

                    }

                } else {
                    UtilsGlobal.store.setGiftCardEnable(false);
                    UtilsGlobal.store.setIsGiftCardAvail("false");

                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                call.cancel();
                UtilsGlobal.store.setGiftCardEnable(false);
                UtilsGlobal.store.setIsGiftCardAvail("false");

            }
        });
    }

    @Subscribe
    public void getMessage(InternetCheck internetAvail) {
        //Write code to perform action after event is received.
        try {
            Log.d("TEST", "INTERNET AVAIL");
            if (!internetAvail.isConnected) {
                isApiCalled=false;
                funForWorkingPingInternet();
            } else {
                retryCount = 0;
                SPLASH_TIME = 300;
                binding.llpopup.setVisibility(View.GONE);
                if(!isApiCalled) {
                    isApiCalled = true;
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    funForWorkingInternet();
                                }
                            });
                        }
                    }, 2000);
                }
            }
        } catch (Exception e) {
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(new NetworkReceiver());
        } catch (Exception e) {
        }
    }

    @Override
    protected void onDestroy() {
        try {
            GlobalBus.getBus().unregister(this);

        } catch (Exception e) {
        }
        super.onDestroy();

    }

    private void callStoreVerificationAPI(String storeNum) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<Store>> call1 = apiService.CheckStoreValidation(storeNum);
        call1.enqueue(new Callback<ArrayList<Store>>() {
            @Override
            public void onResponse(Call<ArrayList<Store>> call, Response<ArrayList<Store>> response) {
                if (response.body() != null) {
                    if (response.body().size() > 0) {
                        if (!response.body().get(0).getKioskControlledBy().equalsIgnoreCase("not enabled") &&
                                !response.body().get(0).getKioskControlledBy().equalsIgnoreCase("Locked")) {
//                            Get Industry Type from API
                            if (response.body().get(0).getIndustryType()!=null && !response.body().get(0).getIndustryType().isEmpty()
                                    && !response.body().get(0).getIndustryType().equals("")) {
                                if ( UtilsGlobal.Industry_Type!=null && !UtilsGlobal.Industry_Type.isEmpty()){
                                    UtilsGlobal.Industry_Type= "";
                                }
                                UtilsGlobal.Industry_Type = response.body().get(0).getIndustryType().trim();
                                Log.e("", "Industry_Type: " +UtilsGlobal.Industry_Type  );
                            }
//                            Checking for QT or Computer perfect
                            if (response.body().get(0).getKioskControlledBy().equals("QT")) {
                                UtilsGlobal.isFromQT = true;
                                UtilsGlobal.type = "QT";
                            } else {
                                UtilsGlobal.isFromQT = false;
                                if (UtilsGlobal.type!="" || !UtilsGlobal.type.isEmpty() || UtilsGlobal.type!=null){
                                    UtilsGlobal.type ="";
                                }
                                UtilsGlobal.type = response.body().get(0).getKioskControlledBy();
                                Log.e("", "onResponse: 123" + UtilsGlobal.type);
                            }
                            callAndroidTabletValidation(UtilsGlobal.store.getId());

                        }else{
                            Toast.makeText(context, "LOCKED", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(context, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "NULL REPSONE", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Store>> call, Throwable t) {
                call.cancel();
            }
        });
    }
}