package com.codelabs.gameservices.huawei

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.codelabs.gameservices.huawei.GameServiceManager.Companion.SIGN_IN_REQUEST_CODE
import com.codelabs.gameservices.huawei.constant.HUAWEI_ID_SIGN_IN_RESULT
import com.huawei.hms.support.hwid.result.HuaweiIdAuthResult
import org.json.JSONException

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }

    //ActivityResult for HuaweiID sign in
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SIGN_IN_REQUEST_CODE) {

            if (null == data) {
                Log.d(TAG, "signIn intent is null")
                return
            }

            val jsonSignInResult = data.getStringExtra(HUAWEI_ID_SIGN_IN_RESULT)

            if (TextUtils.isEmpty(jsonSignInResult)) {
                Log.d(TAG, "signIn result is empty")
                return
            }

            try {
                val signInResult = HuaweiIdAuthResult().fromJson(jsonSignInResult)
                if (0 == signInResult.status.statusCode) {
                    Log.d(TAG, "signIn success. Result: ${signInResult.toJson()}")
                } else {
                    Log.d(TAG,"signIn failed: " + signInResult.status.statusCode)
                }
            } catch (e: JSONException) {
                Log.e(TAG,"Failed to convert json from signInResult.", e)
            }

        }
    }
}