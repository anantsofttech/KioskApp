package com.anantkiosk.kioskapp.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "allowed_pct",
        "aspect_ratios",
        "asset",
        "auction",
        "created_by",
        "eids",
        "id",
        "integration_type",
        "keywords",
        "lastmod",
        "location",
        "measurement",
        "measurement_config",
        "name",
        "network_id",
        "network_name",
        "notes",
        "owned_by",
        "placements",
        "planning",
        "private_auction",
        "private_auction_display",
        "restrictions",
        "slot",
        "start_date",
        "status",
        "status_display",
        "ts",
        "venue",
        "ad_formats"
})

public class RetrieveAdunitModel {

    @JsonProperty("ad_formats")
    private List<AdFormat> adFormats;

    @JsonProperty("allowed_pct")
    private Integer allowedPct;

    @JsonIgnoreProperties(ignoreUnknown = true)
    private List<String> aspectRatios;

    @JsonProperty("asset")
    private Asset asset;

    @JsonProperty("auction")
    private Auction auction;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonIgnoreProperties(ignoreUnknown = true)
    private List<String> eids;

    @JsonProperty("ext")
    private String ext;

    @JsonProperty("id")
    private String id;

    @JsonProperty("integration_type")
    private int integrationType;

    @JsonProperty("keywords")
    private List<String> keywords;

    @JsonProperty("lastmod")
    private String lastmod;

    @JsonIgnoreProperties(ignoreUnknown = true)
    private Location location;

    @JsonIgnoreProperties(ignoreUnknown = true)
    private Measurement measurement;

    @JsonProperty("name")
    private String name;

    @JsonProperty("network_id")
    private String networkId;

    @JsonProperty("network_name")
    private String networkName;

    @JsonProperty("notes")
    private String notes;

    @JsonProperty("owned_by")
    private String ownedBy;

    @JsonProperty("placements")
    private List<String> placements;

    @JsonIgnoreProperties(ignoreUnknown = true)
    private Planning planning;

    @JsonProperty("private_auction")
    private int privateAuction;

    @JsonProperty("private_auction_display")
    private String privateAuctionDisplay;

    @JsonIgnoreProperties(ignoreUnknown = true)
    private Restrictions restrictions;

    @JsonIgnoreProperties("slot")
    private Slot slot;

    @JsonProperty("start_date")
    private String startDate;

    @JsonProperty("status")
    private int status;

    @JsonProperty("status_display")
    private String statusDisplay;

    @JsonProperty("ts")
    private long ts;

    @JsonIgnoreProperties(ignoreUnknown = true)
    private Venue venue;

    // Getter and Setter methods

    public List<AdFormat> getAdFormats() {
        return adFormats;
    }

    public void setAdFormats(List<AdFormat> adFormats) {
        this.adFormats = adFormats;
    }

    public Integer getAllowedPct() {
        return allowedPct;
    }

    public void setAllowedPct(Integer allowedPct) {
        this.allowedPct = allowedPct;
    }

    public List<String> getAspectRatios() {
        return aspectRatios;
    }

