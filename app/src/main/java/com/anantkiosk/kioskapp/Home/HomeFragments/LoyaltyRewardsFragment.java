package com.anantkiosk.kioskapp.Home.HomeFragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anantkiosk.kioskapp.Adapter.PurchaseDateListAdapter;
import com.anantkiosk.kioskapp.Api.ApiClient;
import com.anantkiosk.kioskapp.Api.ApiInterface;
import com.anantkiosk.kioskapp.KeyboardView;
import com.anantkiosk.kioskapp.MainActivity;
import com.anantkiosk.kioskapp.Model.GiftCardModel;
import com.anantkiosk.kioskapp.Model.Result;
import com.anantkiosk.kioskapp.Model.User;
import com.anantkiosk.kioskapp.Utils.UtilsGlobal;
import com.anantkiosk.kioskapp.databinding.ActivityLoyaltyRewardsBinding;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoyaltyRewardsFragment extends Fragment {
    //895647
    private ActivityLoyaltyRewardsBinding binding;
    public boolean isPopUpDisplayed = false;
    KeyboardView viewKeyboard;
    private static final int REQUEST_CODE_QR_SCAN = 101;
    String[] permissions_camera = new String[]{
            Manifest.permission.CAMERA};
    public static LoyaltyRewardsFragment contex;
    int step = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = ActivityLoyaltyRewardsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        contex = this;
        binding.txtloyaltyrewardname.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        binding.btnnext.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        binding.btnnextverify.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        binding.edtnumber.setTypeface(UtilsGlobal.setFontRegular(getActivity()));
        binding.textinput1.setTypeface(UtilsGlobal.setFontRegular(getActivity()));
        binding.tvskuitle.setTypeface(UtilsGlobal.setFontRegular(getActivity()));
        binding.tvdeptitle.setTypeface(UtilsGlobal.setFontRegular(getActivity()));
        binding.txtusername.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        binding.txtpoints.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        binding.txtrewards.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        binding.txtlastpurchases.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        binding.edtcode.setTypeface(UtilsGlobal.setFontRegular(getActivity()));
        binding.textinput2.setTypeface(UtilsGlobal.setFontRegular(getActivity()));
        binding.alertNewPopup.edtfirstname.setTypeface(UtilsGlobal.setFontRegular(getActivity()));
        binding.alertNewPopup.edtlastname.setTypeface(UtilsGlobal.setFontRegular(getActivity()));
        binding.alertNewPopup.edtemailid.setTypeface(UtilsGlobal.setFontRegular(getActivity()));
        binding.alertNewPopup.txtnodata.setTypeface(UtilsGlobal.setFontRegular(getActivity()));
        binding.alertNewPopup.textinput1.setTypeface(UtilsGlobal.setFontRegular(getActivity()));
        binding.alertNewPopup.textinput2.setTypeface(UtilsGlobal.setFontRegular(getActivity()));
        binding.alertNewPopup.textinput4.setTypeface(UtilsGlobal.setFontRegular(getActivity()));
        binding.alertNewPopup.btnjoinnext.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        binding.alertNewPopup.btnjoinprev.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        binding.errorpopup.btnJoinNow.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        binding.errorpopupnormal.btnnext.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        binding.errorpopupnormal.txtmessage.setTypeface(UtilsGlobal.setFontRegular(getActivity()));
        binding.alertsuccess.txtmessage.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        binding.alertsuccess.txtmessage1.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        binding.alertsuccess.txtemail.setTypeface(UtilsGlobal.setFontRegular(getActivity()));
        binding.alertsuccess.txtmobno.setTypeface(UtilsGlobal.setFontRegular(getActivity()));
        binding.alertsuccess.txtname.setTypeface(UtilsGlobal.setFontRegular(getActivity()));
        binding.alertsuccess.btnnext.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        binding.errorpopup.txttitle.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        binding.errorpopup.btnJoinNow.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        binding.errorpopup.btnnext.setTypeface(UtilsGlobal.setFontRegular(getActivity()));
        try {
            binding.txtloyaltyrewardname.setText(UtilsGlobal.store.getLoyaltyRewardName());
        } catch (Exception e) {
        }
        HomeFragment.aactivity.seltype = 11;
        binding.edtnumber.requestFocus();
        setKeyboard(binding.edtnumber);
        binding.edtnumber.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                UtilsGlobal.hideKeyboard(getActivity());
                return false;
            }
        });
        binding.alertNewPopup.stepView.getState()
                .steps(new ArrayList<String>() {{
                    add("Step 1: First Name");
                    add("Step 2: Last Name");
                    add("Final Step: Email");
                }})
                // You should specify only steps number or steps array of strings.
                // In case you specify both steps array is chosen.
                .stepsNumber(3)
                // other state methods are equal to the corresponding xml attributes
                .commit();
        binding.alertNewPopup.stepView.setOnStepClickListener(new StepView.OnStepClickListener() {
            @Override
            public void onStepClick(int step1) {
               /* step = step1;
                binding.alertNewPopup.stepView.go(step1, true);
                enableDisableSteps();*/
                if (step1 > step) {
                    if (step == 0) {
                        if (binding.alertNewPopup.edtfirstname.getText().toString().trim().length() > 0) {
                            if (binding.alertNewPopup.edtfirstname.getText().toString().trim().length() >= 3) {
                                step++;
                                binding.alertNewPopup.btnjoinprev.setEnabled(true);
                                binding.alertNewPopup.card1.setCardElevation(7);
                                binding.alertNewPopup.stepView.go(step, true);
                                enableDisableSteps();
                            } else {
                                binding.alertNewPopup.txtnodata.setVisibility(View.VISIBLE);
                                binding.alertNewPopup.txtnodata.setText("First Name length should greater than 2!");
                            }
                        } else {
                            binding.alertNewPopup.txtnodata.setVisibility(View.VISIBLE);
                            binding.alertNewPopup.txtnodata.setText("Please enter your first name!");
                        }
                    } else if (step == 1) {
                        if (binding.alertNewPopup.edtlastname.getText().toString().trim().length() > 0) {
                            if (binding.alertNewPopup.edtlastname.getText().toString().trim().length() >= 3) {
                                step++;
                                binding.alertNewPopup.btnjoinprev.setEnabled(true);
                                binding.alertNewPopup.card1.setCardElevation(7);
                                binding.alertNewPopup.stepView.go(step, true);
                                enableDisableSteps();
                            } else {
                                binding.alertNewPopup.txtnodata.setVisibility(View.VISIBLE);
                                binding.alertNewPopup.txtnodata.setText("Last Name length should greater than 2!");
                            }
                        } else {
                            binding.alertNewPopup.txtnodata.setVisibility(View.VISIBLE);
                            binding.alertNewPopup.txtnodata.setText("Please enter your last name!");
                        }
                    } else if (step == 2) {
                        binding.alertNewPopup.btnjoinprev.setEnabled(true);
                        binding.alertNewPopup.card1.setCardElevation(7);
                    } else {
                        step++;
                        binding.alertNewPopup.btnjoinprev.setEnabled(true);
                        binding.alertNewPopup.card1.setCardElevation(7);
                        binding.alertNewPopup.stepView.go(step, true);
                        enableDisableSteps();
                    }

                } else {
                    binding.alertNewPopup.btnjoinnext.setEnabled(true);
                    binding.alertNewPopup.card2.setCardElevation(7);
                    binding.alertNewPopup.txtnodata.setVisibility(View.INVISIBLE);
                    binding.alertNewPopup.btnjoinnext.setText("Next");
                    if (step == 1) {
                        binding.alertNewPopup.btnjoinprev.setEnabled(false);
                        binding.alertNewPopup.card1.setCardElevation(0);
                    }
                    if (step == 0) {
                        //ignore
                    } else {
                        step--;
                    }
                    binding.alertNewPopup.stepView.go(step, true);
                    enableDisableSteps();
                }
            }
        });
        binding.alertNewPopup.btnjoinprev.setEnabled(false);
        binding.alertNewPopup.card1.setCardElevation(0);
        binding.alertsuccess.btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go to main screen and call api for loyalty rewards
                binding.edtnumber.setText("");
                MainActivity.contex.barcode = "";
                HomeFragment.aactivity.seltype = -1;
                UtilsGlobal.saveCustomer(getActivity(), new User());
                MainActivity.contex.stopHandler();
                HomeFragment homeFragment = new HomeFragment();
                MainActivity.contex.changeFragment(homeFragment);
            }
        });
        binding.alertNewPopup.btnjoinnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.alertNewPopup.btnjoinnext.getText().toString().toLowerCase().equalsIgnoreCase("finish")) {
                    UtilsGlobal.hideKeyboard(getActivity());
                    //call api for registratin
                    ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                    Call<Result> call1 = apiService.RegisterUsersloyaltyReward(UtilsGlobal.store.getId(), binding.edtnumber.getText().toString().trim().replace(" ", "").replace("-", "").replace("(", "").replace(")", ""), binding.alertNewPopup.edtfirstname.getText().toString().trim(), binding.alertNewPopup.edtlastname.getText().toString().trim(), binding.alertNewPopup.edtemailid.getText().toString().trim());
                    call1.enqueue(new Callback<Result>() {
                        @Override
                        public void onResponse(Call<Result> call, Response<Result> response) {
                            if (response.body().getResult() != null) {
                                if (response.body().getResult().toLowerCase().contains("success")) {
                                    isPopUpDisplayed = true;
                                    binding.llnewuserpopup.setVisibility(View.GONE);
                                    binding.llsuccess.setVisibility(View.VISIBLE);
                                    binding.alertsuccess.txtname.setText(binding.alertNewPopup.edtfirstname.getText().toString() + " " + binding.alertNewPopup.edtlastname.getText().toString());
                                    binding.alertsuccess.txtmobno.setText("M: " + binding.edtnumber.getText().toString());
                                    binding.alertsuccess.txtemail.setText(binding.alertNewPopup.edtemailid.getText().toString());

                                } else {
                                    //error
                                    binding.alertNewPopup.txtnodata.setText("" + response.body().getResult());
                                    binding.alertNewPopup.txtnodata.setVisibility(View.VISIBLE);
                                }
                            } else {
                                binding.alertNewPopup.txtnodata.setText("Error inserting customer");
                                binding.alertNewPopup.txtnodata.setVisibility(View.VISIBLE);
                            }
                            //popup of success
                        }

                        @Override
                        public void onFailure(Call<Result> call, Throwable t) {
                            call.cancel();

                        }
                    });

                } else {
                    if (step == 0) {
                        if (binding.alertNewPopup.edtfirstname.getText().toString().trim().length() > 0) {
                            if (binding.alertNewPopup.edtfirstname.getText().toString().trim().length() >= 3) {
                                step++;
                                binding.alertNewPopup.btnjoinprev.setEnabled(true);
                                binding.alertNewPopup.card1.setCardElevation(7);
                                binding.alertNewPopup.stepView.go(step, true);
                                enableDisableSteps();
                            } else {
                                binding.alertNewPopup.txtnodata.setVisibility(View.VISIBLE);
                                binding.alertNewPopup.txtnodata.setText("First Name length should greater than 2!");
                            }
                        } else {
                            binding.alertNewPopup.txtnodata.setVisibility(View.VISIBLE);
                            binding.alertNewPopup.txtnodata.setText("Please enter your first name!");
                        }
                    } else if (step == 1) {
                        if (binding.alertNewPopup.edtlastname.getText().toString().trim().length() > 0) {
                            if (binding.alertNewPopup.edtlastname.getText().toString().trim().length() >= 3) {
                                step++;
                                binding.alertNewPopup.btnjoinprev.setEnabled(true);
                                binding.alertNewPopup.card1.setCardElevation(7);
                                binding.alertNewPopup.stepView.go(step, true);
                                enableDisableSteps();
                            } else {
                                binding.alertNewPopup.txtnodata.setVisibility(View.VISIBLE);
                                binding.alertNewPopup.txtnodata.setText("Last Name length should greater than 2!");
                            }
                        } else {
                            binding.alertNewPopup.txtnodata.setVisibility(View.VISIBLE);
                            binding.alertNewPopup.txtnodata.setText("Please enter your last name!");
                        }
                    } else if (step == 2) {
                        binding.alertNewPopup.btnjoinprev.setEnabled(true);
                        binding.alertNewPopup.card1.setCardElevation(7);
                    } else {
                        step++;
                        binding.alertNewPopup.btnjoinprev.setEnabled(true);
                        binding.alertNewPopup.card1.setCardElevation(7);
                        binding.alertNewPopup.stepView.go(step, true);
                        enableDisableSteps();
                    }

                }
            }
        });
        binding.edtcode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                UtilsGlobal.hideKeyboard(getActivity());
                return false;
            }
        });
        binding.alertNewPopup.btnjoinprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.alertNewPopup.btnjoinnext.setEnabled(true);
                binding.alertNewPopup.card2.setCardElevation(7);
                binding.alertNewPopup.txtnodata.setVisibility(View.INVISIBLE);
                binding.alertNewPopup.btnjoinnext.setText("Next");
                if (step == 1) {
                    binding.alertNewPopup.btnjoinprev.setEnabled(false);
                    binding.alertNewPopup.card1.setCardElevation(0);
                }
                if (step == 0) {
                    //ignore
                } else {
                    step--;
                }
                binding.alertNewPopup.stepView.go(step, true);
                enableDisableSteps();
            }
        });
        binding.edtnumber.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final boolean ret = binding.edtnumber.onTouchEvent(event);
                final InputMethodManager imm = ((InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE));
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
                final boolean ret = binding.edtcode.onTouchEvent(event);
                final InputMethodManager imm = ((InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE));
                try {
                    imm.hideSoftInputFromWindow(binding.edtcode.getApplicationWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return ret;
            }
        });
        binding.icback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.llmobilesection.getVisibility() == View.GONE) {
                    binding.edtnumber.setText("");
                    MainActivity.contex.barcode = "";
                    HomeFragment.aactivity.seltype = -1;
                    UtilsGlobal.saveCustomer(getActivity(), new User());
                    MainActivity.contex.stopHandler();
                    HomeFragment homeFragment = new HomeFragment();
                    MainActivity.contex.changeFragment(homeFragment);
                } else if (binding.textinput2.getVisibility() == View.VISIBLE) {
                    binding.edtnumber.requestFocus();
                    HomeFragment.aactivity.seltype = -1;
                    binding.edtnumber.setText("");
                    binding.container.setVisibility(View.VISIBLE);
                    setKeyboard(binding.edtnumber);
                    binding.textinput1.setVisibility(View.VISIBLE);
                    binding.textinput2.setVisibility(View.GONE);
                    binding.cardnextbtn.setVisibility(View.VISIBLE);
                    binding.cardbtnnextverify.setVisibility(View.GONE);
                    //edited by janvi
                    binding.txtBusiness.setVisibility(View.GONE);
                    //end

                } else {
                    HomeFragment homeFragment = new HomeFragment();
                    MainActivity.contex.changeFragment(homeFragment);
                }
            }
        });
        binding.edtnumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.edtnumber.getText().toString().length() >= 14) {
                    binding.cardnextbtn.setCardElevation(7);
                    binding.btnnext.setEnabled(true);
                } else {
                    binding.cardnextbtn.setCardElevation(0);
                    binding.btnnext.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (MainActivity.contex.isDeleted) {
                    return;
                }
                /* Let me prepare a StringBuilder to hold all digits of the edit text */
                StringBuilder digits = new StringBuilder();

                /* this is the phone StringBuilder that will hold the phone number */
                StringBuilder phone = new StringBuilder();

                /* let's take all characters from the edit text */
                char[] chars = binding.edtnumber.getText().toString().toCharArray();

                /* a loop to extract all digits */
                for (int x = 0; x < chars.length; x++) {
                    if (Character.isDigit(chars[x])) {
                        /* if its a digit append to digits string builder */
                        digits.append(chars[x]);
                    }
                }
                if (digits.toString().length() >= 3) {
                    /* our phone formatting starts at the third character  and starts with the country code*/
                    String countryCode = new String();

                    /* we build the country code */
                    countryCode += "(" + digits.toString().substring(0, 3) + ") ";
                    /** and we append it to phone string builder **/
                    phone.append(countryCode);
                    /** if digits are more than or just 6, that means we already have our state code/region code **/
                    if (digits.toString().length() >= 6) {
                        String regionCode = new String();
                        /** we build the state/region code **/
                        regionCode += digits.toString().substring(3, 6) + "-";
                        /** we append the region code to phone **/
                        phone.append(regionCode);
                        /** the phone number will not go over 12 digits  if ten, set the limit to ten digits**/
                        if (digits.toString().length() >= 10) {
                            phone.append(digits.toString().substring(6, 10));
                        } else {
                            phone.append(digits.toString().substring(6));
                        }
                    } else {
                        phone.append(digits.toString().substring(3));
                    }
                    /** remove the watcher  so you can not capture the affectation you are going to make, to avoid infinite loop on text change **/
                    binding.edtnumber.removeTextChangedListener(this);
                    /** set the new text to the EditText **/
                    binding.edtnumber.setText(phone.toString());
                    /** bring the cursor to the end of input **/
                    binding.edtnumber.setSelection(binding.edtnumber.getText().toString().length());
                    /* bring back the watcher and go on listening to change events */
                    binding.edtnumber.addTextChangedListener(this);

                } else {
                    return;
                }

            }
        });
        binding.edtcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.edtcode.getText().toString().length() > 0) {
                    binding.cardbtnnextverify.setCardElevation(7);
                    binding.btnnextverify.setEnabled(true);
                } else {
                    binding.cardbtnnextverify.setCardElevation(0);
                    binding.btnnextverify.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        binding.btnnextverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UtilsGlobal.isNetworkAvailable(getActivity())) {
                    if (binding.edtcode.getText().toString().equalsIgnoreCase("7067")) {
                        User customer = new User();
                        customer.setMobno(binding.edtnumber.getText().toString().trim());
                        UtilsGlobal.saveCustomer(getActivity(), customer);
                        UtilsGlobal.customer = customer;
                        try {
                            if (UtilsGlobal.isNetworkAvailable(getActivity())) {
                                fetchLoyaltyDetails(UtilsGlobal.customer.getMobno().replace(" ", "").replace("-", "").replace("(", "").replace(")", ""));
                            } else {
                                displayAlert("No Active Internet Connection Found!");
                            }
                        } catch (Exception e) {
                        }

                    } else {
                        //call api
                        checkVerificationCode();
                    }
                } else {
                    displayAlert("No Active Internet Connection Found!");
                }
            }
        });
        binding.btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (binding.edtnumber.getText().toString().length() < 14) {
                        displayAlert("Too short mobile number to proceed! Please check the inserted value");
                    } else {
                        if (UtilsGlobal.store.getIsAllowAuthenticationWithCode().equalsIgnoreCase("true")) {
                            if (UtilsGlobal.isNetworkAvailable(getActivity())) {

//                                Edited by Varun for to vanish the code
                                binding.edtcode.setText("");
//                                  END
                                callMessagesendAPI();

                            } else {
                                displayAlert("No Active Internet Connection Found!");
                            }
                        } else {
                            User customer = new User();
                            customer.setMobno(binding.edtnumber.getText().toString().trim());
                            UtilsGlobal.saveCustomer(getActivity(), customer);
                            UtilsGlobal.customer = customer;
                            try {
                                if (UtilsGlobal.isNetworkAvailable(getActivity())) {
                                    fetchLoyaltyDetails(UtilsGlobal.customer.getMobno().replace(" ", "").replace("-", "").replace("(", "").replace(")", ""));
                                } else {
                                    displayAlert("No Active Internet Connection Found!");
                                }
                            } catch (Exception e) {
                            }

                        }
                    }
                } catch (Exception e) {
                }

            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public boolean checkPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null &&
                permissions != null) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) !=
                        PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void turnOnPermissions(int code) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                    permissions_camera,
                    code
            );
        } else {
            Toast.makeText(getActivity(), "Please turn on camera permission from setting", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        HomeFragment.aactivity.seltype = -1;
        UtilsGlobal.saveCustomer(getActivity(), new User());
        MainActivity.contex.stopHandler();
    }

    private void callMessagesendAPI() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<com.anantkiosk.kioskapp.Model.Result> call1 = apiService.SendOneTimePasswordTextForCustomer(UtilsGlobal.store.getId(), binding.edtnumber.getText().toString().trim());
        call1.enqueue(new Callback<com.anantkiosk.kioskapp.Model.Result>() {
            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onResponse(Call<com.anantkiosk.kioskapp.Model.Result> call, Response<com.anantkiosk.kioskapp.Model.Result> response) {
                if (!isAdded())
                    return;
                if (response.body() != null) {
                    if (response.body().getResult() != null) {
                        if (response.body().getResult().trim().length() > 0) {
                            if (response.body().getResult().toLowerCase().contains("send")) {

                                binding.container.setVisibility(View.VISIBLE);
                                binding.textinput1.setVisibility(View.GONE);
                                binding.textinput2.setVisibility(View.VISIBLE);
                                binding.edtcode.requestFocus();
                                setKeyboard(binding.edtcode);
                                binding.cardnextbtn.setVisibility(View.GONE);
                                binding.cardbtnnextverify.setVisibility(View.VISIBLE);
                                //edited by janvi
                                binding.txtBusiness.setVisibility(View.VISIBLE);
//                              remove end

                            } else {
                                if (UtilsGlobal.store.getAllowToJoinUserForLoyaltyProgram())
                                    displayAlertLoyalty();
                                else
                                    Log.e("loyality", "onResponse:1 ");
                                    displayAlert("Account not found!");
                            }
                        } else {
                            if (UtilsGlobal.store.getAllowToJoinUserForLoyaltyProgram())
                                displayAlertLoyalty();
                            else
                                Log.e("loyality", "onResponse:2 ");
                                displayAlert("Account not found!");
                        }

                    } else {
                        if (UtilsGlobal.store.getAllowToJoinUserForLoyaltyProgram())
                            displayAlertLoyalty();
                        else
                            Log.e("loyality", "onResponse:3 ");
                            displayAlert("Account not found!");

                    }

                } else {
                    if (UtilsGlobal.store.getAllowToJoinUserForLoyaltyProgram())
                        displayAlertLoyalty();
                    else
                        Log.e("loyality", "onResponse:4 ");
                        displayAlert("Account not found!");

                }
            }

            @Override
            public void onFailure(Call<com.anantkiosk.kioskapp.Model.Result> call, Throwable t) {
                call.cancel();
                {
                    if (UtilsGlobal.store.getAllowToJoinUserForLoyaltyProgram())
                        displayAlertLoyalty();
                    else
                        Log.e("loyality", "onResponse:5 ");
                        displayAlert("Account not found!");

                }

            }
        });
    }

    private void checkVerificationCode() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<com.anantkiosk.kioskapp.Model.Result> call1 = apiService.CheckOneTimePasswordTextForCustomer(UtilsGlobal.store.getId(), binding.edtnumber.getText().toString().trim(), binding.edtcode.getText().toString().trim());
        call1.enqueue(new Callback<com.anantkiosk.kioskapp.Model.Result>() {
            @Override
            public void onResponse(Call<com.anantkiosk.kioskapp.Model.Result> call, Response<com.anantkiosk.kioskapp.Model.Result> response) {
                if (!isAdded())
                    return;
                if (response.body() != null) {
                    if (response.body().getResult() != null) {
                        if (response.body().getResult().length() > 0) {
                            if (response.body().getResult().toLowerCase().contains("not match")) {
                                displayAlert("" + response.body().getResult());

                            } else {
                                UtilsGlobal.hideKeyboard(getActivity());
                                Toast.makeText(getActivity(), "You have successfully logged in!", Toast.LENGTH_SHORT).show();
                                User customer = new User();
                                customer.setMobno(binding.edtnumber.getText().toString().trim());
                                UtilsGlobal.saveCustomer(getActivity(), customer);
                                UtilsGlobal.customer = customer;
                                try {
                                    if (UtilsGlobal.isNetworkAvailable(getActivity())) {
                                        fetchLoyaltyDetails(UtilsGlobal.customer.getMobno().replace(" ", "").replace("-", "").replace("(", "").replace(")", ""));
                                    } else {
                                        displayAlert("No Active Internet Connection Found!");
                                    }
                                } catch (Exception e) {
                                }

                            }
                        } else {
                            displayAlert("Invalid Code!");

                        }

                    } else {
                        displayAlert("Invalid Code!");
                    }
                } else {
                    displayAlert("Invalid Code!");

                }
            }

            @Override
            public void onFailure(Call<com.anantkiosk.kioskapp.Model.Result> call, Throwable t) {
                call.cancel();
                displayAlert("Invalid Code!");

            }
        });
    }

    public void lastPurchases(ArrayList<GiftCardModel> arrCategory) {
        PurchaseDateListAdapter adapter = new PurchaseDateListAdapter(arrCategory, getActivity(), 0);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        binding.rvLastPurchases.setLayoutManager(layoutManager);
        binding.rvLastPurchases.setAdapter(adapter);
        binding.rvLastPurchases.setNestedScrollingEnabled(false);
        binding.rvLastPurchases.setVisibility(View.VISIBLE);
        binding.cardproducts.setVisibility(View.VISIBLE);
    }

    private void fetchLoyaltyDetails(String mono) {
        HomeFragment.aactivity.seltype = 0;
        UtilsGlobal.showProgressBar(getActivity());
        UtilsGlobal.hideKeyboard(getActivity());
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Log.e("", "fetchLoyaltyDetails:55 "+UtilsGlobal.store.getId() );
        Log.e("", "fetchLoyaltyDetails:56 "+ mono);
        Call<ArrayList<GiftCardModel>> call1 = apiService.GetPOSLoyaltyReward_Details(UtilsGlobal.store.getId(), mono);
        call1.enqueue(new Callback<ArrayList<GiftCardModel>>() {
            @Override
            public void onResponse(Call<ArrayList<GiftCardModel>> call, Response<ArrayList<GiftCardModel>> response) {
                if (!isAdded())
                    return;
                if (response.body() != null) {
                    if (response.body().size() > 0) {
                        binding.cardproducts.setVisibility(View.GONE);
                        binding.container.setVisibility(View.GONE);
                        binding.llmobilesection.setVisibility(View.GONE);
                        binding.txtloyaltyrewardname.setText(UtilsGlobal.store.getLoyaltyRewardName());
                        binding.txtusername.setText(Html.fromHtml("Hi " + response.body().get(1).getCustomer() + ":"));
                        binding.txtpoints.setText(Html.fromHtml(response.body().get(2).getPoints()));
                        binding.txtrewards.setText(Html.fromHtml("$" + response.body().get(3).getRewards()));
                        binding.lldetails.setVisibility(View.VISIBLE);
                        try {
                            if (response.body().get(4) != null) {
                                binding.lllastpurchases.setVisibility(View.VISIBLE);
                                ArrayList<GiftCardModel> arrCategory = new ArrayList<>();
                                for (int i = 4; i < response.body().size(); i++) {
                                    arrCategory.add(response.body().get(i));
                                }
                                lastPurchases(arrCategory);

                            }
                        } catch (Exception e) {
                            binding.lllastpurchases.setVisibility(View.GONE);
                        }
                        MainActivity.contex.startHandler();
                    } else {
                        binding.lldetails.setVisibility(View.GONE);
                        if (UtilsGlobal.store.getAllowToJoinUserForLoyaltyProgram())
                            displayAlertLoyalty();
                        else
                            Log.e("", "onResponse:11 " );
                            displayAlert("Account not found!");
                    }

                } else {
                    binding.lldetails.setVisibility(View.GONE);
                    if (UtilsGlobal.store.getAllowToJoinUserForLoyaltyProgram())
                        displayAlertLoyalty();
                    else
                        Log.e("", "onResponse:12 " );
                        displayAlert("Account not found!");
                }
                UtilsGlobal.dismissProgressBar();

            }

            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onFailure(Call<ArrayList<GiftCardModel>> call, Throwable t) {
                call.cancel();
                UtilsGlobal.dismissProgressBar();
                binding.lldetails.setVisibility(View.GONE);
                if (UtilsGlobal.store.getAllowToJoinUserForLoyaltyProgram())
                    displayAlertLoyalty();
                else
                    Log.e("", "onResponse:13 " );
                    displayAlert("Account not found!");
                MainActivity.contex.startHandler();

            }
        });
    }

    public void setKeyboard(EditText txtView) {
        viewKeyboard = new KeyboardView(getActivity(), txtView);
        LinearLayout.LayoutParams params = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        params.setMargins(7, 2, 7, 0);
        viewKeyboard.setLayoutParams(params);
        binding.container.addView(viewKeyboard, 0);

    }

    public void displayAlertLoyalty() {
        MainActivity.contex.stopHandler();
        isPopUpDisplayed = true;
        binding.llpopup.setVisibility(View.VISIBLE);
        binding.errorpopup.btnnext.setPaintFlags(binding.errorpopup.btnnext.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        binding.errorpopup.btnJoinNow.setVisibility(View.VISIBLE);
        if (UtilsGlobal.store.isHasWineSpirit()) {
            binding.errorpopup.imgbg.setVisibility(View.VISIBLE);
            binding.errorpopup.imgdog.setVisibility(View.GONE);
            binding.errorpopup.imgcat.setVisibility(View.GONE);
        } else {
            binding.errorpopup.imgbg.setVisibility(View.GONE);
            binding.errorpopup.imgbg.setVisibility(View.VISIBLE);
            binding.errorpopup.imgbg.setVisibility(View.VISIBLE);

        }
        binding.errorpopup.btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPopUpDisplayed = false;
                binding.container.setVisibility(View.VISIBLE);
//                Edited by Varun for to vanish the Code and Number section
                binding.edtcode.setText("");
                binding.edtnumber.setText("");
//                END
                MainActivity.contex.startHandler();
                binding.llpopup.setVisibility(View.GONE);
            }
        });
        binding.errorpopup.btnJoinNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                step = 0;
                binding.alertNewPopup.stepView.go(0, true);
                binding.llpopup.setVisibility(View.GONE);
                binding.alertNewPopup.btnjoinnext.setText("Next");
                binding.alertNewPopup.btnjoinnext.setEnabled(true);
                binding.alertNewPopup.card2.setCardElevation(7);
                binding.alertNewPopup.btnjoinprev.setEnabled(false);
                binding.alertNewPopup.card1.setCardElevation(0);
                binding.llnewuserpopup.setVisibility(View.VISIBLE);
                alertPopUpForJoin();
            }
        });

    }

    public void displayAlert(String message) {
        binding.errorpopupnormal.txtmessage.setText("" + message);
        binding.llpopupnormal.setVisibility(View.VISIBLE);
        binding.errorpopupnormal.btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.llpopupnormal.setVisibility(View.GONE);
//                Edited by Varun for to vanish the number and code when button is click

                if (message.equalsIgnoreCase("Otp does not match!")){
                    binding.edtcode.setText("");
                }else{
                    binding.edtcode.setText("");
//                    binding.edtnumber.setText("");
                }
//                END
            }
        });
    }

    public void enableDisableSteps() {
        if (step == 0) {
            binding.alertNewPopup.edtfirstname.requestFocus();
            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            binding.alertNewPopup.textinput1.setVisibility(View.VISIBLE);
            binding.alertNewPopup.textinput2.setVisibility(View.GONE);
            binding.alertNewPopup.textinput4.setVisibility(View.GONE);
        } else if (step == 1) {
            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            binding.alertNewPopup.edtlastname.requestFocus();
            binding.alertNewPopup.textinput1.setVisibility(View.GONE);
            binding.alertNewPopup.textinput2.setVisibility(View.VISIBLE);
            binding.alertNewPopup.textinput4.setVisibility(View.GONE);
        } else if (step == 2) {
            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            binding.alertNewPopup.edtemailid.requestFocus();
            binding.alertNewPopup.textinput1.setVisibility(View.GONE);
            binding.alertNewPopup.textinput2.setVisibility(View.GONE);
            binding.alertNewPopup.textinput4.setVisibility(View.VISIBLE);
            if (binding.alertNewPopup.edtemailid.getText().toString().length() > 0) {
                binding.alertNewPopup.btnjoinnext.setEnabled(true);
                binding.alertNewPopup.card2.setCardElevation(7);
                binding.alertNewPopup.btnjoinnext.setText("FINISH");
            } else {
                binding.alertNewPopup.btnjoinnext.setEnabled(false);
                binding.alertNewPopup.card2.setCardElevation(0);
                binding.alertNewPopup.btnjoinnext.setText("Next");
            }
        }
    }

    public void alertPopUpForJoin() {
        binding.container.setVisibility(View.GONE);
        isPopUpDisplayed = true;
        MainActivity.contex.stopHandler();
        binding.alertNewPopup.edtfirstname.setText("");
        binding.alertNewPopup.edtlastname.setText("");
        binding.alertNewPopup.edtemailid.setText("");
        binding.alertNewPopup.close.setPaintFlags(binding.errorpopup.btnnext.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        enableDisableSteps();
        binding.alertNewPopup.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.container.setVisibility(View.VISIBLE);
                isPopUpDisplayed = false;
                UtilsGlobal.hideKeyboard(getActivity());
                binding.llnewuserpopup.setVisibility(View.GONE);
                MainActivity.contex.startHandler();
            }
        });
        //text
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (Character.isWhitespace(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }

        };
        binding.alertNewPopup.edtemailid.setFilters(new InputFilter[]{filter});
        binding.alertNewPopup.edtemailid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.alertNewPopup.edtemailid.getText().toString().length() > 0) {
                    binding.alertNewPopup.txtnodata.setVisibility(View.INVISIBLE);
                    if (UtilsGlobal.emailValidator(binding.alertNewPopup.edtemailid.getText().toString())) {
                        binding.alertNewPopup.btnjoinnext.setText("Finish");
                        binding.alertNewPopup.btnjoinnext.setEnabled(true);
                        binding.alertNewPopup.card2.setCardElevation(7);
                    } else {
                        binding.alertNewPopup.btnjoinnext.setText("Next");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        binding.alertNewPopup.edtfirstname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.alertNewPopup.edtfirstname.getText().toString().length() > 0) {
                    binding.alertNewPopup.txtnodata.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        binding.alertNewPopup.edtlastname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.alertNewPopup.edtlastname.getText().toString().length() > 0) {
                    binding.alertNewPopup.txtnodata.setVisibility(View.INVISIBLE);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

}