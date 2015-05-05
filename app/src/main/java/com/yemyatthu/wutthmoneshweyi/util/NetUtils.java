package com.yemyatthu.wutthmoneshweyi.util;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by yemyatthu on 5/4/15.
 */
public class NetUtils {
  public static boolean isConnected(Context context){
    ConnectivityManager connectivityManager =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    return (connectivityManager.getActiveNetworkInfo()!=null&&connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting());
  }
}
