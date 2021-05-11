@file:Suppress("unused")

package com.myitsolver.baseandroidapp.activities


import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import com.myitsolver.baseandroidapp.R
import com.myitsolver.baseandroidapp.fragments.ActivityResult
import com.myitsolver.baseandroidapp.fragments.ActivityResultListener
import com.myitsolver.baseandroidapp.interfaces.Controllable
import com.myitsolver.baseandroidapp.interfaces.IndicatorShower
import com.myitsolver.baseandroidapp.logic.Navigation
import com.myitsolver.baseandroidapp.util.getViewModel
import okhttp3.Request


/**
 *
 * Created by Patrik on 2015. 11. 02..
 */
abstract class BaseActivity : AppCompatActivity(), IndicatorShower, Controllable, ActivityResult{
    var progressBar: ProgressBar? = null
    var fadeView: LinearLayout? = null
    private var lastBackTime = System.currentTimeMillis()
    val navigation: Navigation by viewModels<Navigation>()

    override val onActivityResultListeners = mutableListOf<ActivityResultListener>()

    override fun addOnActivityResultListener(listener: ActivityResultListener){
        onActivityResultListeners.add(listener)
    }
    override fun removeOnActivityResultListener(listener: ActivityResultListener){
        onActivityResultListeners.remove(listener)
    }
    override val ctx: Context?
        get() = this
    override val ctxActivity: Activity?
        get() = this


    protected abstract var config: Config

    protected open val contentView: Int
        get() = R.layout.activity_base



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigation.supportFragmentManager = supportFragmentManager
        supportFragmentManager?.registerFragmentLifecycleCallbacks(navigation.listener, false)

        setLayoutId(contentView)

        setLoadingMarks()

        if (intent != null) {
            handleIntent(intent)
        }

    }

    open fun setLayoutId(layoutId: Int){
        setContentView(layoutId)

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if (intent != null) {
            setIntent(intent)
            handleIntent(getIntent())
        }

    }


//VIEWS

    protected open fun handleIntent(intent: Intent) {

    }


    protected fun setLoadingMarks() {
        this.fadeView = findViewById(R.id.loading_background)
    }


    override fun showProgressBar() {
        runOnUiThread {
            progressBar?.visibility = View.VISIBLE
            fadeView?.visibility = View.VISIBLE
        }
    }

    override fun isShowingProgressBar(): Boolean {
        return progressBar?.visibility == View.VISIBLE || fadeView?.visibility == View.VISIBLE
    }

    override fun hideProgressBar() {
        runOnUiThread {
            progressBar?.visibility = View.GONE
            fadeView?.visibility = View.GONE

        }
    }


    override fun showAlertDialog(message: String, listener: DialogInterface.OnClickListener?) {
        runOnUiThread {
            val builder = AlertDialog.Builder(this@BaseActivity)
            builder.setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("OK", listener)
            val alert = builder.create()
            alert.show()
        }
    }


    fun setupUI(view: View?) {
        view?.let {
            // Set up touch listener for non-text box views to hide keyboard.
            if (view !is EditText) {
                view.setOnTouchListener { _, _ ->
                    hideSoftKeyboard()
                    false
                }
            }

            //If a layout container, iterate over children and seed recursion.
            if (view is ViewGroup) {
                for (i in 0 until view.childCount) {
                    val innerView = view.getChildAt(i)
                    setupUI(innerView)
                }
            }
        }
    }

    fun hideSoftKeyboard() {
        try {
            val inputMethodManager = getSystemService(
                    Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun showLoadingIndicator(show: Boolean) {
        if (show) {
            runOnUiThread { showProgressBar() }

        } else {
            runOnUiThread { hideProgressBar() }

        }
    }

    override fun loadFragment(f: Fragment, params: (Navigation.LoadParams.() -> Navigation.LoadParams)?): Boolean {
        return navigation.loadFragment(f, params ?: {this})
                ?: false
    }




    override fun loginRequired() {

    }

    override fun resetBackStack() {
        navigation.resetBackStack()
    }

    fun navigateBack(){
        if (navigation.shouldHandleOnBackPressed()) {
            super.onBackPressed()
        }

    }

    override fun onBackPressed() {
        if (isShowingProgressBar()) {
            //TODO: ide később majd be lehet olyasmit tenni, hogy egy ablakban megkérdezzük a usert, hogy biztos meg akarja-e szakítani az akutális folyamatot, blablabla
            return
        }
        if (!navigation.isUsed()) {
            super.onBackPressed()
            return
        }
        if (navigation.shouldHandleOnBackPressed()) {
            if (!navigation.isOnBackPressedHandled()) {
                super.onBackPressed()
            }
        } else {
            handleExitAttempt()
        }
    }

    open fun handleExitAttempt() {
        if (!config.doubleBackToExit) {
            finish()
            return
        }
        if (System.currentTimeMillis() - 1000 < lastBackTime) {
            finish()
        } else {
            Toast.makeText(this, getString(R.string.press_again_to_exit), Toast.LENGTH_SHORT).show()
        }
        lastBackTime = System.currentTimeMillis()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        onActivityResultListeners.forEach { it.onActivityResult(requestCode, resultCode, data) }
        for (fragment in supportFragmentManager.fragments) {
            if (fragment != null) {
                try {
                    fragment.onActivityResult(requestCode, resultCode, data)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }


    class Config(val hasToolbar: Boolean, val hasBottomMenu: Boolean, val hasNavDrawer: Boolean, val doubleBackToExit: Boolean)

    companion object {

        fun show(context: Context) {
            val i = Intent(context, BaseActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(i)
        }

        fun show(context: Context, bundle: Bundle) {
            val i = Intent(context, BaseActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            i.putExtras(bundle)
            context.startActivity(i)
        }
    }
}
