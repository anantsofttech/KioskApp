package com.anantkiosk.kioskapp.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class GetAdRequestModel {

    @JsonProperty("creative")
    private Creative creative;

    @JsonProperty("context")
    private String context;

    @JsonProperty("bid")
    private Bid bid;

    @JsonProperty("adapproval_id")
    private String adapprovalId;

    @JsonProperty("auction")
    private Auction auction;

    // Getter and Setter methods here...

    public Creative getCreative() {
        return creative;
    }

    public void setCreative(Creative creative) {
        this.creative = creative;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Bid getBid() {
        return bid;
    }

    public void setBid(Bid bid) {
        this.bid = bid;
    }

    public String getAdapprovalId() {
        return adapprovalId;
    }

    public void setAdapprovalId(String adapprovalId) {
        this.adapprovalId = adapprovalId;
    }

    public Auction getAuction() {
        return auction;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }

    // Inner class 'Creative'
    public static class Creative {
        @JsonProperty("id")
        private String id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("type")
        private String type;

        @JsonProperty("markup")
        private String markup;

        @JsonProperty("snapshots")
        private List<Snapshot> snapshots;

        @JsonProperty("cta")
        private CTA cta;

        @JsonProperty("status")
        private String status;

        @JsonProperty("h")
        private int h;

        @JsonProperty("w")
        private int w;

        @JsonProperty("duration")
        private double duration;

        @JsonProperty("cache_key")
        private String cacheKey;

        // Getter and Setter methods here...

        // Inner class 'Snapshot'
        public static class Snapshot {
            @JsonProperty("h")
            private int h;

            @JsonProperty("w")
            private int w;

            @JsonProperty("mime")
            private String mime;

            @JsonProperty("iurl")
            private String curl;

            @JsonProperty("bitrate")
            private int bitrate;

            // Getter and Setter methods here...

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

            public String getMime() {
                return mime;
            }

            public void setMime(String mime) {
                this.mime = mime;
            }

            public String getCurl() {
                return curl;
            }

            public void setCurl(String curl) {
                this.curl = curl;
            }

            public int getBitrate() {
                return bitrate;
            }

            public void setBitrate(int bitrate) {
                this.bitrate = bitrate;
            }
        }

        // Inner class 'CTA'
        public static class CTA {
            // Define properties for CTA as needed
        }

        // Getter and Setter methods here...

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMarkup() {
            return markup;
        }

        public void setMarkup(String markup) {
            this.markup = markup;
        }

        public List<Snapshot> getSnapshots() {
            return snapshots;
        }

        public void setSnapshots(List<Snapshot> snapshots) {
            this.snapshots = snapshots;
        }

        public CTA getCta() {
            return cta;
        }

        public void setCta(CTA cta) {
            this.cta = cta;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

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

        public double getDuration() {
            return duration;
        }

        public void setDuration(double duration) {
            this.duration = duration;
        }

        public String getCacheKey() {
            return cacheKey;
        }

        public void setCacheKey(String cacheKey) {
            this.cacheKey = cacheKey;
        }
    }

    // Inner class 'Bid'
    public static class Bid {
        @JsonProperty("price")
        private double price;

        @JsonProperty("buyer")
        private String buyer;

        @JsonProperty("seat")
        private String seat;

        @JsonProperty("deal")
        private Deal deal;

        @JsonProperty("cid")
        private String cid;

        @JsonProperty("crid")
        private String crid;

        @JsonProperty("adomain")
        private List<String> adomain;

        @JsonProperty("imp_x")
        private int impX;

        // Inner class 'Deal'
        public static class Deal {
            @JsonProperty("name")
            private String name;

            @JsonProperty("bidfloor")
            private double bidfloor;

            @JsonProperty("bidfloorcur")
            private String bidfloorcur;

            @JsonProperty("at")
            private int at;

            @JsonProperty("status")
            private String status;

            @JsonProperty("token")
            private String token;

            @JsonProperty("id")
            private String id;

            @JsonProperty("wbuyer")
            private List<String> wbuyer;

            @JsonProperty("owned_by")
            private String ownedBy;

            @JsonProperty("lastmod")
            private String lastmod;

            @JsonProperty("priority")
            private int priority;

            @JsonProperty("archived")
            private boolean archived;

            // Getter and Setter methods here...
            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public double getBidfloor() {
                return bidfloor;
            }

            public void setBidfloor(double bidfloor) {
                this.bidfloor = bidfloor;
            }

            public String getBidfloorcur() {
                return bidfloorcur;
            }

            public void setBidfloorcur(String bidfloorcur) {
                this.bidfloorcur = bidfloorcur;
            }

            public int getAt() {
                return at;
            }

            public void setAt(int at) {
                this.at = at;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public List<String> getWbuyer() {
                return wbuyer;
            }

            public void setWbuyer(List<String> wbuyer) {
                this.wbuyer = wbuyer;
            }

            public String getOwnedBy() {
                return ownedBy;
            }

            public void setOwnedBy(String ownedBy) {
                this.ownedBy = ownedBy;
            }

            public String getLastmod() {
                return lastmod;
            }

            public void setLastmod(String lastmod) {
                this.lastmod = lastmod;
            }

            public int getPriority() {
                return priority;
            }

            public void setPriority(int priority) {
                this.priority = priority;
            }

            public boolean isArchived() {
                return archived;
            }

            public void setArchived(boolean archived) {
                this.archived = archived;
            }
        }

        // Getter and Setter methods here...
        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getBuyer() {
            return buyer;
        }

        public void setBuyer(String buyer) {
            this.buyer = buyer;
        }

        public String getSeat() {
            return seat;
        }

        public void setSeat(String seat) {
            this.seat = seat;
        }

        public Deal getDeal() {
            return deal;
        }

        public void setDeal(Deal deal) {
            this.deal = deal;
        }

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getCrid() {
            return crid;
        }

        public void setCrid(String crid) {
            this.crid = crid;
        }

        public List<String> getAdomain() {
            return adomain;
        }

        public void setAdomain(List<String> adomain) {
            this.adomain = adomain;
        }

        public int getImpX() {
            return impX;
        }

        public void setImpX(int impX) {
            this.impX = impX;
        }
    }

    // Inner class 'Auction'
    public static class Auction {
        @JsonProperty("at")
        private int at;

        @JsonProperty("bidfloorcur")
        private String bidfloorcur;

        @JsonProperty("bidfloor")
        private double bidfloor;

        @JsonProperty("price")
        private double price;

        // Getter and Setter methods here...


        public int getAt() {
            return at;
        }

        public void setAt(int at) {
            this.at = at;
        }

        public String getBidfloorcur() {
            return bidfloorcur;
        }

        public void setBidfloorcur(String bidfloorcur) {
            this.bidfloorcur = bidfloorcur;
        }

        public double getBidfloor() {
            return bidfloor;
        }

        public void setBidfloor(double bidfloor) {
            this.bidfloor = bidfloor;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }
}
