package com.anantkiosk.kioskapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.anantkiosk.kioskapp.Api.ApiClient;
import com.anantkiosk.kioskapp.Api.ApiInterface;
import com.anantkiosk.kioskapp.Model.Result;
import com.anantkiosk.kioskapp.Model.Store;
import com.anantkiosk.kioskapp.Utils.UtilsGlobal;
import com.anantkiosk.kioskapp.databinding.ActivityLoginBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    Store store;
    int index = 0;
    KeyboardView viewKeyboard;
    boolean allowsetup = true;
    String versionName = null, webserverno = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        getSupportActionBar().hide();
        setKeyboard(binding.edtnumber);
        binding.edtnumber.requestFocus();

        binding.txttitle1.setTypeface(UtilsGlobal.setFontSemiBold(LoginActivity.this));
        binding.txtinfo.setTypeface(UtilsGlobal.setFontSemiBold(LoginActivity.this));
        binding.txtmessage.setTypeface(UtilsGlobal.setFontRegular(LoginActivity.this));

        binding.edtcode.setTypeface(UtilsGlobal.setFontRegular(LoginActivity.this));
        binding.edtnumber.setTypeface(UtilsGlobal.setFontRegular(LoginActivity.this));
        binding.textinput1.setTypeface(UtilsGlobal.setFontRegular(LoginActivity.this));
        binding.textinput2.setTypeface(UtilsGlobal.setFontRegular(LoginActivity.this));

        binding.txtnodata.setTypeface(UtilsGlobal.setFontRegular(LoginActivity.this));
        binding.txttitle1.setTypeface(UtilsGlobal.setFontSemiBold(LoginActivity.this));
        binding.txtnodata2.setTypeface(UtilsGlobal.setFontRegular(LoginActivity.this));
        binding.txtresend.setTypeface(UtilsGlobal.setFontSemiBold(LoginActivity.this));
        binding.txtappname.setTypeface(UtilsGlobal.setFontRegular(LoginActivity.this));
        binding.txtversion.setTypeface(UtilsGlobal.setFontRegular(LoginActivity.this));

        binding.btnnext.setTypeface(UtilsGlobal.setFontSemiBold(LoginActivity.this));
        binding.btncheckcode.setTypeface(UtilsGlobal.setFontSemiBold(LoginActivity.this));
        binding.btnsend.setTypeface(UtilsGlobal.setFontSemiBold(LoginActivity.this));

        try {
            allowsetup = getIntent().getBooleanExtra("allowsetup", true);
        } catch (Exception e) {
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        binding.edtnumber.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                binding.container.setVisibility(View.VISIBLE);
                final boolean ret = binding.edtnumber.onTouchEvent(event);
                final InputMethodManager imm = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
                try {
                    imm.hideSoftInputFromWindow(binding.edtnumber.getApplicationWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return ret;
            }
        });
        binding.edtcode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                binding.container.setVisibility(View.VISIBLE);
                final boolean ret = binding.edtcode.onTouchEvent(event);
                final InputMethodManager imm = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
                try {
                    imm.hideSoftInputFromWindow(binding.edtcode.getApplicationWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return ret;
            }
        });
        binding.txtresend.setPaintFlags(binding.txtresend.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        binding.icback.setOnClickListener(view -> {
            binding.txtnodata.setVisibility(View.INVISIBLE);
            if (index == 1) {
                index--;
                setKeyboard(binding.edtnumber);
                binding.edtnumber.requestFocus();
                binding.txtinfo.setText("In order to authenticate your license please enter in the following information:");
                binding.llsend.setVisibility(View.GONE);
                binding.llstoreinfo.setVisibility(View.VISIBLE);
                binding.btnnext.setVisibility(View.VISIBLE);
                binding.icback.setVisibility(View.GONE);
                binding.container.setVisibility(View.VISIBLE);

            } else if (index == 2) {
                index--;
                binding.llcodedetail.setVisibility(View.GONE);
                binding.llsend.setVisibility(View.VISIBLE);
                binding.btnsend.setVisibility(View.VISIBLE);
                binding.container.setVisibility(View.INVISIBLE);

            }
        });
        UtilsGlobal.getVersionName(binding.txtversion, LoginActivity.this);
        binding.edtnumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.llinfo.setVisibility(View.GONE);
                binding.btnnext.setVisibility(View.VISIBLE);
                binding.txtnodata.setVisibility(View.INVISIBLE);
                binding.txtnodata.setText("");
                checkEmpty();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        binding.edtcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.txtnodata2.setVisibility(View.INVISIBLE);
                checkEmptyCode();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        binding.btncheckcode.setOnClickListener(view -> {
            if (binding.edtcode.getText().toString().equalsIgnoreCase("7067")) {
                binding.container.setVisibility(View.INVISIBLE);
                Toast.makeText(LoginActivity.this, "You have successfully enrolled in the Lightning Kiosk program", Toast.LENGTH_SHORT).show();
                Store user = new Store();
                user.setId(binding.edtnumber.getText().toString().trim());
                user.setName(store.getName());
                UtilsGlobal.store = user;
                //show startup wizard
                if (allowsetup) {
                    UtilsGlobal.store.setWebserver(webserverno);
                    Intent intent = new Intent(LoginActivity.this, GeneralSystemSetupQuestionActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("showback", false);
                    startActivity(intent);
                } else {
                    if (UtilsGlobal.isNetworkAvailable(LoginActivity.this)) {
                        callStoreLoyaltyPointCheckAPI(UtilsGlobal.store.getId());
                    }
                }

            } else {
                if (UtilsGlobal.isNetworkAvailable(LoginActivity.this)) {
                    checkVerificationCode();

                }
            }

        });
        binding.btnnext.setOnClickListener(view -> {
            try {
                UtilsGlobal.getVersionName(binding.txtversion, LoginActivity.this);
            } catch (Exception e) {
            }
            if (UtilsGlobal.isNetworkAvailable(LoginActivity.this)) {
                callStoreVerificationAPI(binding.edtnumber.getText().toString().trim());

            } else {
                Toast.makeText(LoginActivity.this, "Internet is not connected! Please try after connecting a working internet!", Toast.LENGTH_SHORT).show();
            }

        });
        binding.btnsend.setOnClickListener(view -> {
            if (UtilsGlobal.isNetworkAvailable(LoginActivity.this)) {
                callMessagesendAPI(false);

            } else {
                Toast.makeText(LoginActivity.this, "Internet is not connected! Please try after connecting a working internet!", Toast.LENGTH_SHORT).show();
            }

        });
        binding.txtresend.setOnClickListener(view -> {
            if (UtilsGlobal.isNetworkAvailable(LoginActivity.this)) {
                binding.edtcode.setText("");
                callMessagesendAPI(true);

            } else {
                Toast.makeText(LoginActivity.this, "Internet is not connected! Please try after connecting a working internet!", Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void checkEmpty() {
        if (binding.edtnumber.getText().toString().trim().length() > 0) {
            binding.btnnext.setEnabled(true);
        } else {
            binding.btnnext.setEnabled(false);
        }
    }

    public void checkEmptyCode() {
        if (binding.edtcode.getText().toString().trim().length() > 0) {
            binding.btncheckcode.setEnabled(true);
        } else {
            binding.btncheckcode.setEnabled(false);
        }
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

                            if (response.body().get(0).getIndustryType()!=null && !response.body().get(0).getIndustryType().isEmpty()
                                && !response.body().get(0).getIndustryType().equals("")) {
                                if ( UtilsGlobal.Industry_Type!=null && !UtilsGlobal.Industry_Type.isEmpty()){
                                    UtilsGlobal.Industry_Type= "";
                                }
                                UtilsGlobal.Industry_Type = response.body().get(0).getIndustryType().trim();
                                Log.e("", "Industry_Type: " +UtilsGlobal.Industry_Type  );
                            }
                            if (response.body().get(0).getKioskControlledBy().equals("QT")){
                                UtilsGlobal.isFromQT=true;
                                UtilsGlobal.type ="QT";
                            }
//                            Edited by Varun for Lighting server image
                            else {
                                UtilsGlobal.isFromQT=false;
                                if (UtilsGlobal.type!="" || !UtilsGlobal.type.isEmpty() || UtilsGlobal.type!=null){
                                    UtilsGlobal.type ="";
                                }
                                UtilsGlobal.type = response.body().get(0).getKioskControlledBy();
                            }
                            UtilsGlobal.AppPref = LoginActivity.this.getSharedPreferences(UtilsGlobal.PrefName, Context.MODE_PRIVATE);
                            UtilsGlobal.AppPref.edit().putString("storeType",response.body().get(0).getKioskControlledBy()).apply();
                            UtilsGlobal.AppPref.edit().putString("storeIndustryType",response.body().get(0).getIndustryType().trim()).apply();
//                            END

                            binding.container.setVisibility(View.INVISIBLE);
                            index++;
                            binding.txtinfo.setText("");
                            binding.icback.setVisibility(View.VISIBLE);
                            binding.llstoreinfo.setVisibility(View.GONE);
                            binding.llinfo.setVisibility(View.VISIBLE);
                            binding.llsend.setVisibility(View.VISIBLE);
                            binding.btnnext.setVisibility(View.GONE);
                            store = response.body().get(0);
                            binding.txtmessage.setText("We will send a code to " + response.body().get(0).getName() + "'s mobile number of " + response.body().get(0).getPhone());
                            callAndroidTabletValidation(storeNum);
                        } else {
                            binding.edtnumber.startAnimation(shakeError());
                            binding.btnnext.setVisibility(View.VISIBLE);
                            binding.txtnodata.setVisibility(View.VISIBLE);
                            binding.txtnodata.setText("");
                            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                            } else {
                                v.vibrate(500);
                            }
                            callAndroidTabletValidationForObj(storeNum);
                        }

                    } else {
                        binding.btnnext.setVisibility(View.VISIBLE);
                        binding.txtnodata.setVisibility(View.VISIBLE);
                        binding.edtnumber.startAnimation(shakeError());
                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            //deprecated in API 26
                            v.vibrate(500);
                        }
                        callAndroidTabletValidationForObj(storeNum);
                    }
                } else {
                    binding.edtnumber.startAnimation(shakeError());
                    binding.txtnodata.setVisibility(View.VISIBLE);
                    binding.btnnext.setVisibility(View.VISIBLE);
                    callAndroidTabletValidationForObj(storeNum);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Store>> call, Throwable t) {
                call.cancel();
                callAndroidTabletValidationForObj(storeNum);
                binding.edtnumber.startAnimation(shakeError());
                binding.txtnodata.setVisibility(View.VISIBLE);
                binding.btnnext.setVisibility(View.VISIBLE);

            }
        });
    }

    private void callMessagesendAPI(boolean isRsend) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Result> call1 = apiService.SendOneTimePasswordText(binding.edtnumber.getText().toString().trim());
        call1.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.body() != null) {
                    if (isRsend) {
                        Toast.makeText(LoginActivity.this, "We have send a code to registered mobile number!", Toast.LENGTH_SHORT).show();
                    }
                    if (response.body().getResult() != null) {
                        if (response.body().getResult().trim().length() > 0) {
                            index++;
                            setKeyboard(binding.edtcode);
                            binding.container.setVisibility(View.VISIBLE);
                            binding.icback.setVisibility(View.VISIBLE);
                            binding.llsend.setVisibility(View.GONE);
                            binding.llcodedetail.setVisibility(View.VISIBLE);
                            binding.btnsend.setVisibility(View.GONE);
                            binding.edtcode.requestFocus();
                            UtilsGlobal.hideKeyboard(LoginActivity.this);

                        } else {
                            binding.txtnodata.setVisibility(View.VISIBLE);
                            binding.btnsend.setVisibility(View.VISIBLE);
                            binding.txtnodata.setText("Unable to send message");
                        }

                    } else {
                        binding.txtnodata.setVisibility(View.VISIBLE);
                        binding.btnsend.setVisibility(View.VISIBLE);
                        binding.txtnodata.setText("Unable to send message");

                    }

                } else {
                    binding.txtnodata.setVisibility(View.VISIBLE);
                    binding.btnsend.setVisibility(View.VISIBLE);
                    //UNCOMMENT AFTER SECURE TESTING
                    binding.txtnodata.setText("Unable to send message");

                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                call.cancel();
                {
                    binding.txtnodata.setVisibility(View.VISIBLE);
                    binding.btnsend.setVisibility(View.VISIBLE);
                    binding.txtnodata.setText("Unable to send message");

                }

            }
        });
    }

    private void checkVerificationCode() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Result> call1 = apiService.CheckOneTimePasswordText(binding.edtnumber.getText().toString().trim(), binding.edtcode.getText().toString().trim());
        call1.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.body() != null) {
                    if (response.body().getResult() != null) {
                        if (response.body().getResult().length() > 0) {
                            if (response.body().getResult().toLowerCase().contains("not match")) {
                                binding.txtnodata2.setVisibility(View.VISIBLE);
                                binding.txtnodata2.setText("" + response.body().getResult());

                            } else {
                                Toast.makeText(LoginActivity.this, "You have successfully enrolled in the Lightning Kiosk program", Toast.LENGTH_SHORT).show();
                                Store user = new Store();
                                user.setId(binding.edtnumber.getText().toString().trim());
                                user.setName(store.getName());
                                UtilsGlobal.store = user;
                                if (allowsetup) {
                                    UtilsGlobal.store.setWebserver(webserverno);
                                    Intent intent = new Intent(LoginActivity.this, GeneralSystemSetupQuestionActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                            Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("showback", false);
                                    startActivity(intent);
                                } else {
                                    if (UtilsGlobal.isNetworkAvailable(LoginActivity.this)) {
                                        callStoreLoyaltyPointCheckAPI(UtilsGlobal.store.getId());
                                    }
                                }
                            }
                        } else {
                            binding.txtnodata2.setVisibility(View.VISIBLE);
                            binding.txtnodata2.setText("Invalid Code!");
                        }

                    } else {
                        binding.txtnodata2.setVisibility(View.VISIBLE);
                        binding.txtnodata2.setText("Invalid Code!");

                    }
                } else {
                    binding.txtnodata2.setVisibility(View.VISIBLE);
                    binding.txtnodata2.setText("Invalid Code!");

                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                call.cancel();
                binding.txtnodata2.setVisibility(View.VISIBLE);
                binding.txtnodata2.setText("Invalid Code!");

            }
        });
    }

    public TranslateAnimation shakeError() {
        TranslateAnimation shake = new TranslateAnimation(0, 7, 0, 0);
        shake.setDuration(250);
        shake.setInterpolator(new CycleInterpolator(2));
        return shake;
    }

    private void callAndroidTabletValidation(String storeNum) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<Result>> call1 = apiService.AndroidTabletValidation(storeNum);
        call1.enqueue(new Callback<ArrayList<Result>>() {
            @Override
            public void onResponse(Call<ArrayList<Result>> call, Response<ArrayList<Result>> response) {
                if (response.body() != null) {
                    if (!response.body().get(0).getResult().equalsIgnoreCase("null")) {
                        if (response.body().get(0).getResult().trim().length() > 0) {
                            if (!response.body().get(0).getResult().equalsIgnoreCase("success")) {
                                if (!response.body().get(0).getResult().equalsIgnoreCase("The store number is incorrect.")) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setMessage(response.body().get(0).getResult()).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    }).show();
                                }
                            }
                        }
                    }
                    if (response.body().get(0).getWebstore() != null) {
                        if (response.body().get(0).getWebstore().trim().length() > 0) {
                            try {
                                webserverno = response.body().get(0).getWebstore();
                                if (response.body().get(0).getKioskAvailableStors() != null) {
                                    if (response.body().get(0).getKioskAvailableStors().size() > 0) {
                                        UtilsGlobal.address = response.body().get(0).getKioskAvailableStors().get(0).getStoreNickName();
                                    }
                                }
                                UtilsGlobal.store.setWebserver(response.body().get(0).getWebstore());
                            } catch (Exception e) {
                            }
                            binding.txtversion.setVisibility(View.VISIBLE);
                            UtilsGlobal.getVersionName(binding.txtversion, LoginActivity.this);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Result>> call, Throwable t) {
                call.cancel();
                callAndroidTabletValidationForObj(storeNum);
            }
        });
    }

    private void callAndroidTabletValidationForObj(String storeNum) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Result> call1 = apiService.AndroidTabletValidationObj(storeNum);
        call1.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.body() != null) {
                    if (!response.body().getResult().equalsIgnoreCase("null")) {
                        if (response.body().getResult().trim().length() > 0) {
                            if (!response.body().getResult().equalsIgnoreCase("success")) {
                                if (!response.body().getResult().equalsIgnoreCase("The store number is incorrect.")) {
                                    binding.txtnodata.setText("" + response.body().getResult());
                                   /* AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setMessage(response.body().getResult()).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    }).show();*/
                                } else {
                                    binding.txtnodata.setText("" + response.body().getResult());
                                }
                            }
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                call.cancel();

            }
        });
    }

    public void setKeyboard(EditText txtView) {
        final float scale = LoginActivity.this.getResources().getDisplayMetrics().density;
        int widthfor = (int) (780 * scale + 0.5f);
        viewKeyboard = new KeyboardView(this, txtView);
        LinearLayout.LayoutParams params = new
                LinearLayout.LayoutParams(widthfor,
                LinearLayout.LayoutParams.MATCH_PARENT);
        params.weight = 5;
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        params.setMargins(7, 16, 7, 30);
        viewKeyboard.setLayoutParams(params);
        try {
            if (binding.container.getChildCount() > 0) {
                binding.container.removeViewAt(0);
            }
        } catch (Exception e) {
        }
        binding.container.addView(viewKeyboard, 0);
    }

    private void callStoreLoyaltyPointCheckAPI(String storeNum) {
        UtilsGlobal.showProgressBar(LoginActivity.this);
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
//                                    Edited by Varun for to check which program type is selected from the POS
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
                                    UtilsGlobal.store.setIsAllowGiftCardBalanceCheck("true");
                                    UtilsGlobal.store.setGiftCardEnable(true);
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

               /* Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();*/
                new Handler().postDelayed(() -> {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            UtilsGlobal.dismissProgressBar();
                        }
                    });
                    UtilsGlobal.saveStore(LoginActivity.this, UtilsGlobal.store);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }, 2000);
            }

            @Override
            public void onFailure(Call<ArrayList<Store>> call, Throwable t) {
                call.cancel();
                {
                    UtilsGlobal.saveStore(LoginActivity.this, UtilsGlobal.store);
                    new Handler().postDelayed(() -> {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                UtilsGlobal.dismissProgressBar();
                            }
                        });
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }, 2000);

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
}