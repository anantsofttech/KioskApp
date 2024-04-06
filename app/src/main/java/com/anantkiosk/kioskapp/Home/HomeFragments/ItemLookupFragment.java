package com.anantkiosk.kioskapp.Home.HomeFragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anantkiosk.kioskapp.Adapter.MultiPackProductListAdapter;
import com.anantkiosk.kioskapp.Adapter.ProductListAdapter;
import com.anantkiosk.kioskapp.Api.ApiClient;
import com.anantkiosk.kioskapp.Api.ApiInterface;
import com.anantkiosk.kioskapp.MainActivity;
import com.anantkiosk.kioskapp.Model.Product;
import com.anantkiosk.kioskapp.Model.ProductResponseMain;
import com.anantkiosk.kioskapp.R;
import com.anantkiosk.kioskapp.Utils.UtilsGlobal;
import com.anantkiosk.kioskapp.databinding.FragmentItemlookupBinding;
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

public class ItemLookupFragment extends Fragment {
    //08559213858
    public FragmentItemlookupBinding binding;
    ProductListAdapter adapter;
    ArrayList<Product> productArrayList = new ArrayList<>();
    public static Dialog dialog;
    public static boolean isFromSKU = false;
    public static ItemLookupFragment activity;
    public boolean isInStock = false, needToDisplayDate = false, isStockTouch = false;
    Handler handler = new Handler();
    long delay = 1500; // 2 seconds after user stops typing
    long last_text_edit = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentItemlookupBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        activity = this;
        binding.search.requestFocus();
        binding.txttitle.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        binding.txtswitch.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        binding.search.setTypeface(UtilsGlobal.setFontRegular(getActivity()));
        binding.switch2.setTypeface(UtilsGlobal.setFontRegular(getActivity()));
        binding.txtnodata.setTypeface(UtilsGlobal.setFontRegular(getActivity()));
        binding.txtdetails.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        binding.txtdetails1.setTypeface(UtilsGlobal.setFontLibre(getActivity()));
        adapter = new ProductListAdapter(productArrayList, getActivity(), false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        binding.rvItemList.setLayoutManager(layoutManager);
        binding.rvItemList.setAdapter(adapter);
        binding.rvItemList.setNestedScrollingEnabled(false);
        binding.rvItemList.setVisibility(View.VISIBLE);
        binding.search.setFilters(new InputFilter[]{new EmojiExcludeFilter()});
        isInStock = false;
        binding.switch2.setChecked(false);
        needToDisplayDate = UtilsGlobal.store.getIsNeedToDisplayDate();
        if (!UtilsGlobal.store.getIsInStockCheck()) {
            isInStock = true;
            needToDisplayDate = false;
            binding.switch2.setChecked(true);

        }
        binding.switch2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                isStockTouch = true;
                return false;
            }
        });
        binding.switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (isStockTouch) {
                    isStockTouch = false;
                    isInStock = b;
                    if (binding.search.getText().toString().length() >= 3) {
                        binding.txtnodata.setVisibility(View.GONE);
                        isFromSKU = false;
                        if (UtilsGlobal.isNetworkAvailable(getActivity())) {
                            fetchProductList();
                        }
                    } else {
                        binding.rvItemList.setVisibility(View.GONE);
                        binding.txtnodata.setVisibility(View.GONE);
                    }
                }
            }
        });
        binding.icback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFromSKU = false;
                MainActivity.contex.barcode = "";
                HomeFragment homeFragment = new HomeFragment();
                MainActivity.contex.changeFragment(homeFragment);
            }
        });
        binding.search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // Your piece of code on keyboard search click
                    UtilsGlobal.hideKeyboard(getActivity());
                    return true;
                }
                return false;
            }
        });
        binding.search.addTextChangedListener(new TextWatcher() {
                                                  @Override
                                                  public void beforeTextChanged(CharSequence s, int start, int count,
                                                                                int after) {
                                                  }

                                                  @Override
                                                  public void onTextChanged(final CharSequence s, int start, int before,
                                                                            int count) {
                                                      //You need to remove this to run only once
                                                      if (binding.search.getText().toString().length() <= 0) {
                                                          binding.llinstock.setVisibility(View.GONE);
                                                          binding.rvItemList.setVisibility(View.GONE);
                                                          binding.txtnodata.setVisibility(View.GONE);
                                                          binding.llscan.setVisibility(View.VISIBLE);
                                                          binding.ivprogress.setVisibility(View.GONE);

                                                      } else if (binding.search.getText().toString().length() == 1) {
                                                          binding.llscan.setVisibility(View.GONE);
                                                      }else if(binding.search.getText().toString().length()>3){
                                                          binding.ivprogress.setVisibility(View.VISIBLE);
                                                      }

                                                      handler.removeCallbacks(input_finish_checker);

                                                  }

                                                  @Override
                                                  public void afterTextChanged(final Editable s) {
                                                      //avoid triggering event when text is empty

                                                      if (s.length() > 0) {
                                                          last_text_edit = System.currentTimeMillis();
                                                          handler.postDelayed(input_finish_checker, delay);
                                                      }
                                                  }
                                              }
        );
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            isFromSKU = false;
            UtilsGlobal.hideKeyboard(getActivity());
        } catch (Exception e) {
        }
        binding = null;

    }

    private void fetchProductList() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
