package com.codelabs.gameservices.huawei

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.codelabs.gameservices.huawei.ui.main.gamebegin.GameBeginViewModel
import com.huawei.hmf.tasks.Task
import com.huawei.hms.common.ApiException
import com.huawei.hms.jos.games.AchievementsClient
import com.huawei.hms.jos.games.Games
import com.huawei.hms.jos.games.PlayersClient
import com.huawei.hms.jos.games.buoy.BuoyClient
import com.huawei.hms.jos.games.player.Player
import com.huawei.hms.support.hwid.HuaweiIdAuthManager
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper


class GameServiceUtils(activity: Activity?, viewModel: GameBeginViewModel?) {


    var activity: Activity? = null
    var viewModel: GameBeginViewModel? = null
    var achievementClient: AchievementsClient? = null
    var buoyClient: BuoyClient? = null
    private var playersClient: PlayersClient? = null

    companion object{
        private var playerID: String? = null
    }



    init {
        this.activity = activity
        this.viewModel = viewModel


        //get the referances of the achievement clients and buoy client
        achievementClient = Games.getAchievementsClient(activity)
        buoyClient = Games.getBuoyClient(activity)

    }

    //This function returns the HuaweiIDAuth parameters for HuaweiID sign in. Parameters is set for Game Sign in
    fun getHuaweiIdParams(): HuaweiIdAuthParams? {
        return HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM_GAME).setIdToken()
            .createParams()
    }

    //this function is used to sign in the user. If the user already signed in before, it process silent sign in. If it is the first time for the user, it starts the intent of Huawei ID sign in
    fun signIn() {
        if(playerID==null) {
            val authHuaweiIdTask =
                HuaweiIdAuthManager.getService(activity, getHuaweiIdParams()).silentSignIn()
            authHuaweiIdTask.addOnSuccessListener {
                viewModel!!.accountName.value = it.displayName
                getPlayerID()
            }.addOnFailureListener { e: Exception? ->
                if (e is ApiException) {
                    val service = HuaweiIdAuthManager.getService(activity, getHuaweiIdParams())
                    activity!!.startActivityForResult(service.signInIntent, 6013)
                }
            }
        }
    }

    //this is the function for getting Player ID
    private fun getPlayerID() {
        playersClient = Games.getPlayersClient(activity)
        val playerTask: Task<Player> = playersClient!!.currentPlayer
        playerTask.addOnSuccessListener { player: Player ->
            playerID = player.playerId
        }.addOnFailureListener { e: java.lang.Exception? ->
            if (e is ApiException) {
                Log.wtf("GAMELOGIN", e.message)
            }
        }
    }

    //this is the achievements list which Huawei Game Service provide. However we used our own list adapter for the achievements page
    fun showAchivementList() {
        val task =
            achievementClient!!.showAchievementListIntent
        task.addOnSuccessListener { intent: Intent? ->
            if (intent == null) {
                Log.d("achivementsList", "intent = null")
            } else {
                try {
                    activity!!.startActivityForResult(intent, 1)
                } catch (e: java.lang.Exception) {
                    Log.d("achivementsList", "Achievement Activity is Invalid - " + e.message)
                }
            }
        }.addOnFailureListener { e: java.lang.Exception? ->
            if (e is ApiException) {
                val result = ("rtnCode:"
                        + e.statusCode)
                Log.d("achivementsList", "result:$result")
            }
        }
    }

    //this function unlocks the achievement immediately

    fun unlockAchievement(achievementID: String) {
        val task: Task<Void> =
            achievementClient!!.reachWithResult(achievementID)
        task.addOnSuccessListener { v: Void? ->
            Log.d("Unlock Achievement", "reach  success")
        }.addOnFailureListener { e: Exception? ->
            if (e is ApiException) {
                val result = ("rtnCode:"
                        + e.statusCode)
                Log.d("Unlock Achievement", "reach result$result")
            }
        }
    }

    //this function reveals the achievement immediately
    fun revealAchievement(achievementId: String) {
        val task =
            achievementClient!!.visualizeWithResult(achievementId)
        task.addOnSuccessListener {
            Log.d("revealAchievement", "revealAchievemen isSucess")
        }.addOnFailureListener { e ->
            if (e is ApiException) {
                val result = "rtnCode:" + e.statusCode
                Log.d("revealAchievement", "achievement is not hidden$result")
            }
        }
    }


    //this function increments the steps of the achievements. Step number to increment is configurable
    fun increment(achievementId: String?, isChecked: Boolean) {
        if (!isChecked) {
            achievementClient!!.grow(achievementId, 1)
        } else {
            incrementWithResult(achievementId!!, 1)
        }
    }

    //this function sets the step number of the achievement for the user. However You cant set the step lower then current state.
    fun setStep(achievementId: String?, stepsNum: Int, isChecked: Boolean) {
        if (!isChecked) {
            achievementClient!!.makeSteps(achievementId, stepsNum)
        } else {
            setStepsWithResult(achievementId!!, stepsNum)
        }
    }

    //Same function but task version, can attach callbacks
    private fun incrementWithResult(
        achievementId: String,
        stepsNum: Int
    ) {
        val task =
            achievementClient!!.growWithResult(achievementId, stepsNum)
        task.addOnSuccessListener { isSucess ->
            if (isSucess) {
                Log.d("AchievementIncrement", "incrementAchievement isSucess")
            } else {
                Log.d("AchievementIncrement", "achievement can not grow")
            }
        }.addOnFailureListener { e ->
            if (e is ApiException) {
                val result = "rtnCode:" + e.statusCode
                Log.d("AchievementIncrement", "has bean already unlocked$result")
            }
        }
    }
    //Same function but task version, can attach callbacks
    private fun setStepsWithResult(
        achievementId: String,
        stepsNum: Int
    ) {
        val task =
            achievementClient!!.makeStepsWithResult(achievementId, stepsNum)
        task.addOnSuccessListener { isSucess ->
            if (isSucess) {
                Log.d("AchievementSetStep", "setAchievementSteps isSucess")
            } else {
                Log.d("AchievementSetStep", "achievement can not makeSteps")
            }
        }.addOnFailureListener { e ->
            if (e is ApiException) {
                val result = "rtnCode:" + e.statusCode
                Log.d("AchievementSetStep", "step num is invalid$result")
            }
        }

    }
}