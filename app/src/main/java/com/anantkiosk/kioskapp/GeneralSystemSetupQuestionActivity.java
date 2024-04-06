package com.anantkiosk.kioskapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.anantkiosk.kioskapp.Api.ApiClient;
import com.anantkiosk.kioskapp.Api.ApiInterface;
import com.anantkiosk.kioskapp.Eventbus.GlobalBus;
import com.anantkiosk.kioskapp.Eventbus.InternetCheck;
import com.anantkiosk.kioskapp.Model.Result;
import com.anantkiosk.kioskapp.Model.Store;
import com.anantkiosk.kioskapp.Home.HomeFragments.AdvertisementFragment;
import com.anantkiosk.kioskapp.Utils.UtilsGlobal;
import com.anantkiosk.kioskapp.databinding.ActivityGeneralSetupBinding;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GeneralSystemSetupQuestionActivity extends AppCompatActivity {

    private ActivityGeneralSetupBinding binding;
    boolean isLoyaltyRewardEnable = false, isGiftCardsEnable = false;
    public static boolean finishApp = false;
    //loyalty
    boolean isLoyaltyRewardAllowPoints = false, isLoyaltyRewardAllowAllCustomers = false;

    //customerauthentication
    boolean isCustomerAuthenticationWithCode = false;

    //giftcards
    boolean isAllowGiftCardBalanceCheck = false;
    int index = 0;
    int retryCount = 0;

    boolean isTouchRadio1 = false, isTouchRadio1_1 = false, isTouchRadio2 = false, isTouchRadio3 = false, isTouchRadio4 = false, isTouchRadio5 = false, isTouchRadio6 = false, isTouchRadio7 = false, isTouchRadio8 = false, isTouchRadio9 = false, isTouchRadio10 = false;
    boolean isApiCalled=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGeneralSetupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.txttitle.setTypeface(UtilsGlobal.setFontSemiBold(this));

        binding.txtloyaltyque1.setTypeface(UtilsGlobal.setFontRegular(this));
        binding.txtloyaltyque11.setTypeface(UtilsGlobal.setFontRegular(this));
        binding.txtloyaltyrewardname.setTypeface(UtilsGlobal.setFontSemiBold(this));

        binding.btnnext.setTypeface(UtilsGlobal.setFontSemiBold(this));

        binding.txtgiftcardnames.setTypeface(UtilsGlobal.setFontSemiBold(this));
        binding.txtgiftcardq1.setTypeface(UtilsGlobal.setFontRegular(this));

        binding.txttitle1.setTypeface(UtilsGlobal.setFontSemiBold(this));
        binding.txttitle2.setTypeface(UtilsGlobal.setFontSemiBold(this));
        binding.txttitle3.setTypeface(UtilsGlobal.setFontSemiBold(this));
        binding.txttitle4.setTypeface(UtilsGlobal.setFontSemiBold(this));
        binding.txttitle5.setTypeface(UtilsGlobal.setFontSemiBold(this));

        binding.txtrefresh.setTypeface(UtilsGlobal.setFontSemiBold(this));
        binding.txtlogoffnew.setTypeface(UtilsGlobal.setFontSemiBold(this));
        binding.txtlogoff.setTypeface(UtilsGlobal.setFontSemiBold(this));



        binding.txtauthtitle.setTypeface(UtilsGlobal.setFontRegular(this));
        binding.authtitle2.setTypeface(UtilsGlobal.setFontRegular(this));

        binding.txtappname.setTypeface(UtilsGlobal.setFontRegular(this));
        binding.txtversion.setTypeface(UtilsGlobal.setFontRegular(this));

        binding.txtsubtitle.setTypeface(UtilsGlobal.setFontRegular(this));

        binding.txtfoodpair.setTypeface(UtilsGlobal.setFontRegular(this));
        binding.textinput1.setTypeface(UtilsGlobal.setFontRegular(this));
        binding.edtwebsite.setTypeface(UtilsGlobal.setFontRegular(this));

        binding.txtdrinkreceipe.setTypeface(UtilsGlobal.setFontRegular(this));

        getSupportActionBar().hide();
        try {
            GlobalBus.getBus().register(this);

        } catch (Exception e) {
        }
        try {
            try {
                binding.txtlogoff.setPaintFlags(binding.txtlogoff.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            } catch (Exception e1) {
            }
        } catch (Exception e) {
        }
        if (UtilsGlobal.isNetworkAvailable(GeneralSystemSetupQuestionActivity.this)) {
            callStoreLoyaltyPointCheckAPI(UtilsGlobal.store.getId());
        } else {
            funForWorkingPingInternet();
        }
        binding.edtwebsite.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.edtwebsite.setTextColor(getResources().getColor(R.color.primary_color));
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        UtilsGlobal.getVersionName(binding.txtversion, GeneralSystemSetupQuestionActivity.this);
        binding.txtlogoffnew.setVisibility(View.GONE);
        binding.txtlogoff.setVisibility(View.INVISIBLE);
        if (getIntent().getBooleanExtra("showback", false)) {
            binding.icback.setVisibility(View.VISIBLE);
            binding.txtlogoffnew.setVisibility(View.VISIBLE);
            binding.txtlogoff.setVisibility(View.VISIBLE);
            binding.txtrefresh.setVisibility(View.VISIBLE);
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
                                    finishApp = true;
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(GeneralSystemSetupQuestionActivity.this);
                    builder.setMessage("Are you sure you want to log out from this device?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                } catch (Exception e) {
                }
            }
        });
        binding.txtrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GeneralSystemSetupQuestionActivity.this, ResponseListActivity.class);
                intent.putExtra("androidID", AdvertisementFragment.context.android_id);
                startActivity(intent);
             /*   if (UtilsGlobal.getTimeToRefresh(GeneralQuestionActivity.this, false)) {
                    AdvertisementFragment.context.scheduleAsveCalls();
                }else {
                    try {
                        Double timeis = Double.parseDouble(String.valueOf(UtilsGlobal.getTimeToDisplay(GeneralQuestionActivity.this)));
                        if (timeis == 5.5)
                            timeis = 5.30;
                        Toast.makeText(GeneralQuestionActivity.this, "Refresh already requested " + (6 - timeis) + " minutes ago! Try refresh after " + UtilsGlobal.getTimeToDisplay(GeneralQuestionActivity.this) + " minutes!", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(GeneralQuestionActivity.this, "We already refreshed few minutes ago! Try refresh after sometime!", Toast.LENGTH_LONG).show();
                    }
                }*/
            }
        });
        binding.txtlogoffnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilsGlobal.saveStore(GeneralSystemSetupQuestionActivity.this, null);
                Intent intent = new Intent(GeneralSystemSetupQuestionActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("allowsetup", false);
                startActivity(intent);
                finish();
            }
        });
        binding.icback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (index == 0) {
                    Intent intent = new Intent(GeneralSystemSetupQuestionActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else if (index == 1) {
                    index--;
                    binding.llloyaltyreward.setVisibility(View.VISIBLE);
                    binding.llcustomerauthentication.setVisibility(View.GONE);
                    if (getIntent().getBooleanExtra("showback", false)) {
                        binding.icback.setVisibility(View.VISIBLE);
                    }
                    binding.btnnext.setText("    Next    ");
                } else if (index == 2) {
                    index--;
                    binding.llgiftcards.setVisibility(View.GONE);
                    binding.llcustomerauthentication.setVisibility(View.VISIBLE);
                    binding.btnnext.setText("    Next    ");
                } else if (index == 3) {
                    index--;
                    binding.llgiftcards.setVisibility(View.VISIBLE);
                    binding.llitemlookup.setVisibility(View.GONE);
                    binding.btnnext.setText("    Next    ");
                } else {
                    index--;
                    binding.llitemlookup.setVisibility(View.VISIBLE);
                    binding.llfoodpairing.setVisibility(View.GONE);
                    binding.btnnext.setText("    Next    ");
                }
            }
        });
        binding.switch5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isTouchRadio5 = true;
                return false;
            }
        });
        binding.switch5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (isTouchRadio5) {
                    isTouchRadio5 = false;
                    if (b) {
                        UtilsGlobal.store.setHasFoodPairing(true);
                        binding.textinput1.setVisibility(View.VISIBLE);
                    } else {
                        UtilsGlobal.store.setHasFoodPairing(false);
                        binding.textinput1.setVisibility(View.GONE);
                    }
                    updateFoodAndDrinks(UtilsGlobal.store.getId(), b, UtilsGlobal.store.isHasDrinkReceipes(), binding.edtwebsite.getText().toString().trim(), false);
                }
            }
        });
        binding.switch6.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isTouchRadio6 = true;
                return false;
            }
        });
        binding.switch8.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isTouchRadio8 = true;
                return false;
            }
        });
        binding.switch8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (isTouchRadio8) {
                    isTouchRadio8 = false;
                    UtilsGlobal.store.setIsInStockCheck(b);
                    if (b) {
                        binding.llshowdate.setVisibility(View.VISIBLE);
                        UtilsGlobal.store.setIsInStockCheck_flag("Y");
                    } else {
                        UtilsGlobal.store.setIsInStockCheck_flag("N");
                        binding.llshowdate.setVisibility(View.GONE);
                    }
                    updateInventorySettings(b, UtilsGlobal.store.getShowRelatedProducts(), UtilsGlobal.store.getIsNeedToDisplayDate());
                }
            }
        });
        binding.switch9.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isTouchRadio9 = true;
                return false;
            }
        });
        binding.switch10.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isTouchRadio10 = true;
                return false;
            }
        });
        binding.switch10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (isTouchRadio10) {
                    UtilsGlobal.store.setShowRelatedProducts(b);
                    isTouchRadio10 = false;
                    if (b) {
                        UtilsGlobal.store.setShowRelatedProducts_flag("Y");
                    } else {
                        UtilsGlobal.store.setShowRelatedProducts_flag("N");
                    }
                    updateInventorySettings(UtilsGlobal.store.getIsInStockCheck(), b, UtilsGlobal.store.getIsNeedToDisplayDate());
                }
            }
        });
        binding.switch9.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (isTouchRadio9) {
                    isTouchRadio9 = false;
                    if (b) {
                        UtilsGlobal.store.setIsNeedToDisplayDate(true);
                    } else {
                        UtilsGlobal.store.setIsNeedToDisplayDate(false);
                    }
                    updateInventorySettings(UtilsGlobal.store.getIsInStockCheck(), UtilsGlobal.store.getShowRelatedProducts(), b);
                }
            }
        });
        binding.switch6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (isTouchRadio6) {
                    isTouchRadio6 = false;
                    if (b) {
                        UtilsGlobal.store.setHasDrinkReceipes(true);
                    } else {
                        UtilsGlobal.store.setHasDrinkReceipes(false);
                    }
                    updateFoodAndDrinks(UtilsGlobal.store.getId(), UtilsGlobal.store.isHasFoodPairing(), b, binding.edtwebsite.getText().toString().trim(), false);
                }
            }
        });
        binding.switch1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isTouchRadio1 = true;
                return false;
            }
        });
        binding.switch11.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isTouchRadio1_1 = true;
                return false;
            }
        });
        binding.switch11.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (isTouchRadio1_1) {
                    isTouchRadio1_1 = false;
                    if (b) {
                        updateLoyaltyPointAllowCheck(UtilsGlobal.store.getId(), "Y", "Y");
                        UtilsGlobal.store.setAllowToJoinUserForLoyaltyProgram(true);
                    } else {
                        if (isLoyaltyRewardAllowPoints) {
                            updateLoyaltyPointAllowCheck(UtilsGlobal.store.getId(), "Y", "Y");
                        } else {
                            updateLoyaltyPointAllowCheck(UtilsGlobal.store.getId(), "Y", "N");
                        }
                        UtilsGlobal.store.setAllowToJoinUserForLoyaltyProgram(false);
                    }
                }
            }
        });
        binding.switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (isTouchRadio1) {
                    isTouchRadio1 = false;
                    if (b) {
                        isLoyaltyRewardAllowPoints = true;
                        UtilsGlobal.store.setIsLoyaltyPointAllowCheckPoint("true");
                        if (UtilsGlobal.store.isAllowToJoinUserForLoyaltyProgram()) {
                            updateLoyaltyPointAllowCheck(UtilsGlobal.store.getId(), "Y", "Y");
                        } else {
                            updateLoyaltyPointAllowCheck(UtilsGlobal.store.getId(), "Y", "N");
                        }
                        binding.lljoinloyalty.setVisibility(View.VISIBLE);
                    } else {
                        isLoyaltyRewardAllowPoints = false;
                        UtilsGlobal.store.setIsLoyaltyPointAllowCheckPoint("false");
                        UtilsGlobal.store.setAllowToJoinUserForLoyaltyProgram(false);
                        updateLoyaltyPointAllowCheck(UtilsGlobal.store.getId(), "N", "N");
                        binding.lljoinloyalty.setVisibility(View.GONE);
                    }
                }
            }
        });
        binding.switch2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isTouchRadio2 = true;
                return false;
            }
        });
        binding.switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (isTouchRadio2) {
                    isTouchRadio2 = false;
                    if (b) {
                        isLoyaltyRewardAllowAllCustomers = true;
                        UtilsGlobal.store.setIsLoyaltyPointAllowAllCustomers("true");
                        updateLoyaltyPointAllowCheckCustomers(UtilsGlobal.store.getId(), "Y");
                    } else {
                        isLoyaltyRewardAllowAllCustomers = false;
                        UtilsGlobal.store.setIsLoyaltyPointAllowAllCustomers("false");
                        updateLoyaltyPointAllowCheckCustomers(UtilsGlobal.store.getId(), "N");
                    }
                }
            }
        });
        binding.switch3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isTouchRadio3 = true;
                return false;
            }
        });
        binding.switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (isTouchRadio3) {
                    isTouchRadio3 = false;
                    if (b) {
                        isCustomerAuthenticationWithCode = true;
                        UtilsGlobal.store.setIsAllowAuthenticationWithCode("true");
                        updateCustomerAuthentication(UtilsGlobal.store.getId(), "Y");
                    } else {
                        isCustomerAuthenticationWithCode = false;
                        UtilsGlobal.store.setIsAllowAuthenticationWithCode("false");
                        updateCustomerAuthentication(UtilsGlobal.store.getId(), "N");
                    }
                }

            }
        });
        binding.switch4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isTouchRadio4 = true;
                return false;
            }
        });
        binding.switch4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (isTouchRadio4) {
                    isTouchRadio4 = false;
                    if (b) {
                        isAllowGiftCardBalanceCheck = true;
                        UtilsGlobal.store.setIsAllowGiftCardBalanceCheck("true");
                        updateGiftcardPointcheck(UtilsGlobal.store.getId(), "Y");
                    } else {
                        isAllowGiftCardBalanceCheck = false;
                        UtilsGlobal.store.setIsAllowGiftCardBalanceCheck("false");
                        updateGiftcardPointcheck(UtilsGlobal.store.getId(), "N");
                    }
                }
            }
        });
        binding.btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (index == 0) {
                    index++;
                    binding.llloyaltyreward.setVisibility(View.GONE);
                    binding.llcustomerauthentication.setVisibility(View.VISIBLE);
                    binding.icback.setVisibility(View.VISIBLE);
                    binding.btnnext.setText("    Next    ");
                    try {
                        if (UtilsGlobal.store.getIsAllowAuthenticationWithCode() == null) {
                            UtilsGlobal.store.setIsAllowAuthenticationWithCode("false");
                        }
                    } catch (Exception e) {
                    }

                } else if (index == 1) {
                    index++;
                    binding.llcustomerauthentication.setVisibility(View.GONE);
                    binding.llgiftcards.setVisibility(View.VISIBLE);
                    binding.btnnext.setText("    Next    ");
                    try {
                        callGiftCardCheckAPI(UtilsGlobal.store.getId());
                    } catch (Exception e) {
                        UtilsGlobal.store.setGiftCardEnable(false);
                        UtilsGlobal.store.setIsGiftCardAvail("false");
                        binding.txtgiftcardnames.setText("Gift Cards not activated at the store!");
                        binding.txtgiftcardnames.setTextColor(getResources().getColor(R.color.disable_title_color));
                        binding.txtgiftcardnames.setVisibility(View.VISIBLE);
                        binding.llgiftcardsdetail.setVisibility(View.GONE);
                    }
                } else if (index == 2) {
                    index++;
                    binding.llcustomerauthentication.setVisibility(View.GONE);
                    binding.llgiftcards.setVisibility(View.GONE);
                    binding.llitemlookup.setVisibility(View.VISIBLE);
                    if (UtilsGlobal.store.isHasWineSpirit()) {
                        binding.btnnext.setText("    Next    ");
                    } else {
                        binding.btnnext.setText("Continue");
                    }

                } else if (index == 3) {
                    if (UtilsGlobal.store.isHasWineSpirit()) {
                        index++;
                        binding.llitemlookup.setVisibility(View.GONE);
                        binding.llfoodpairing.setVisibility(View.VISIBLE);
                        binding.btnnext.setText("Continue");
                    } else {
                        UtilsGlobal.saveStore(GeneralSystemSetupQuestionActivity.this, UtilsGlobal.store);
                        Intent intent = new Intent(GeneralSystemSetupQuestionActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                } else {
                    if (UtilsGlobal.store.isHasWineSpirit()) {
                        if (UtilsGlobal.store.isHasFoodPairing()) {
                            if (binding.edtwebsite.getText().toString().trim().length() <= 0) {
                                Toast.makeText(GeneralSystemSetupQuestionActivity.this, "Please insert food pairing website!", Toast.LENGTH_SHORT).show();
                            } else if (isValidUrl(binding.edtwebsite.getText().toString())) {
                                UtilsGlobal.store.setFoodpairingSite(binding.edtwebsite.getText().toString());
                                updateFoodAndDrinks(UtilsGlobal.store.getId(), UtilsGlobal.store.isHasFoodPairing(), UtilsGlobal.store.isHasDrinkReceipes(), binding.edtwebsite.getText().toString().trim(), true);

                            } else {
                                binding.edtwebsite.setTextColor(Color.RED);

                            }
                        } else {
                            UtilsGlobal.saveStore(GeneralSystemSetupQuestionActivity.this, UtilsGlobal.store);
                            Intent intent = new Intent(GeneralSystemSetupQuestionActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    } else {
                        UtilsGlobal.saveStore(GeneralSystemSetupQuestionActivity.this, UtilsGlobal.store);
                        Intent intent = new Intent(GeneralSystemSetupQuestionActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
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
                        isApiCalled=true;
                        if (response.body().get(0).getLoyaltyRewardName() != null) {
                            if (response.body().get(0).getLoyaltyRewardName().trim().length() > 0) {
                                UtilsGlobal.store.setLoyaltyRewardName(response.body().get(0).getLoyaltyRewardName());
//                                UtilsGlobal.store.setProgramtype(response.body().get(0).getProgramtype());
                                binding.txtloyaltyrewardname.setText(response.body().get(0).getLoyaltyRewardName());
                                //tom's words and request
                                //The above option is for ‘Valutec’ which I want to remove
                                //If Lightning is configured for Valuetec or Skoop, please gray out the first question related to loyalty rewards.  I only want to support our internal program.
                                UtilsGlobal.store.setLoyaltyEnable(false);
                                if (response.body().get(0).getFrequentBuyer().trim().toLowerCase().contains("y")) {
//                                    Edited by Varun for to check whisch program type is selected from the POS
//                                    if (response.body().get(0).getLoyaltyRewardName().trim().toLowerCase().contains("internal")) {
                                    if (response.body().get(0).getProgramtype().trim().toLowerCase().contains("internal")) {
//                                        END
                                        UtilsGlobal.store.setLoyaltyEnable(true);
                                        if (response.body().get(0).getIsLoyaltyPointAllowCheckPoint() != null) {
                                            if (response.body().get(0).getIsLoyaltyPointAllowCheckPoint().trim().length() > 0)
                                                if (response.body().get(0).getIsLoyaltyPointAllowCheckPoint().equalsIgnoreCase("y")) {
                                                    isLoyaltyRewardEnable = true;
                                                    UtilsGlobal.store.setLoyaltyEnable(true);
                                                    binding.switch1.setChecked(true);
                                                    UtilsGlobal.store.setIsLoyaltyPointAllowCheckPoint("true");
                                                    binding.lljoinloyalty.setVisibility(View.VISIBLE);
                                                    //UtilsGlobal.store.setIsLoyaltyPointAllowCheckPoint("true");
                                                    if (response.body().get(0).getAllowCustmLoyalltyCheckProgram() != null) {
                                                        if (response.body().get(0).getAllowCustmLoyalltyCheckProgram().equalsIgnoreCase("y")) {
                                                            UtilsGlobal.store.setAllowToJoinUserForLoyaltyProgram(true);
                                                            binding.switch11.setChecked(true);
                                                        } else {
                                                            UtilsGlobal.store.setAllowToJoinUserForLoyaltyProgram(false);
                                                            binding.switch11.setChecked(false);
                                                        }
                                                    } else {
                                                        UtilsGlobal.store.setAllowToJoinUserForLoyaltyProgram(false);
                                                        binding.switch11.setChecked(false);
                                                    }
                                                } else {
                                                    isLoyaltyRewardEnable = false;
                                                    UtilsGlobal.store.setAllowToJoinUserForLoyaltyProgram(false);
                                                    binding.switch1.setChecked(false);
                                                    binding.lljoinloyalty.setVisibility(View.GONE);
                                                    UtilsGlobal.store.setIsLoyaltyPointAllowCheckPoint("false");
                                                }
                                        }
                                    } else {
                                        binding.switch1.setEnabled(false);
                                        binding.switch11.setEnabled(false);
                                        binding.txtloyaltyque1.setTextColor(getResources().getColor(R.color.hintcolor));
                                        binding.txtloyaltyque11.setTextColor(getResources().getColor(R.color.hintcolor));
                                    }
                                } else {
                                    UtilsGlobal.store.setLoyaltyEnable(false);
                                    Log.e("TAG", "onResponse: 94" );
                                    binding.txtloyaltyrewardname.setText("Loyalty rewards not activated at the store!");
                                    binding.txtloyaltyrewardname.setTextColor(getResources().getColor(R.color.disable_title_color));
                                    binding.llloyaltyrewardDetails.setVisibility(View.GONE);
                                }
                                if (response.body().get(0).getIsLoyaltyPointAllowAllCustomers() != null) {
                                    if (response.body().get(0).getIsLoyaltyPointAllowAllCustomers().trim().length() > 0)
                                        if (response.body().get(0).getIsLoyaltyPointAllowAllCustomers().equalsIgnoreCase("y")) {
                                            isLoyaltyRewardAllowAllCustomers = true;
                                            binding.switch2.setChecked(true);
                                            UtilsGlobal.store.setIsLoyaltyPointAllowAllCustomers("true");
                                        } else {
                                            binding.switch2.setChecked(false);
                                            UtilsGlobal.store.setIsLoyaltyPointAllowAllCustomers("false");
                                        }
                                }

                            } else {
                                UtilsGlobal.store.setLoyaltyEnable(false);
                                Log.e("TAG", "onResponse: 93" );
                                binding.txtloyaltyrewardname.setText("Loyalty rewards not activated at the store!");
                                binding.txtloyaltyrewardname.setTextColor(getResources().getColor(R.color.disable_title_color));
                                binding.llloyaltyrewardDetails.setVisibility(View.GONE);
                            }
                        } else {
                            UtilsGlobal.store.setLoyaltyEnable(false);
                            Log.e("TAG", "onResponse: 92" );
                            binding.txtloyaltyrewardname.setText("Loyalty rewards not activated at the store!");
                            binding.txtloyaltyrewardname.setTextColor(getResources().getColor(R.color.disable_title_color));
                            binding.llloyaltyrewardDetails.setVisibility(View.GONE);
                        }
                        if (response.body().get(0).getAuthAllowCustomer() != null) {
                            if (response.body().get(0).getAuthAllowCustomer().trim().length() > 0)
                                if (response.body().get(0).getAuthAllowCustomer().equalsIgnoreCase("y")) {
                                    isCustomerAuthenticationWithCode = true;
                                    binding.switch3.setChecked(true);
                                    UtilsGlobal.store.setIsAllowAuthenticationWithCode("true");
                                } else {
                                    binding.switch3.setChecked(false);
                                    UtilsGlobal.store.setIsAllowAuthenticationWithCode("false");
                                }
                        }
                        if (response.body().get(0).getAllowCustmGiftcardCheck() != null) {
                            if (response.body().get(0).getAllowCustmGiftcardCheck().trim().length() > 0)
                                if (response.body().get(0).getAllowCustmGiftcardCheck().equalsIgnoreCase("y")) {
                                    isAllowGiftCardBalanceCheck = true;
                                    binding.switch4.setChecked(true);
                                    UtilsGlobal.store.setIsAllowGiftCardBalanceCheck("true");
                                } else {
                                    binding.switch4.setChecked(false);
                                    UtilsGlobal.store.setIsAllowGiftCardBalanceCheck("false");
                                }
                        }
                        if (response.body().get(0).getIsInStockCheck_flag() != null) {
                            if (response.body().get(0).getIsInStockCheck_flag().trim().length() > 0)
                                if (response.body().get(0).getIsInStockCheck_flag().equalsIgnoreCase("y")) {
                                    binding.switch8.setChecked(true);
                                    UtilsGlobal.store.setIsInStockCheck(true);
                                    binding.llshowdate.setVisibility(View.VISIBLE);
                                } else {
                                    binding.switch8.setChecked(false);
                                    UtilsGlobal.store.setIsInStockCheck(false);
                                }
                        }
                        if (response.body().get(0).getShowRelatedProducts_flag() != null) {
                            if (response.body().get(0).getShowRelatedProducts_flag().trim().length() > 0)
                                if (response.body().get(0).getShowRelatedProducts_flag().equalsIgnoreCase("y")) {
                                    binding.switch10.setChecked(true);
                                    UtilsGlobal.store.setShowRelatedProducts(true);
                                } else {
                                    binding.switch10.setChecked(false);
                                    UtilsGlobal.store.setShowRelatedProducts(false);
                                }
                        }
                        if (response.body().get(0).getIsExpacteddateforkiosk() != null) {
                            if (response.body().get(0).getIsExpacteddateforkiosk().trim().length() > 0)
                                if (response.body().get(0).getIsExpacteddateforkiosk().equalsIgnoreCase("y")) {
                                    binding.switch9.setChecked(true);
                                    UtilsGlobal.store.setIsNeedToDisplayDate(true);
                                } else {
                                    binding.switch9.setChecked(false);
                                    UtilsGlobal.store.setIsNeedToDisplayDate(false);
                                }
                        }
                        if (response.body().get(0).getStoreType() != null) {
                            if (response.body().get(0).getStoreType().contains("Wine & Spirits Retailers")) {
                                UtilsGlobal.store.setHasWineSpirit(true);
                                if (response.body().get(0).getIsFoodPairingForKiosk() != null) {
                                    if (response.body().get(0).getIsFoodPairingForKiosk().trim().length() > 0) {
                                        if (response.body().get(0).getIsFoodPairingForKiosk().equalsIgnoreCase("Y")) {
                                            UtilsGlobal.store.setHasFoodPairing(true);
                                            binding.switch5.setChecked(true);
                                            binding.textinput1.setVisibility(View.VISIBLE);
                                            try {
                                                binding.edtwebsite.setText(response.body().get(0).getFoodPairingURL());
                                                binding.edtwebsite.setSelection(binding.edtwebsite.getText().toString().length());
                                            } catch (Exception e) {
                                            }
                                        } else {
                                            UtilsGlobal.store.setHasFoodPairing(false);
                                            binding.switch5.setChecked(false);
                                        }
                                    } else {
                                        UtilsGlobal.store.setHasFoodPairing(false);
                                        binding.switch5.setChecked(false);
                                    }
                                } else {
                                    UtilsGlobal.store.setHasFoodPairing(false);
                                    binding.switch5.setChecked(false);
                                }
                                if (response.body().get(0).getIsDrinkRecipesForKiosk() != null) {
                                    if (response.body().get(0).getIsDrinkRecipesForKiosk().trim().length() > 0) {
                                        if (response.body().get(0).getIsDrinkRecipesForKiosk().equalsIgnoreCase("Y")) {
                                            UtilsGlobal.store.setHasDrinkReceipes(true);
                                            binding.switch6.setChecked(true);
                                        } else {
                                            UtilsGlobal.store.setHasDrinkReceipes(false);
                                            binding.switch6.setChecked(false);
                                        }
                                    } else {
                                        UtilsGlobal.store.setHasDrinkReceipes(false);
                                        binding.switch6.setChecked(false);
                                    }
                                } else {
                                    UtilsGlobal.store.setHasDrinkReceipes(false);
                                    binding.switch6.setChecked(false);
                                }

                            } else {
                                UtilsGlobal.store.setHasWineSpirit(false);
                            }
                        } else {
                            UtilsGlobal.store.setHasWineSpirit(false);
                        }
                    } else {
                        UtilsGlobal.store.setLoyaltyEnable(false);
                        Log.e("TAG", "onResponse: 91" );
                        binding.txtloyaltyrewardname.setText("Loyalty rewards not activated at the store!");
                        binding.txtloyaltyrewardname.setTextColor(getResources().getColor(R.color.disable_title_color));
                        binding.llloyaltyrewardDetails.setVisibility(View.GONE);
                    }
                } else {
                    UtilsGlobal.store.setLoyaltyEnable(false);
                    Log.e("TAG", "onResponse: 90" );
                    binding.txtloyaltyrewardname.setText("Loyalty rewards not activated at the store!");
                    binding.txtloyaltyrewardname.setTextColor(getResources().getColor(R.color.disable_title_color));
                    binding.llloyaltyrewardDetails.setVisibility(View.GONE);
                }
                UtilsGlobal.dismissProgressBar();
            }

            @Override
            public void onFailure(Call<ArrayList<Store>> call, Throwable t) {
                call.cancel();
                UtilsGlobal.dismissProgressBar();
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
                            binding.txtgiftcardq1.setTextColor(getResources().getColor(R.color.hintcolor));
                            binding.switch4.setEnabled(false);
                            isGiftCardsEnable = false;
                            UtilsGlobal.store.setGiftCardEnable(false);
                            UtilsGlobal.store.setIsGiftCardAvail("false");
                        } else {
                            isGiftCardsEnable = true;
                            UtilsGlobal.store.setGiftCardEnable(true);
                        }
                        binding.txtgiftcardnames.setText("" + response.body().getResult());
                    } else {
                        UtilsGlobal.store.setGiftCardEnable(false);
                        UtilsGlobal.store.setIsGiftCardAvail("false");
                        binding.txtgiftcardnames.setText("Gift Cards not activated at the store!");
                        binding.txtgiftcardnames.setTextColor(getResources().getColor(R.color.disable_title_color));
                        binding.txtgiftcardnames.setVisibility(View.VISIBLE);
                        binding.llgiftcardsdetail.setVisibility(View.GONE);
                    }

                } else {
                    UtilsGlobal.store.setGiftCardEnable(false);
                    UtilsGlobal.store.setIsGiftCardAvail("false");
                    binding.txtgiftcardnames.setText("Gift Cards not activated at the store!");
                    binding.txtgiftcardnames.setTextColor(getResources().getColor(R.color.disable_title_color));
                    binding.txtgiftcardnames.setVisibility(View.VISIBLE);
                    binding.llgiftcardsdetail.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                call.cancel();
                UtilsGlobal.store.setGiftCardEnable(false);
                UtilsGlobal.store.setIsGiftCardAvail("false");
                binding.txtgiftcardnames.setText("Gift Cards not activated at the store!");
                binding.txtgiftcardnames.setTextColor(getResources().getColor(R.color.disable_title_color));
                binding.txtgiftcardnames.setVisibility(View.VISIBLE);
                binding.llgiftcardsdetail.setVisibility(View.GONE);
            }
        });
    }

    private void updateLoyaltyPointAllowCheck(String storeNum, String isAllowStatus, String isAllowStatusSub) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Result> call1 = apiService.InsertCustomGrantPurchase(storeNum, isAllowStatus, isAllowStatusSub);
        call1.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                call.cancel();

            }
        });
    }

    private void updateFoodAndDrinks(String storeNum, boolean isAllowFoodPairing, boolean isAllowDrikPairing, String foodPairingWebsite, boolean isNext) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Result> call1 = apiService.SaveFoodPairingAndDrinkRecipes(storeNum, String.valueOf(isAllowFoodPairing), String.valueOf(isAllowDrikPairing), foodPairingWebsite);
        call1.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (isNext) {
                    UtilsGlobal.saveStore(GeneralSystemSetupQuestionActivity.this, UtilsGlobal.store);
                    Intent intent = new Intent(GeneralSystemSetupQuestionActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                call.cancel();
                if (isNext) {
                    UtilsGlobal.saveStore(GeneralSystemSetupQuestionActivity.this, UtilsGlobal.store);
                    Intent intent = new Intent(GeneralSystemSetupQuestionActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
    }

    private void updateInventorySettings(boolean stockstatus, boolean showrelated, boolean isshowdate) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Result> call1 = apiService.SaveInventorySettings(UtilsGlobal.store.getId(), String.valueOf(stockstatus), String.valueOf(showrelated), String.valueOf(isshowdate));
        call1.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                call.cancel();

            }
        });
    }

    private void updateLoyaltyPointAllowCheckCustomers(String storeNum, String isAllowStatus) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Result> call1 = apiService.InsertCustomLoyaltyCheckPoint(storeNum, isAllowStatus);
        call1.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.body() != null) {
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                call.cancel();

            }
        });
    }

    private void updateGiftcardPointcheck(String storeNum, String isAllowStatus) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Result> call1 = apiService.InsertCustomGiftCard(storeNum, isAllowStatus);
        call1.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.body() != null) {
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                call.cancel();

            }
        });
    }

    private void updateCustomerAuthentication(String storeNum, String isAllowStatus) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Result> call1 = apiService.InsertAuthAllowCustomer(storeNum, isAllowStatus);
        call1.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.body() != null) {
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                call.cancel();

            }
        });
    }

    private boolean isValidUrl(String url) {
        String WebUrl = "^((ftp|http|https):\\/\\/)?(www.)?(?!.*(ftp|http|https|www.))[a-zA-Z0-9_-]+(\\.[a-zA-Z]+)+((\\/)[\\w#]+)*(\\/\\w+\\?[a-zA-Z0-9_]+=\\w+(&[a-zA-Z0-9_]+=\\w+)*)?$";
        String website = url;
        if (website.trim().length() > 0) {
            if (!website.matches(WebUrl)) {
                //validation msg
                return false;
            }
        }
        return true;
    }

    private void funForWorkingPingInternet() {
        {
            //popup update
            ScheduledExecutorService exec3 = Executors.newSingleThreadScheduledExecutor();
            exec3.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    // do stuff
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!UtilsGlobal.isNetworkAvailable(GeneralSystemSetupQuestionActivity.this)) {
                                retryCount++;
                                if (retryCount == 1) {
                                    binding.errorpopup.ivprogress.setVisibility(View.VISIBLE);
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
                                    binding.errorpopup.ivprogress.setVisibility(View.VISIBLE);
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

    @Subscribe
    public void getMessage(InternetCheck internetAvail) {
        //Write code to perform action after event is received.
        try {
            if (!internetAvail.isConnected) {
                if (!UtilsGlobal.isNetworkAvailable(GeneralSystemSetupQuestionActivity.this)) {
                    isApiCalled=false;
                    UtilsGlobal.dismissProgressBar();
                    funForWorkingPingInternet();
                }
            } else {
                if(!isApiCalled) {
                    if (UtilsGlobal.isNetworkAvailable(GeneralSystemSetupQuestionActivity.this)) {
                        retryCount = 0;
                        isApiCalled=true;
                        UtilsGlobal.showProgressBar(GeneralSystemSetupQuestionActivity.this);
                        binding.llpopup.setVisibility(View.GONE);
                        //  callStoreLoyaltyPointCheckAPI(UtilsGlobal.store.getId());
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 5s = 5000ms
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callStoreLoyaltyPointCheckAPI(UtilsGlobal.store.getId());
                                    }
                                });
                            }
                        }, 2500);
                    }
                }

            }
        } catch (Exception e) {
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            //unregisterReceiver(new NetworkReceiver());
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

    @Override
    protected void onStart() {
        super.onStart();
       /* IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new NetworkReceiver(), intentFilter);*/
    }
}