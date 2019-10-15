package cn.guanmai.demo;

import android.Manifest;
import android.os.Bundle;

import androidx.annotation.NonNull;
import cn.guanmai.common.view.X5WebViewActivity;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class DemoActivity extends X5WebViewActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getConfig().setUrl("http://soft.imtt.qq.com/browser/tes/feedback.html");
        getConfig().setJsApi(new JsApi(this));
        initView();
        DemoActivityPermissionsDispatcher.getPermissionWithPermissionCheck(this);
    }


    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void getPermission() {
        pgyerCheckUpdate();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        DemoActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
