package com.linqinen.library.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.linqinen.library.R;
import com.linqinen.library.utils.LogT;

/**
 * 纯净版下拉刷新webview
 */

public class PullToRefreshWebView extends RelativeLayout {
    private WebView web_view_;
    private Context current_context_;
    private String current_url_;
    public static final int STATUS_PULL_TO_REFRESH = 0;
    public static final int STATUS_RELEASE_TO_REFRESH = 1;
    public static final int STATUS_REFRESHING = 2;
    public static final int STATUS_REFRESH_FINISHED = 3;
    private int current_status_ = STATUS_REFRESH_FINISHED;
    private float pressed_coords_y;
    private View header_;
    private MarginLayoutParams header_layout_params_;
    private int hide_header_height_;
    private float original_location_y_;
    private int[] location_array_ = new int[2];
    private boolean load_once_ = false;
    private int touchSlop;
    private int last_status_;
    private final ProgressBar progressbar;
    boolean can_pull_to_refresh_ = false;//刷新开关

    public PullToRefreshWebView(Context aContext, AttributeSet attrs) {
        super(aContext,attrs);
        LayoutInflater.from(aContext).inflate(R.layout.pull_to_refresh_web_view,this);
        current_context_ = aContext;
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
    }

    /**
     * 将页面信息缓存到文件夹
     * Description: init the webview object
     *  We add webView object into view with codes but not in XML, the benefits is that we can release
     *  the memory more deeply and clearly when we destroy webview's instance.
     * @param   aContext        current mContext
     */
    private void initWebView(Context aContext) {
        RelativeLayout webview_container = (RelativeLayout) findViewById(R.id.base_web_view_container);
        web_view_ = new MyWebView(aContext,null);

        
        web_view_.getSettings().setSupportZoom(true);//可缩放
        web_view_.getSettings().setJavaScriptEnabled(true);
        web_view_.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        web_view_.getSettings().setBlockNetworkImage(false);//把图片加载放在最后来加载渲染
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.LOLLIPOP ){
            web_view_.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        web_view_.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);  //设置 缓存模式
        web_view_.getSettings().setDomStorageEnabled(true);
        web_view_.getSettings().setDatabaseEnabled(true);
        String cacheDirPath = aContext.getFilesDir().getAbsolutePath() + "InteractionWebView";
        LogT.d("WebView缓存目录=" + cacheDirPath);
        web_view_.getSettings().setDatabasePath(cacheDirPath);
        web_view_.getSettings().setAppCachePath(cacheDirPath);
        web_view_.getSettings().setAppCacheEnabled(true);
        web_view_.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
//        progressbar =  (ProgressBar) findViewById(progressbar);
        webview_container.addView(web_view_);
    }

    /**
     * Description: this method is 'public' for outer class to load url into progressWebView
     *  manually.
     *  In some case, outer class may want to load url into webView.
     *  For example, when login success, the inside activity should refresh the url before goto login.
     * @param   url     url for navigating
     */
    public void loadUrl(String url) {
        if (web_view_ == null && current_context_ != null) {
            LogT.d("current web view has not be init, do it now");
            initWebView(current_context_);
        }
        if (url != null && !url.equals("")) {
            web_view_.loadUrl(url);
        } else {
            LogT.e("Url navigating is NULL or empty");
        }
    }

    public boolean canGoBack() {
        return web_view_.canGoBack();
    }

    /**
     * Description: webView's goBack
     */
    public void goBack() {
        if (canGoBack()) {
            web_view_.goBack();
        } else {
            LogT.d("Can NOT go back anymore");
        }
    }



    /**
     * Description: update header's view
     */


    public void destroyView(){
        web_view_.destroy();
    }

    /**
     * Description: refreshing state
     *  1. set top margin
     *  2. password_eyes_show progress view and password_eyes_hide arrow
     *  3. reload webview
     */

    public class MyWebView extends WebView {

        public MyWebView(Context context) {
            this(context,null);
        }

        public MyWebView(Context context, AttributeSet attrs) {
            this(context, attrs, Resources.getSystem().getIdentifier("webViewStyle", "attr", "android"));
        }

        @SuppressLint("SetJavaScriptEnabled")
        public MyWebView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            getSettings().setJavaScriptEnabled(true);

            setWebViewClient(new MyWebViewClient());
            setWebChromeClient(new WebChromeClient());
            this.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true;
                }
            });
        }
        //实时更新进度的样式
        public class WebChromeClient extends android.webkit.WebChromeClient {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                if (newProgress == 100) {
                    progressbar.setVisibility(GONE);
                } else {
                    if (progressbar.getVisibility() == GONE)
                        progressbar.setVisibility(VISIBLE);
                    progressbar.setProgress(newProgress);
                }

                super.onProgressChanged(view, newProgress);
            }
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            current_url_ = url;
            super.onPageStarted(view,url,favicon);
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view,url);

        }
    }


}