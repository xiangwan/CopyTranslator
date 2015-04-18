package com.definebytime.copytranslator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public class MyWindowManager {

    /**
     * 小悬浮窗View的实例
     */
    private static FloatWindowBarView barWindow;

    /**
     * 大悬浮窗View的实例
     */
    private static FloatWindowBoxView boxWindow;

    /**
     * 小悬浮窗View的参数
     */
    private static LayoutParams barWindowParams;

    /**
     * 大悬浮窗View的参数
     */
    private static LayoutParams boxWindowParams;

    /**
     * 用于控制在屏幕上添加或移除悬浮窗
     */
    private static WindowManager mWindowManager;

    /**
     * 用于获取手机可用内存
     */
    private static ActivityManager mActivityManager;

    /**
     * 创建一个小悬浮窗。初始位置为屏幕的右部中间位置。
     *
     * @param context 必须为应用程序的Context.
     */
    private static void createBarWindow(Context context) {
        if (barWindow == null) {
            WindowManager windowManager = getWindowManager(context);
            int screenWidth = windowManager.getDefaultDisplay().getWidth();
            int screenHeight = windowManager.getDefaultDisplay().getHeight();
            barWindow = new FloatWindowBarView(context);
            if (barWindowParams == null) {
                barWindowParams = new LayoutParams();
                barWindowParams.type = LayoutParams.TYPE_PHONE;
                barWindowParams.format = PixelFormat.RGBA_8888;
                barWindowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | LayoutParams.FLAG_NOT_FOCUSABLE;
                barWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
                barWindowParams.width = FloatWindowBarView.viewWidth;
                barWindowParams.height = FloatWindowBarView.viewHeight;
                barWindowParams.x = screenWidth;
                barWindowParams.y = screenHeight / 2 - 50;
            }
            barWindow.setParams(barWindowParams);
            windowManager.addView(barWindow, barWindowParams);
            if (boxWindow == null) createBoxWindow(context);
            boxWindow.setWebViewListener(new FloatWindowBoxView.WebViewListener() {
                @Override
                public void onPageFinished(String url) {
                    barWindow.progressBar.setVisibility(View.GONE);
                    barWindow.ivOpen.setVisibility(View.VISIBLE);
                    barWindow.launchTask.execute(false);
                }

                @Override
                public void onReceivedError(String url, String desc) {
                    barWindow.progressBar.setVisibility(View.GONE);
                    barWindow.ivOpen.setVisibility(View.VISIBLE);
                    barWindow.launchTask.execute(false);
                }

                @Override
                public void onProgressChanged(int progress) {
                   barWindow.progressBar.setProgress(progress);

                }
            });
            hideBoxWindow(context);
        }
    }

    /**
     * 显示bar window 如果必要创建新实例
     */
    public static void showBarWindow(Context context, boolean isLoading) {

        if (barWindow == null || barWindowParams == null) createBarWindow(context);
        if (isLoading){

        }else{

        }
        barWindow.setVisibility(View.VISIBLE);
    }

    /**
     * 将小悬浮窗从屏幕上移除。
     */
    public static void hideBarWindow(Context context) {
        if (barWindow != null) {
            barWindow.setVisibility(View.GONE);
        }

    }

    /**
     * 解除引用
     */
    public static void removeBarWindow(Context context) {
        if (barWindow != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(barWindow);
            barWindow = null;
        }
    }

    /**
     * 创建一个大悬浮窗。位置为屏幕正中间。
     *
     * @param context 必须为应用程序的Context.
     */
    private static void createBoxWindow(Context context) {
        if (boxWindow == null) {
            WindowManager windowManager = getWindowManager(context);
            int screenWidth = windowManager.getDefaultDisplay().getWidth();
            int screenHeight = windowManager.getDefaultDisplay().getHeight();
            boxWindow = new FloatWindowBoxView(context);
            if (boxWindowParams == null) {
                boxWindowParams = new LayoutParams();
                boxWindowParams.x = screenWidth / 2 - FloatWindowBoxView.viewWidth / 2;
                boxWindowParams.y = screenHeight / 2 - FloatWindowBoxView.viewHeight / 2;
                boxWindowParams.type = LayoutParams.TYPE_SYSTEM_ALERT;
                boxWindowParams.format = PixelFormat.RGBA_8888;
                boxWindowParams.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
                boxWindowParams.width = FloatWindowBoxView.viewWidth;
                boxWindowParams.height = FloatWindowBoxView.viewHeight;
            }
            windowManager.addView(boxWindow, boxWindowParams);
        }
    }

    /**
     * 显示bar window 如果必要创建新实例
     *
     * @param context
     */
    public static void showBoxWindow(Context context) {
        WindowManager windowManager = getWindowManager(context);
        if (boxWindow == null || boxWindowParams == null) createBoxWindow(context);
        boxWindow.setVisibility(View.VISIBLE);
    }

    /**
     * 将小悬浮窗从屏幕上移除。
     */
    public static void hideBoxWindow(Context context) {
        if (boxWindow != null) {
            boxWindow.setVisibility(View.GONE);
        }
    }
    public static void loadNewPage(String word) {
        barWindow.progressBar.setProgress(0);
        barWindow.progressBar.setVisibility(View.VISIBLE);
        barWindow.launchTask.execute(true);
        barWindow.ivOpen.setVisibility(View.GONE);
        boxWindow.wvResult.loadUrl("http://newwap.iciba.com/cword/" + word);
    }
    /**
     * 将大悬浮窗从屏幕上移除。
     *
     * @param context 必须为应用程序的Context.
     */
    public static void removeBoxWindow(Context context) {
        if (boxWindow != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(boxWindow);
            boxWindow = null;
        }
    }




    /**
     * 如果WindowManager还未创建，则创建一个新的WindowManager返回。否则返回当前已创建的WindowManager。
     *
     * @param context 必须为应用程序的Context.
     * @return WindowManager的实例，用于控制在屏幕上添加或移除悬浮窗。
     */
    private static WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }

    /**
     * 如果ActivityManager还未创建，则创建一个新的ActivityManager返回。否则返回当前已创建的ActivityManager。
     *
     * @param context 可传入应用程序上下文。
     * @return ActivityManager的实例，用于获取手机可用内存。
     */
    private static ActivityManager getActivityManager(Context context) {
        if (mActivityManager == null) {
            mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        }
        return mActivityManager;
    }

}
