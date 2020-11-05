package com.codelabs.gameservices.huawei

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

object Utils {

    fun buildAlertDialogAndNavigate(fragment: Fragment) {
        AlertDialog.Builder(fragment.activity).apply {
            setTitle(fragment.getString(R.string.done))
            setMessage(fragment.getString(R.string.you_have_completed_the_quiz))
            setCancelable(false)
            setNeutralButton(fragment.getString(R.string.yes)) { _: DialogInterface, _: Int ->
                fragment.findNavController().navigate(R.id.end_game_navigation)
            }
            show()
        }
    }

}

/*
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
 */