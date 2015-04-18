package com.definebytime.copytranslator;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class ClipChangedListenerService extends Service {
    private static boolean isRegistered = false;
    private ClipboardManager clipboardManager;
    private MyClipListener myClipListener;

    public ClipChangedListenerService() {
        Log.d("XXX", " ClipChangedListenerService()");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (clipboardManager == null) {
            Log.d("XXX", "onCreate：clipboardManager == null");
            clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        }

        if (myClipListener == null) {
            Log.d("XXX", "onCreate：myClipListener == null");
            myClipListener = new MyClipListener(ClipChangedListenerService.this);
        }
        Notification notification = new Notification(R.drawable.ic_action,
                "复制翻译者已启动", System.currentTimeMillis());
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        notification.setLatestEventInfo(this, "复制翻译者", "复制文本后将自动弹出翻译结果",
                pendingIntent);
        startForeground(1, notification);
        Log.d("XXX", "onCreate() executed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (isRegistered) return Service.START_STICKY;
        if (clipboardManager == null) {
            Log.d("XXX", "onStartCommand：clipboardManager == null");
            clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        }

        if (myClipListener == null) {
            Log.d("XXX", "onStartCommand：myClipListener == null");
            myClipListener = new MyClipListener(ClipChangedListenerService.this);
        }

        clipboardManager.addPrimaryClipChangedListener(myClipListener);
        isRegistered = true;
        Log.d("XXX", "onStartCommand()");
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (clipboardManager != null) {
            if (myClipListener != null) {
                Log.d("XXX", "removePrimaryClipChangedListener");
                clipboardManager.removePrimaryClipChangedListener(myClipListener);
                isRegistered = false;
                MyWindowManager.removeBarWindow(getApplicationContext());
                MyWindowManager.removeBoxWindow(getApplicationContext());
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public class MyClipListener implements ClipboardManager.OnPrimaryClipChangedListener {

        private final Context mContext;

        public MyClipListener(Context context) {
            mContext = context;

        }

        @Override
        public void onPrimaryClipChanged() {
            ClipData cdText = clipboardManager.getPrimaryClip();
            String text = cdText.getItemAt(0).coerceToText(getApplicationContext()).toString();
            String msg = text;
            if (text == null || text.isEmpty()) {
                msg = "剪贴板中无内容";
            } else {
                if (Utils.isNetAvailable(ClipChangedListenerService.this)) {
                    MyWindowManager.showBarWindow(mContext, true);
                    MyWindowManager.loadNewPage(text);
                } else {
                    msg += "网络不可用";
                }

            }
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        }
    }
}

