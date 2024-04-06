package com.anantkiosk.kioskapp.Api;

import com.anantkiosk.kioskapp.Model.AdvRefreshCalls;
import com.anantkiosk.kioskapp.Model.AdvSign;
import com.anantkiosk.kioskapp.Model.Auth_QTModel;
import com.anantkiosk.kioskapp.Model.Drinks;
import com.anantkiosk.kioskapp.Model.GiftCardModel;
import com.anantkiosk.kioskapp.Model.ProductResponseMain;
import com.anantkiosk.kioskapp.Model.Product;
import com.anantkiosk.kioskapp.Model.Result;
import com.anantkiosk.kioskapp.Model.Sign;
import com.anantkiosk.kioskapp.Model.Store;

import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("CSCode/LightningOnlineService1.asmx/CheckStoreValidation")
    Call<ArrayList<Store>> CheckStoreValidation(@Field("storeno") String StoreNo);

    @FormUrlEncoded
    @POST("CSCode/LightningOnlineService1.asmx/SendOneTimePasswordText")
    Call<Result> SendOneTimePasswordText(@Field("storeno") String StoreNo);

    @FormUrlEncoded
    @POST("CSCode/LightningOnlineService1.asmx/CheckOneTimePasswordText")
    Call<Result> CheckOneTimePasswordText(@Field("storeno") String storeno, @Field("Accesscode") String Accesscode);

    @FormUrlEncoded
    @POST("CSCode/LightningOnlineService1.asmx/SendOneTimePasswordTextForCustomer")
    Call<Result> SendOneTimePasswordTextForCustomer(@Field("storeno") String StoreNo, @Field("phoneno") String phoneno);

    @FormUrlEncoded
    @POST("CSCode/LightningOnlineService1.asmx/CheckOneTimePasswordTextForCustomer")
    Call<Result> CheckOneTimePasswordTextForCustomer(@Field("storeno") String storeno, @Field("phoneno") String phoneno, @Field("Otp") String Accesscode);

    @GET("CSCode/LightningOnlineService1.asmx/CheckStoreLoyaltyReward")
    Call<ArrayList<Store>> CheckLoyaltyProgram(@Query("storeno") String StoreNo);

    @FormUrlEncoded
    @POST("CSCode/LightningOnlineService1.asmx/InsertCustomLoyaltyCheckPoint_V1")
    Call<Result> InsertCustomGrantPurchase(@Field("storeno") String StoreNo, @Field("AllowCustmLoyalltyCheckPoint") String status, @Field("AllowCustmLoyalltyCheckProgram") String AllowCustmLoyalltyCheckProgram);

    @FormUrlEncoded
    @POST("CSCode/LightningOnlineService1.asmx/InsertCustomGrantPurchase")
    Call<Result> InsertCustomLoyaltyCheckPoint(@Field("storeno") String StoreNo, @Field("AllowCustmGrantPurchase") String status);

    @FormUrlEncoded
    @POST("CSCode/LightningOnlineService1.asmx/SaveFoodPairingAndDrinkRecipes")
    Call<Result> SaveFoodPairingAndDrinkRecipes(@Field("storeno") String StoreNo, @Field("blnFoodPairing") String blnFoodPairing, @Field("blnDrinkRecipes") String blnDrinkRecipes, @Field("strFoodPairingURL") String strFoodPairingURL);

    @FormUrlEncoded
    @POST("CSCode/LightningOnlineService1.asmx/AllItemAndBoughtForKiosk")
    Call<Result> SaveInventorySettings(@Field("storeno") String StoreNo, @Field("IsseeallitemforKiosk") String blnstockstatus, @Field("Isboughtitemforkiosk") String Isboughtitemforkiosk, @Field("IsExpacteddateforkiosk") String IsExpacteddateforkiosk);

    @FormUrlEncoded
    @POST("CSCode/LightningOnlineService1.asmx/CheckStoreGiftCards")
    Call<Result> CheckGiftCards(@Field("storeno") String StoreNo);

    @FormUrlEncoded
    @POST("CSCode/LightningOnlineService1.asmx/InsertCustomGiftCard")
    Call<Result> InsertCustomGiftCard(@Field("storeno") String StoreNo, @Field("AllowCustmGiftcardCheck") String status);

    @FormUrlEncoded
    @POST("CSCode/LightningOnlineService1.asmx/InsertAuthAllowCustomer")
    Call<Result> InsertAuthAllowCustomer(@Field("storeno") String StoreNo, @Field("AuthAllowCustomer") String status);

    @FormUrlEncoded
    @POST("CSCode/LightningOnlineService1.asmx/GetPosGiftReport_details_data")
    Call<ArrayList<GiftCardModel>> GetPosGiftReport_details_data(@Field("StoreNo") String StoreNo, @Field("Gc_Num") String Gc_Num);

    @FormUrlEncoded
    @POST("CSCode/LightningOnlineService1.asmx/GetPosInventory")
    Call<ArrayList<ProductResponseMain>> GetPosInventory(@Field("StoreNo") String StoreNo, @Field("ItemType") String ItemType, @Field("NoofRows") String NoofRows
            , @Field("CurrentPage") String CurrentPage, @Field("SearchText") String SearchText, @Field("chkwildcard") String chkwildcard, @Field("checkstock") String checkstock,
                                                         @Field("sortcolumn") String sortcolumn, @Field("sortorder") String sortorder);

    @FormUrlEncoded
    @POST("CSCode/LightningOnlineService1.asmx/GetDrinksList")
    Call<ArrayList<Drinks>> GetDrinksList(@Field("StoreNo") String StoreNo, @Field("SearchText") String SearchText, @Field("PageIndex") String PageIndex
            , @Field("PageSize") String PageSize);

    @FormUrlEncoded
    @POST("CSCode/LightningOnlineService1.asmx/GetLoyaltyRewards_Json")
    Call<ArrayList<GiftCardModel>> GetPOSLoyaltyReward_Details(@Field("StoreNo") String StoreNo, @Field("RewardNumber") String RewardNumber);

    @FormUrlEncoded
    @POST("CSCode/LightningOnlineService1.asmx/PoleDisplayErrorLogSave")
    Call<Result> PoleDisplayErrorLogSave(@Field("Storeno") String StoreNo, @Field("strRequestXML") String strRequestXML, @Field("strResponseXML") String strResponseXML, @Field("DeviceID") String deviceID, @Field("SignID") String Signid, @Field("ErrorMessage") String ErrorMessage);

    @FormUrlEncoded
    @POST("CSCode/LightningOnlineService1.asmx/RegisterUsersloyaltyReward")
    Call<Result> RegisterUsersloyaltyReward(@Field("storeno") String StoreNo, @Field("mobilenumber") String strRequestXML, @Field("firstname") String strResponseXML, @Field("lastname") String deviceID, @Field("emailID") String emailID);

    @GET("CSCode/LightningOnlineService1.asmx/GetPoleDisplayErrorLog")
    Call<ArrayList<AdvRefreshCalls>> getResponseLogs(@Query("Storeno") String Storeno, @Query("DeviceID") String DeviceID);

    @GET("CSCode/LightningOnlineService1.asmx/GetRecommandedItems")
    Call<ArrayList<Product>> GetRecommandedItems(@Query("storeno") String storeno, @Query("ItemId") String ItemId);

    @GET("/CSCode/LightningOnlineService1.asmx/AndroidTabletValidation")
    Call<ArrayList<Result>> AndroidTabletValidation(@Query("storeno") String storeno);

    @GET("/CSCode/LightningOnlineService1.asmx/AndroidTabletValidation")
    Call<Result> AndroidTabletValidationObj(@Query("storeno") String storeno);

    @FormUrlEncoded
    @POST("CSCode/LightningOnlineService1.asmx/SendDrinkInfoformkiosk")
    Call<Result> SendFoodPairingAndDrinkRecipes(@Field("StoreNo") String StoreNo, @Field("titleID") String id, @Field("emailID") String email);

    //ALL APIS OF BLIPBOARD
    @POST("signs")
    Call<Sign> CreateSigns(@Header("Authorization") String authHeader, @Body RequestBody str);

    @GET("sign/{sign_id}/images")
    Call<ArrayList<AdvSign>> signSignIdImagesGet(@Path("sign_id") String sign_id, @Header("Authorization") String authHeader);

    @POST("sign/{sign_id}/schedule")
    Call<ArrayList<AdvSign>> signSignIdImagesGetschedule(@Path("sign_id") String sign_id, @Header("Authorization") String authHeader, @Body RequestBody str);

    @POST("sign/{sign_id}/flips")
    Call<Result> recordFlips(@Path("sign_id") String sign_id, @Header("Authorization") String authHeader, @Body RequestBody str);

    @GET("signs")
    Call<ArrayList<Sign>> GetSigns(@Header("Authorization") String authHeader);

    @POST("token")
    Call<Auth_QTModel> AUTH_QT_CALL(@Body RequestBody str);

}