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

        response = performNetworkRequest(strings[0], authToken);

        //        Edited by varun
        //        Added this code for only to take 1920*1080 size image to display.

        try {
            if (response != null && !response.isEmpty()) {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray snapshotsArray = jsonObject.getJSONObject("creative").getJSONArray("snapshots");

                JSONObject desiredSnapshot = null;
                UtilsGlobal.imageMime="";
                UtilsGlobal.imageURL="";

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
                        desiredSnapshot = snapshot;
                        break;
                    } else if (scalingFactor == 1.0 && mime.equals("image/jpeg")) {
                        desiredSnapshot = snapshot;
                        break;
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

                } else {
                    // Handle the case where a snapshot with a height of 1080 is not found
                }
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
            if (response != null && !response.isEmpty()) {
                getAdRequestModel = objectMapper.readValue(response, GetAdRequestModel.class);
            } else {
                // Handle the case when the response is null
                getAdRequestModel = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return getAdRequestModel;
    }


    @Override
    protected void onPostExecute(GetAdRequestModel s) {
        super.onPostExecute(s);

        if (s!=null) {
            AdvSign imagesDetailModel = new AdvSign();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                imagesDetailModel.setUrl(imageUrl);
            } else {
                imagesDetailModel.setUrl(imageUrl);
            }
            imagesDetailModel.setId(imagemime);
//        Constant.ImageDetalList2.clear();
            UtilsGlobal.ImageDetalList2.add(imagesDetailModel);
            if (!UtilsGlobal.ImageDetalList3.isEmpty()&&UtilsGlobal.ImageDetalList3!=null){
                UtilsGlobal.ImageDetalList3.clear();
            }
            UtilsGlobal.ImageDetalList3.add(imagesDetailModel);
            String urlImageinner = "";
            if (imageUrl != null && !imageUrl.isEmpty()) {
                urlImageinner = imageUrl;
            } else {
                urlImageinner = imageUrl;
            }
            try {
                Glide.with(context).asDrawable().load(urlImageinner).dontTransform().priority(Priority.LOW).fitCenter().preload(800, 1280);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            UtilsGlobal.saveImages(context, UtilsGlobal.ImageDetalList2, "QT", UtilsGlobal.STOREID);

            UtilsGlobal.LocalImageDetalList2 = new ArrayList<>();

            if (!UtilsGlobal.LocalImageDetalList2.isEmpty() && UtilsGlobal.LocalImageDetalList2!=null){
                UtilsGlobal.LocalImageDetalList2.clear();
            }

            for (int j = 0; j < UtilsGlobal.ImageDetalList2.size(); j++) {
                AdvSign imagesDetailModel1 = UtilsGlobal.ImageDetalList2.get(j);
                // commented below line for displaying centralized img with imgpath
//                    String urlDetail = Constant.IMG_BASE + Constant.IMG_BANNER_URL + imagesDetailModel.getStoreNo() + "/" + imagesDetailModel.getImage();
                String urlDetail = imagesDetailModel1.getUrl().toString().trim();

                // aaded to prevent app crashes when image=""
                String imgName = "";
                String extention = "";

                if (imagesDetailModel1.getId() != null && !imagesDetailModel1.getId().isEmpty()) {
                    String[] split = imagesDetailModel1.getId().split("/");
//                    String[] split = imagesDetailModel.getImagepath().toString().split("\\.");
                    imgName = split[0];
                    extention = split[1];
                }
                // end **********************

                File tempFile = null;
                File sdcard = null;
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    //Do something
                    sdcard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                } else {
                    sdcard = Environment.getExternalStorageDirectory();
                }
                tempFile = new File(sdcard + "/NewQT/" + j + "_" +
                        imgName +
                        "." + extention);
                File dir = tempFile.getParentFile();
                try {
                    if (!dir.exists())
                        dir.mkdirs();
                    if (!tempFile.exists()) {
                        tempFile.createNewFile();
                    } else {
                        tempFile.delete();
                        tempFile = new File(sdcard + "/NewQT/" + j + "_" +
                                imgName +
                                "." + extention);
                        tempFile.createNewFile();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
                if (tempFile.exists()) {
                    AdvSign model = new AdvSign();
                    model.setName("Home");
                    model.setId(tempFile.getAbsolutePath());
                    model.setUrl(tempFile.getAbsolutePath());
//                        Constant.LocalImageDetalList.add(i, model); //crashed here
                    UtilsGlobal.LocalImageDetalList2.add(model); //edited by janvi on 03/31/2023

                    if (UtilsGlobal.LocalImageDetalList2.size() >= UtilsGlobal.ImageDetalList2.size()) {
//                        UtilsGlobal.saveImages(context, UtilsGlobal.LocalImageDetalList2, "QT", UtilsGlobal.STOREID);
                    }
                    //Log.d("kaveriImage", "SAVED IN LOCAL home");
                    saveImage(context, urlDetail, tempFile);
                }
            }

            if (taskGetAdRequestEvent != null && taskGetAdRequestEvent != null) {
                // Convert the single object into a list
                List<GetAdRequestModel> resultList = new ArrayList<>();
                resultList.add(getAdRequestModel);

                taskGetAdRequestEvent.onGetAdRequestResult(resultList,ad_unit_name);
            }
        }else {
            // Handle the case when the response is null
            taskGetAdRequestEvent.onGetAdRequestResult(null,ad_unit_name);
        }

    }

    public void saveImage(Context mContext, String urlDetail, File tempFile) {
        if (mContext != null) {
            Glide.with(mContext)
                    .asBitmap()
                    .load(urlDetail)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            write(resource, tempFile);
                        }

                        @Override
                        public void onLoadCleared(Drawable placeholder) {
                        }
                    });
        }
    }

    public void write(Bitmap bitmap, File tempFile) {
        OutputStream outStream = null;
        try {
            outStream = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, outStream);
            outStream.close();

            // Perform required operations when all images are saved
            UtilsGlobal.ImageDetalList2 = new ArrayList<>();
//                Constant.ImageHeaderList = new ArrayList<>();
//                Constant.ImageFooterList = new ArrayList<>();
            UtilsGlobal.ImageDetalList2.addAll(UtilsGlobal.LocalImageDetalList2);
//                Constant.ImageHeaderList.addAll(Constant.LocalImageHeaderList);
//                Constant.ImageFooterList.addAll(Constant.LocalImageFooterList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String performNetworkRequest(String string, String authToken) {

        String response = null;

        try {
            URL url = new URL(string);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set request method and headers
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + authToken);


            // Get the response code
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                response = stringBuilder.toString();

                // Close the streams
                bufferedReader.close();
                inputStream.close();
            }

            // Disconnect the connection
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

}

