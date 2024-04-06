package com.anantkiosk.kioskapp.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Environment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.anantkiosk.kioskapp.Api.ApiClientBillBoard;
import com.anantkiosk.kioskapp.MainActivity;
import com.anantkiosk.kioskapp.Model.AdvSign;
import com.anantkiosk.kioskapp.Model.Auth_QTModel;
import com.anantkiosk.kioskapp.Model.GetAdRequestModel;
import com.anantkiosk.kioskapp.Model.ImagesDetailModel;
import com.anantkiosk.kioskapp.Model.RetrieveAdunitModel;
import com.anantkiosk.kioskapp.Model.Sign;
import com.anantkiosk.kioskapp.Model.Store;
import com.anantkiosk.kioskapp.Model.User;
import com.anantkiosk.kioskapp.NetworkReceiver.NetworkReceiver;
import com.anantkiosk.kioskapp.R;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class UtilsGlobal {

    public static Store store = new Store();
    public static User customer;
    public static Dialog dialog;
    public static String textColor = "#535766";

    public static String IMAGEURL = "WebStoreImages/Inventory/";

    public static long timeForFlips = 10;
    public static String address = "";

    public static boolean isgetAdreponesnull = false;
    public static ProgressDialog loading = null;
    public static String imageURL = "";
    public static String imageMime = "";

    public static final String ORGS = "orgs";
    public static final String WS_AD_UNIT = "/adunits";
    public static final String WS_SUBMIT_AD_REQUEST = "/adrequests";
    public static final String WS_PLAYS = "/plays";
    public static final String QT_USERNAME = "cp.support";
    public static final String QT_PASSWORD = "PWx9Mr;Z-n3%aD8";
    public static final String QT_ID = "/00cb716a-c7f8-4f14-8244-cf510531e696";


    public static Auth_QTModel localAuthQTlist = new Auth_QTModel();
    public static List<RetrieveAdunitModel> localretrieveAdunitModelList = new ArrayList<RetrieveAdunitModel>();

    public static List<AdvSign> Computerperfect = new ArrayList<AdvSign>();
    public static List<AdvSign> ImageDetalList = new ArrayList<AdvSign>();
    public static List<AdvSign> ImageDetalList2 = new ArrayList<AdvSign>();
    public static List<AdvSign> ImageDetalList3 = new ArrayList<AdvSign>();
    public static List<AdvSign> ImageDetalList4 = new ArrayList<AdvSign>();
    public static List<AdvSign> LocalImageDetalList2 = new ArrayList<AdvSign>();
    public static List<GetAdRequestModel> localgetAdRequestModel = new ArrayList<GetAdRequestModel>();

    public static boolean isFromQT = false;
    public static boolean isfromLogout = false;
    public static String type = "";
    public static String Industry_Type = "";
//    public static String type = "Computer Perfect-Wine & Spirit";

    public static List<AdvSign> Lighting = new ArrayList<AdvSign>();

    public static SharedPreferences AppPref = null;
    public static String PrefName = "KIOSKPref";
    public static boolean iscomefrom_adrequest_Same = false;
    public static int count = 0;
    public static String storetype ="";
    public static int adunit_height = 0;
    public static int adunit_width = 0;


    public static void saveStore(Activity activity, Store store) {
        Gson gson = new Gson();
        SharedPreferences pref = activity.getApplicationContext().getSharedPreferences("userList", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("userDataList", gson.toJson(store)); // Storing string
        editor.commit();
    }

    public static Store getStoreList(Activity activity) {
        try {
            Gson gson = new Gson();
            SharedPreferences pref = activity.getApplicationContext().getSharedPreferences("userList", 0);
            Type type = new TypeToken<Store>() {
            }.getType();
            store = gson.fromJson(pref.getString("userDataList", null), type);
            return store;
        } catch (Exception e) {
            return null;
        }
    }

    public static void saveSign(Activity activity, Sign Sign) {
        Gson gson = new Gson();
        SharedPreferences pref = activity.getApplicationContext().getSharedPreferences("Sign", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("userSign", gson.toJson(Sign)); // Storing string
        editor.commit();
    }

    public static Sign getSign(Activity activity) {
        try {
            Gson gson = new Gson();
            SharedPreferences pref = activity.getApplicationContext().getSharedPreferences("Sign", 0);
            Type type = new TypeToken<Sign>() {
            }.getType();
            Sign Sign = gson.fromJson(pref.getString("userSign", null), type);
            return Sign;
        } catch (Exception e) {
            return null;
        }
    }

    public static void saveCustomer(Activity activity, User user) {
        Gson gson = new Gson();
        SharedPreferences pref = activity.getApplicationContext().getSharedPreferences("customerList", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("customerDataList", gson.toJson(user)); // Storing string
        editor.commit();
    }

    public static User getCustomerList(Activity activity) {
        try {
            Gson gson = new Gson();
            SharedPreferences pref = activity.getApplicationContext().getSharedPreferences("customerList", 0);
            Type type = new TypeToken<User>() {
            }.getType();
             customer = gson.fromJson(pref.getString("customerDataList", null), type);
            return customer;
        } catch (Exception e) {
            return null;
        }
    }

    public static void showProgressBar(Context context) {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dismissProgressBar();
            }
        }
        try {
            dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.progress_custom_dialog);
            dialog.setCancelable(false);
            Window window = dialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            }
            dialog.show();
        } catch (Exception e) {
        }
    }

    public static void dismissProgressBar() {
        try {
            dialog.dismiss();
        } catch (Exception e) {
        }
    }

    public static void hideKeyboard(Activity activity) {
        try {
            View view = activity.findViewById(android.R.id.content);
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        boolean isConnected = true;
        try {
            final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
            isConnected = connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();

        } catch (Exception e) {
        }
        return isConnected;
    }

    public static boolean isToday(Date d) {
        return DateUtils.isToday(d.getTime());
    }


    public static boolean isTomorrow(Date d) {
        return DateUtils.isToday(d.getTime() - DateUtils.DAY_IN_MILLIS);
    }

    public static void saveTimeToRefresh(Activity activity, String strDate) {
        SharedPreferences pref = activity.getApplicationContext().getSharedPreferences("SignTime", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("SignTimeSave", strDate); // Storing string
        editor.commit();
    }

    public static boolean getTimeToRefresh(Activity activity, boolean isPreCache) {
        try {
            Log.d("KAVERI LOGS", "Checking time");
            SharedPreferences pref = activity.getApplicationContext().getSharedPreferences("SignTime", 0);
            //match two times
            TimeZone tz = TimeZone.getTimeZone("UTC");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            df.setTimeZone(tz);
            String nowAsISO = df.format(new Date());
            String timeToCheck = pref.getString("SignTimeSave", null);
            if (timeToCheck == null) {
                return true;
            } else if (timeToCheck.length() <= 0) {
                return true;
            } else {
                Date seldate = null, checkdate = null;
                try {
                    DateFormat dfNew = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    seldate = dfNew.parse(nowAsISO);
                    checkdate = dfNew.parse(timeToCheck);
                    Log.d("KAVERI LOGS", "Checking time: " + seldate);
                    Log.d("KAVERI LOGS", "Checking time: " + checkdate);
                } catch (ParseException e) {
                }
                if (seldate.after(checkdate)) {
                    Log.d("KAVERI LOGS", "Checking time: Match for load ");
                    return true;
                } else {
                    Log.d("KAVERI LOGS", "Checking time: Not Match for load ");
                    return false;
                }
            }

        } catch (Exception e) {
            return false;
        }
    }

    public static void saveImages(Context activity, ArrayList<AdvSign> progress) {
        try {
            Gson gson = new Gson();
            SharedPreferences pref = activity.getApplicationContext().getSharedPreferences("Images", 0);
            SharedPreferences.Editor editor = pref.edit();
            if (pref.contains("imageList")) {
                editor.remove("imageList");
            }
            editor.putString("imageList", gson.toJson(progress)); // Storing string
            editor.commit();
            //Log.d("kaveriImage","SAVED IN LOCAL");
        } catch (Exception e) {
        }
    }

    public static ArrayList<AdvSign> getImages(Activity activity) {
        Gson gson = new Gson();
        SharedPreferences pref = activity.getApplicationContext().getSharedPreferences("Images", 0);
        Type type1 = new TypeToken<ArrayList<AdvSign>>() {
        }.getType();
        ArrayList<AdvSign> userValue = gson.fromJson(pref.getString("imageList", null), type1);
        return userValue;
    }

    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

    public synchronized static String getUUId(Context context) {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.commit();
            }
        }
        return uniqueID;
    }

    public static void getVersionName(TextView txtversion, Context context) {
        String versionName = null;
        try {
            String webStore = "1", SignID = "";
            if (UtilsGlobal.store != null) {
                if (UtilsGlobal.store.getWebserver() != null) {
                    if (UtilsGlobal.store.getWebserver().trim().length() > 0) {
                        webStore = UtilsGlobal.store.getWebserver();
                    }
                }
                if (ApiClientBillBoard.signId.trim().length() > 0) {
                    SignID = ApiClientBillBoard.signId;
                }
                versionName = "V" + UtilsGlobal.store.getId() + "-" + SignID + "-1." + context.getPackageManager()
                        .getPackageInfo(context.getPackageName(), 0).versionCode + "-" + webStore;
            } else {
                versionName = "V" + "-1." + context.getPackageManager()
                        .getPackageInfo(context.getPackageName(), 0).versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        txtversion.setText(versionName);

    }

    public static String getTimeToDisplay(Activity activity) {
        try {

            SharedPreferences pref = activity.getApplicationContext().getSharedPreferences("SignTime", 0);
            //match two times
            TimeZone tz = TimeZone.getTimeZone("UTC");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            df.setTimeZone(tz);
            String nowAsISO = df.format(new Date());
            String timeToCheck = pref.getString("SignTimeSave", null);
            if (timeToCheck == null) {
                return "0";
            } else if (timeToCheck.length() <= 0) {
                return "0";
            } else {
                Date seldate = null, checkdate = null;
                try {
                    DateFormat dfNew = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                    seldate = dfNew.parse(nowAsISO);
                    checkdate = dfNew.parse(timeToCheck);
                    Log.d("KAVERI LOGS", "Checking time: " + seldate);
                    Log.d("KAVERI LOGS", "Checking time: " + checkdate);
                } catch (ParseException e) {
                }
                if (seldate.after(seldate)) {
                    Log.d("KAVERI LOGS", "Checking time: Match for load ");
                    return "0";
                } else {
                    long diff = checkdate.getTime() - seldate.getTime();
                    long seconds = diff / 1000;
                    long minutes = seconds / 60;
                    if(minutes ==0)
                        return "0.5";
                    else
                        return String.valueOf(minutes);
                }
            }

        } catch (Exception e) {
            return "0";
        }
    }

    public static boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void LogOff(Activity activity){
        try {
            UtilsGlobal.saveTimeToRefresh(activity, "");
            UtilsGlobal.saveStore(activity, null);
        } catch (Exception e) {
        }

        activity.finish();
    }

    //applied a new fonts
    public static Typeface setFontSemiBold(Context context) {
        Typeface face = Typeface.createFromAsset(context.getAssets(), "font/poppins_semibold.ttf");
        return face;
    }

    public static Typeface setFontRegular(Context context) {
        Typeface face = Typeface.createFromAsset(context.getAssets(), "font/poppins_regular.ttf");
        return face;
    }

    public static Typeface setFontLibre(Context context) {
        Typeface face = Typeface.createFromAsset(context.getAssets(), "font/libre.ttf");
        return face;
    }

    public static void deleteFile() {
        String path = "";
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Do something
            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/NewQT/";
        } else {
            path = Environment.getExternalStorageDirectory() + "/NewQT/";
        }
        File file = new File(path);

        if (file.exists()) {
            file.delete();
            String deleteCmd = "rm -r " + path;
            try {
                Runtime.getRuntime().exec(deleteCmd);
            } catch (IOException e) {
                Log.d("TEST", "ERROR: " + e.getMessage());
            }
        }
    }

    public static void doNetworkProcessGet(String request_str
            , StringBuilder responseStrBuilder /*, String web_service_method*/)
            throws SocketTimeoutException, JsonGenerationException, IOException, JSONException {

        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            //Log.e("Log","Request Url="+request_str);
            if(request_str.contains(" ")){
                request_str=request_str.replace(" ","%20");
            }
            URL url = new URL(request_str);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            connection.setConnectTimeout(1000);
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();

            String line = " ";
            while ((line = reader.readLine()) != null) {
                responseStrBuilder.append(line);
            }
        } finally {
            if (connection != null) connection.disconnect();
        }

    }


}
