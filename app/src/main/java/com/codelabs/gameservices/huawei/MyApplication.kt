package com.codelabs.gameservices.huawei

import android.app.Application
import com.huawei.hms.api.HuaweiMobileServicesUtil


class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // have to call this method here
        HuaweiMobileServicesUtil.setApplication(this)
    }

}