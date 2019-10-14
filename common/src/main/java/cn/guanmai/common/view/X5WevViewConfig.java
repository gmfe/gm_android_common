package cn.guanmai.common.view;

public class X5WevViewConfig {
    // 长按屏幕切换 URL 的时间
    private int switchUrlDelayTime = 3000;

    // WebView 的 URL
    private String url = "";

    // 是否在 debug 模式下才能切换 URL
    private boolean onlyCanSwitchUrlInDebug = true;

    // js api 类实例
    private Object jsApi = null;

    // js api 的命名空间
    private String namespace = null;

    private boolean isDebugEnable = true;

    public Object getJsApi() {
        return jsApi;
    }

    public void setJsApi(Object jsApi) {
        this.jsApi = jsApi;
    }

    public boolean isDebugEnable() {
        return isDebugEnable;
    }

    public void setDebugEnable(boolean debugEnable) {
        isDebugEnable = debugEnable;
    }


    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public boolean isOnlyCanSwitchUrlInDebug() {
        return onlyCanSwitchUrlInDebug;
    }

    public void setOnlyCanSwitchUrlInDebug(boolean onlyCanSwitchUrlInDebug) {
        this.onlyCanSwitchUrlInDebug = onlyCanSwitchUrlInDebug;
    }

    public X5WevViewConfig() {
    }

    public int getSwitchUrlDelayTime() {
        return switchUrlDelayTime;
    }

    public void setSwitchUrlDelayTime(int switchUrlDelayTime) {
        this.switchUrlDelayTime = switchUrlDelayTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
