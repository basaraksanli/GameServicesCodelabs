package com.codelabs.gameservices.huawei

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class Utils {
    companion object {
        fun buildAlertDialogAndNavigate(fragment: Fragment) {
            val alert = AlertDialog.Builder(fragment.activity)
            alert.setTitle("Done!")
            alert.setMessage("You have completed the quiz")
            alert.setCancelable(false)
            alert.setNeutralButton("Evet") { _: DialogInterface, _: Int ->
                fragment.findNavController().navigate(R.id.end_game_navigation)
            }
            alert.show()
        }
    }


}