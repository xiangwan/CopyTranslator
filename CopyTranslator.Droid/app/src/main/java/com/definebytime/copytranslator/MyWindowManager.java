package com.definebytime.copytranslator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;

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
    private static int screenWidth;
    private static int screenHeight;
    private static Animation animBoxOut;
    private static Animation animBoxIn;
    private static Animation animBarIn;
    private static Animation animBarDock;

    /**
     * 创建一个小悬浮窗。初始位置为屏幕的右部中间位置。
     *
     * @param context 必须为应用程序的Context.
     */
    private static void createBarWindow(final Context context) {
        if (barWindow == null) {
            WindowManager windowManager = getWindowManager(context);
             screenWidth = windowManager.getDefaultDisplay().getWidth();
              screenHeight = windowManager.getDefaultDisplay().getHeight();
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
                barWindowParams.x = screenWidth/2- FloatWindowBarView.viewWidth-100;;
                barWindowParams.y = screenHeight / 2-FloatWindowBarView.viewHeight-100;;
            }
            barWindow.setParams(barWindowParams);
            windowManager.addView(barWindow, barWindowParams);
            if (boxWindow == null) createBoxWindow(context);
              boxWindow.setWebViewListener(new FloatWindowBoxView.WebViewListener() {
                @Override
                public void onPageFinished(String url) {
                    if (isAutoOpenBox(context)){
                        hideBarWindow(context);
                        showBoxWindow(context);
                    }else{
                        showBarWindow(context, false);
                    }
                }

                @Override
                public void onReceivedError(String url, String desc) {
                    if (isAutoOpenBox(context)){
                        hideBarWindow(context);
                        showBoxWindow(context);
                    }else{
                        showBarWindow(context, false);
                    }
                }

                @Override
                public void onProgressChanged(int progress) {
                   barWindow.progressBar.setProgress(progress);
                }
            });

        }
    }

    /**
     * 显示bar window 如果必要创建新实例
     */
    public static void showBarWindow(Context context, boolean isLoading) {
        Log.d("XXX", "showBarWindow");
        if (barWindow == null || barWindowParams == null) createBarWindow(context);
        WindowManager windowManager = getWindowManager(context);
        barWindow.setVisibility(View.VISIBLE);
        if (isLoading) {
            barWindowParams.x = screenWidth/2- FloatWindowBarView.viewWidth-100;
            barWindowParams.y = screenHeight / 2-FloatWindowBarView.viewHeight-100;
            windowManager.updateViewLayout(barWindow,barWindowParams);
            barWindow.progressBar.setProgress(0);
            barWindow.progressBar.setVisibility(View.VISIBLE);
            barWindow.ivOpen.setVisibility(View.GONE);
            return;
        }
        barWindowParams.x =screenWidth-FloatWindowBarView.viewWidth-100;
        barWindowParams.y = screenHeight / 2-FloatWindowBarView.viewHeight-100;
        windowManager.updateViewLayout(barWindow,barWindowParams);
        barWindow.progressBar.setVisibility(View.GONE);
        barWindow.ivOpen.setVisibility(View.VISIBLE);
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
    private static void createBoxWindow(final Context context) {
        if (boxWindow == null) {
            WindowManager windowManager = getWindowManager(context);
              screenWidth = windowManager.getDefaultDisplay().getWidth();
              screenHeight = windowManager.getDefaultDisplay().getHeight();
            boxWindow = new FloatWindowBoxView(context);
            if (boxWindowParams == null) {
                boxWindowParams = new LayoutParams();
             //   boxWindowParams.x = screenWidth / 2 - FloatWindowBoxView.viewWidth / 2;
               // boxWindowParams.y = screenHeight / 2 - FloatWindowBoxView.viewHeight / 2;
                boxWindowParams.type = LayoutParams.TYPE_SYSTEM_ALERT;
                boxWindowParams.format = PixelFormat.RGBA_8888;
                boxWindowParams.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
                boxWindowParams.width = FloatWindowBoxView.viewWidth;
                boxWindowParams.height = FloatWindowBoxView.viewHeight;
            }
            windowManager.addView(boxWindow, boxWindowParams);
            boxWindow.setVisibility(View.GONE);
            boxWindow.boxContainer.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   hideBoxWindow(context);//点击空白处隐藏窗口
               }
           });
        }
    }

    /**
     * 显示bar window 如果必要创建新实例
     *
     * @param context
     */
    public static void showBoxWindow(Context context) {
        if (boxWindow == null || boxWindowParams == null) createBoxWindow(context);
        boxWindow.setVisibility(View.VISIBLE);
        if (animBoxIn==null){
            animBoxIn=AnimationUtils.loadAnimation(context,R.anim.box_in);
        }
        boxWindow.boxContainer.startAnimation(animBoxIn);
    }

    /**
     * 将小悬浮窗从屏幕上移除。
     */
    public static void hideBoxWindow(Context context) {
        if (boxWindow != null) {
            if (boxWindow.getVisibility()!=View.VISIBLE)return;
            if (animBoxOut==null){
                animBoxOut=AnimationUtils.loadAnimation(context,R.anim.box_out);
                animBoxOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        boxWindow.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
            boxWindow.boxContainer.startAnimation(animBoxOut);
        }
    }
    public static void loadNewPage(Context context,String word) {
        Log.d("XXX","loadNewPage");
        showBarWindow(context, true);
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

    private static  boolean isAutoOpenBox(Context context){
        SharedPreferences pref=PreferenceManager.getDefaultSharedPreferences(context);
       return pref.getBoolean("isAutoOpenBox",true);
    }

}
