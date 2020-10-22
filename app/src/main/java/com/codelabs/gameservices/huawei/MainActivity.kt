package com.codelabs.gameservices.huawei

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hms.support.hwid.result.HuaweiIdAuthResult
import org.json.JSONException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 6013) {
            if (null == data) {
                Log.d("MainActivity", "signIn intent is null")
                return
            }
            val jsonSignInResult = data.getStringExtra("HUAWEIID_SIGNIN_RESULT")
            if (TextUtils.isEmpty(jsonSignInResult)) {
                Log.d("MainActivity", "signIn result is empty")
                return
            }
            try {
                val signInResult =
                    HuaweiIdAuthResult().fromJson(jsonSignInResult)
                if (0 == signInResult.status.statusCode) {
                    Log.d("MainActivity", ("signIn success."))
                    Log.d("MainActivity", "signIn result: " + signInResult.toJson())
                } else {
                    Log.d("MainActivity","signIn failed: " + signInResult.status.statusCode)
                }
            } catch (var7: JSONException) {
                Log.d("MainActivity","Failed to convert json from signInResult.")
            }
        }
    }
}