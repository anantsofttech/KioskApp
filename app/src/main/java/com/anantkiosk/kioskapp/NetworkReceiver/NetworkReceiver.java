package com.anantkiosk.kioskapp.NetworkReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.anantkiosk.kioskapp.Eventbus.InternetCheck;
import com.anantkiosk.kioskapp.Utils.UtilsGlobal;

import org.greenrobot.eventbus.EventBus;

public class NetworkReceiver extends BroadcastReceiver {

    public Context context;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (UtilsGlobal.isNetworkAvailable(context)) {
            EventBus.getDefault().post(new InternetCheck(true));

        } else {
            EventBus.getDefault().post(new InternetCheck(false));

        }
    }

}