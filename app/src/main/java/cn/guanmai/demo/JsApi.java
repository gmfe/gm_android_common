package cn.guanmai.demo;

import android.app.Activity;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class JsApi {
    private Activity mActivity;

    public JsApi(Activity activity) {
        mActivity = activity;
    }

    @JavascriptInterface
    public void test(Object o) {
        Toast.makeText(mActivity, "test", Toast.LENGTH_SHORT).show();
    }
}
