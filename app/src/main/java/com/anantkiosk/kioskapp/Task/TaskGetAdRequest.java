package com.anantkiosk.kioskapp.Task;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.anantkiosk.kioskapp.Home.HomeFragments.AdvertisementFragment;
import com.anantkiosk.kioskapp.Model.AdvSign;
import com.anantkiosk.kioskapp.Model.GetAdRequestModel;
import com.anantkiosk.kioskapp.Utils.UtilsGlobal;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class TaskGetAdRequest extends AsyncTask<String, Void, GetAdRequestModel> {

    TaskGetAdRequestEvent taskGetAdRequestEvent;
    Context context;
    GetAdRequestModel getAdRequestModel;
    ProgressDialog loading = null;
    private String authToken;
    int countTotal =0;
    String response;;
    String ad_unit_name;;

    String imageUrl;
    String imagemime;

    public TaskGetAdRequest(AdvertisementFragment splashActivity, Context context, String access_token, String name) {
        this.taskGetAdRequestEvent = (TaskGetAdRequestEvent) splashActivity;
        this.context = context;
        this.authToken=access_token;
        this.ad_unit_name = name;
    }

    public interface TaskGetAdRequestEvent{
        void onGetAdRequestResult(List<GetAdRequestModel> getAdRequestModel, String ad_unit_name);
    }


    @Override
    protected GetAdRequestModel doInBackground(String... strings) {
        Log.i("web service--GetAdunit", "request url : " + strings[0]);

        response = UtilsGlobal.performNetworkRequest(strings[0], authToken);

        //        Edited by varun
        //        Added this code for only to take 1920*1080 size image to display.

        try {
            if (response != null && !response.isEmpty() && response.equals("null")) {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray snapshotsArray = jsonObject.getJSONObject("creative").getJSONArray("snapshots");

                JSONObject desiredSnapshot = null;
                UtilsGlobal.imageMime="";
                UtilsGlobal.imageURL="";

                if (snapshotsArray != null && snapshotsArray.length() >= 0) {

                    for (int i = 0; i < snapshotsArray.length(); i++) {
                        JSONObject snapshot = snapshotsArray.getJSONObject(i);
                        double scalingFactor = snapshot.getDouble("scaling_factor");
                        int height = snapshot.getInt("h");
                        int width = snapshot.getInt("w");
                        String mime = snapshot.getString("mime");

//                    if (height == 1920 && width == 1080 && mime.contains("jpeg")) {
//                        desiredSnapshot = snapshot;
//                        Log.e("", "taken:1");
//                        break;
//                    }
                        if (height == UtilsGlobal.adunit_height && width == UtilsGlobal.adunit_width && mime.contains("jpeg")) {
                            Log.e("", "desired snapshot: from comparing width and height" );
                            desiredSnapshot = snapshot;
                            break;
                        } else if (scalingFactor == 1.0 && mime.equals("image/jpeg")) {
                            Log.e("", "desired snapshot: from comparing Scaling factor" );
                            desiredSnapshot = snapshot;
                            break;
                        }
                    }

                }

                if (desiredSnapshot != null) {
                    if (desiredSnapshot.has("iurl")) {
                        imageUrl = desiredSnapshot.getString("iurl");
                        Log.e("", "doInBackground: 123 " + imageUrl);
                    } else if (desiredSnapshot.has("curl")) {
                        imageUrl = desiredSnapshot.getString("curl");
                    }
                    imagemime = desiredSnapshot.getString("mime");

                    UtilsGlobal.imageURL = imageUrl;
                    UtilsGlobal.imageMime = imagemime;

                }
//                else {
////                    This is called when the desired snapshot will not be there
//                    Log.e("", "Doing repsonse null because desired snap shot is not found ");
//                    response = "";
//                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //        END

        Log.i("web service--GetAdunit", "response: " + response);

        // Parse the JSON response using Jackson ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {

            if (response==null || response.isEmpty() || response.equals("null")){
                // Handle the case when the response is null
                UtilsGlobal.call_log_WS(context,"Receive from QT for Ad request", strings[0], response);
                getAdRequestModel = null;
            }else if (response.equals("Unauthorized")){
                UtilsGlobal.call_log_WS(context,"Receive from QT for Ad request", strings[0], response);
                UtilsGlobal.unauthorized=true;
                getAdRequestModel = null;
            }else{
                UtilsGlobal.call_log_WS(context,"Receive from QT for Ad request", strings[0], response);
                getAdRequestModel = objectMapper.readValue(response, GetAdRequestModel.class);
            }
        } catch (IOException e){
            getAdRequestModel = null;
            e.printStackTrace();
            UtilsGlobal.call_log_WS(context,"Ad request Task Catch", strings[0], response);
        }

        return getAdRequestModel;
    }


    @Override
    protected void onPostExecute(GetAdRequestModel s) {
        super.onPostExecute(s);

        if (s!=null) {
            UtilsGlobal.call_log_WS(context,"Going to the Ad Request response handling in onGetAdRequestResult Function-1","","");
            if (taskGetAdRequestEvent != null && taskGetAdRequestEvent != null) {
                // Convert the single object into a list
                List<GetAdRequestModel> resultList = new ArrayList<>();
                resultList.add(getAdRequestModel);

                taskGetAdRequestEvent.onGetAdRequestResult(resultList,ad_unit_name);
            }
        }else {
            UtilsGlobal.call_log_WS(context,"Going to the Ad Request response handling in onGetAdRequestResult Function-2","","");
            taskGetAdRequestEvent.onGetAdRequestResult(null,ad_unit_name);
        }
    }

}

