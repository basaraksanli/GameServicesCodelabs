package com.codelabs.gameservices.huawei

import android.app.Activity
import android.util.Log
import com.codelabs.gameservices.huawei.ui.main.ResultListener
import com.huawei.hmf.tasks.Task
import com.huawei.hms.common.ApiException
import com.huawei.hms.jos.JosApps
import com.huawei.hms.jos.games.AchievementsClient
import com.huawei.hms.jos.games.Games
import com.huawei.hms.jos.games.PlayersClient
import com.huawei.hms.jos.games.buoy.BuoyClient
import com.huawei.hms.jos.games.player.Player
import com.huawei.hms.support.hwid.HuaweiIdAuthManager
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper

class GameServiceManager(activity: Activity) {

    companion object {
        private const val TAG = "GameServiceManager"
        const val SIGN_IN_REQUEST_CODE = 6013
    }

    private var mActivity: Activity? = activity

    private var mAchievementClient: AchievementsClient? = null
    private var mBuoyClient: BuoyClient? = null
    private var mPlayersClient: PlayersClient? = null
    private var mDisplayName: String? = null

    private var playerID: String? = null

    init {
        init()
    }

    private fun init() {

        // TODO: Initialize the Client in order to make Game Service work
        JosApps.getJosAppsClient(mActivity).init()

        mAchievementClient = Games.getAchievementsClient(mActivity)
        mBuoyClient = Games.getBuoyClient(mActivity)
    }

    fun getAchievementClient() = mAchievementClient

    fun setActivity(activity: Activity) {
        mActivity = activity
    }

    //this function is used to sign in the user. If the user already signed in before, it process silent sign in. If it is the first time for the user, it starts the intent of Huawei ID sign in
    fun signIn(resultListener: ResultListener) {
        // TODO: Sign in with Huawei ID
        if(playerID == null) {
            val authHuaweiIdTask =
                HuaweiIdAuthManager.getService(mActivity, getHuaweiIdParams()).silentSignIn()

            authHuaweiIdTask.addOnSuccessListener {
                mDisplayName = it.displayName
                resultListener.onSuccess(it.displayName)
                getPlayerID()
            }.addOnFailureListener { e: Exception? ->
                if (e is ApiException) {
                    val service = HuaweiIdAuthManager.getService(mActivity, getHuaweiIdParams())
                    mActivity!!.startActivityForResult(service.signInIntent, SIGN_IN_REQUEST_CODE)
                }
                resultListener.onFailure(e?.localizedMessage)
            }
        }
    }

    //This function returns the HuaweiIDAuth parameters for HuaweiID sign in. Parameters is set for Game Sign in
    private fun getHuaweiIdParams(): HuaweiIdAuthParams? {
        // TODO: Set HuaweiAuth Parameter as Game Login
        return HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM_GAME)
            .setIdToken()
            .createParams()
    }

    //this is the function for getting Player ID
    private fun getPlayerID() {
        mPlayersClient = Games.getPlayersClient(mActivity)
        val playerTask: Task<Player> = mPlayersClient!!.currentPlayer
        playerTask.addOnSuccessListener { player: Player ->
            playerID = player.playerId
        }.addOnFailureListener { e: java.lang.Exception? ->
            if (e is ApiException) {
                Log.wtf(TAG, "Game login status: ${e.statusCode}", e)
            } else {
                Log.e(TAG, "Game login error:", e)
            }
        }
    }

    //this function unlocks the achievement immediately
    fun unlockAchievement(achievementID: String) {
        // TODO: Unlock Achievement using reachwithResult function. This can be also used without the callback function.
        mAchievementClient!!.reachWithResult(achievementID)
            .addOnSuccessListener { v: Void? ->
                Log.d(TAG, "Unlock Achievement: reach  success")
            }.addOnFailureListener { e: Exception? ->
                if (e is ApiException) {
                    val result = ("rtnCode:"
                            + e.statusCode)
                    Log.e(TAG, "Unlock Achievement: reach result: $result")
                } else {
                    Log.e(TAG, "Unlock Achievement: Error: ", e)
                }
            }
    }

    //this function reveals the achievement immediately
    fun revealAchievement(achievementId: String) {
        // TODO: Reveal Hidden Achievement with visualizeWithResult function. This can be also used without the callback function.
        val task = mAchievementClient!!.visualizeWithResult(achievementId)
        task.addOnSuccessListener {
            Log.d("revealAchievement", "revealAchievemen isSucess")
        }.addOnFailureListener { e ->
            if (e is ApiException) {
                val result = "rtnCode:" + e.statusCode
                Log.e(TAG, "Reveal Achievement: achievement is not hidden$result")
            } else {
                Log.e(TAG, "Reveal Achievement: Error: ", e)
            }
        }
    }


    //this function increments the steps of the achievements. Step number to increment is configurable
    fun increment(achievementId: String?, isChecked: Boolean) {
        // TODO: Increment step count of the achievement using grow function or use the grow function with a call back
        if (!isChecked) {
            mAchievementClient!!.grow(achievementId, 1)
        } else {
            incrementWithResult(achievementId!!, 1)
        }
    }

    //this function sets the step number of the achievement for the user. However You cant set the step lower then current state.
    fun setStep(achievementId: String?, stepsNum: Int, isChecked: Boolean) {
        // TODO: set step count of the achievement using makeSteos function or use the makeStep function with a call back
        if (!isChecked) {
            mAchievementClient!!.makeSteps(achievementId, stepsNum)
        } else {
            setStepsWithResult(achievementId!!, stepsNum)
        }
    }

    //Same function but task version, can attach callbacks
    private fun incrementWithResult(
        // TODO: Increment step count of achievement using growWithResult with a callback
        achievementId: String,
        stepsNum: Int
    ) {
        val task =
            mAchievementClient!!.growWithResult(achievementId, stepsNum)
        task.addOnSuccessListener { isSuccess ->
            if (isSuccess) {
                Log.d(TAG, "Achievement Increment: incrementAchievement isSuccess")
            } else {
                Log.d(TAG, "Achievement Increment: achievement can not grow")
            }
        }.addOnFailureListener { e ->
            if (e is ApiException) {
                val result = "rtnCode:" + e.statusCode
                Log.e(TAG, "Achievement Increment: has bean already unlocked$result")
            } else {
                Log.e(TAG, "Achievement Increment: Error: ", e)
            }
        }
    }

    //Same function but task version, can attach callbacks
    private fun setStepsWithResult(
        // TODO: Set step count of achievement using makeStepsWithResult with a callback
        achievementId: String,
        stepsNum: Int
    ) {
        val task = mAchievementClient!!.makeStepsWithResult(achievementId, stepsNum)
        task.addOnSuccessListener { isSuccess ->
            if (isSuccess) {
                Log.d(TAG, "Achievement Set Step: setAchievementSteps isSuccess")
            } else {
                Log.d(TAG, "Achievement Set Step: achievement can not makeSteps")
            }
        }.addOnFailureListener { e ->
            if (e is ApiException) {
                val result = "rtnCode:" + e.statusCode
                Log.e(TAG, "Achievement Set Step: step num is invalid$result")
            } else {
                Log.e(TAG, "Achievement Set Step: Error: ", e)
            }
        }
    }
}