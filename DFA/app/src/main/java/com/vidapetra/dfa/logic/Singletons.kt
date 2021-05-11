package com.vidapetra.dfa.logic

import android.annotation.SuppressLint
import android.app.Application
import android.app.Dialog
import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.myitsolver.baseandroidapp.logic.EventBus
import com.myitsolver.baseandroidapp.logic.Navigation
import com.myitsolver.baseandroidapp.views.biggerThan
import com.vidapetra.dfa.dialog.*
import com.vidapetra.dfa.MainActivity
import com.vidapetra.dfa.R


@SuppressLint("StaticFieldLeak")
object Singletons {
    fun initAll(application: Application) {
        applicationContext = application

//        onIO{
//            db.pressureDao().insertPressure(Pressure(130,90,61,"régi", Date(Date().time-3*24*3600*1000)))
////            db.pressureDao().insertPressure(Pressure(125,85,51,null, Date(Date().time-10000)))
//        }
        uiData = UiData()
    }



    lateinit var applicationContext: Context
    lateinit var uiData: UiData
    /**
     * Single activity app, so it should be OK, but it must be cleared in activity's ondestroy
     */
    var activityContext: MainActivity? = null
}

val activityContext: MainActivity?
    get() = Singletons.activityContext
val applicationContext: Context
    get() = Singletons.applicationContext

val uiData
    get() = Singletons.uiData


/**
 * Router object to make the navigation easier in single activity apps
 */
object Router {

    fun loadFragment(f: Fragment, params: (Navigation.LoadParams.() -> Navigation.LoadParams)? = null) {
        activityContext?.loadFragment(f, params)
    }

    inline fun <reified T : Fragment> goBackUntil(fragment: T) {
        activityContext?.navigation?.positionInStack(T::class.java)?.let {
            for (i in 0 until it) back()
            return
        }
        loadFragment(fragment)
    }

    fun back() {
        activityContext?.navigateBack()
    }
}

/**
 * Toastr object to show dialogs in single activity apps without explicitly having the context
 */
object Toastr {
    fun showInfoDialog(
            title: String?,
            message: String?,
            yesText: String = string(R.string.ok),
            listener: ((Boolean, Dialog) -> Unit)? = null
    ) {
        activityContext?.showInfoDialog(title = title, message = message, yesText = yesText, listener = listener)
    }

    fun showQuestionDialog(
        title: String?,
        message: String?,
        yesText: String,
        noText: String,
        listener: ((Boolean) -> Unit)
    ) {
        activityContext?.showQuestionDialog(
            title = title,
            message = message,
            yesText = yesText,
            noText = noText,
            listener = listener
        )
    }

    private var dialog: Dialog? = null

    fun showLoadingDialog(title: String?) {
        if (dialog != null) {
            hideLoadingDialog()
        }
        dialog = activityContext?.showLoadingDialog(title)
    }

    fun hideLoadingDialog() {
        try {
            dialog?.dismiss()
        }finally {
            dialog = null
        }
    }
}


val eventBus = EventBus()



fun string(@StringRes res: Int): String = applicationContext.getString(res)

class UiData {
//    val toolbarVisible = MediatorLiveData<Boolean>().apply {
//        addSource(userManager.isUserLoggedIn) {
//            postValue(it)
//        }
//    }
    val toolbarTitle = MutableLiveData<String>()
    val toolbarEvents = eventBus.getBus("toolbar")
    val notificationCount = MutableLiveData<Int>()
    val bottomBarVisible = MutableLiveData<Boolean>()
}

enum class ToolbarEvents {
    MENU, SETTINGS, BACK
}

object badgeData {
    val menuBadge = MediatorLiveData<Boolean>()
    val chatBadge = MutableLiveData<Int>()
    val notiBadge = MutableLiveData<Int>()

    init {
        menuBadge.addSource(chatBadge) {
            setMenuBadge()
        }
        menuBadge.addSource(notiBadge) {
            setMenuBadge()
        }
    }

    private fun setMenuBadge() {
        menuBadge.postValue(chatBadge.value?.biggerThan(0) ?: false || notiBadge.value?.biggerThan(0) ?: false)
    }
}
