package com.anantkiosk.kioskapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.anantkiosk.kioskapp.Api.ApiClient;
import com.anantkiosk.kioskapp.Api.ApiClientBillBoard;
import com.anantkiosk.kioskapp.Api.ApiInterface;
import com.anantkiosk.kioskapp.Eventbus.GlobalBus;
import com.anantkiosk.kioskapp.Eventbus.InternetCheck;
import com.anantkiosk.kioskapp.Model.Result;
import com.anantkiosk.kioskapp.Home.HomeFragments.AdvertisementFragment;
import com.anantkiosk.kioskapp.Home.HomeFragments.LoyaltyRewardsFragment;
import com.anantkiosk.kioskapp.Home.HomeFragments.GiftCardFragment;
import com.anantkiosk.kioskapp.Home.HomeFragments.HomeFragment;
import com.anantkiosk.kioskapp.Home.HomeFragments.ItemLookupFragment;
import com.anantkiosk.kioskapp.Utils.UtilsGlobal;
import com.anantkiosk.kioskapp.databinding.ActivityMainBinding;
import com.google.android.material.textfield.TextInputLayout;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public boolean isDeleted = false;
    public static MainActivity contex;
    Handler handler;
    Runnable r;
    int retryCount = 0;
    private int count = 0;
    private long startMillis = 0;
    boolean isTouchVer = false;
    public String barcode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String android_id = Settings.Secure.getString(getApplication().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.e("", "android_id"+android_id );
        try {
            getSupportActionBar().hide();
        } catch (Exception e) {
        }
        contex = this;
        try {
            GlobalBus.getBus().register(this);

        } catch (Exception e) {
        }
        UtilsGlobal.customer = null;
        binding.txtappname.setTypeface(UtilsGlobal.setFontRegular(this));
        binding.txtversion.setTypeface(UtilsGlobal.setFontRegular(this));
        binding.txtlogoff.setPaintFlags(binding.txtlogoff.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        UtilsGlobal.saveCustomer(MainActivity.this, null);
        UtilsGlobal.getVersionName(binding.txtversion, MainActivity.this);
        binding.txtversion.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isTouchVer = true;
                return false;
            }
        });
        try {
            Log.d("TEST", "IN MAIN");
            callAndroidTabletValidation(UtilsGlobal.store.getId());
        } catch (Exception e) {
        }
        binding.txtlogoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    ApiClientBillBoard.signId = "";
                                    finishAffinity();
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("This option is for administrator! Are you sure you want to log out from this device?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                } catch (Exception e) {
                }

            }
        });
        HomeFragment homeFragment = new HomeFragment();
        AdvertisementFragment advFragment = new AdvertisementFragment();
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.leftpane, homeFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        final FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
        if (!GeneralSystemSetupQuestionActivity.finishApp) {
            transaction1.replace(R.id.rightpane, advFragment);
            transaction1.addToBackStack(null);
            transaction1.commit();
        }

        handler = new Handler();
        r = new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    if ((UtilsGlobal.customer != null && HomeFragment.aactivity.seltype == 0 && !LoyaltyRewardsFragment.contex.isPopUpDisplayed) || (HomeFragment.aactivity.seltype == 1 && GiftCardFragment.context.isDetailUp)) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this,
                                R.style.DialogTheme));
                        alertDialogBuilder.setTitle("\uD83D\uDE34Session Time Out!");
                        alertDialogBuilder.setMessage(Html.fromHtml("Due to inactivity you have been logged out."));
                        alertDialogBuilder.setCancelable(true);
                        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.leftpane, homeFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        final AlertDialog dialog = alertDialogBuilder.show();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 5s = 5000ms
                                if (alertDialogBuilder != null) {
                                    dialog.dismiss();
                                }
                            }
                        }, 3000);
                        dialog.show();
                        stopHandler();
                        UtilsGlobal.customer = null;
                        UtilsGlobal.saveCustomer(MainActivity.this, null);
                    }

                } catch (Exception e) {
                }

            }
        };

    }

    public void changeFragment(Fragment newFragment) {
        final FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
        transaction1.replace(R.id.leftpane, newFragment);
        transaction1.addToBackStack(null);
        transaction1.commit();
    }

    @Override
    public void onUserInteraction() {
        // TODO Auto-generated method stub
        super.onUserInteraction();
        try {
            UtilsGlobal.getCustomerList(MainActivity.this);
            if ((UtilsGlobal.customer != null && HomeFragment.aactivity.seltype == 0 && !LoyaltyRewardsFragment.contex.isPopUpDisplayed) || (HomeFragment.aactivity.seltype == 1 && GiftCardFragment.context.isDetailUp)) {
                stopHandler();//stop first and then start
                startHandler();
            }
        } catch (Exception e) {
        }
    }

    public void stopHandler() {
        handler.removeCallbacks(r);
    }

    public void startHandler() {
        // handler.postDelayed(r, 10000); //for  10 seconds
        handler.postDelayed(r, 20000); //for  20 seconds
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (GeneralSystemSetupQuestionActivity.finishApp) {
                GeneralSystemSetupQuestionActivity.finishApp = false;
                UtilsGlobal.LogOff(MainActivity.this);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventaction = event.getAction();
        if (eventaction == MotionEvent.ACTION_UP) {
            //get system current milliseconds
            long time = System.currentTimeMillis();
            //if it is the first time, or if it has been more than 5 seconds since the first tap ( so it is like a new try), we reset everything
            if (startMillis == 0 || (time - startMillis > 3000)) {
                startMillis = time;
                count = 1;
            }
            //it is not the first, and it has been  less than 3 seconds since the first
            else { //  time-startMillis< 3000
                count++;
            }
            if (count == 3 && isTouchVer) {
                isTouchVer = false;
                //Log.d("TEST","Count: "+count);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.MyAlertDialogTheme);
                LayoutInflater inflater = this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alert_password, null);
                dialogBuilder.setView(dialogView);
                AlertDialog alertDialog = dialogBuilder.create();

                TextView txttitle = (TextView) dialogView.findViewById(R.id.txttitle);
                txttitle.setTypeface(UtilsGlobal.setFontSemiBold(MainActivity.this));

                TextInputLayout inputtype1=dialogView.findViewById(R.id.textinput1);
                inputtype1.setTypeface(UtilsGlobal.setFontRegular(MainActivity.this));

                EditText editText = (EditText) dialogView.findViewById(R.id.edtpassword);
                editText.setTypeface(UtilsGlobal.setFontRegular(MainActivity.this));
                Button btn = dialogView.findViewById(R.id.btnnext);
                btn.setTypeface(UtilsGlobal.setFontSemiBold(MainActivity.this));
                alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                editText.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        final boolean ret = editText.onTouchEvent(event);
                        final InputMethodManager imm = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
                        try {
                            imm.hideSoftInputFromWindow(editText.getApplicationWindowToken(), 0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return ret;
                    }
                });
                LinearLayout container1 = dialogView.findViewById(R.id.container);
                KeyboardView viewKeyboard = new KeyboardView(contex, editText);
                ImageView icback = dialogView.findViewById(R.id.icback);
                icback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UtilsGlobal.hideKeyboard(MainActivity.this);
                        alertDialog.dismiss();
                    }
                });
                final float scale = getResources().getDisplayMetrics().density;
                int widthfor = (int) (780 * scale + 0.5f);
                LinearLayout.LayoutParams params = new
                        LinearLayout.LayoutParams(widthfor,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                params.weight = 5;
                params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                params.setMargins(0, 2, 0, 16);
                viewKeyboard.setLayoutParams(params);
                container1.addView(viewKeyboard, 0);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                TextView txtview = dialogView.findViewById(R.id.txtnodata);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        txtview.setText("");
                        if (editText.getText().toString().trim().length() >= 3) {
                            btn.setEnabled(true);
                        } else {
                            btn.setEnabled(false);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                    }
                });
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String edtVal = editText.getText().toString().trim();
                        if (edtVal.toString().trim().equalsIgnoreCase("8959")) {
                            alertDialog.dismiss();
                            UtilsGlobal.hideKeyboard(MainActivity.this);
                            Intent intent = new Intent(MainActivity.this, GeneralSystemSetupQuestionActivity.class);
                            intent.putExtra("showback", true);
                            startActivity(intent);
                        } else {
                            txtview.setText("Invalid password");
                        }
                    }
                });
                alertDialog.show();
            }
            return true;
        }
        return false;
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
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
                    //version web
                    if (response.body().get(0).getWebstore() != null) {
                        if (response.body().get(0).getWebstore().trim().length() > 0) {
                            UtilsGlobal.store.setWebserver(response.body().get(0).getWebstore());
                            UtilsGlobal.getVersionName(binding.txtversion, MainActivity.this);
                        }
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Result>> call, Throwable t) {
                call.cancel();
                //send in login
            }
        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        try {
            ItemLookupFragment.dialog.dismiss();
            ItemLookupFragment.isFromSKU = false;
        } catch (Exception e1) {
        }
        try {
            if (e.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                if (barcode != null) {
                    if (barcode.length() > 0) {
                        barcode = barcode.substring(0, barcode.length() - 1);
                    }
                }
            } else if (e.getAction() == KeyEvent.ACTION_DOWN
                    && e.getKeyCode() != KeyEvent.KEYCODE_ENTER) { //Not Adding ENTER_KEY to barcode String
                char pressedKey = (char) e.getUnicodeChar();
                barcode += pressedKey;
            }
            if (e.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                if (HomeFragment.aactivity != null) {
                    if (HomeFragment.aactivity.seltype == 1) {
                        if (GiftCardFragment.context != null) {
                            if (barcode != null) {
                                if (barcode.length() > 0) {
                                    GiftCardFragment.context.fetchGiftCardDetails(barcode, true);
                                }
                            }

                        }
                    } else {
                        if (ItemLookupFragment.activity != null) {
                            if (barcode != null) {
                                if (barcode.length() > 0) {
                                    ItemLookupFragment.activity.scaneQR(barcode);
                                }
                            }
                        } else {
                            HomeFragment.aactivity.seltype = 2;
                            ItemLookupFragment homeFragment = new ItemLookupFragment();
                            changeFragment(homeFragment);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (barcode != null) {
                                                if (barcode.length() > 0) {
                                                    ItemLookupFragment.activity.scaneQR(barcode);
                                                }
                                            }
                                        }
                                    });

                                }
                            }, 1000);

                        }
                    }
                }
                barcode = "";
                return false;
            } else {
                return super.dispatchKeyEvent(e);
            }
        } catch (Exception e5) {
            return super.dispatchKeyEvent(e);
        }
    }

    public void setVersion() {
        UtilsGlobal.getVersionName(binding.txtversion, MainActivity.this);
    }

    @Subscribe
    public void getMessage(InternetCheck internetAvail) {
        //Write code to perform action after event is received.
        try {
            Log.d("TEST", "here for internet");
            if (!internetAvail.isConnected) {
                funForWorkingPingInternet();
                try {
                    if (HomeFragment.aactivity.seltype != -1) {
                        HomeFragment homeFragment = new HomeFragment();
                        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.leftpane, homeFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                } catch (Exception e) {
                    HomeFragment homeFragment = new HomeFragment();
                    final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.leftpane, homeFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();

                }

            } else {
                retryCount = 0;
                binding.llpopup.setVisibility(View.GONE);

            }
        } catch (Exception e) {
        }

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
                            if (!UtilsGlobal.isNetworkAvailable(MainActivity.this)) {
                                retryCount++;
                                if (retryCount == 1) {
                                    binding.llpopup.setVisibility(View.VISIBLE);
                                    binding.errorpopup.txtmessage.setText("There appears to be an internet connection issue as we are not able to get a response from the Lightning or Google servers");
                                    binding.errorpopup.btnnext.setText("Close App");
                                    binding.errorpopup.btnnext.setVisibility(View.GONE);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            GlobalBus.getBus().unregister(this);
        } catch (Exception e) {
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

//     Edited by Varun for the back press
    @Override
    public void onBackPressed() {

        if (!HomeFragment.aactivity.isVisible()) {
            getSupportFragmentManager().popBackStack();
        }
//        else{
//            finish();
//        }
        return;
    }
//    END
}