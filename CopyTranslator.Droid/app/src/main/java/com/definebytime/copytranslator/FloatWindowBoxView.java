package com.definebytime.copytranslator;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
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
        ImageButton close = (ImageButton) findViewById(R.id.btnClose);
        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MyWindowManager.hideBoxWindow(context);
                //MyWindowManager.showBarWindow(context, false);
            }
        });
      //  close.getBackground().setAlpha(100);
        wvResult = (WebView) findViewById(R.id.wvResult);
        wvResult.setWebViewClient(new MyWebViewClient());
        wvResult.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                Log.d("XXX","webview % ："+String.valueOf(newProgress));
                if (webViewListener != null) webViewListener.onProgressChanged(newProgress);
            }
        });
        wvResult.getSettings().setJavaScriptEnabled(true);

    }

    public interface WebViewListener {

        void onPageFinished(String url);
        void onReceivedError(String url,String desc);
        void onProgressChanged(int progress);
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

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            if (webViewListener != null) webViewListener.onReceivedError(failingUrl,description);
        }
    }
}
