package com.anantkiosk.kioskapp.Api;

import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.ConnectionPool;
import okhttp3.ConnectionSpec;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    //live environment
    public static final String BASE_URL = "https://posservice.lightningpos.com/";
    public static String WS_BASE = "https://ecomsecureWCF.lightningpos.com";
    public static final String IMG_BASE = "https://images.lightningpos.com/";
    public static String authHeader="Bearer 971dcf8b19b3bc1cac56e40f5858bbad38823d4e";
    public static final String BASE_URL_BLIP = "https://app.blipbillboards.com/external/";
    public static final String BASE_URL_QT = "https://api.placeexchange.com/v3/";

    //testserver
//    public static final String BASE_URL = "http://evo.lightningpos.com/";
//    public static String WS_BASE = "https://ecomtestWCF.lightningpos.com";
//    public static final String IMG_BASE = "https://testimages.lightningpos.com/";
//    public static String authHeader="Bearer 9ed36663c7905eb3dffe55d9ce79ef78cd30877a";
//    public static final String BASE_URL_BLIP = "https://blipboards-tech.herokuapp.com/external/";
//    public static final String BASE_URL_QT = "https://api.placeexchange.com/v3/";

//   Local URL
//    public static String BASE_URL = "http://192.168.172.244:140";
//    public static String WS_BASE = "http://192.168.172.244:889";
////    public static String LighningURL = "http://192.168.172.244:140/LigtningOnlineService.asmx/";
//    public static String authHeader="Bearer 9ed36663c7905eb3dffe55d9ce79ef78cd30877a";
//    public static final String BASE_URL_BLIP = "https://blipboards-tech.herokuapp.com/external/";;
//    public static final String BASE_URL_QT = "https://api.placeexchange.com/v3/";


    public static String WS_BASE_URL = WS_BASE + "/WebStoreRestService.svc/";

    public static String GETPOLE_IMAGES_DETAIL = "GetPoleDisplayImageForMobile/";



    private static Retrofit retrofit = null;
    private static OkHttpClient okHttpClient;
    private static Gson gson;

    public static Retrofit getClient() {
        try {
            if (gson == null) {
                gson = new GsonBuilder()
                        .setLenient()
                        .create();
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                if (okHttpClient == null) {
                    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    okHttpClient = new OkHttpClient.Builder().addNetworkInterceptor(interceptor)
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .writeTimeout(30, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .build();
                }
            } else {
                okHttpClient = getUnsafeOkHttpClient();
            }
            retrofit = new Retrofit.Builder()

                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        } catch (Exception e) {
            gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .build();

        }
        return retrofit;
    }

    public static OkHttpClient getUnsafeOkHttpClient() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };
            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            builder.connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS).connectionPool(new ConnectionPool(50, 50000, TimeUnit.SECONDS));
            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
