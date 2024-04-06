package com.anantkiosk.kioskapp.Task;

import android.os.AsyncTask;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.anantkiosk.kioskapp.Home.HomeFragments.AdvertisementFragment;
import com.anantkiosk.kioskapp.Model.RetrieveAdunitModel;
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

            if (response==null || response.isEmpty()){
                return Collections.emptyList();
            }else {
                // Deserialize the response into a RetrieveAdunitModel object (not a list)
                retrieveAdunitModel = objectMapper.readValue(response, RetrieveAdunitModel.class);
                return Collections.singletonList(retrieveAdunitModel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String performNetworkRequest(String string, String authToken) {

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

    // Rest of the code...


    @Override
    protected void onPostExecute(List<RetrieveAdunitModel> resultList) {
        super.onPostExecute(resultList);

        if (taskRetrieveAdunitEvent != null) {
            if (resultList != null && !resultList.isEmpty()) {
                taskRetrieveAdunitEvent.onRetrieveAdunitResult(resultList, false);
            } else {
                taskRetrieveAdunitEvent.onRetrieveAdunitResult(resultList, true);
            }
        }
    }

}