//        Edited by Varun for static condition
        Call<ArrayList<ProductResponseMain>> call1 = apiService.GetPosInventory(UtilsGlobal.store.getId(), "I", "99", "1", binding.search.getText().toString().trim(), "true", String.valueOf(isInStock), "desc1", "asc");
//        Call<ArrayList<ProductResponseMain>> call1 = apiService.GetPosInventory(UtilsGlobal.store.getId(), "I", "999", "1", binding.search.getText().toString().trim(), "true", String.valueOf(isInStock), "desc1", "asc");
//        END
        call1.enqueue(new Callback<ArrayList<ProductResponseMain>>() {
            @Override
            public void onResponse(Call<ArrayList<ProductResponseMain>> call, Response<ArrayList<ProductResponseMain>> response) {
                if (!isAdded())
                    return;
//                Edited by Varun for loading issue
                if (response.body().get(0).getUserInfo()!=null){
                    productArrayList = response.body().get(0).getUserInfo();
                }
//                END
                if (binding.search.getText().toString().length() >= 3) {
                    binding.llinstock.setVisibility(View.VISIBLE);
                    if (response.body() != null) {
                        if (response.body().size() > 0) {
                            binding.txtnodata.setVisibility(View.GONE);
                            if (isFromSKU) {
                                if (response.body().get(0).getUserInfo() != null) {
                                    if (response.body().get(0).getUserInfo().size() > 0) {
                                        binding.llscan.setVisibility(View.GONE);
                                        UtilsGlobal.hideKeyboard(getActivity());
                                        showDialog(getActivity(), response.body().get(0).getUserInfo().get(0), false);
                                    } else {
                                        binding.rvItemList.setVisibility(View.GONE);
                                        binding.txtnodata.setVisibility(View.VISIBLE);
                                        binding.txtnodata.setText("No data found!");
                                    }
                                } else {
                                    binding.rvItemList.setVisibility(View.GONE);
                                    binding.txtnodata.setVisibility(View.VISIBLE);
                                    binding.txtnodata.setText("No data found!");
                                }
                            } else {
//                                Edited by Varun for loading issue
                                adapter = new ProductListAdapter(response.body().get(0).getUserInfo(), getActivity(), false);
//                                adapter = new ProductListAdapter(productArrayList, getActivity(), false);
//                                END
                                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                                binding.rvItemList.setLayoutManager(layoutManager);
                                binding.rvItemList.setAdapter(adapter);
                                binding.rvItemList.setNestedScrollingEnabled(false);
                                binding.rvItemList.setVisibility(View.VISIBLE);
                            }

                        } else {
                            binding.rvItemList.setVisibility(View.GONE);
                            binding.txtnodata.setVisibility(View.VISIBLE);
                            binding.txtnodata.setText("No data found!");
                        }

                    } else {
                        binding.rvItemList.setVisibility(View.GONE);
                        binding.txtnodata.setVisibility(View.VISIBLE);
                        binding.txtnodata.setText("No data found!");
                    }
                }
                binding.ivprogress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ArrayList<ProductResponseMain>> call, Throwable t) {
                call.cancel();
                try {
                    binding.ivprogress.setVisibility(View.GONE);
                    binding.llinstock.setVisibility(View.VISIBLE);
                    binding.rvItemList.setVisibility(View.GONE);
                    binding.txtnodata.setVisibility(View.VISIBLE);
                    binding.txtnodata.setText("No data found!");
                } catch (Exception e) {
                }

            }
        });
    }

    private void fetchRelatedProductList(RecyclerView relatedProductList, String productSKU, LinearLayout llrelated) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<Product>> call1 = apiService.GetRecommandedItems(UtilsGlobal.store.getId(), productSKU);
        call1.enqueue(new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                if (!isAdded())
                    return;
                if (response.body() != null) {
                    if (response.body().size() > 0) {
                        ProductListAdapter relatedAdapter = new ProductListAdapter(response.body(), getActivity(), true);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
                        relatedProductList.setLayoutManager(layoutManager);
                        relatedProductList.setAdapter(relatedAdapter);
                        relatedProductList.setNestedScrollingEnabled(false);
                        relatedProductList.setVisibility(View.VISIBLE);
                        llrelated.setVisibility(View.VISIBLE);

                    }

                }
            }

            @Override
            public void onFailure(Call<ArrayList<Product>> call, Throwable t) {
                call.cancel();

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("in resume", "hehe");
        binding.search.requestFocus();
        binding.search.post(new Runnable() {
            @Override
            public void run() {
                try {
                    InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imgr.showSoftInput(binding.search, InputMethodManager.SHOW_FORCED);
                } catch (Exception e) {
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onPause() {
        super.onPause();
        UtilsGlobal.hideKeyboard(getActivity());
    }

    public void showDialog(Activity activity, Product product, boolean isRelated) {
        if (dialog == null) {
            dialog = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.product_detail);
            dialog.show();
        }
        TextView title, tvunit, txtdiscountedprice, txtprice, tvdesc;
        //ImageView ic_product;
        TextView tvdepartment = dialog.findViewById(R.id.tvdepartment);
        tvdepartment.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        LinearLayout lldepartment = dialog.findViewById(R.id.lldepartment);
        title = dialog.findViewById(R.id.tv_product_name);
        title.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        ImageView ic_product = dialog.findViewById(R.id.ic_product);
        tvunit = dialog.findViewById(R.id.tvunit);
        View viewfor = dialog.findViewById(R.id.viewfor);
        viewfor.setVisibility(View.GONE);
        tvunit.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        txtdiscountedprice = dialog.findViewById(R.id.txtdiscountedprice);
        txtdiscountedprice.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        txtprice = dialog.findViewById(R.id.txtprice);
        txtprice.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        title.setText(product.getProduct_name());
        tvdesc = dialog.findViewById(R.id.tvdesc);
        tvdesc.setTypeface(UtilsGlobal.setFontRegular(getActivity()));
        RecyclerView rvItemList = dialog.findViewById(R.id.rvItemList);
        TextView txtrelated = dialog.findViewById(R.id.txtrelated);
        txtrelated.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        //multiple options avail than display price as "Multiple Options" and set options on this
        TextView tvinstock = dialog.findViewById(R.id.tvinstock);
        tvinstock.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        tvinstock.setVisibility(View.VISIBLE);
        LinearLayout llrelated = dialog.findViewById(R.id.llrelated);
        llrelated.setVisibility(View.GONE);
        //multi optios
        LinearLayout llmulti = dialog.findViewById(R.id.llmulti);
        llmulti.setVisibility(View.GONE);
        TextView tvmultititle = dialog.findViewById(R.id.tvmultititle);
        tvmultititle.setTypeface(UtilsGlobal.setFontRegular(getActivity()));
        RecyclerView rvmultiitem = dialog.findViewById(R.id.rvmultiitem);
        View view_dept = dialog.findViewById(R.id.view_dept);
        view_dept.setVisibility(View.GONE);
        if (product.getMultipackArray() != null) {
            if (product.getMultipackArray().trim().length() > 0) {
                ArrayList<Product> arrMultiOption = new ArrayList<>();
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(product.getMultipackArray());
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
//                            Edted by Varun for Centralized image
                            productVals.setInvSmallImageFullPath(explrObject.getString("InvSmallImageFullPath"));
                            productVals.setInvLargeImageFullPath(explrObject.getString("InvLargeImageFullPath"));
//                            END
                        } catch (Exception e) {
                        }
                        arrMultiOption.add(productVals);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (arrMultiOption.size() > 0) {
                    MultiPackProductListAdapter relatedAdapter = new MultiPackProductListAdapter(arrMultiOption, getActivity(), false, product);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                    rvmultiitem.setLayoutManager(layoutManager);
                    rvmultiitem.setAdapter(relatedAdapter);
                    rvmultiitem.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                    rvmultiitem.setNestedScrollingEnabled(false);
                    rvmultiitem.setVisibility(View.VISIBLE);
                    try {
                        if (Double.parseDouble(product.getPrice()) <= 0) {
                            txtprice.setVisibility(View.GONE);
                            txtdiscountedprice.setVisibility(View.GONE);

                        }
                    } catch (Exception e) {
                    }
                }
                llmulti.setVisibility(View.VISIBLE);

            }
        }
        if (!isRelated) {
            if (UtilsGlobal.store.getShowRelatedProducts()) {
                String sku = "";
                if (product.getSKU() != null)
                    sku = product.getSKU().trim();
                fetchRelatedProductList(rvItemList, sku, llrelated);
                viewfor.setVisibility(View.VISIBLE);
            }

        }
        try {
            tvdesc.setText(product.getProduct_name().trim());
            if (product.getProduct_name().trim().length() > 30) {
                viewfor.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
        }

//        Edited by Varun for Centralized Image

//        if (product.getInvLargeImage() != null) {
//            if (product.getInvLargeImage().trim().length() > 0) {
//                Glide.with(activity)
//                        .load(ApiClient.IMG_BASE + UtilsGlobal.IMAGEURL + UtilsGlobal.store.getId() + "/" + product.getInvLargeImage())
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .error(R.drawable.noimage)
//                        .into(ic_product);
//            }
//        }

        if (product.getInvLargeImageFullPath() != null) {
            if (product.getInvLargeImageFullPath().trim().length() > 0) {
                Glide.with(activity)
                        .load( product.getInvLargeImageFullPath())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.noimage)
                        .into(ic_product);
            }
        }

//        End

        TextView tvlightingdesc = dialog.findViewById(R.id.tvlightingdesc);
        tvlightingdesc.setTypeface(UtilsGlobal.setFontRegular(activity));
        tvlightingdesc.setVisibility(View.GONE);
        if (product.getExtend_desc() != null) {
            if (product.getExtend_desc().trim().length() > 0) {
                tvlightingdesc.setVisibility(View.VISIBLE);
                tvlightingdesc.setText(Html.fromHtml(product.getExtend_desc()));
                if (product.getExtend_desc().trim().length() > 30) {
                    viewfor.setVisibility(View.VISIBLE);
                }
            }
        }
        if (product.getDiscountedamount() != null) {
            if (Double.parseDouble(product.getDiscountedamount()) > 0) {
                Date promoStart = null, promoEnd = null;
                if (product.getPromoStart() != null) {
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        promoStart = df.parse(product.getPromoStart());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (product.getPromoEnd() != null) {
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        promoEnd = df.parse(product.getPromoEnd());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                Date today = new Date();
                if (promoStart != null && promoEnd != null) {
                    if (promoStart.compareTo(today) == 1 || promoEnd.compareTo(today) == 1 || (today.after(promoStart) && today.before(promoEnd))) {
                        txtdiscountedprice.setVisibility(View.VISIBLE);
                        txtdiscountedprice.setText("$" + product.getPrice());
                        txtprice.setText("$" + product.getDiscountedamount());
                        txtdiscountedprice.setPaintFlags(txtdiscountedprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        txtprice.setTextColor(activity.getResources().getColor(R.color.green));
                    } else {
                        txtprice.setText("$" + product.getPrice());
                        txtdiscountedprice.setVisibility(View.GONE);
                        txtprice.setTextColor(activity.getResources().getColor(R.color.clr_itemlook));
                    }
                } else {
                    txtprice.setText("$" + product.getPrice());
                    txtdiscountedprice.setVisibility(View.GONE);
                    txtprice.setTextColor(activity.getResources().getColor(R.color.clr_itemlook));
                }
            } else {
                txtprice.setText("$" + product.getPrice());
                txtdiscountedprice.setVisibility(View.GONE);
                txtprice.setTextColor(activity.getResources().getColor(R.color.clr_itemlook));
            }
        } else {
            txtprice.setText("$" + product.getPrice());
            txtdiscountedprice.setVisibility(View.GONE);
            txtprice.setTextColor(activity.getResources().getColor(R.color.clr_itemlook));
        }
        TextView tvweight = dialog.findViewById(R.id.tvweight);
        tvweight.setTypeface(UtilsGlobal.setFontRegular(activity));
        LinearLayout llunit = dialog.findViewById(R.id.llunit);
        LinearLayout lllocation = dialog.findViewById(R.id.lllocation);
        TextView tvlocationtitle = dialog.findViewById(R.id.tvLocation);
        tvlocationtitle.setTypeface(UtilsGlobal.setFontRegular(activity));
        TextView tvlocation = dialog.findViewById(R.id.tvLocationname);
        tvlocation.setTypeface(UtilsGlobal.setFontSemiBold(activity));
        View view_unit = dialog.findViewById(R.id.viewunit);
        View view_loc = dialog.findViewById(R.id.view_loc);
        view_loc.setVisibility(View.GONE);
        lllocation.setVisibility(View.GONE);
        if (product.getBinLocation() != null) {
            if (product.getBinLocation().trim().length() > 0) {
                lllocation.setVisibility(View.VISIBLE);
                tvlocation.setText(product.getBinLocation());
                view_loc.setVisibility(View.VISIBLE);
            }
        }
        lldepartment.setVisibility(View.GONE);
        if (product.getDepartment() != null) {
            if (product.getDepartment().length() > 0) {
                tvdepartment.setText(product.getDepartment());
                lldepartment.setVisibility(View.VISIBLE);
                view_dept.setVisibility(View.VISIBLE);
            }
        }
        ImageView icclose = dialog.findViewById(R.id.icclose);
        TextView tvsku = dialog.findViewById(R.id.tvsku);
        tvsku.setTypeface(UtilsGlobal.setFontSemiBold(activity));
        TextView tvoutofstock = dialog.findViewById(R.id.tvdateupto);
        TextView tvexpecteddate = dialog.findViewById(R.id.tvexpectedate);
        tvoutofstock.setVisibility(View.GONE);
        try {
            tvsku.setText("SKU-" + product.getSKU().trim());
        } catch (Exception e) {
            tvsku.setText("SKU-" + product.getSKU());
        }
        tvexpecteddate.setVisibility(View.GONE);
        tvoutofstock.setVisibility(View.GONE);
        if (product.getQuantityonHand() != null) {
            if (product.getQuantityonHand().trim().length() > 0) {
                if (Double.parseDouble(product.getQuantityonHand().trim()) <= 0) {
                    tvoutofstock.setText(Html.fromHtml("Out Of Stock!"));
                    tvoutofstock.setVisibility(View.VISIBLE);
                    tvinstock.setVisibility(View.GONE);
                    if (needToDisplayDate) {
                        if (product.getExpectedDate() != null) {
                            if (product.getExpectedDate().trim().length() > 0) {
                                try {
                                    tvexpecteddate.setVisibility(View.VISIBLE);
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                                    if (UtilsGlobal.isToday(format.parse(product.getExpectedDate()))) {
                                        tvexpecteddate.setText("Available Today!");
                                    } else if (UtilsGlobal.isTomorrow(format.parse(product.getExpectedDate()))) {
                                        tvexpecteddate.setText("Available Tomorrow!");
                                    } else {
                                        Date formatdate = format.parse(product.getExpectedDate());
                                        //YYYY
                                        if (formatdate.after(new Date())) {
                                            SimpleDateFormat format2 = new SimpleDateFormat("yyyy", Locale.getDefault());
                                            String strCurYear = format2.format(formatdate);
                                            String strNow = format2.format(new Date());
                                            SimpleDateFormat format1;
                                            if (strCurYear.equalsIgnoreCase(strNow)) {
                                                format1 = new SimpleDateFormat("EEEE,MMM dd", Locale.getDefault());
                                            } else {
                                                format1 = new SimpleDateFormat("EEEE,MMM dd, yyyy", Locale.getDefault());
                                            }
                                            String dateIs = format1.format(formatdate);
                                            tvexpecteddate.setText("Available On " + dateIs);
                                        } else {
                                            tvexpecteddate.setText("Available Soon!");
                                        }
                                    }
                                } catch (Exception e) {
                                    tvexpecteddate.setVisibility(View.VISIBLE);
                                    tvexpecteddate.setText("" + product.getExpectedDate());
                                    tvexpecteddate.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                }
            }
        }
        try {
            llunit.setVisibility(View.GONE);
            view_unit.setVisibility(View.GONE);
            if (product.getSizeFlag().equalsIgnoreCase("y")) {
                tvunit.setVisibility(View.VISIBLE);
                if (product.getProduct_unit() != null) {
                    if (product.getProduct_unit().trim().length() > 0) {
                        tvunit.setText(product.getProduct_unit().trim());
                        llunit.setVisibility(View.VISIBLE);
                        view_unit.setVisibility(View.VISIBLE);
                    }
                }
            }
        } catch (Exception e) {
        }
        icclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                dialog = null;
                if (isFromSKU) {
                    binding.search.setText("");
                    binding.search.requestFocus();
                    ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    isFromSKU = false;
                }
            }
        });

    }

    public void scaneQR(String result) {
        if (result != null) {
            if (result.trim().length() > 0) {
                if (result.trim().toString().length() >= 3) {
                    binding.txtnodata.setVisibility(View.GONE);
                    binding.search.setText("" + result.trim());
                    binding.search.setSelection(binding.search.getText().length());
                    isFromSKU = true;
                    if (UtilsGlobal.isNetworkAvailable(getActivity())) {
                        fetchProductList();
                    } else {
                        binding.txtnodata.setVisibility(View.VISIBLE);
                        binding.txtnodata.setText("No Working Internet Connection Found!");
                    }
                }
            } else {
                Toast.makeText(getActivity(), "No scan result found!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "No scan result found!", Toast.LENGTH_SHORT).show();
        }
    }

    private class EmojiExcludeFilter implements InputFilter {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                int type = Character.getType(source.charAt(i));
                if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
                    return "";
                }
            }
            return null;
        }
    }

    public class MyBounceInterpolator implements android.view.animation.Interpolator {
        private double mAmplitude = 1;
        private double mFrequency = 10;

        public MyBounceInterpolator(double amplitude, double frequency) {
            mAmplitude = amplitude;
            mFrequency = frequency;
        }

        public float getInterpolation(float time) {
            return (float) (-1 * Math.pow(Math.E, -time / mAmplitude) *
                    Math.cos(mFrequency * time) + 1);
        }
    }

    private Runnable input_finish_checker = new Runnable() {
        public void run() {
            if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
                // TODO: do what you need here
                if (binding.search.getText().toString().trim().length() >= 3) {
                    try {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                binding.llinstock.setVisibility(View.VISIBLE);
                                if (UtilsGlobal.isNetworkAvailable(getActivity())) {
                                    fetchProductList();
                                } else {
                                    binding.txtnodata.setVisibility(View.VISIBLE);
                                    binding.txtnodata.setText("No Working Internet Connection Found!");
                                }
                            }
                        });

                    } catch (Exception e) {
                    }
                }

            }
        }
    };
}