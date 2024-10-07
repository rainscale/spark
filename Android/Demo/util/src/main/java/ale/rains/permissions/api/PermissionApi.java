package ale.rains.permissions.api;

import android.content.Context;

import ale.rains.util.RomUtils;

public class PermissionApi extends BasePermissionApi {

    private BasePermissionApi mPermissionApi;

    public PermissionApi() {
        if (RomUtils.isHuaweiSystem()) {
            mPermissionApi = new HuaWeiPermissionApi();
        } else if (RomUtils.isVivoSystem()) {
            mPermissionApi = new VivoPermissionApi();
        } else if (RomUtils.isMiuiSystem()) {
            mPermissionApi = new XiaoMiPermissionApi();
        } else {
            mPermissionApi = new BasePermissionApi();
        }
    }

    @Override
    public boolean checkFloatWindowPermission23(Context context) {
        return mPermissionApi.checkFloatWindowPermission23(context);
    }

    @Override
    public void applyFloatWindowPermission23(Context context) {
        mPermissionApi.applyFloatWindowPermission23(context);
    }

    @Override
    public void applyFloatWindowPermission(Context context) {
        mPermissionApi.applyFloatWindowPermission(context);
    }

    @Override
    public boolean checkFloatWindowPermission(Context context) {
        return mPermissionApi.checkFloatWindowPermission(context);
    }
}