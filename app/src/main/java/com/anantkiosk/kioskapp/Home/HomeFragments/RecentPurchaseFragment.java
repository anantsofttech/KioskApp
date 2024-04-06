package com.anantkiosk.kioskapp.Home.HomeFragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anantkiosk.kioskapp.Adapter.PurchaseDateListAdapter;
import com.anantkiosk.kioskapp.Api.ApiClient;
import com.anantkiosk.kioskapp.Api.ApiInterface;
import com.anantkiosk.kioskapp.KeyboardView;
import com.anantkiosk.kioskapp.MainActivity;
import com.anantkiosk.kioskapp.Model.GiftCardModel;
import com.anantkiosk.kioskapp.Model.User;
import com.anantkiosk.kioskapp.Utils.UtilsGlobal;
import com.anantkiosk.kioskapp.databinding.FragmentRecentpurchasehistoryBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecentPurchaseFragment extends Fragment {

    private FragmentRecentpurchasehistoryBinding binding;
    public boolean isTouch = false;
    KeyboardView viewKeyboard;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRecentpurchasehistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.txttitle.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        binding.btnnext.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        binding.edtnumber.setTypeface(UtilsGlobal.setFontRegular(getActivity()));
        binding.textinput1.setTypeface(UtilsGlobal.setFontRegular(getActivity()));
        binding.txtusername.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        binding.errorpopup.btnnext.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        binding.errorpopup.txtmessage.setTypeface(UtilsGlobal.setFontRegular(getActivity()));
        viewKeyboard = new KeyboardView(getActivity(), binding.edtnumber);
        LinearLayout.LayoutParams params = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        params.setMargins(7, 2, 7, 0);
        viewKeyboard.setLayoutParams(params);
        binding.container.addView(viewKeyboard, 0);
        binding.edtnumber.requestFocus();
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
        binding.edtnumber.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                UtilsGlobal.hideKeyboard(getActivity());
                return false;
            }
        });
        binding.icback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.lltop.getVisibility() == View.VISIBLE) {
                    MainActivity.contex.barcode = "";
                    HomeFragment homeFragment = new HomeFragment();
                    MainActivity.contex.changeFragment(homeFragment);
                } else {
                    binding.edtnumber.setText("");
                    binding.edtnumber.requestFocus();
                    binding.lltop.setVisibility(View.VISIBLE);
                    binding.lldetails.setVisibility(View.GONE);
                    binding.btnnext.setVisibility(View.VISIBLE);
                    binding.container.setVisibility(View.VISIBLE);
                }
            }
        });
        binding.edtnumber.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isTouch = true;
                return false;
            }
        });
        binding.edtnumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.lldetails.setVisibility(View.GONE);
                if (binding.edtnumber.getText().toString().length() >= 14) {
                    binding.cardnextbtn.setCardElevation(7);
                    binding.btnnext.setVisibility(View.VISIBLE);
                    binding.btnnext.setEnabled(true);
                } else {
                    binding.cardnextbtn.setCardElevation(0);
                    binding.lldetails.setVisibility(View.GONE);
                    binding.btnnext.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (MainActivity.contex.isDeleted)
                    return;
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
        binding.btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.edtnumber.getText().toString().length() < 14) {
                    displayAlert("Too short mobile number to proceed! Please check the inserted value");
                } else {
                    if (UtilsGlobal.isNetworkAvailable(getActivity())) {
                        fetchLoyaltyDetails(binding.edtnumber.getText().toString().replace(" ", "").replace("-", "").replace("(", "").replace(")", ""));
                    } else {
                        displayAlert("No Active Internet Connection Found!");
                    }
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

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private void fetchLoyaltyDetails(String mono) {
        UtilsGlobal.showProgressBar(getActivity());
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<GiftCardModel>> call1 = apiService.GetPOSLoyaltyReward_Details(UtilsGlobal.store.getId(), mono);
        call1.enqueue(new Callback<ArrayList<GiftCardModel>>() {
            @Override
            public void onResponse(Call<ArrayList<GiftCardModel>> call, Response<ArrayList<GiftCardModel>> response) {
                if (!isAdded())
                    return;
                if (response.body() != null) {
                    if (response.body().size() > 0) {
                        //do code for loyalty reward details binding here
                        try {
                            if (response.body().get(4) != null) {
                                binding.container.setVisibility(View.GONE);
                                ArrayList<GiftCardModel> arrCategory = new ArrayList<>();
                                for (int i = 4; i < response.body().size(); i++) {
                                    arrCategory.add(response.body().get(i));
                                }
                                if (arrCategory != null) {
                                    if (arrCategory.size() > 0) {
                                        if (UtilsGlobal.store.isLoyaltyEnable()) {
                                            if(response.body().get(1).getCustomer()==null){
                                                binding.txtusername.setText(Html.fromHtml("Hi " + response.body().get(0).getCustomer() + ":"));
                                            }else {
                                                binding.txtusername.setText(Html.fromHtml("Hi " + response.body().get(1).getCustomer() + ":"));
                                            }
                                        } else {
                                            binding.txtusername.setText(Html.fromHtml("Hi " + response.body().get(0).getCustomer() + ":"));
                                        }
                                        binding.lldetails.setVisibility(View.VISIBLE);
                                        binding.lltop.setVisibility(View.GONE);
                                        lastPurchases(arrCategory);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            binding.lldetails.setVisibility(View.GONE);
                            displayAlert("No purchase history available!");
                        }

                    } else {
                        binding.lldetails.setVisibility(View.GONE);
                        Log.e("", "onResponse:1 " );
                        displayAlert("Account Not Found!");

                    }

                } else {
                    binding.lldetails.setVisibility(View.GONE);
                    Log.e("", "onResponse:2 " );
                    displayAlert("Account Not Found!");

                }
                UtilsGlobal.dismissProgressBar();

            }

            @Override
            public void onFailure(Call<ArrayList<GiftCardModel>> call, Throwable t) {
                call.cancel();
                UtilsGlobal.dismissProgressBar();
                Log.e("", "onResponse:3 " );
                displayAlert("Account Not Found!");

            }
        });
    }

    public void lastPurchases(ArrayList<GiftCardModel> arrCategory) {
        PurchaseDateListAdapter adapter = new PurchaseDateListAdapter(arrCategory, getActivity(), 1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        binding.rvLastPurchases.setLayoutManager(layoutManager);
        binding.rvLastPurchases.setAdapter(adapter);
        binding.rvLastPurchases.setNestedScrollingEnabled(false);
        binding.rvLastPurchases.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        MainActivity.contex.isDeleted = false;
    }

    public void displayAlert(String message) {
        binding.errorpopup.txtmessage.setText("" + message);
        binding.llpopup.setVisibility(View.VISIBLE);
        binding.errorpopup.btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.llpopup.setVisibility(View.GONE);
                binding.edtnumber.setText("");
            }
        });
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                try {
                    if (binding.llpopup.getVisibility() == View.VISIBLE) {
                        HomeFragment.aactivity.seltype = -1;
                        UtilsGlobal.saveCustomer(getActivity(), new User());
                        MainActivity.contex.stopHandler();
                        HomeFragment homeFragment = new HomeFragment();
                        MainActivity.contex.changeFragment(homeFragment);
                    }
                } catch (Exception e) {
                }
            }
        }, 5000);

    }
}