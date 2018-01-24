package org.poornima.aarohan.aarohan_2018forcoorfinators.AarohanCoordinatorClass;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by HeartBeat on 31-12-2017.
 */

public class NetWorkManager {
    public static boolean checkInternetAccess(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
               return true;
            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return true;
        }
        else
            return false;
        return false;
    }
}
