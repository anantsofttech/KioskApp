package com.anantkiosk.kioskapp.Task;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.anantkiosk.kioskapp.Model.AdvSign;
import com.anantkiosk.kioskapp.Model.ImagesDetailModel;
import com.anantkiosk.kioskapp.R;
import com.anantkiosk.kioskapp.Utils.UtilsGlobal;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class TaskImages extends AsyncTask<String, Void, String> {
    Context context;
    ProgressDialog loading = null;
    int countTotal = 0, countForImgae = 0;
    TaskImagesEvent taskImagesEvent;

    @SuppressLint("ResourceType")
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            if (context != null) {
                if (!UtilsGlobal.isFromQT) {
                    loading = new ProgressDialog(context, R.style.MyprogressDTheme);
                    loading.setCancelable(false);
                    loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    loading.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public TaskImages(TaskImagesEvent taskImagesEvent, Context context) {
        this.taskImagesEvent = taskImagesEvent;
        this.context = context;
    }

    public interface TaskImagesEvent {
        void onGetImagesDetailsResult(List<AdvSign> ImageDetalList, boolean b);
    }


    @Override
    protected String doInBackground(String... strings) {
        Log.i("web service--Cart", "request url : " + strings[0]);
        int count = 0;
        boolean retry = false;
        StringBuilder responseStrBuilder = new StringBuilder();
        do {
            retry = false;
            try {
                UtilsGlobal.doNetworkProcessGet(strings[0], responseStrBuilder);
                String response = responseStrBuilder.toString();
                Log.e("tracking", "onGetImagesDetailsResult: 7"+response);
                if (response != null) {
                    JSONArray jsonArray = new JSONArray(response);
                    countTotal = 0;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject explrObject = jsonArray.getJSONObject(i);
                        // commented below line for displaying centralized img with imgpath
                        String url_img = explrObject.getString("Image");
                        String url = explrObject.getString("ImagePath");
                        // end **************
                        String storeno = explrObject.getString("StoreNo");
                        String BannerType = explrObject.getString("BannerType");
                        ImagesDetailModel model = new ImagesDetailModel();
                        model.setId(url_img);
                        model.setUrl(url);
                        model.setStoreNo(storeno);
                        model.setBannerType(BannerType);


                        if (BannerType.equals("Home") ) {

//                          Edited by Varun for
                            if (model.getUrl()!=null && !model.getUrl().equals("")){
                                countTotal++;
//                                ?END
                                UtilsGlobal.ImageDetalList.add(model);
                                UtilsGlobal.Computerperfect.add(model);
                            }
                        }
                    }
                }
                Log.e("tracking", "onGetImagesDetailsResult: 6");
                UtilsGlobal.call_log_WS(context, "Receive Response from Computer Perfect Server", strings[0], response);
                return response;

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            retry = true;
            count += 1;
        } while (count < 3 && retry);

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
            if (UtilsGlobal.ImageDetalList.size() == 0) {
//               TRUE when images is not coming from lighting server
                if (loading != null && loading.isShowing()) {
                    //Log.d("kaveriImage","Stop loader");
                    loading.dismiss();
                }
                Log.e("tracking", "onGetImagesDetailsResult: 8");
                    taskImagesEvent.onGetImagesDetailsResult(UtilsGlobal.ImageDetalList,true);

            }else{
                if (loading != null && loading.isShowing()) {
                    //Log.d("kaveriImage","Stop loader");
                    loading.dismiss();
                }
                Log.e("tracking", "onGetImagesDetailsResult: 9");
                    taskImagesEvent.onGetImagesDetailsResult(UtilsGlobal.ImageDetalList,false);

            }
    }
}
