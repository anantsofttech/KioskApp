package com.anantkiosk.kioskapp.Home.HomeFragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anantkiosk.kioskapp.Adapter.DrinkReceipeListAdapter;
import com.anantkiosk.kioskapp.Api.ApiClient;
import com.anantkiosk.kioskapp.Api.ApiInterface;
import com.anantkiosk.kioskapp.MainActivity;
import com.anantkiosk.kioskapp.Model.Drinks;
import com.anantkiosk.kioskapp.Model.Result;
import com.anantkiosk.kioskapp.R;
import com.anantkiosk.kioskapp.Utils.UtilsGlobal;
import com.anantkiosk.kioskapp.databinding.FragmentDrinkreceipesBinding;

import java.net.URLEncoder;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DrinkReceipesFragment extends Fragment {

    public FragmentDrinkreceipesBinding binding;
    DrinkReceipeListAdapter adapter;
    ArrayList<Drinks> productArrayList = new ArrayList<>();
    ArrayList<Drinks> productArrayListMain = new ArrayList<>();
    public static DrinkReceipesFragment activity;
    LinearLayoutManager layoutManager;
    public int selectedpos = -1;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDrinkreceipesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        activity = this;

        binding.txttitle.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        binding.titleemail.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        binding.txtdrinknames.setTypeface(UtilsGlobal.setFontRegular(getActivity()));

        binding.btnnext.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));

        binding.search.setTypeface(UtilsGlobal.setFontRegular(getActivity()));

        binding.edtemailid.setTypeface(UtilsGlobal.setFontRegular(getActivity()));
        binding.textinput1.setTypeface(UtilsGlobal.setFontRegular(getActivity()));

        binding.icsendtext.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));

        binding.txtcontent.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        binding.txtdirection.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        binding.txtingrident.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));


        binding.search.requestFocus();
        binding.search.setFilters(new InputFilter[]{new EmojiExcludeFilter()});
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
        binding.edtemailid.setFilters(new InputFilter[]{filter});
        binding.edtemailid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.edtemailid.getText().toString().length() > 4) {
                    if(UtilsGlobal.emailValidator(binding.edtemailid.getText().toString())) {
                        binding.btnnext.setEnabled(true);
                    }else{
                        binding.btnnext.setEnabled(false);
                    }
                } else {
                    binding.btnnext.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.icsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.lldetails.setVisibility(View.GONE);
                
                DrinkReceipesFragment.activity.selectedpos = -1;
                if (binding.searchcard.getVisibility() == View.VISIBLE) {
                    binding.icsearch.setImageResource(R.drawable.ic_round_search_24);
                    binding.searchcard.setVisibility(View.GONE);
                    binding.search.setText("");
                    adapter.updateList(productArrayListMain, false);
                    UtilsGlobal.hideKeyboard(getActivity());
//                    Edited by Varun for when search bar is visible again the progress bar loading does not happen again
                    binding.ivprogress.setVisibility(View.GONE);
//                    END
                } else {
//                    Edited by Varun for Search button collapse when click
//                    binding.icsearch.setImageResource(R.drawable.ic_round_search_off_24);
                    binding.icsearch.setImageResource(R.drawable.ic_baseline_search_off_62);
//                    END
                    binding.searchcard.setVisibility(View.VISIBLE);
                }
            }
        });
        binding.icclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    binding.wvcontentdetails.loadData("", "text/html; charset=utf-8", "UTF-8");
                    binding.wvdirection.loadData("", "text/html; charset=utf-8", "UTF-8");
                    binding.wvingrediants.loadData("", "text/html; charset=utf-8", "UTF-8");
                    adapter.updateList(productArrayListMain, false);
                    binding.rvItemList.smoothScrollToPosition(selectedpos);
                    layoutManager.scrollToPositionWithOffset(selectedpos, 20);
                } catch (Exception e) {
                }
                binding.lldetails.setVisibility(View.GONE);
                
            }
        });
        adapter = new DrinkReceipeListAdapter(productArrayList, getActivity(), 1);
        layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        binding.rvItemList.setLayoutManager(layoutManager);
        binding.rvItemList.setAdapter(adapter);
        binding.rvItemList.setNestedScrollingEnabled(false);
        binding.rvItemList.setVisibility(View.VISIBLE);
        if (UtilsGlobal.isNetworkAvailable(getActivity())) {
            fetchDrinkList();
        } else {
            binding.txtnodata.setVisibility(View.VISIBLE);
            binding.txtnodata.setText("No Working Internet Connection Found!");
        }
        binding.icback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.contex.barcode = "";
                UtilsGlobal.hideKeyboard(getActivity());
                HomeFragment homeFragment = new HomeFragment();
                MainActivity.contex.changeFragment(homeFragment);
            }
        });
        binding.icclosepopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.search.setCursorVisible(true);
                binding.search.setPressed(true);
                binding.search.setFocusable(true);
                binding.llpopup.setVisibility(View.GONE);
            }
        });
        binding.btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.llpopup.setVisibility(View.GONE);
                ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                Log.e("", "onClick: "+ productArrayList.get(selectedpos).getId());
                Call<Result> call1 = apiService.SendFoodPairingAndDrinkRecipes(UtilsGlobal.store.getId(), productArrayList.get(selectedpos).getId(), binding.edtemailid.getText().toString().trim());
                call1.enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        try {
                            if (response.body() != null) {
                                Toast.makeText(getActivity(), response.body().getResult(), Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){

                        }
                        binding.edtemailid.setText("");
                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                        call.cancel();
                        Toast.makeText(getActivity(), "This feature is in-progress!", Toast.LENGTH_SHORT).show();
                        binding.edtemailid.setText("");

                    }
                });
            }
        });
        binding.icsendtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //api to send text
                binding.txtdrinknames.setText(Html.fromHtml("You will get '<b>"+productArrayList.get(selectedpos).getTitle()+"</b>'s full detail on your email."));
                binding.llpopup.setVisibility(View.VISIBLE);
                binding.edtemailid.requestFocus();

            }
        });
        binding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int o, int i1, int i2) {
                binding.lldetails.setVisibility(View.GONE);
                
                DrinkReceipesFragment.activity.selectedpos = -1;
                if (binding.search.getText().toString().length() >= 3) {
                    if (UtilsGlobal.isNetworkAvailable(getActivity())) {
                        fetchDrinkList();
                    } else {
                        binding.txtnodata.setVisibility(View.VISIBLE);
                        binding.txtnodata.setText("No Working Internet Connection Found!");
                    }
                } else {
                    binding.rvItemList.setVisibility(View.GONE);
                    binding.txtnodata.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (binding.search.getText().toString().length() <= 0) {
                    binding.txtnodata.setVisibility(View.GONE);
                    if (UtilsGlobal.isNetworkAvailable(getActivity())) {
                        adapter.updateList(productArrayListMain, false);
                        binding.rvItemList.setVisibility(View.VISIBLE);
                    } else {
                        binding.txtnodata.setVisibility(View.VISIBLE);
                        binding.txtnodata.setText("No Working Internet Connection Found!");
                    }

                }
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            UtilsGlobal.hideKeyboard(getActivity());
        } catch (Exception e) {
        }
        binding = null;
    }

    private void fetchDrinkList() {
        binding.ivprogress.setVisibility(View.VISIBLE);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<Drinks>> call1 = apiService.GetDrinksList(UtilsGlobal.store.getId(), binding.search.getText().toString().trim().toLowerCase(), "0", "50");
        call1.enqueue(new Callback<ArrayList<Drinks>>() {
            @Override
            public void onResponse(Call<ArrayList<Drinks>> call, Response<ArrayList<Drinks>> response) {
                if (!isAdded())
                    return;
                if (response.body() != null) {
                    if (response.body().size() > 0) {
                        productArrayList = response.body();
                        if (binding.search.getText().toString().length() <= 0) {
                            productArrayListMain = response.body();
                        }
                        adapter = new DrinkReceipeListAdapter(response.body(), getActivity(), 1);
//                        adapter = new DrinkReceipeListAdapter(productArrayList, getActivity(), 1);
                        layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                        binding.rvItemList.setLayoutManager(layoutManager);
                        binding.rvItemList.setAdapter(adapter);
                        binding.rvItemList.setNestedScrollingEnabled(false);
                        binding.rvItemList.setVisibility(View.VISIBLE);
                        binding.txtnodata.setVisibility(View.GONE);
                    } else {
                        binding.rvItemList.setVisibility(View.GONE);
                        binding.txtnodata.setVisibility(View.VISIBLE);
                        Log.e("", "onResponse: 1" );
                        binding.ivprogress.setVisibility(View.GONE);
                        binding.txtnodata.setText("No data found!");
                    }

                } else {
                    binding.rvItemList.setVisibility(View.GONE);
                    binding.txtnodata.setVisibility(View.VISIBLE);
                    Log.e("", "onResponse: 2" );
                    binding.ivprogress.setVisibility(View.GONE);
                    binding.txtnodata.setText("No data found!");
                }

                binding.ivprogress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ArrayList<Drinks>> call, Throwable t) {
                call.cancel();
                try {
                    binding.rvItemList.setVisibility(View.GONE);
                    binding.txtnodata.setVisibility(View.VISIBLE);
                    Log.e("", "onResponse: 3" );
                    binding.ivprogress.setVisibility(View.GONE);
                    binding.txtnodata.setText("No data found!");
                } catch (Exception e) {
                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.search.requestFocus();
        binding.search.post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imgr.showSoftInput(binding.search, InputMethodManager.SHOW_FORCED);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public void showContent(int selPos) {
        try {

            try {
                binding.wvcontentdetails.setVisibility(View.GONE);
                binding.wvingrediants.setVisibility(View.GONE);
                binding.wvdirection.setVisibility(View.GONE);
                if (productArrayList.get(selPos).getContent() == null) {
                    binding.wvcontentdetails.loadData("", "text/html; charset=utf-8", "UTF-8");
                } else if (productArrayList.get(selPos).getContent().trim().length() <= 0) {
                    binding.wvcontentdetails.loadData("", "text/html; charset=utf-8", "UTF-8");
                } else {
                    binding.wvcontentdetails.loadData(URLEncoder.encode("<html><body>" + productArrayList.get(selPos).getContent().replace("&amp;", "&") + "</body></html>", "utf-8").replaceAll("\\+", " "), "text/html; charset=utf-8", "UTF-8");
                }
            } catch (Exception e) {
            }
            try {
                if (productArrayList.get(selPos).getIngredients() == null) {
                    binding.wvingrediants.loadData(productArrayList.get(selPos).getIngredients(), "text/html; charset=utf-8", "UTF-8");
                } else if (productArrayList.get(selPos).getIngredients().trim().length() <= 0) {
                    binding.wvingrediants.loadData("", "text/html; charset=utf-8", "UTF-8");
                } else {
                    binding.wvingrediants.loadData(URLEncoder.encode("<html><body>" + productArrayList.get(selPos).getIngredients().replace("&amp;", "&") + "</body></html>", "utf-8").replaceAll("\\+", " "), "text/html; charset=utf-8", "UTF-8");
                }
            } catch (Exception e) {
            }
            try {
                String direction = "";
                if (productArrayList.get(selPos).getMethod() != null) {
                    if (productArrayList.get(selPos).getMethod().trim().length() > 0) {
                        if (productArrayList.get(selPos).getServe() != null) {
                            if (productArrayList.get(selPos).getServe().trim().length() > 0) {
                                direction = productArrayList.get(selPos).getMethod() + "<br><br><b>Preferred Glass</b><br>" + productArrayList.get(selPos).getServe() + "</br></br></br>";
                            } else {
                                direction = productArrayList.get(selPos).getMethod();
                            }
                        } else {
                            direction = productArrayList.get(selPos).getMethod();
                        }
                    } else {
                        if (productArrayList.get(selPos).getServe() != null) {
                            if (productArrayList.get(selPos).getServe().trim().length() > 0) {
                                direction = "<b>Preferred Glass</b><br>" + productArrayList.get(selPos).getServe() + "</br>";
                            }
                        }
                    }

                } else {
                    if (productArrayList.get(selPos).getServe() != null) {
                        if (productArrayList.get(selPos).getServe().trim().length() > 0) {
                            direction = "<b>Preferred Glass</b><br>" + productArrayList.get(selPos).getServe() + "</br>";
                        }
                    }
                }
                if (direction != null) {
                    if (direction.length() > 0) {
                        binding.wvdirection.loadData(URLEncoder.encode("<html><body>" + direction.replace("&amp;", "&") + "</body></html>", "utf-8").replaceAll("\\+", " "), "text/html; charset=utf-8", "UTF-8");
                    } else {
                        binding.wvdirection.loadData("-", "text/html; charset=utf-8", "UTF-8");
                    }
                } else {
                    binding.wvdirection.loadData("-", "text/html; charset=utf-8", "UTF-8");
                }
                try {
                    WebSettings settings = binding.wvdirection.getSettings();
                    settings.setTextZoom(152);
                    WebSettings settings2 = binding.wvingrediants.getSettings();
                    settings2.setTextZoom(152);
                    WebSettings settings3 = binding.wvcontentdetails.getSettings();
                    settings3.setTextZoom(152);

                } catch (Exception e) {
                }
            } catch (Exception e) {
            }

        } catch (Exception e) {
        }
        binding.wvcontentdetails.setVisibility(View.VISIBLE);
        binding.wvingrediants.setVisibility(View.VISIBLE);
        binding.wvdirection.setVisibility(View.VISIBLE);
        binding.lldetails.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();

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

}