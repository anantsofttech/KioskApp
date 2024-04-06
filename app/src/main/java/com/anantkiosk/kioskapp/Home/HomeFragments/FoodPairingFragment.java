package com.anantkiosk.kioskapp.Home.HomeFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.anantkiosk.kioskapp.MainActivity;
import com.anantkiosk.kioskapp.Utils.UtilsGlobal;
import com.anantkiosk.kioskapp.databinding.FragmentFoodpairingBinding;

public class FoodPairingFragment extends Fragment {

    private FragmentFoodpairingBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFoodpairingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.txttitle.setTypeface(UtilsGlobal.setFontSemiBold(getActivity()));
        binding.icback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragment homeFragment = new HomeFragment();
                MainActivity.contex.changeFragment(homeFragment);
            }
        });
        String webUrl = UtilsGlobal.store.getFoodpairingSite();
        if (webUrl == null) {
            webUrl = "https://www.palmandvine.com/kiosk";
        } else if (webUrl.length() <= 0) {
            webUrl = "https://www.palmandvine.com/kiosk";
        }
        binding.txttitle.setText("" + webUrl);
        binding.webView.setWebViewClient(new MyBrowser());
        binding.webView.getSettings().setLoadsImagesAutomatically(true);
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        if(UtilsGlobal.isNetworkAvailable(getActivity())) {
            if (webUrl.contains("http")) {
                binding.webView.loadUrl(webUrl);
            } else {
                binding.webView.loadUrl("https://" + webUrl);
            }
            binding.webView.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    if (progress >= 60) {
                        try {
                            binding.ivprogress.setVisibility(View.GONE);
                        }catch (Exception e){

                        }
                    }
                }
            });
        }else{
            binding.webView.loadData("No Active Internet Connection Found!", "text/html","utf-8");
            WebSettings settings = binding.webView.getSettings();
            binding.webView.setPadding(20,10,20,10);
            settings.setTextZoom(300);
        }
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

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}