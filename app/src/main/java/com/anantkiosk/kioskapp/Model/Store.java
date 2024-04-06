package com.anantkiosk.kioskapp.Model;

import com.google.gson.annotations.SerializedName;

public class Store {


    @SerializedName("IndustryType")
    private String IndustryType;

    public String getIndustryType() {
        return IndustryType;
    }

    public void setIndustryType(String industryType) {
        IndustryType = industryType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("Name")
    private String name;

    public String getStoreType() {
        return StoreType;
    }

    public void setStoreType(String storeType) {
        StoreType = storeType;
    }

    @SerializedName("StoreType")
    private String StoreType;

    @SerializedName("points")
    private String points;

    public String getWebserver() {
        return webserver;
    }

    public void setWebserver(String webserver) {
        this.webserver = webserver;
    }

    @SerializedName("webserver")
    private String webserver;

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getRewards() {
        return rewards;
    }

    public void setRewards(String rewards) {
        this.rewards = rewards;
    }

    @SerializedName("rewards")
    private String rewards;
    @SerializedName("phone")
    private String phone;

    @SerializedName("displayPhone")
    private String displayPhone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDisplayPhone() {
        return displayPhone;
    }

    public void setDisplayPhone(String displayPhone) {
        this.displayPhone = displayPhone;
    }

    public String getKioskControlledBy() {
        return KioskControlledBy;
    }

    public void setKioskControlledBy(String kioskControlledBy) {
        KioskControlledBy = kioskControlledBy;
    }

    @SerializedName("KioskControlledBy")
    private String KioskControlledBy;

    @SerializedName("id")
    private String id;

    @SerializedName("imageURL")
    private String imageURL;

    @SerializedName("isGiftCardAvail")
    private String isGiftCardAvail;


    @SerializedName("AllowCustmLoyalltyCheckPoint")
    private String isLoyaltyPointAllowCheckPoint;

    public boolean getAllowToJoinUserForLoyaltyProgram() {
        return AllowToJoinUserForLoyaltyProgram;
    }

    public void setAllowToJoinUserForLoyaltyProgram(boolean allowToJoinUserForLoyaltyProgram) {
        AllowToJoinUserForLoyaltyProgram = allowToJoinUserForLoyaltyProgram;
    }

    @SerializedName("AllowToJoinUserForLoyaltyProgram")
    private boolean AllowToJoinUserForLoyaltyProgram;

    @SerializedName("AuthAllowCustomer")
    private String AuthAllowCustomer;

    public String getAuthAllowCustomer() {
        return AuthAllowCustomer;
    }

    public void setAuthAllowCustomer(String authAllowCustomer) {
        AuthAllowCustomer = authAllowCustomer;
    }

    public String getAllowCustmGiftcardCheck() {
        return AllowCustmGiftcardCheck;
    }

    public void setAllowCustmGiftcardCheck(String allowCustmGiftcardCheck) {
        AllowCustmGiftcardCheck = allowCustmGiftcardCheck;
    }

    @SerializedName("AllowCustmGiftcardCheck")
    private String AllowCustmGiftcardCheck;

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getIsGiftCardAvail() {
        return isGiftCardAvail;
    }

    public void setIsGiftCardAvail(String isGiftCardAvail) {
        this.isGiftCardAvail = isGiftCardAvail;
    }


    public String getIsLoyaltyPointAllowCheckPoint() {
        return isLoyaltyPointAllowCheckPoint;
    }

    public void setIsLoyaltyPointAllowCheckPoint(String isLoyaltyPointAllowCheckPoint) {
        this.isLoyaltyPointAllowCheckPoint = isLoyaltyPointAllowCheckPoint;
    }

    public String getIsLoyaltyPointAllowAllCustomers() {
        return isLoyaltyPointAllowAllCustomers;
    }

    public void setIsLoyaltyPointAllowAllCustomers(String isLoyaltyPointAllowAllCustomers) {
        this.isLoyaltyPointAllowAllCustomers = isLoyaltyPointAllowAllCustomers;
    }

    public String getIsAllowAuthenticationWithCode() {
        return isAllowAuthenticationWithCode;
    }

    public void setIsAllowAuthenticationWithCode(String isAllowAuthenticationWithCode) {
        this.isAllowAuthenticationWithCode = isAllowAuthenticationWithCode;
    }

    public String getIsAllowGiftCardBalanceCheck() {
        return isAllowGiftCardBalanceCheck;
    }

    public void setIsAllowGiftCardBalanceCheck(String isAllowGiftCardBalanceCheck) {
        this.isAllowGiftCardBalanceCheck = isAllowGiftCardBalanceCheck;
    }

    public String getIsSizeAvail() {
        return isSizeAvail;
    }

    public void setIsSizeAvail(String isSizeAvail) {
        this.isSizeAvail = isSizeAvail;
    }

    @SerializedName("AllowCustmGrantPurchase")
    private String isLoyaltyPointAllowAllCustomers;

    @SerializedName("isAllowAuthenticationWithCode")
    private String isAllowAuthenticationWithCode;

    @SerializedName("isAllowGiftCardBalanceCheck")
    private String isAllowGiftCardBalanceCheck;

    @SerializedName("isSizeAvail")
    private String isSizeAvail;

    public String getLoyaltyRewardName() {
        return LoyaltyRewardName;
    }

    public void setLoyaltyRewardName(String loyaltyRewardName) {
        LoyaltyRewardName = loyaltyRewardName;
    }

    @SerializedName("LoyaltyRewardName")
    private String LoyaltyRewardName;

    @SerializedName("programtype")
    private String programtype;

    public String getProgramtype() {
        return programtype;
    }

    public void setProgramtype(String programtype) {
        this.programtype = programtype;
    }

    public boolean isLoyaltyEnable() {
        return isLoyaltyEnable;
    }

    public void setLoyaltyEnable(boolean loyaltyEnable) {
        isLoyaltyEnable = loyaltyEnable;
    }

    private boolean isLoyaltyEnable;

    public boolean isGiftCardEnable() {
        return isGiftCardEnable;
    }

    public void setGiftCardEnable(boolean giftCardEnable) {
        isGiftCardEnable = giftCardEnable;
    }

    private boolean isGiftCardEnable;

    public boolean isHasWineSpirit() {
        return hasWineSpirit;
    }

    public void setHasWineSpirit(boolean hasWineSpirit) {
        this.hasWineSpirit = hasWineSpirit;
    }

    @SerializedName("hasWineSpirit")
    private boolean hasWineSpirit;

    public String getFoodpairingSite() {
        return foodpairingSite;
    }

    public void setFoodpairingSite(String foodpairingSite) {
        this.foodpairingSite = foodpairingSite;
    }

    @SerializedName("foodpairingSite")
    private String foodpairingSite;

    public boolean isHasDrinkReceipes() {
        return hasDrinkReceipes;
    }

    public void setHasDrinkReceipes(boolean hasDrinkReceipes) {
        this.hasDrinkReceipes = hasDrinkReceipes;
    }

    private boolean hasDrinkReceipes;

    public boolean isHasFoodPairing() {
        return hasFoodPairing;
    }

    public void setHasFoodPairing(boolean hasFoodPairing) {
        this.hasFoodPairing = hasFoodPairing;
    }

    private boolean hasFoodPairing;

    public String getIsFoodPairingForKiosk() {
        return IsFoodPairingForKiosk;
    }

    public void setIsFoodPairingForKiosk(String isFoodPairingForKiosk) {
        IsFoodPairingForKiosk = isFoodPairingForKiosk;
    }

    public String getIsDrinkRecipesForKiosk() {
        return IsDrinkRecipesForKiosk;
    }

    public void setIsDrinkRecipesForKiosk(String isDrinkRecipesForKiosk) {
        IsDrinkRecipesForKiosk = isDrinkRecipesForKiosk;
    }

    @SerializedName("IsFoodPairingForKiosk")
    private String IsFoodPairingForKiosk;

    @SerializedName("IsDrinkRecipesForKiosk")
    private String IsDrinkRecipesForKiosk;

    public String getFoodPairingURL() {
        return FoodPairingURL;
    }

    public void setFoodPairingURL(String foodPairingURL) {
        FoodPairingURL = foodPairingURL;
    }

    @SerializedName("FoodPairingURL")
    private String FoodPairingURL;

    public boolean getIsInStockCheck() {
        return IsInStockCheck;
    }

    public void setIsInStockCheck(boolean isInStockCheck) {
        IsInStockCheck = isInStockCheck;
    }


    private boolean IsInStockCheck;



    public boolean getIsNeedToDisplayDate() {
        return isNeedToDisplayDate;
    }

    public void setIsNeedToDisplayDate(boolean isNeedToDisplayDate) {
        this.isNeedToDisplayDate = isNeedToDisplayDate;
    }

    @SerializedName("isNeedToDisplayDate")
    private boolean isNeedToDisplayDate;

    public String getIsPurchaseOrderAllowed() {
        return isPurchaseOrderAllowed;
    }

    public void setIsPurchaseOrderAllowed(String isPurchaseOrderAllowed) {
        this.isPurchaseOrderAllowed = isPurchaseOrderAllowed;
    }

    @SerializedName("isPurchaseOrderAllowed")
    private String isPurchaseOrderAllowed;

    public boolean getShowRelatedProducts() {
        return showRelatedProducts;
    }

    public void setShowRelatedProducts(boolean showRelatedProducts) {
        this.showRelatedProducts = showRelatedProducts;
    }


    private boolean showRelatedProducts;

    @SerializedName("Isboughtitemforkiosk")
    private String showRelatedProducts_flag;

    public boolean isNeedToDisplayDate() {
        return isNeedToDisplayDate;
    }

    public void setNeedToDisplayDate(boolean needToDisplayDate) {
        isNeedToDisplayDate = needToDisplayDate;
    }

    public boolean isShowRelatedProducts() {
        return showRelatedProducts;
    }

    public String getIsExpacteddateforkiosk() {
        return IsExpacteddateforkiosk;
    }

    public void setIsExpacteddateforkiosk(String isExpacteddateforkiosk) {
        IsExpacteddateforkiosk = isExpacteddateforkiosk;
    }

    @SerializedName("IsExpacteddateforkiosk")
    private String IsExpacteddateforkiosk;

    public String getShowRelatedProducts_flag() {
        return showRelatedProducts_flag;
    }

    public void setShowRelatedProducts_flag(String showRelatedProducts_flag) {
        this.showRelatedProducts_flag = showRelatedProducts_flag;
    }

    public String getIsInStockCheck_flag() {
        return IsInStockCheck_flag;
    }

    public void setIsInStockCheck_flag(String isInStockCheck_flag) {
        IsInStockCheck_flag = isInStockCheck_flag;
    }

    @SerializedName("IsseeallitemforKiosk")
    private String IsInStockCheck_flag;

    public boolean isAllowToJoinUserForLoyaltyProgram() {
        return AllowToJoinUserForLoyaltyProgram;
    }

    public String getAllowCustmLoyalltyCheckProgram() {
        return AllowCustmLoyalltyCheckProgram;
    }

    public void setAllowCustmLoyalltyCheckProgram(String allowCustmLoyalltyCheckProgram) {
        AllowCustmLoyalltyCheckProgram = allowCustmLoyalltyCheckProgram;
    }

    //AllowCustmLoyalltyCheckProgram
    @SerializedName("AllowCustmLoyalltyCheckProgram")
    private String AllowCustmLoyalltyCheckProgram;
    //FoodPairingURL

    public String getFrequentBuyer() {
        return FrequentBuyer;
    }

    public void setFrequentBuyer(String frequentBuyer) {
        FrequentBuyer = frequentBuyer;
    }

    @SerializedName("FrequentBuyer")
    private String FrequentBuyer;
}
