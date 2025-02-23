package com.sam.mylife.ui.base


import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.sam.mylife.R
import com.sam.mylife.core.model.AppUser
import com.sam.mylife.core.preferences.PrefManager
import com.sam.mylife.data.DataViewModel
import com.sam.mylife.ui.fragments.AddRecordFragment
import com.sam.mylife.utils.AppConstants.LOCAL_LANG_CODE
import com.sam.mylife.utils.ContextUtils
import com.sam.mylife.utils.NetworkHelper
import java.util.*


/**
 * Copyright (C) Glasswing Partner - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * <p>
 * Created by Sanjay Singh (samset) on 16,May,2021 at 7:05 PM for Glasswing Partner.
 * <p>
 * New Delhi,India
 */



abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity(), BaseView, BaseFragment.Callback {
    private var REQUEST_CHECK_SETTINGS = 10001;
    private  var isFirstTime=false
    lateinit var prefManager: PrefManager
    private lateinit var userid:String
    private lateinit var userData: AppUser
    lateinit var binding: VB
    private var dialog: Dialog? = null
    private  val REQUEST_CODE=100
    val sharedViewModel: DataViewModel by viewModels()


    public abstract fun getViewBinding(): VB
    protected abstract fun initView(bundle: Bundle?)

    override fun attachBaseContext(newBase: Context) {// get chosen language from shread preference
        val localeToSwitchTo = PrefManager.getInstance(newBase).getPreference(LOCAL_LANG_CODE,"")
        val locale = Locale(localeToSwitchTo)
        val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase, locale)
        super.attachBaseContext(localeUpdatedContext)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)
        prefManager = PrefManager.getInstance(this)
        if (prefManager.userData!=null){
            userData= prefManager.userData!!
        }
        initView(savedInstanceState)
    }


    private fun hideProgress() {
        if (dialog != null && dialog!!.isShowing)
            dialog?.dismiss()
    }

    override fun showLoading() {
        //setLoading(true)
    }
    private fun showSnackBar(message: String) {
       Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    }

    override fun showError(message: String) {
        if (message != null) {
            showSnackBar(message)
            errorItBaby()
        } else {
            showSnackBar(getString(R.string.server_error))
        }
    }

    private fun errorItBaby() {
        if (Build.VERSION.SDK_INT >= 26) {
            (getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            (getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(500)
        }
    }

    override fun showError(resId: Int) {
        showError(getString(resId))
    }


    override fun isNetworkConnected(): Boolean {
        return NetworkHelper.isNetworkConnected(this)
    }

    override fun onFragmentAttached() {
        Log.e("TAG", " Fragment attached")
    }

    override fun onFragmentDetached(tag: String?) {}
    override fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onTokenExpire() {
        // startActivity(LoginActivity.getStartIntent(this));
        finish()
    }

    override fun showServerError(msg:String) {
        if (msg.isEmpty())  showError(getString(R.string.server_error)) else  showError(msg)
        hideProgress()
    }

    override fun showNetworkError() {
        hideProgress()
        showError(getString(R.string.network_error))
    }

    fun setYear(year: String) {
        sharedViewModel.selectedyear.postValue(year)

    }

    override fun showNoData() {
        hideProgress()
    }
    override fun onShowContent() {
        hideProgress()
    }
    override fun onRetry() {

    }

    public fun showAlertDialog(activity: Activity, title: String, msg: String): Dialog {
        val builder = AlertDialog.Builder(activity)
        if (title != null && title != "") {
            builder.setTitle(title)
        }
        if (msg != null && msg != "") builder.setMessage(msg)
        builder.setPositiveButton(R.string.ok) { dialog, id ->
            dialog.dismiss()
        }
        return builder.show()
    }
    companion object {
        private const val KEY_ACCOUNT = "key_account"
    }

    public fun showBottomSheetDialogFragment(bdl:Bundle?) {
        val arg=AddRecordFragment()
        arg.arguments=bdl
        arg.show(supportFragmentManager, "AddRecord")
    }

}