    public void setAspectRatios(List<String> aspectRatios) {
        this.aspectRatios = aspectRatios;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public Auction getAuction() {
        return auction;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public List<String> getEids() {
        return eids;
    }

    public void setEids(List<String> eids) {
        this.eids = eids;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIntegrationType() {
        return integrationType;
    }

    public void setIntegrationType(int integrationType) {
        this.integrationType = integrationType;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public String getLastmod() {
        return lastmod;
    }

    public void setLastmod(String lastmod) {
        this.lastmod = lastmod;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Measurement getMeasurement() {
        return measurement;
    }

    public void setMeasurement(Measurement measurement) {
        this.measurement = measurement;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNetworkId() {
        return networkId;
    }

    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }

    public String getNetworkName() {
        return networkName;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getOwnedBy() {
        return ownedBy;
    }

    public void setOwnedBy(String ownedBy) {
        this.ownedBy = ownedBy;
    }

    public List<String> getPlacements() {
        return placements;
    }

    public void setPlacements(List<String> placements) {
        this.placements = placements;
    }

    public Planning getPlanning() {
        return planning;
    }

    public void setPlanning(Planning planning) {
        this.planning = planning;
    }

    public int getPrivateAuction() {
        return privateAuction;
    }

    public void setPrivateAuction(int privateAuction) {
        this.privateAuction = privateAuction;
    }

    public String getPrivateAuctionDisplay() {
        return privateAuctionDisplay;
    }

    public void setPrivateAuctionDisplay(String privateAuctionDisplay) {
        this.privateAuctionDisplay = privateAuctionDisplay;
    }

    public Restrictions getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(Restrictions restrictions) {
        this.restrictions = restrictions;
    }

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusDisplay() {
        return statusDisplay;
    }

    public void setStatusDisplay(String statusDisplay) {
        this.statusDisplay = statusDisplay;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public static class AdFormat {
        @JsonProperty("h")
        private int h;

        @JsonProperty("w")
        private int w;

        // Getter and Setter methods
        public int getH() {
            return h;
        }

        public void setH(int h) {
            this.h = h;
        }

        public int getW() {
            return w;
        }

        public void setW(int w) {
            this.w = w;
        }
    }

    public static class Asset {
        @SerializedName("aspect_ratio")
        private String aspectRatio;

        @SerializedName("capability")
        private Capability capability;

        @SerializedName("category")
        private int category;

        @SerializedName("category_display")
        private String categoryDisplay;

        @SerializedName("image_url")
        private String imageUrl;

        @SerializedName("mimes")
        private String[] mimes;

        @SerializedName("name")
        private String name;

        @SerializedName("screen_count")
        private int screenCount;

        @SerializedName("size")
        private int size;

        @SerializedName("type")
        private String type;

        public String getAspectRatio() {
            return aspectRatio;
        }

        public void setAspectRatio(String aspectRatio) {
            this.aspectRatio = aspectRatio;
        }

        public Capability getCapability() {
            return capability;
        }

        public void setCapability(Capability capability) {
            this.capability = capability;
        }

        public int getCategory() {
            return category;
        }

        public void setCategory(int category) {
            this.category = category;
        }

        public String getCategoryDisplay() {
            return categoryDisplay;
        }

        public void setCategoryDisplay(String categoryDisplay) {
            this.categoryDisplay = categoryDisplay;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String[] getMimes() {
            return mimes;
        }

        public void setMimes(String[] mimes) {
            this.mimes = mimes;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getScreenCount() {
            return screenCount;
        }

        public void setScreenCount(int screenCount) {
            this.screenCount = screenCount;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static class Capability {
        @SerializedName("audio")
        private boolean audio;

        @SerializedName("banner")
        private boolean banner;

        @SerializedName("video")
        private boolean video;

        @SerializedName("perview")
        private boolean preview;

        public boolean isAudio() {
            return audio;
        }

        public void setAudio(boolean audio) {
            this.audio = audio;
        }

        public boolean isBanner() {
            return banner;
        }

        public void setBanner(boolean banner) {
            this.banner = banner;
        }

        public boolean isVideo() {
            return video;
        }

        public void setVideo(boolean video) {
            this.video = video;
        }

        public boolean isPreview() {
            return preview;
        }

        public void setPreview(boolean preview) {
            this.preview = preview;
        }
    }

    public static class Auction {
        @JsonProperty("alt_bidfloors")
        private AltBidFloors altBidfloors;

        @JsonProperty("at")
        private int at;

        @JsonProperty("bidfloor")
        private String bidfloor;

        @JsonProperty("bidfloorcur")
        private String bidfloorcur;

        // Getter and Setter methods

        public AltBidFloors getAltBidfloors() {
            return altBidfloors;
        }

        public void setAltBidfloors(AltBidFloors altBidfloors) {
            this.altBidfloors = altBidfloors;
        }

        public int getAt() {
            return at;
        }

        public void setAt(int at) {
            this.at = at;
        }

        public String getBidfloor() {
            return bidfloor;
        }

        public void setBidfloor(String bidfloor) {
            this.bidfloor = bidfloor;
        }

        public String getBidfloorcur() {
            return bidfloorcur;
        }

        public void setBidfloorcur(String bidfloorcur) {
            this.bidfloorcur = bidfloorcur;
        }

        public static class AltBidFloors {
            @JsonProperty("banner")
            private String banner;

            @JsonProperty("video")
            private String video;

            // Getter and Setter methods

            public String getBanner() {
                return banner;
            }

            public void setBanner(String banner) {
                this.banner = banner;
            }

            public String getVideo() {
                return video;
            }

            public void setVideo(String video) {
                this.video = video;
            }
        }
    }

    public static class Location {
        @JsonProperty("country")
        private String country;

        @JsonProperty("latitude")
        private double latitude;

        @JsonProperty("longitude")
        private double longitude;

        @JsonProperty("state")
        private String state;

        // Getter and Setter methods

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }

    public static class Measurement {
        @JsonProperty("api")
        private List<String> api;

        @JsonProperty("vendor")
        private String vendor;

        // Getter and Setter methods

        public List<String> getApi() {
            return api;
        }

        public void setApi(List<String> api) {
            this.api = api;
        }

        public String getVendor() {
            return vendor;
        }

        public void setVendor(String vendor) {
            this.vendor = vendor;
        }
    }

    public static class Planning {
        @JsonProperty("frequency_capping")
        private FrequencyCapping frequencyCapping;

        @JsonProperty("frequency_capping_display")
        private String frequencyCappingDisplay;

        // Getter and Setter methods

        public FrequencyCapping getFrequencyCapping() {
            return frequencyCapping;
        }

        public void setFrequencyCapping(FrequencyCapping frequencyCapping) {
            this.frequencyCapping = frequencyCapping;
        }

        public String getFrequencyCappingDisplay() {
            return frequencyCappingDisplay;
        }

        public void setFrequencyCappingDisplay(String frequencyCappingDisplay) {
            this.frequencyCappingDisplay = frequencyCappingDisplay;
        }

        public static class FrequencyCapping {
            @JsonProperty("session")
            private int session;

            @JsonProperty("user")
            private int user;

            // Getter and Setter methods

            public int getSession() {
                return session;
            }

            public void setSession(int session) {
                this.session = session;
            }

            public int getUser() {
                return user;
            }

            public void setUser(int user) {
                this.user = user;
            }
        }
    }

    public static class Restrictions {
        @JsonProperty("blocked_creatives")
        private List<String> blockedCreatives;

        @JsonProperty("blocked_networks")
        private List<String> blockedNetworks;

        @JsonProperty("blocked_publishers")
        private List<String> blockedPublishers;

        // Getter and Setter methods

        public List<String> getBlockedCreatives() {
            return blockedCreatives;
        }

        public void setBlockedCreatives(List<String> blockedCreatives) {
            this.blockedCreatives = blockedCreatives;
        }

        public List<String> getBlockedNetworks() {
            return blockedNetworks;
        }

        public void setBlockedNetworks(List<String> blockedNetworks) {
            this.blockedNetworks = blockedNetworks;
        }

        public List<String> getBlockedPublishers() {
            return blockedPublishers;
        }

        public void setBlockedPublishers(List<String> blockedPublishers) {
            this.blockedPublishers = blockedPublishers;
        }
    }

    public static class Slot {
        @JsonProperty("h")
        private int height;

        @JsonProperty("max_duration")
        private Double maxDuration;

        @JsonProperty("min_duration")
        private Double minDuration;

        @JsonProperty("w")
        private int width;

        // Getter and setter methods with JSON property annotations for the inner class fields
        // (Same as before)


        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public Double getMaxDuration() {
            return maxDuration;
        }

        public void setMaxDuration(Double maxDuration) {
            this.maxDuration = maxDuration;
        }

        public Double getMinDuration() {
            return minDuration;
        }

        public void setMinDuration(Double minDuration) {
            this.minDuration = minDuration;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }
    }

    public static class Venue {
        @JsonProperty("city")
        private String city;

        @JsonProperty("country")
        private String country;

        @JsonProperty("state")
        private String state;

        // Getter and Setter methods

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }

    private static class Capabilityy {

        @JsonProperty("audio")
        private Boolean audio;

        @JsonProperty("banner")
        private Boolean banner;

        @JsonProperty("video")
        private Boolean video;

        @JsonProperty("perview")
        private Boolean perview;

        public Boolean getAudio() {
            return audio;
        }

        public void setAudio(Boolean audio) {
            this.audio = audio;
        }

        public Boolean getBanner() {
            return banner;
        }

        public void setBanner(Boolean banner) {
            this.banner = banner;
        }

        public Boolean getVideo() {
            return video;
        }

        public void setVideo(Boolean video) {
            this.video = video;
        }

        public Boolean getPerview() {
            return perview;
        }

        public void setPerview(Boolean perview) {
            this.perview = perview;
        }
    }

}