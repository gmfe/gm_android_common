package cn.guanmai.demo;

import android.os.Bundle;

import cn.guanmai.common.view.X5WebViewActivity;

public class DemoActivity extends X5WebViewActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getConfig().setUrl("http://soft.imtt.qq.com/browser/tes/feedback.html");
        getConfig().setJsApi(new JsApi(this));
        super.onCreate(savedInstanceState);
    }
}
