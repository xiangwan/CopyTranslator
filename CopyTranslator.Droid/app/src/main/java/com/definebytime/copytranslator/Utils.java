package com.definebytime.copytranslator;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xiangwan on 2015/4/15.
 */
public class Utils {
    /**
     * 判断服务是否后台运行
     *
     * @param mContext
     *            Context
     * @param className
     *            判断的服务名字
     * @return true 在运行 false 不在运行
     */
    public static boolean isServiceRun(Context mContext, String className) {
        boolean isRun = false;
        ActivityManager activityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(40);
        int size = serviceList.size();
        for (int i = 0; i < size; i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRun = true;
                break;
            }
        }
        return isRun;
    }

    /**
     * 获取所有应用
     *
     * @param context;
     * @return List<PackageInfo> apps;
     */
    public static HashMap<String,String > getAllApps(Context context) {
        HashMap<String,String > apps=new HashMap<>();
        PackageManager pManager = context.getPackageManager();
        //获取手机内所有的应用
        List<PackageInfo> pakList = pManager.getInstalledPackages(0);
        for (int i = 0; i < pakList.size(); i++) {
            PackageInfo pak =  pakList.get(i);
            //判断是否为系统预装的应用
            if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0){
                apps.put(pak.packageName,pak.applicationInfo.loadLabel(pManager).toString());
            }
        }
        return apps;
    }
    /**
     * 获取应用程序名称
     *
     * @param context
     * @return appName;
     */
    public static String getAppName(Context context) {
        try {
            PackageManager pManager = context.getPackageManager();
            PackageInfo pInfo = pManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = pInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
