package com.definebytime.copytranslator;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;

public class FloatWindowBoxView extends LinearLayout {

    /**
     * 记录大悬浮窗的宽度
     */
    public static int viewWidth;

    /**
     * 记录大悬浮窗的高度
     */
    public static int viewHeight;

    public final WebView wvResult;
    private WebViewListener webViewListener;

    public void setWebViewListener(WebViewListener listener) {
        webViewListener = listener;
    }

    public FloatWindowBoxView(final Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.float_box, this);
        View view = findViewById(R.id.llBox);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        Button close = (Button) findViewById(R.id.btnClose);
        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MyWindowManager.hideBoxWindow(context);
                MyWindowManager.showBarWindow(context, false);
            }
        });

        wvResult = (WebView) findViewById(R.id.wvResult);
        wvResult.setWebViewClient(new MyWebViewClient());
        wvResult.getSettings().setJavaScriptEnabled(true);
    }

    public interface WebViewListener {
        void onPageFinished(String url);
    }

    public class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (url.contains("iciba")){
                view.loadUrl("javascript:$('#floatbar').remove();");
            }
            if (webViewListener != null) webViewListener.onPageFinished(url);
        }
    }
}
