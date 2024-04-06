package com.anantkiosk.kioskapp.Model;

import java.util.ArrayList;


public class AdvRefreshCallsResponse {
    public ArrayList<AdvRefreshCalls> getAdvRefreshCalls() {
        return advRefreshCalls;
    }

    public void setAdvRefreshCalls(ArrayList<AdvRefreshCalls> advRefreshCalls) {
        this.advRefreshCalls = advRefreshCalls;
    }

    ArrayList<AdvRefreshCalls> advRefreshCalls;
}