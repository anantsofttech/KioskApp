package com.anantkiosk.kioskapp.Task;

import android.os.AsyncTask;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.anantkiosk.kioskapp.Home.HomeFragments.AdvertisementFragment;
import com.anantkiosk.kioskapp.Model.RetrieveAdunitModel;
import com.anantkiosk.kioskapp.Utils.UtilsGlobal;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;


public class TaskRetrieveAdunit extends AsyncTask<String, Void, List<RetrieveAdunitModel>> {


    TaskRetrieveAdunitEvent taskRetrieveAdunitEvent;
    Context context;
    RetrieveAdunitModel retrieveAdunitModel; // Change this to RetrieveAdunitModel
    ProgressDialog loading = null;
    private String authToken;
    URL url ;

    public TaskRetrieveAdunit(AdvertisementFragment context1, Context context, String access_token) {
        this.taskRetrieveAdunitEvent = (TaskRetrieveAdunitEvent) context1;
        this.context = context;
        this.authToken = access_token;
    }



    public interface TaskRetrieveAdunitEvent {
        void onRetrieveAdunitResult(List<RetrieveAdunitModel> retrieveAdunitModel, boolean b);
    }


    @Override
    protected List<RetrieveAdunitModel> doInBackground(String... strings) {
        Log.i("web service--GetAdunit", "request url : " + strings[0]);

        String response = performNetworkRequest(strings[0], authToken);

        Log.i("web service--GetAdunit", "response: " + response);

        // Parse the JSON response using Jackson ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {

            if (response == null || response.isEmpty() || response.equals("null")) {
                UtilsGlobal.call_log_WS(context,"Receive from QT for Ad_Unit verification", url.toString(), response);
                return Collections.emptyList();
            } else if (response.equals("Unauthorized")) {
                UtilsGlobal.unauthorized = true;
                UtilsGlobal.call_log_WS(context,"Receive from QT for Ad_Unit verification", url.toString(), response);
                return Collections.emptyList();
            } else if (response.equals("Not Found")) {
                UtilsGlobal.ad_unit_not_found = true;
                UtilsGlobal.call_log_WS(context,"Receive from QT for Ad_Unit verification", url.toString(), response);
                return Collections.emptyList();
            } else {
                try {
                    // Deserialize the response into a RetrieveAdunitModel object (not a list)
                    retrieveAdunitModel = objectMapper.readValue(response, RetrieveAdunitModel.class);
                    UtilsGlobal.call_log_WS(context,"Receive from QT for Ad_Unit verification", url.toString() , response.toString());
                    return Collections.singletonList(retrieveAdunitModel);
                } catch (IOException e) {
                    UtilsGlobal.call_log_WS(context,"Ad_Unit verification task catch", url.toString(), response);
                    return Collections.emptyList();
                }
            }
        }catch (Exception e) {
            UtilsGlobal.call_log_WS(context,"Ad_Unit verification Task Catch", url.toString(), response);
            e.printStackTrace();
        }

        return null;
    }


    private String performNetworkRequest(String string, String authToken) {

        String response = null;
        HttpURLConnection connection = null;
        BufferedReader bufferedReader =null;

        try {
            url = new URL(string);
            connection = (HttpURLConnection) url.openConnection();

            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
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
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                response = stringBuilder.toString();

                // Close the streams
                bufferedReader.close();
                inputStream.close();
            }else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED){
                response = "Unauthorized";
                return response;
            }else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND){
                response = "Not Found";
                return response;
            }
            else{
                response ="null";
                return response;
            }

            // Disconnect the connection
            connection.disconnect();
        } catch (IOException e) {
            response = "null";
            e.printStackTrace();
            return response;
        }finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }

        return response;
    }

    // Rest of the code...


    @Override
    protected void onPostExecute(List<RetrieveAdunitModel> resultList) {
        super.onPostExecute(resultList);

        if (taskRetrieveAdunitEvent != null) {
            UtilsGlobal.call_log_WS(context,"Going to the Ad unit response handling in onRetrieveAdunitResult Function","","");
            if (resultList != null && !resultList.isEmpty()) {
                taskRetrieveAdunitEvent.onRetrieveAdunitResult(resultList, false);
            } else {
                taskRetrieveAdunitEvent.onRetrieveAdunitResult(resultList, true);
            }
        }
    }

}

