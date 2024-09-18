package com.anantkiosk.kioskapp.Home.HomeFragments;

import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.anantkiosk.kioskapp.Api.ApiClient;
import com.anantkiosk.kioskapp.Api.ApiClientBillBoard;
import com.anantkiosk.kioskapp.Api.ApiClientQT;
import com.anantkiosk.kioskapp.Api.ApiInterface;
import com.anantkiosk.kioskapp.MainActivity;
import com.anantkiosk.kioskapp.Model.AdvSign;
import com.anantkiosk.kioskapp.Model.Auth_QTModel;
import com.anantkiosk.kioskapp.Model.GetAdRequestModel;
import com.anantkiosk.kioskapp.Model.Result;
import com.anantkiosk.kioskapp.Model.RetrieveAdunitModel;
import com.anantkiosk.kioskapp.Model.Sign;
import com.anantkiosk.kioskapp.Home.slider.AdvertiseImageSliderAdapter;
import com.anantkiosk.kioskapp.Task.TaskGetAdRequest;
import com.anantkiosk.kioskapp.Task.TaskImages;
import com.anantkiosk.kioskapp.Task.TaskPlay;
import com.anantkiosk.kioskapp.Task.TaskRetrieveAdunit;
import com.anantkiosk.kioskapp.Utils.UtilsGlobal;
import com.anantkiosk.kioskapp.databinding.FragmentAdvertiseBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdvertisementFragment extends Fragment implements   TaskRetrieveAdunit.TaskRetrieveAdunitEvent, TaskGetAdRequest.TaskGetAdRequestEvent
                            , TaskPlay.TaskPlayEvent , TaskImages.TaskImagesEvent {

    private FragmentAdvertiseBinding binding;
    ApiInterface apiServiceBillboard;
    public static AdvertisementFragment context;
    int count;
    ArrayList<String> dateList = new ArrayList<>();
    AdvertiseImageSliderAdapter myCustomPagerAdapter;
    String signName = "";

    String address = "175 Memorial Hwy # 2-7, New Rochelle, NY 10801, United States";

    String latitude = "0.0", longitude = "0.0";
    boolean isAllowLoading = true;
    public String android_id = null, storename = "";
    int advCount = 0;
    ArrayList<AdvSign> advLists = new ArrayList<>();

    int noDataFoundCount = 0;

    Auth_QTModel auth_qt = new Auth_QTModel();
    int countTotal = 0;
    int countForImage = 0;
    int numberofcall;
    int num;
    ProgressDialog loading = null;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable delayedRunnable;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdvertiseBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        apiServiceBillboard = ApiClientBillBoard.getClient().create(ApiInterface.class);
        context = this;
        try {
            android_id = Settings.Secure.getString(getActivity().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
        }
        if (android_id == null) {
            android_id = UtilsGlobal.getUUId(getActivity());
        } else if (android_id.trim().length() <= 0) {
            //android_id = "emulator";
            android_id = UtilsGlobal.getUUId(getActivity());
        }
        if (android_id == null) {
            android_id = "emulator";
        } else if (android_id.trim().length() <= 0) {
            android_id = "emulator";
        }
        Sign sign = UtilsGlobal.getSign(getActivity());
        address = UtilsGlobal.address;
        try {
            String[] storename_arr = address.split("-");
            if (storename_arr != null) {
                if (storename_arr.length >= 2) {
                    String[] storename_inner = storename_arr[1].split(",");
                    if (storename_inner != null) {
                        if (storename_inner.length > 0) {
                            storename = storename_inner[0];
                            signName = "KIOSK_" + android_id + "_" + UtilsGlobal.store.getId() + "" + storename.trim().replace(" ", "_");
                        }
                    }
                }
            }

        } catch (Exception e) {
            signName = "KIOSK_" + android_id + "_" + UtilsGlobal.store.getId();
        }
        Log.d("TEST", "SIGNNAME: " + signName);
//        if (sign != null) {
//            if (sign.getId() != null) {
//                if (sign.getId().trim().length() > 0) {
//                    ApiClientBillBoard.signId = sign.getId();
//                    MainActivity.contex.setVersion();
//                }
//            }
//        }
        if (UtilsGlobal.isNetworkAvailable(getActivity())) {
//            if (ApiClientBillBoard.signId == null) {
//                fetchAllSigns();
//            } else if (ApiClientBillBoard.signId.trim().length() <= 0) {
//                fetchAllSigns();
//            } else {
//                if (UtilsGlobal.isNetworkAvailable(getActivity())) {
//                    scheduleAsveCalls();
//                } else {
//                    setDummyAdvertisements();
//                }
//            }
            if (UtilsGlobal.isFromQT) {
                Auth_QT();
            }
//            Edited by Varun for Lighting server image
            else {
                callImagesWs();
//                setDummyAdvertisements();
            }
//             END

        }


        binding.viewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset == 0) {
//                    if (advLists != null) {
//                        if (advLists.size() > 0) {
//                            if (advCount <= advLists.size() - 1) {
//                                //nothing
//                            } else {
//                                advCount = 0;
//                            }
//                            Log.d("TEST", "position: " + position + " positionOffset: " + positionOffset + " positionOffsetPixels: " + positionOffsetPixels + " adv coount: " + advCount);
//                            if (!advLists.get(advCount).getId().equalsIgnoreCase("-1")) {
//                                if (UtilsGlobal.isNetworkAvailable(getActivity())) {
//                                    recordFlips(advLists.get(advCount).getId());
//                                    advCount++;
//                                }
//                            }
//
//                        }
//                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

        handler.removeCallbacks(delayedRunnable);

    }

    //Edited by Varun for QT
    public void Auth_QT() {

//        handler.removeCallbacks(delayedRunnable);
        UtilsGlobal.call_log_WS(getContext(), "Appear in the callQT_Token Function", "", "");

        if (UtilsGlobal.ImageDetalList4 != null && !UtilsGlobal.ImageDetalList4.isEmpty()) {
            UtilsGlobal.ImageDetalList4.clear();
        }
        if (UtilsGlobal.localgetAdRequestModel!=null && !UtilsGlobal.localgetAdRequestModel.isEmpty()){
            UtilsGlobal.localgetAdRequestModel.clear();
        }
        UtilsGlobal.showProgressBar(getActivity());

        auth_qt.setUsername(UtilsGlobal.QT_USERNAME);
        auth_qt.setPassword(UtilsGlobal.QT_PASSWORD);

        Gson gson = new Gson();
        String jsonStr = gson.toJson(auth_qt);
        RequestBody body = RequestBody.create(jsonStr,
                MediaType.parse("application/json")
        );
        UtilsGlobal.call_log_WS(getContext(), "Send to QT QT for Token WS", "https://api.placeexchange.com/v3/token", "");
        ApiInterface apiServiceBillboard = ApiClientQT.getClient().create(ApiInterface.class);
        Call<Auth_QTModel> call1 = apiServiceBillboard.AUTH_QT_CALL(body);
        call1.enqueue(new Callback<Auth_QTModel>() {
            @Override
            public void onResponse(Call<Auth_QTModel> call, Response<Auth_QTModel> response) {
                if (response.body() != null) {
                    auth_qt.setAccess_token(response.body().getAccess_token());
                    auth_qt.setId_token(response.body().getId_token());
                    auth_qt.setToken_type(response.body().getToken_type());
                    auth_qt.setExpires_in(response.body().getExpires_in());
                    auth_qt.setError(response.body().getError());
                    auth_qt.setError_description(response.body().getError_description());

                    UtilsGlobal.localAuthQTlist = auth_qt;
                    Gson gson = new Gson();
                    String responseBodyString = gson.toJson(response.body());
                    UtilsGlobal.call_log_WS(getContext(), "receive from QT for Token WS", "https://api.placeexchange.com/v3/token", responseBodyString);

                    if (response.body().getAccess_token() != null && !response.body().getAccess_token().equals("")) {
                        callgetAdunit(UtilsGlobal.store.getId() + "_"+"k1");
//                        callgetAdunit(UtilsGlobal.store.getId() + "_"+"11");
                    }

                }
            }

            @Override
            public void onFailure(Call<Auth_QTModel> call, Throwable t) {
                call.cancel();
//                If there is no image then we are calling the computer perfect server and showing the images from computer perfect with the help of Industry type
//                        setnoimage();
                UtilsGlobal.call_log_WS(getContext(), "Receive from QT and calling Computer Perfect : 1", "https://api.placeexchange.com/v3/token", t.getMessage());
                UtilsGlobal.iscomefrom_adrequest_Same =true;
                UtilsGlobal.unauthorized =true;
                callcomputerperfectimage();
                Toast.makeText(getActivity(), "Calling computer perfect because there is no resposne in QT", Toast.LENGTH_LONG).show();
//                        END
            }
        });
    }

    public void callgetAdunit(String adunitname) {

//        handler.removeCallbacks(delayedRunnable);

        if (UtilsGlobal.ImageDetalList4 != null && !UtilsGlobal.ImageDetalList4.isEmpty()) {
            UtilsGlobal.ImageDetalList4.clear();
        }

        String url = null;
        //URL :- https://api.placeexchange.com/v3/orgs/00cb716a-c7f8-4f14-8244-cf510531e696/adunits
        url = ApiClient.BASE_URL_QT + UtilsGlobal.ORGS + UtilsGlobal.QT_ID + UtilsGlobal.WS_AD_UNIT + "/" + adunitname;
        UtilsGlobal.call_log_WS(getContext(),"Send to QT for Ad_Unit verification",url,"");
        TaskRetrieveAdunit taskretrieveadunit = new TaskRetrieveAdunit(context, getContext(), auth_qt.getAccess_token());
        taskretrieveadunit.execute(url);
    }

    @Override
    public void onRetrieveAdunitResult(List<RetrieveAdunitModel> retrieveAdunitModel, boolean b) {

//        handler.removeCallbacks(delayedRunnable);

        try {
            if (b) {
//            If there is no image then we are calling the computer perfect server and showing the images from computer perfect with the help of Industry type
//                        setnoimage();
                UtilsGlobal.call_log_WS(getContext(), "Ad Unit verification is failed so calling the Computer Perfect : 2", "", "");

                UtilsGlobal.iscomefrom_adrequest_Same = true;
                callcomputerperfectimage();
                Toast.makeText(getActivity(), "Calling computer perfect because there is no resposne in QT", Toast.LENGTH_LONG).show();
//                        END
            } else {

                if (UtilsGlobal.localretrieveAdunitModelList.size() <= 0 && UtilsGlobal.localretrieveAdunitModelList.isEmpty() && UtilsGlobal.localretrieveAdunitModelList == null) {
                    UtilsGlobal.localretrieveAdunitModelList.addAll(retrieveAdunitModel);
                }

                for (int i = 0; i < retrieveAdunitModel.size(); i++) {
                    if (retrieveAdunitModel.get(i).getName() != null && !retrieveAdunitModel.get(i).getName().equals("")) {
                        if (!retrieveAdunitModel.get(i).getAsset().getCapability().isVideo() && retrieveAdunitModel.get(i).getAsset().getCapability().isBanner()) {
                            if (retrieveAdunitModel.get(i).getStatus() == 3 && retrieveAdunitModel.get(i).getStatusDisplay().equals("Live")) {

                                if (UtilsGlobal.adunit_height != 0 && UtilsGlobal.adunit_width != 0) {
                                    UtilsGlobal.adunit_width = 0;
                                    UtilsGlobal.adunit_height = 0;
                                }
                                if (retrieveAdunitModel.get(i).getAdFormats() != null && !retrieveAdunitModel.get(i).getAdFormats().isEmpty()) {
                                    // Check if the adFormats list is not empty
                                    UtilsGlobal.adunit_height = retrieveAdunitModel.get(i).getAdFormats().get(0).getH();
                                    UtilsGlobal.adunit_width = retrieveAdunitModel.get(i).getAdFormats().get(0).getW();
                                } else {
                                    // Handle the case where the adFormats list is empty or null
                                    UtilsGlobal.adunit_height = retrieveAdunitModel.get(i).getSlot().getHeight();
                                    UtilsGlobal.adunit_width = retrieveAdunitModel.get(i).getSlot().getWidth();
                                }
                                Log.e("", "Height: " + UtilsGlobal.adunit_height + "Width: " + UtilsGlobal.adunit_width);
                                callgetAdRequest(retrieveAdunitModel.get(i).getName());
                                numberofcall++;
                            } else {
                                UtilsGlobal.call_log_WS(getContext(), "Ad Unit verification is failed so calling the Computer Perfect : 3", "", "");
                                UtilsGlobal.iscomefrom_adrequest_Same = true;
                                callcomputerperfectimage();
                                Toast.makeText(getActivity(), "Calling computer perfect because there is no resposne in QT", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            callcomputerperfectimage();
            UtilsGlobal.call_log_WS(getContext(), "Ad unit Verify Catch -1", "", "");
        }
    }

    public void callgetAdRequest(String name) {

//        handler.removeCallbacks(delayedRunnable);

        String url = null;
        url = ApiClient.BASE_URL_QT + UtilsGlobal.ORGS + UtilsGlobal.QT_ID + UtilsGlobal.WS_AD_UNIT + "/" + name + UtilsGlobal.WS_SUBMIT_AD_REQUEST;
        UtilsGlobal.call_log_WS(getContext(),"Send to QT for getting the image",url,"");
        TaskGetAdRequest taskGetAdRequest = new TaskGetAdRequest(context, getContext(), auth_qt.getAccess_token(), name);
        taskGetAdRequest.execute(url);
    }

    @Override
    public void onGetAdRequestResult(List<GetAdRequestModel> getAdRequestModel, String ad_unit_name) {

        //        Edited by Varun for when ther eis no response from QT or we don't get any Ad in return then we will show the dummy Ad

        try {
            if (!UtilsGlobal.localgetAdRequestModel.isEmpty() && UtilsGlobal.localgetAdRequestModel != null
                    && UtilsGlobal.localgetAdRequestModel.get(0).getCreative().getName().equals(getAdRequestModel.get(0).getCreative().getName())) {

                UtilsGlobal.call_log_WS(getContext(), "Both images previous and new are same form QT so calling Computer Perfect : 4", "", "");
                UtilsGlobal.iscomefrom_adrequest_Same = true;
                callcomputerperfectimage();

            } else {

                if (getAdRequestModel != null) {
//            Setting the image URL and MIME type
                    if (!UtilsGlobal.localgetAdRequestModel.isEmpty() && UtilsGlobal.localgetAdRequestModel != null) {
                        UtilsGlobal.localgetAdRequestModel.clear();
                    }
                    UtilsGlobal.localgetAdRequestModel.addAll(getAdRequestModel);

                    if (!UtilsGlobal.ImageDetalList3.isEmpty() && UtilsGlobal.ImageDetalList3 != null) {
                        UtilsGlobal.ImageDetalList4.addAll(UtilsGlobal.ImageDetalList3);
                    }

                    displayQT(UtilsGlobal.localgetAdRequestModel, ad_unit_name);

                } else {
//                Used to call when there is only 1 ad return from the API and other 2 are null then it will only show 1 ad rather than no image
                    UtilsGlobal.dismissProgressBar();
//                    If there is no image then we are calling the computer perfect server and showing the images from computer perfect with the help of Industry type
//                        setnoimage();
                    UtilsGlobal.call_log_WS(getContext(), "Null response in QT Ad request so calling computer perfect : 5", "", "");
                    UtilsGlobal.iscomefrom_adrequest_Same = true;
                    callcomputerperfectimage();
                    Toast.makeText(getActivity(), "Calling computer perfect because there is no resposne in QT", Toast.LENGTH_LONG).show();
//                        END
                }
            }
        } catch (Exception e) {
            if (loading != null && loading.isShowing()) {
                loading.dismiss();
            }
            UtilsGlobal.iscomefrom_adrequest_Same = true;
            callcomputerperfectimage();
            UtilsGlobal.call_log_WS(getContext(), "Gone into Ad Request API Catch -2", "", "");
        }
    }

    private void callcomputerperfectimage() {

        Log.e("", "callcomputerperfectimage: "+UtilsGlobal.Computerperfect.size() );
        try {
            if (UtilsGlobal.Computerperfect != null && !UtilsGlobal.Computerperfect.isEmpty()) {
                Log.e("", "2 ");
                UtilsGlobal.iscomefrom_adrequest_Same = false;
                int selectedImageIndex = UtilsGlobal.count;
                if (selectedImageIndex >= UtilsGlobal.Computerperfect.size()) {
                    UtilsGlobal.count = 0;
                    selectedImageIndex = UtilsGlobal.count;
                }
                UtilsGlobal.count++;
                myCustomPagerAdapter = new AdvertiseImageSliderAdapter(getActivity(), Collections.singletonList(UtilsGlobal.Computerperfect.get(selectedImageIndex)));
                binding.viewPage.setAdapter(myCustomPagerAdapter);
                Log.e("", "3 ");

                // Delay in milliseconds (15 seconds in this case)
                long delayMillis;
                delayMillis = (long) (15 * 1000);

                if (handler == null) {
                    handler = new Handler(Looper.getMainLooper());
                }

                if (delayedRunnable!=null){
                    handler.removeCallbacks(delayedRunnable);
                    delayedRunnable=null;
                }

                synchronized (this) {
                    UtilsGlobal.call_log_WS(getContext(),"Another call will be after 15 sec","","");
                    delayedRunnable = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                UtilsGlobal.call_log_WS(getContext(),"Calling The Delay Code Again","","");
                                if (UtilsGlobal.localgetAdRequestModel != null && !UtilsGlobal.localgetAdRequestModel.isEmpty()) {
                                    UtilsGlobal.localgetAdRequestModel.clear();
                                }
                                Toast.makeText(getActivity(), "Calling AGAIN", Toast.LENGTH_SHORT).show();
                                if (UtilsGlobal.unauthorized) {
                                    UtilsGlobal.unauthorized = false;
                                    Auth_QT();
                                } else {
                                    callgetAdunit(UtilsGlobal.store.getId() + "_" + "k1");
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                                String s1 = "Delay Code catch and there is Some Error";
                                callcomputerperfectimage();
                                UtilsGlobal.call_log_WS(getContext(),s1,e.toString(), "");

                            }
                        }
                    };
                    handler.postDelayed(delayedRunnable, delayMillis);

                }
            } else {
                Log.e("", "4 ");
                callImagesWs();
            }
        }catch (Exception e) {
            Log.e("TAG", "Exception in callcomputerperfectimage: " + e.getMessage(), e);
            if (delayedRunnable!=null){
                handler.removeCallbacks(delayedRunnable);
                delayedRunnable = null;
            }
            callcomputerperfectimage();
            UtilsGlobal.call_log_WS(getContext(), "Computer Perfect Catch ", e.toString(), "");
        }
    }

    private void displayQT(List<GetAdRequestModel> localgetAdRequestModel, String ad_unit_name) {

            if (UtilsGlobal.ImageDetalList4 != null && !UtilsGlobal.ImageDetalList4.isEmpty()) {
                UtilsGlobal.call_log_WS(getContext(),"Going For Adapter 4","","");
                Log.e("", "IMAGEDETAILLIST4: "+UtilsGlobal.ImageDetalList4.size());
                myCustomPagerAdapter = new AdvertiseImageSliderAdapter(getActivity(), UtilsGlobal.ImageDetalList4);
                binding.viewPage.setAdapter(myCustomPagerAdapter);
                binding.viewPage.setOffscreenPageLimit(UtilsGlobal.ImageDetalList3.size());
                binding.viewPage.setCurrentItem(0);
//                binding.viewPage.startAutoScroll();
            } else {
                UtilsGlobal.call_log_WS(getContext(),"Null response in QT Ad request so calling computer perfect : 6","","");
                UtilsGlobal.dismissProgressBar();
//                If there is no image then we are calling the computer perfect server and showing the images from computer perfect with the help of Industry type
//                        setnoimage();
                UtilsGlobal.iscomefrom_adrequest_Same =true;
                callcomputerperfectimage();
                Toast.makeText(getActivity(), "Calling computer perfect because there is no resposne in QT", Toast.LENGTH_LONG).show();
//                        END
            }

        for (int i = 0; i < localgetAdRequestModel.size(); i++) {
            if (localgetAdRequestModel.get(i).getCreative() != null) {
//                        ?used to call the proof of play function
                double min_duration = localgetAdRequestModel.get(i).getCreative().getDuration();
                callplays(localgetAdRequestModel.get(i).getContext(), ad_unit_name , min_duration);

            }
        }
//            END
    }

    private void callplays(String scontext, String ad_unit_name, double min_duration) {

        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();

        String url = null;
        url = ApiClient.BASE_URL_QT + UtilsGlobal.ORGS + UtilsGlobal.QT_ID + UtilsGlobal.WS_AD_UNIT + "/" + ad_unit_name + UtilsGlobal.WS_PLAYS;
        UtilsGlobal.call_log_WS(getContext(),"Send to QT",url,"");
        TaskPlay taskPlay = new TaskPlay(scontext, context, getContext(), auth_qt.getAccess_token(), ts , min_duration);
        taskPlay.execute(url);

    }

    @Override
    public void onGetplay(Object o, Double min_duration) {

        long delayMillis = (long) (min_duration * 1000);

        if (delayedRunnable!=null){
            handler.removeCallbacks(delayedRunnable);
            delayedRunnable=null;
        }
        delayedRunnable = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), "Calling AGAIN", Toast.LENGTH_SHORT).show();
                callgetAdunit(UtilsGlobal.store.getId() + "_"+"k1");
//                callgetAdunit(UtilsGlobal.store.getId() + "_"+"11");
            }
        };

        handler.postDelayed(delayedRunnable, delayMillis);

    }

    public void setnoimage() {

        UtilsGlobal.ImageDetalList = new ArrayList<>();
        AdvSign imagesDetailModel = new AdvSign();
        imagesDetailModel.setName("Home");
        imagesDetailModel.setId("-1");
        imagesDetailModel.setName("no image");
        imagesDetailModel.setUrl("no image");
        UtilsGlobal.ImageDetalList.add(imagesDetailModel);

        if (binding != null && UtilsGlobal.ImageDetalList != null) {
            Log.e("tracking", "onGetImagesDetailsResult: 2");
            myCustomPagerAdapter = new AdvertiseImageSliderAdapter(getActivity(), UtilsGlobal.ImageDetalList);
            binding.viewPage.setAdapter(myCustomPagerAdapter);
            binding.viewPage.setOffscreenPageLimit(UtilsGlobal.ImageDetalList.size());
        } else {
            Log.e("tracking", "onGetImagesDetailsResult: 3");
            // Handle the case when binding or UtilsGlobal.ImageDetalList is null
            // Log an error or perform alternative actions
        }

    }

//    public void fetchAllSigns() {
//        Call<ArrayList<Sign>> call1 = apiServiceBillboard.GetSigns(ApiClient.authHeader);
//        call1.enqueue(new Callback<ArrayList<Sign>>() {
//            @Override
//            public void onResponse(Call<ArrayList<Sign>> call, Response<ArrayList<Sign>> response) {
//                if (response.body() != null) {
//                    if (response.body().size() > 0) {
//                        for (int i = 0; i < response.body().size(); i++) {
//                            if (response.body().get(i).getName().contains("KIOSK_" + android_id)) {
//                                ApiClientBillBoard.signId = response.body().get(i).getId();
//                                MainActivity.contex.setVersion();
//                                UtilsGlobal.saveSign(getActivity(), response.body().get(i));
//                                break;
//                            }
//                        }
//                        if (ApiClientBillBoard.signId != null) {
//                            if (ApiClientBillBoard.signId.trim().length() > 0) {
//                                if (UtilsGlobal.isNetworkAvailable(getActivity())) {
//                                    scheduleAsveCalls();
//                                } else {
//                                    Log.e("", "IMAGE: 12 13");
//                                    setDummyAdvertisements();
//                                }
//                            } else {
//                                getLocationFromAddress(address);
//                            }
//                        } else {
//                            getLocationFromAddress(address);
//                        }
//
//                    } else {
//                        getLocationFromAddress(address);
//                    }
//                } else {
//                    getLocationFromAddress(address);
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<Sign>> call, Throwable t) {
//                call.cancel();
//                Log.e("", "IMAGE: 12 12");
//                setDummyAdvertisements();
//
//            }
//        });
//
//    }
//
//    public void createNewSignID() {
//        Sign signNew = new Sign();
//        signNew.setEnabled(true);
//        signNew.setName(signName);
//        signNew.setDescription("CP for " + UtilsGlobal.store.getId() + "[" + UtilsGlobal.address + "]");
//        signNew.setLatitude(String.valueOf(latitude));
//        signNew.setLongitude(String.valueOf(longitude));
//        signNew.setLocation(address);
//        signNew.setHeight(1080);
//        signNew.setWidth(960);
//        TimeZone tz = TimeZone.getDefault();
//        signNew.setTimezone(tz.getID());
//        signNew.setDaily_impressions("100");
//        signNew.setSeconds_per_flip("" + UtilsGlobal.timeForFlips);
//        Gson gson = new Gson();
//        String jsonStr = gson.toJson(signNew);
//        RequestBody body = RequestBody.create(jsonStr,
//                MediaType.parse("application/json")
//        );
//        Call<Sign> call1 = apiServiceBillboard.CreateSigns(ApiClient.authHeader, body);
//        call1.enqueue(new Callback<Sign>() {
//            @Override
//            public void onResponse(Call<Sign> call, Response<Sign> response) {
//                if (response.body() != null) {
//                    //Log.d("TEST", "AdvSignS " + response.body());
//                    //pick a sign
//                    //save to local
//                    if (response.body().getId() != null) {
//                        if (response.body().getId().trim().length() > 0) {
//                            ApiClientBillBoard.signId = response.body().getId();
//                            UtilsGlobal.saveSign(getActivity(), response.body());
//                            MainActivity.contex.setVersion();
//                            if (UtilsGlobal.isNetworkAvailable(getActivity())) {
//                                scheduleAsveCalls();
//                            } else {
//                                Log.e("", "IMAGE: 12 11");
//                                setDummyAdvertisements();
//                            }
//                        } else {
//                            Log.e("", "IMAGE: 12 10");
//                            setDummyAdvertisements();
//                        }
//                    } else {
//                        Log.e("", "IMAGE: 12 9");
//                        setDummyAdvertisements();
//                    }
//
//                } else {
//                    Log.e("", "IMAGE: 12 8");
//                    setDummyAdvertisements();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<Sign> call, Throwable t) {
//                call.cancel();
//                Log.e("", "IMAGE: 12 7");
//                setDummyAdvertisements();
//
//            }
//        });
//    }
//
//    public void scheduleAsveCalls() {
//        //fetch advs first call
//        if (!UtilsGlobal.getTimeToRefresh(getActivity(), false)) {
//            ArrayList<AdvSign> advLists = UtilsGlobal.getImages(getActivity());
//            if (advLists != null) {
//                if (advLists.size() > 0) {
//                    if (myCustomPagerAdapter == null) {
//                        myCustomPagerAdapter = new AdvertiseImageSliderAdapter(getActivity(), advLists);
//                        binding.viewPage.setAdapter(myCustomPagerAdapter);
//                        binding.viewPage.startAutoScroll();
//                    } else {
//                        myCustomPagerAdapter.setData(advLists);
//                    }
//
//                } else {
//                    Log.e("", "IMAGE: 12 6");
//                    setDummyAdvertisements();
//
//                }
//            } else {
//                Log.e("", "IMAGE: 12 5");
//                setDummyAdvertisements();
//
//            }
//        }
//
//        ScheduledExecutorService exec2 = Executors.newSingleThreadScheduledExecutor();
//        exec2.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                // do stuff
//                precacheBlipAdv();
//            }
//        }, 0, 30, TimeUnit.SECONDS);
//        ScheduledExecutorService exec1 = Executors.newSingleThreadScheduledExecutor();
//        exec1.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                // do stuff
//                if (isAllowLoading) {
//                    if (UtilsGlobal.getTimeToRefresh(getActivity(), false)) {
//                        if (ApiClientBillBoard.signId != null) {
//                            if (ApiClientBillBoard.signId.trim().length() > 0) {
//                                fetchAdvFromBlip();
//                            }
//                        }
//                    }
//                }
//            }
//        }, 0, 1, TimeUnit.MINUTES);
//
//    }
//
//    public void fetchAdvFromBlip() {
//        //Log.d("TEST", "FLIP");
//        binding.viewPage.stopAutoScroll();
//        isAllowLoading = false;
//        dateList = new ArrayList<>();
//        TimeZone tz = TimeZone.getTimeZone("UTC");
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
//        DateFormat dfParse = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//        df.setTimeZone(tz);
//        String nowAsISO = df.format(new Date());
//        Date seldate = new Date();
//        dateList.add(nowAsISO);
//        Calendar cal = Calendar.getInstance();
//        cal.setTimeZone(tz);
//        cal.setTime(seldate);
//        cal.add(Calendar.SECOND, 10);
//        seldate = cal.getTime();
//        nowAsISO = df.format(seldate);
//        try {
//            seldate = dfParse.parse(nowAsISO);
//        } catch (ParseException e) {
//            e.printStackTrace();
//            seldate = new Date();
//        }
//        dateList.add(nowAsISO);
//        Calendar cal1 = Calendar.getInstance();
//        cal1.setTimeZone(tz);
//        cal1.setTime(seldate);
//        cal1.add(Calendar.SECOND, 10);
//        seldate = cal1.getTime();
//        nowAsISO = dfParse.format(seldate);
//        dateList.add((nowAsISO + "+0000"));
//        Calendar cal2 = Calendar.getInstance();
//        cal2.setTimeZone(tz);
//        cal2.setTime(seldate);
//        cal2.add(Calendar.SECOND, 10);
//        seldate = cal2.getTime();
//        nowAsISO = dfParse.format(seldate);
//        dateList.add(nowAsISO + "+0000");
//        Calendar cal3 = Calendar.getInstance();
//        cal3.setTimeZone(tz);
//        cal3.setTime(seldate);
//        cal3.add(Calendar.SECOND, 10);
//        seldate = cal3.getTime();
//        nowAsISO = dfParse.format(seldate);
//        dateList.add(nowAsISO + "+0000");
//        Calendar cal4 = Calendar.getInstance();
//        cal4.setTimeZone(tz);
//        cal4.setTime(seldate);
//        cal4.add(Calendar.SECOND, 10);
//        seldate = cal4.getTime();
//        nowAsISO = dfParse.format(seldate);
//        dateList.add(nowAsISO + "+0000");
//        UtilsGlobal.saveTimeToRefresh(getActivity(), nowAsISO);
//        Gson gson = new Gson();
//        String jsonStr = gson.toJson(dateList);
//        Log.d("ms", "" + jsonStr);
//        RequestBody body = RequestBody.create(jsonStr,
//                MediaType.parse("application/json")
//        );
//        Log.d("TEST", "" + dateList);
//        Call<ArrayList<AdvSign>> call1 = apiServiceBillboard.signSignIdImagesGetschedule(ApiClientBillBoard.signId, ApiClient.authHeader, body);
//        call1.enqueue(new Callback<ArrayList<AdvSign>>() {
//            @Override
//            public void onResponse(Call<ArrayList<AdvSign>> call, Response<ArrayList<AdvSign>> response) {
//                if (!isAdded())
//                    return;
//                try {
//                    advCount = 0;
//                    isAllowLoading = true;
//                    advLists = new ArrayList<>();
//                    Log.d("Test adv", "" + jsonStr);
//                    Log.d("Test adv", "" + response.body());
//                    if (response.body() != null) {
//                        if (response.body().size() > 0) {
//                            advLists = response.body();
//                            try {
//                                Toast.makeText(getActivity(), "Adv refresh!!!", Toast.LENGTH_SHORT).show();
//                            } catch (Exception e) {
//                            }
//                            if (advLists != null) {
//                                if (advLists.size() > 0) {
//                                    myCustomPagerAdapter = new AdvertiseImageSliderAdapter(getActivity(), advLists);
//                                    binding.viewPage.setAdapter(myCustomPagerAdapter);
//                                    binding.viewPage.startAutoScroll();
//                                }
//                            }
//
//                        } else {
//                            Log.e("", "IMAGE: 12 4");
//                            setDummyAdvertisements();
//                            try {
//                                Toast.makeText(getActivity(), "Adv refresh!!! Error from blipboard", Toast.LENGTH_SHORT).show();
//                            } catch (Exception e) {
//                            }
//                        }
//                    } else {
//                        if (myCustomPagerAdapter == null) {
//                            Log.e("", "IMAGE: 12 3");
//                            setDummyAdvertisements();
//
//                        }
//                        try {
//                            Toast.makeText(getActivity(), "Adv refresh!!! Error from blipboard", Toast.LENGTH_SHORT).show();
//                        } catch (Exception e) {
//                        }
//                    }
//                    //entry
//                } catch (Exception e) {
//                    if (myCustomPagerAdapter == null) {
//                        Log.e("", "IMAGE: 12 2");
//                        setDummyAdvertisements();
//                    }
//                }
//                String responseVals = "No ads!";
//                String isPassFail = "fail";
//                try {
//                    if (response.body() == null) {
//                        if (response.errorBody() != null) {
//                            if (response.errorBody().toString().length() > 0) {
//                                responseVals = response.errorBody().toString();
//                            } else {
//                                responseVals = "No ads retrived!";
//                            }
//                        } else {
//                            responseVals = "No ads retrived!";
//                        }
//                    } else if (response.body().size() <= 0) {
//                        responseVals = "No ads retrived!";
//                    } else {
//                        isPassFail = "pass";
//                        responseVals = gson.toJson(response.body());
//                    }
//                } catch (Exception e) {
//                }
//                ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
//                Call<Result> call1 = apiService.PoleDisplayErrorLogSave(UtilsGlobal.store.getId(), jsonStr, responseVals, android_id, ApiClientBillBoard.signId, isPassFail);
//                call1.enqueue(new Callback<Result>() {
//                    @Override
//                    public void onResponse(Call<Result> call, Response<Result> response) {
//                    }
//
//                    @Override
//                    public void onFailure(Call<Result> call, Throwable t) {
//                        call.cancel();
//
//                    }
//                });
//                try {
//                    UtilsGlobal.saveImages(getActivity(), advLists);
//                } catch (Exception e) {
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<AdvSign>> call, Throwable t) {
//                call.cancel();
//                try {
//                    isAllowLoading = true;
//                    Log.e("", "IMAGE: 12 1");
//                    setDummyAdvertisements();
//                    try {
//                        String responseVals = "In Failure: " + t.getMessage();
//                        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
//                        Call<Result> call1 = apiService.PoleDisplayErrorLogSave(UtilsGlobal.store.getId(), jsonStr, responseVals, android_id, ApiClientBillBoard.signId, "fail");
//                        call1.enqueue(new Callback<Result>() {
//                            @Override
//                            public void onResponse(Call<Result> call, Response<Result> response) {
//                            }
//
//                            @Override
//                            public void onFailure(Call<Result> call, Throwable t) {
//                                call.cancel();
//
//                            }
//                        });
//                    } catch (Exception e) {
//                    }
//                } catch (Exception e) {
//                }
//            }
//        });
//    }
//
//    public void precacheBlipAdv() {
//        //Log.d("TEST", "Pre FLIP");
//        Call<ArrayList<AdvSign>> call1 = apiServiceBillboard.signSignIdImagesGet(ApiClientBillBoard.signId, ApiClient.authHeader);
//        call1.enqueue(new Callback<ArrayList<AdvSign>>() {
//            @Override
//            public void onResponse(Call<ArrayList<AdvSign>> call, Response<ArrayList<AdvSign>> response) {
//                try {
//                    if (response.body() != null) {
//                        if (response.body().size() > 0) {
//                            ArrayList<AdvSign> advLists = response.body();
//                            count = advLists.size();
//                            for (int i = 0; i < count; i++) {
//                                Glide.with(getActivity())
//                                        .load(advLists.get(i).getUrl())
//                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                        .priority(Priority.LOW)
//                                        .preload(960, 1080);
//                            }
//                        }
//                    }
//                } catch (Exception e) {
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<AdvSign>> call, Throwable t) {
//                call.cancel();
//            }
//        });
//    }
//
//    public void recordFlips(String advID) {
//        ArrayList<AdvSign> flipDataList = new ArrayList<>();
//        AdvSign sign = new AdvSign();
//        sign.setId(advID);
//        TimeZone tz = TimeZone.getTimeZone("UTC");
//        sign.setDuration("" + UtilsGlobal.timeForFlips);
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
//        df.setTimeZone(tz);
//        String nowAsISO = df.format(new Date());
//        Log.d("TEST", "Checking time flip DATE IS: " + nowAsISO);
//        sign.setStart(nowAsISO);
//        flipDataList.add(sign);
//        Gson gson = new Gson();
//        String jsonStr = gson.toJson(flipDataList);
//        RequestBody body = RequestBody.create(jsonStr,
//                MediaType.parse("application/json")
//        );
//        ApiInterface apiServiceBillboard = ApiClientBillBoard.getClient().create(ApiInterface.class);
//        Call<Result> call1 = apiServiceBillboard.recordFlips(ApiClientBillBoard.signId, ApiClient.authHeader, body);
//        call1.enqueue(new Callback<Result>() {
//            @Override
//            public void onResponse(Call<Result> call, Response<Result> response) {
//            }
//
//            @Override
//            public void onFailure(Call<Result> call, Throwable t) {
//                call.cancel();
//            }
//        });
//
//    }

//    public void getLocationFromAddress(String strAddress) {
//        Geocoder coder = new Geocoder(getActivity());
//        List<Address> address;
//        try {
//            address = coder.getFromLocationName(strAddress, 5);
//            if (address != null) {
//                Address location = address.get(0);
//                if (location.getLatitude() > 0) {
//                    latitude = String.valueOf(Location.convert(location.getLatitude(), Location.FORMAT_DEGREES));
//                    longitude = Location.convert(location.getLongitude(), Location.FORMAT_DEGREES);
//                }
//                createNewSignID();
//            } else {
//                createNewSignID();
//            }
//
//        } catch (Exception e) {
//            createNewSignID();
//        }
//    }

    public void setDummyAdvertisements() {
        advLists = new ArrayList<>();

        AdvSign adv1 = new AdvSign();
        adv1.setUrl("dummy1");
        adv1.setId("-1");
        advLists.add(adv1);

        adv1 = new AdvSign();
        adv1.setUrl("dummy2");
        adv1.setId("-1");
        advLists.add(adv1);

        adv1 = new AdvSign();
        adv1.setUrl("dummy3");
        adv1.setId("-1");
        advLists.add(adv1);

        myCustomPagerAdapter = new AdvertiseImageSliderAdapter(getActivity(), advLists);
        binding.viewPage.setAdapter(myCustomPagerAdapter);
        binding.viewPage.startAutoScroll();
    }

    public void callImagesWs() {

        if (UtilsGlobal.ImageDetalList != null) {
            UtilsGlobal.ImageDetalList.clear();
        }

//        Setting the Industry type in the Store type for getting the Computer perfect images
        if (UtilsGlobal.Computerperfect!=null && !UtilsGlobal.Computerperfect.isEmpty()){
            UtilsGlobal.Computerperfect.clear();
        }
        if (UtilsGlobal.iscomefrom_adrequest_Same || UtilsGlobal.isFromQT){
//            UtilsGlobal.type = "Computer Perfect-Wine & Spirit";
            UtilsGlobal.type = UtilsGlobal.Industry_Type;
        }
//            END

        String Urlban = ApiClient.WS_BASE_URL + ApiClient.GETPOLE_IMAGES_DETAIL + UtilsGlobal.store.getId() + "/" + UtilsGlobal.type;
        UtilsGlobal.call_log_WS(getContext(),"Sending Request to Computer Perfect Server",Urlban,"");;
        TaskImages taskImages = new TaskImages((TaskImages.TaskImagesEvent) context, getContext());
        taskImages.execute(Urlban);

    }

    @Override
    public void onGetImagesDetailsResult(List<AdvSign> ImageDetalList, boolean b) {
        Log.e("tracking", "onGetImagesDetailsResult: 5");

        if (UtilsGlobal.iscomefrom_adrequest_Same && !b){
            Log.e("", "1");
            UtilsGlobal.call_log_WS(getContext(),"Going Back to Computer perfect function because to show the images","","");
            callcomputerperfectimage();
            Log.e("tracking", "onGetImagesDetailsResult: 10");
        }else {
            if (!b) {
                Log.e("", "onGetImagesDetailsResult: " + UtilsGlobal.ImageDetalList.size());
                myCustomPagerAdapter = new AdvertiseImageSliderAdapter(getActivity(), UtilsGlobal.ImageDetalList);
                binding.viewPage.setAdapter(myCustomPagerAdapter);
                binding.viewPage.setOffscreenPageLimit(UtilsGlobal.ImageDetalList.size());
                binding.viewPage.setCurrentItem(0);
                binding.viewPage.startAutoScroll();
            } else {
//                setDummyAdvertisements();\
                Log.e("tracking", "onGetImagesDetailsResult: 1");
                setnoimage();
            }
        }
    }

}