package com.definebytime.copytranslator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

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
	 * @param context
	 *            必须为应用程序的Context.
	 */
	public static void createBarWindow(Context context) {
		WindowManager windowManager = getWindowManager(context);
		int screenWidth = windowManager.getDefaultDisplay().getWidth();
		int screenHeight = windowManager.getDefaultDisplay().getHeight();
		if (barWindow == null) {
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
				barWindowParams.y = screenHeight / 2;
			}
			barWindow.setParams(barWindowParams);
			windowManager.addView(barWindow, barWindowParams);
		}
	}

	/**
	 * 将小悬浮窗从屏幕上移除。
	 * 
	 * @param context
	 *            必须为应用程序的Context.
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
	 * @param context
	 *            必须为应用程序的Context.
	 */
	public static void createBoxWindow(Context context) {
		WindowManager windowManager = getWindowManager(context);
		int screenWidth = windowManager.getDefaultDisplay().getWidth();
		int screenHeight = windowManager.getDefaultDisplay().getHeight();
		if (boxWindow == null) {
			boxWindow = new FloatWindowBoxView(context);
			if (boxWindowParams == null) {
				boxWindowParams = new LayoutParams();
				boxWindowParams.x = screenWidth / 2 - FloatWindowBoxView.viewWidth / 2;
				boxWindowParams.y = screenHeight / 2 - FloatWindowBoxView.viewHeight / 2;
				boxWindowParams.type = LayoutParams.TYPE_SYSTEM_ALERT;
				boxWindowParams.format = PixelFormat.RGBA_8888;
				boxWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
				boxWindowParams.width = FloatWindowBoxView.viewWidth;
				boxWindowParams.height = FloatWindowBoxView.viewHeight;
			}
			windowManager.addView(boxWindow, boxWindowParams);
		}
	}

	/**
	 * 将大悬浮窗从屏幕上移除。
	 * 
	 * @param context
	 *            必须为应用程序的Context.
	 */
	public static void removeBoxWindow(Context context) {
		if (boxWindow != null) {
			WindowManager windowManager = getWindowManager(context);
			windowManager.removeView(boxWindow);
			boxWindow = null;
		}
	}

	/**
	 * 更新小悬浮窗的TextView上的数据，显示内存使用的百分比。
	 * 
	 * @param context
	 *            可传入应用程序上下文。
	 */
	public static void updateUsedPercent(Context context) {
		if (barWindow != null) {
			TextView percentView = (TextView) barWindow.findViewById(R.id.tvMsg);
			percentView.setText(getUsedPercentValue(context));
		}
	}

	/**
	 * 是否有悬浮窗(包括小悬浮窗和大悬浮窗)显示在屏幕上。
	 * 
	 * @return 有悬浮窗显示在桌面上返回true，没有的话返回false。
	 */
	public static boolean isWindowShowing() {
		return barWindow != null || boxWindow != null;
	}

	/**
	 * 如果WindowManager还未创建，则创建一个新的WindowManager返回。否则返回当前已创建的WindowManager。
	 * 
	 * @param context
	 *            必须为应用程序的Context.
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
	 * @param context
	 *            可传入应用程序上下文。
	 * @return ActivityManager的实例，用于获取手机可用内存。
	 */
	private static ActivityManager getActivityManager(Context context) {
		if (mActivityManager == null) {
			mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		}
		return mActivityManager;
	}

	/**
	 * 计算已使用内存的百分比，并返回。
	 * 
	 * @param context
	 *            可传入应用程序上下文。
	 * @return 已使用内存的百分比，以字符串形式返回。
	 */
	public static String getUsedPercentValue(Context context) {
		String dir = "/proc/meminfo";
		try {
			FileReader fr = new FileReader(dir);
			BufferedReader br = new BufferedReader(fr, 2048);
			String memoryLine = br.readLine();
			String subMemoryLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"));
			br.close();
			long totalMemorySize = Integer.parseInt(subMemoryLine.replaceAll("\\D+", ""));
			long availableSize = getAvailableMemory(context) / 1024;
			int percent = (int) ((totalMemorySize - availableSize) / (float) totalMemorySize * 100);
			return percent + "%";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "悬浮窗";
	}

	/**
	 * 获取当前可用内存，返回数据以字节为单位。
	 * 
	 * @param context
	 *            可传入应用程序上下文。
	 * @return 当前可用内存。
	 */
	private static long getAvailableMemory(Context context) {
		ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
		getActivityManager(context).getMemoryInfo(mi);
		return mi.availMem;
	}

}
