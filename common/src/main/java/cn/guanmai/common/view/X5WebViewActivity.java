package cn.guanmai.common.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.pgyersdk.update.javabean.AppBean;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import cn.guanmai.common.R;
import cn.guanmai.common.dialog.EnvDialogFragment;
import cn.guanmai.common.dialog.RebootDialogFragment;
import cn.guanmai.common.dialog.UpdateDialogFragment;
import cn.guanmai.jsbridge.X5WebView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class X5WebViewActivity extends AppCompatActivity {

    public static final int SHOW_ENV_DIALOG = 1;
    public static final String HOME_PAGE_KEY = "home_page";
    private X5WebView mWebView;
    private X5WevViewConfig mConfig = new X5WevViewConfig();
    private ProgressBar mProgressBar;

    public X5WebView getWebView() {
        return mWebView;
    }

    public X5WevViewConfig getConfig() {
        return mConfig;
    }

    public ProgressBar getProgressBar() {
        return mProgressBar;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SHOW_ENV_DIALOG) {
                EnvDialogFragment dialog = new EnvDialogFragment(new EnvDialogFragment.EnvDialogListener() {
                    @Override
                    public void onSubmit(String url) {
                        SPUtils.getInstance().put(HOME_PAGE_KEY, url);
                        AppUtils.relaunchApp();
                    }

                    @Override
                    public void onCancel() {
                    }
                });
                dialog.setCancelable(false);
                dialog.show(getSupportFragmentManager(), EnvDialogFragment.class.getSimpleName());
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_x5webview);

        initX5WebView();
        initView();
        X5WebViewActivityPermissionsDispatcher.getPermissionWithPermissionCheck(this);
        checkUpdate();
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void getPermission() {
        Log.e("TAG", "permission granted");
    }

    public void checkUpdate() {
        new PgyUpdateManager.Builder()
                .setForced(false)
                .setUserCanRetry(true)
                .setDeleteHistroyApk(true)
                .setUpdateManagerListener(new UpdateManagerListener() {
                    @Override
                    public void onNoUpdateAvailable() {
                        Log.d("TAG", "there is no new version");
                    }

                    @Override
                    public void onUpdateAvailable(AppBean appBean) {
                        UpdateDialogFragment dialog = new UpdateDialogFragment(appBean, new UpdateDialogFragment.UpdateDialogListener() {
                            @Override
                            public void onUpdate(AppBean app) {
                                PgyUpdateManager.downLoadApk(app.getDownloadURL());
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
                        dialog.show(getSupportFragmentManager(), UpdateDialogFragment.class.getSimpleName());
                    }

                    @Override
                    public void checkUpdateFailed(Exception e) {
                        Log.d("TAG", "check update failed ", e);
                    }
                })
                .register();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.onResume();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        X5WebViewActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    private void initView() {
        if (!mConfig.isOnlyCanSwitchUrlInDebug() || AppUtils.isAppDebug()) {
            String url = SPUtils.getInstance().getString(HOME_PAGE_KEY, mConfig.getUrl());
            if (!StringUtils.isSpace(url)) {
                mConfig.setUrl(url);
            }
        }
        mProgressBar = findViewById(R.id.progress);
        mWebView = findViewById(R.id.x5_webview);

        if (!StringUtils.isSpace(mConfig.getUrl())) {
            mWebView.loadUrl(mConfig.getUrl());
        }
        if (mConfig.getJsApi() != null) {
            mWebView.addJavascriptObject(mConfig.getJsApi(), mConfig.getNamespace());
        }

        WebSettings settings = mWebView.getSettings();
        setupWebViewSettings(settings);

        X5WebView.setWebContentsDebuggingEnabled(mConfig.isDebugEnable());

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                webView.loadUrl(s);
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
                return super.shouldOverrideUrlLoading(webView, webResourceRequest);
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setupWebViewSettings(WebSettings settings) {
        settings.setSaveFormData(false);
        settings.setSavePassword(false);
        settings.setDatabaseEnabled(true);   //开启 database storage API 功能
        settings.setDomStorageEnabled(true); // 开启 DOM storage 功能
        settings.setAppCacheEnabled(true);
        String cachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        settings.setAppCachePath(cachePath);
        // settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccess(true);

        //设置自适应屏幕，两者合用
        // settings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        // settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        settings.setSupportZoom(false); //支持缩放，默认为true。是下面那个的前提。
        settings.setBuiltInZoomControls(false); //设置内置的缩放控件。若为false，则该WebView不可缩放
        settings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setDefaultTextEncodingName("utf-8");
    }

    private void initX5WebView() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_PRIVATE_CLASSLOADER, true);
        QbSdk.initTbsSettings(map);
        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {
            }

            @Override
            public void onInstallFinish(int i) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    RebootDialogFragment dialog = new RebootDialogFragment(new RebootDialogFragment.RebootDialogListener() {
                        @Override
                        public void onReboot() {
                            AppUtils.relaunchApp(true);
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                    dialog.setCancelable(false);
                    dialog.show(getSupportFragmentManager(), RebootDialogFragment.class.getSimpleName());
                }
            }

            @Override
            public void onDownloadProgress(int i) {
            }
        });
    }

    private int startX;
    private int startY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (AppUtils.isAppDebug()) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = (int) ev.getX();
                    startY = (int) ev.getY();
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mHandler.obtainMessage(SHOW_ENV_DIALOG).sendToTarget();
                        }
                    }, mConfig.getSwitchUrlDelayTime());
                    break;
                case MotionEvent.ACTION_MOVE:
                    int lastX = (int) ev.getX();
                    int lastY = (int) ev.getY();
                    if (Math.abs(startX - lastX) > 20 || Math.abs(startY - lastY) > 20) {
                        mHandler.removeCallbacksAndMessages(null);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    mHandler.removeCallbacksAndMessages(null);
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        if (mWebView == null) {
            return;
        }
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        if (mWebView != null) {
            mWebView.onPause();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        // 释放资源
        recycleWebView();
        super.onDestroy();
    }

    private void recycleWebView() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
    }
}
