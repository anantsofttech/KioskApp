package com.anantkiosk.kioskapp.Home.HomeFragments;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.anantkiosk.kioskapp.Api.ApiClient;
import com.anantkiosk.kioskapp.Api.ApiInterface;
import com.anantkiosk.kioskapp.KeyboardView;
import com.anantkiosk.kioskapp.MainActivity;
import com.anantkiosk.kioskapp.Model.GiftCardModel;
import com.anantkiosk.kioskapp.Model.User;
import com.anantkiosk.kioskapp.Utils.UtilsGlobal;
import com.anantkiosk.kioskapp.databinding.FragmentGiftcardsBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GiftCardFragment extends Fragment {

    private FragmentGiftcardsBinding binding;
    public  boolean isDetailUp = false;
    ApiInterface apiService;
    int count = 0;
    KeyboardView viewKeyboard;
    public static GiftCardFragment context;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGiftcardsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        context = this;
        binding.edtnumber.requestFocus();

        binding.txttitle.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        binding.btnnext.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        binding.edtnumber.setTypeface(UtilsGlobal.setFontRegular(getActivity()));
        binding.textinput1.setTypeface(UtilsGlobal.setFontRegular(getActivity()));

        binding.txtusername.setTypeface(UtilsGlobal.setFontRegular(getActivity()));
        binding.txtgiftcardnumber.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));

        binding.txtcardused.setTypeface(UtilsGlobal.setFontRegular(getActivity()));
        binding.txtcardusedamt.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));

        binding.txtoriginalpurchase.setTypeface(UtilsGlobal.setFontRegular(getActivity()));
        binding.txtoriginalpurchaseamt.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));

        binding.txtused.setTypeface(UtilsGlobal.setFontRegular(getActivity()));
        binding.txtousedeamt.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));

        binding.txtremaining.setTypeface(UtilsGlobal.setFontRegular(getActivity()));
        binding.txtremainingamt.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));

        binding.txtpurchaser.setTypeface(UtilsGlobal.setFontRegular(getActivity()));
        binding.txtpurchasername.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));

        binding.txtreceipient.setTypeface(UtilsGlobal.setFontRegular(getActivity()));
        binding.txtreceipientname.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));

        binding.errorpopup.btnnext.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        binding.errorpopup.txtmessage.setTypeface(UtilsGlobal.setFontRegular(getActivity()));

        apiService = ApiClient.getClient().create(ApiInterface.class);
        viewKeyboard = new KeyboardView(getActivity(), binding.edtnumber);
        LinearLayout.LayoutParams params = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        params.setMargins(7, 2, 7, 0);
        viewKeyboard.setLayoutParams(params);
        binding.container.addView(viewKeyboard, 0);
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
                if (binding.lltopview.getVisibility() == View.VISIBLE) {
                    MainActivity.contex.barcode="";
                    HomeFragment homeFragment = new HomeFragment();
                    MainActivity.contex.changeFragment(homeFragment);
                } else {
                    binding.edtnumber.requestFocus();
                    binding.edtnumber.setText("");
                    binding.lltopview.setVisibility(View.VISIBLE);
                    binding.lldetails.setVisibility(View.GONE);
                    binding.container.setVisibility(View.VISIBLE);
                }
            }
        });
        binding.edtnumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                MainActivity.contex.stopHandler();
                if (binding.edtnumber.getText().toString().length() >=3) {
                    binding.card1.setCardElevation(7);
                    binding.btnnext.setEnabled(true);
                } else {
                    binding.card1.setCardElevation(0);
                    binding.lldetails.setVisibility(View.GONE);
                    binding.btnnext.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        binding.btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (UtilsGlobal.isNetworkAvailable(getActivity())) {
                        //call api
                        fetchGiftCardDetails(binding.edtnumber.getText().toString().trim(), false);
                    } else {
                        displayAlert("No Active Internet Connection Found!");
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
        isDetailUp = false;

    }

    public void fetchGiftCardDetails(String gftcard_no, boolean isshow) {
        if (isshow) {
            binding.edtnumber.setText("" + gftcard_no);
            try {
                MainActivity.contex.stopHandler();
            }catch (Exception e){

            }
        }
        binding.txtgiftcardnumber.setText("Gift Card Number: " + gftcard_no);
        binding.txtusername.setText(Html.fromHtml("Hello, below is your gift card details"));
        Call<ArrayList<GiftCardModel>> call1 = apiService.GetPosGiftReport_details_data(UtilsGlobal.store.getId(), gftcard_no);
        call1.enqueue(new Callback<ArrayList<GiftCardModel>>() {
            @Override
            public void onResponse(Call<ArrayList<GiftCardModel>> call, Response<ArrayList<GiftCardModel>> response) {

                if (!isAdded())
                    return;
                isDetailUp = true;
                if (response.body() != null) {

                    if (response.body().size() > 0) {

                        binding.container.setVisibility(View.GONE);
                        binding.lltopview.setVisibility(View.GONE);
                        if (response.body().get(0).getGift_Card_Recipient() == null) {
                            binding.txtusername.setText(Html.fromHtml("Hello, below is your gift card details"));
                        } else if (response.body().get(0).getGift_Card_Recipient().trim().length() <= 0) {
                            binding.txtusername.setText(Html.fromHtml("Hello, below is your gift card details"));
                        } else {
                            binding.txtusername.setText(Html.fromHtml("Hello " + response.body().get(0).getGift_Card_Recipient() + ", below is your gift card details"));
                        }
                        binding.lldetails.setVisibility(View.VISIBLE);
                        if (response.body().get(0).getCard_was_last_used_on() != null) {
                            if (response.body().get(0).getCard_was_last_used_on().trim().length() > 0) {
                                binding.txtcardusedamt.setText("" + response.body().get(0).getCard_was_last_used_on());
                            } else {
                                binding.txtcardusedamt.setText("-");
                            }
                        } else {
                            binding.txtcardusedamt.setText("-");
                        }
                        if (response.body().get(0).getOriginal_purchase_amount() != null) {
                            if (response.body().get(0).getOriginal_purchase_amount().trim().length() > 0) {
                                binding.txtoriginalpurchaseamt.setText("$ " + response.body().get(0).getOriginal_purchase_amount());
                            } else {
                                binding.txtoriginalpurchaseamt.setText("-");
                            }
                        } else {
                            binding.txtoriginalpurchaseamt.setText("-");
                        }
                        if (response.body().get(0).getDollars_used_sofar() != null) {
                            if (response.body().get(0).getDollars_used_sofar().trim().length() > 0) {
                                binding.txtousedeamt.setText("$ " + response.body().get(0).getDollars_used_sofar());
                                try {
                                    if (Double.parseDouble(response.body().get(0).getDollars_used_sofar()) <= 0) {
                                        binding.txtcardusedamt.setText("Never used");
                                    }
                                } catch (Exception e) {
                                }
                            } else {
                                binding.txtousedeamt.setText("-");
                            }
                        } else {
                            binding.txtousedeamt.setText("-");
                        }
                        if (response.body().get(0).getBalance_remaining() != null) {
                            if (response.body().get(0).getBalance_remaining().trim().length() > 0) {
                                binding.txtremainingamt.setText("$ " + response.body().get(0).getBalance_remaining());
                            }
                        }
                        if (response.body().get(0).getGift_Card_Purchaser() != null) {
                            if (response.body().get(0).getGift_Card_Purchaser().trim().length() > 0) {
                                binding.txtpurchasername.setText("" + response.body().get(0).getGift_Card_Purchaser());
                            }
                        }
                        if (response.body().get(0).getGift_Card_Recipient() != null) {
                            if (response.body().get(0).getGift_Card_Recipient().trim().length() > 0) {
                                binding.txtreceipientname.setText("" + response.body().get(0).getGift_Card_Recipient());
                            }
                        }

                    } else {
                        binding.lldetails.setVisibility(View.GONE);
                        binding.lltopview.setVisibility(View.VISIBLE);
                        binding.container.setVisibility(View.VISIBLE);
                        displayAlert("Gift Card Not Found!");

                    }

                } else {
                    binding.lldetails.setVisibility(View.GONE);
                    binding.lltopview.setVisibility(View.VISIBLE);
                    binding.container.setVisibility(View.VISIBLE);
                    displayAlert("Gift Card Not Found!");
                }
                MainActivity.contex.startHandler();
            }

            @Override
            public void onFailure(Call<ArrayList<GiftCardModel>> call, Throwable t) {
                if (!isAdded())
                    return;
                call.cancel();
                isDetailUp = true;
                binding.lldetails.setVisibility(View.GONE);
                binding.lltopview.setVisibility(View.VISIBLE);
                binding.container.setVisibility(View.VISIBLE);
                displayAlert("Gift Card Not Found!");
                MainActivity.contex.startHandler();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onPause() {
        super.onPause();
        MainActivity.contex.stopHandler();
    }

    public void displayAlert(String message) {
        binding.errorpopup.txtmessage.setText("" + message);
        binding.llpopup.setVisibility(View.VISIBLE);
        binding.errorpopup.btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.llpopup.setVisibility(View.GONE);
//                Edited by Varun for to clear card number when error message appear
                binding.edtnumber.setText("");
//                END
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