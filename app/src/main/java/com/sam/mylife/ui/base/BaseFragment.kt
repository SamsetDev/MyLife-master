package com.sam.mylife.ui.base


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import com.sam.mylife.R
import com.sam.mylife.core.model.AppUser
import com.sam.mylife.core.preferences.PrefManager
import com.sam.mylife.data.DataViewModel
import com.sam.mylife.databinding.ActivityMainBinding
import com.sam.mylife.ui.activity.MainActivity
import com.sam.mylife.ui.activity.AuthActivity
import com.sam.mylife.utils.CommonUtils
import com.sam.mylife.utils.NetworkHelper
import com.sam.mylife.utils.createDialog

/**
 * Copyright (C) QaddooV2 - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 *
 * Created by Sanjay Singh (samset) on 09,February,2021 at 1:03 AM for QaddooV2.
 *
 *
 * New Delhi,India
 */

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

public abstract class BaseFragment<VBinding: ViewBinding>(private val inflate: Inflate<VBinding>) : Fragment(), BaseView {

    private lateinit var baseActivity: BaseActivity<*>
    private lateinit var prefManager: PrefManager
    private var dialog: Dialog? = null
    private  var userid:String=""
    private  var mobileno:String=""
    private var userInfo: AppUser? = null
    private var viewBinding: VBinding? = null
    val sharedViewModel: DataViewModel by activityViewModels()

     val binding get() = viewBinding!!
    protected abstract fun initView()
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*>) {
            val activity = context
            if (activity is MainActivity){
                baseActivity = activity as BaseActivity<ActivityMainBinding>
            }
            activity.onFragmentAttached()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        try {
            viewBinding = inflate.invoke(inflater, container, false)
        } catch (e: Exception) {
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefManager = PrefManager.getInstance(context)
        if (prefManager?.userData!=null){
            userInfo= prefManager?.userData as AppUser

        }


        initView()
    }

    public fun getUserInfo() : AppUser?{
        return userInfo
    }

    public fun getPrefmanager(): PrefManager {
        return prefManager
    }

    protected fun openBottomSheet(bdl:Bundle){
        baseActivity.showBottomSheetDialogFragment(bdl)
    }


    open fun setLoading() {
            hideProgress()
            if (dialog == null) {
                dialog = createDialog()
            }
            dialog?.show()
    }

    protected fun hideProgress() {
        dialog?.let {
            if (it.isShowing) {
                it.dismiss()
                Log.e("TAG", "Progress bar is hidden")
            }
        }
    }


    override fun showLoading() {
        setLoading()

    }

    override fun showError(resId: Int) {
        if (baseActivity != null) {
            baseActivity?.showError(resId)
        }
    }


    override fun showError(message: String) {
        if (baseActivity != null) {
            baseActivity?.showError(message)
        }
    }


    override fun onShowContent() {
       hideProgress()

    }

    override fun showServerError(msg:String) {
       if (msg.isEmpty())  showError(getString(R.string.server_error)) else  showError(msg)
        hideProgress()


    }

    override fun showNetworkError() {
        showError(getString(R.string.network_error))
        hideProgress()

    }

    override fun showNoData() {

        hideProgress()
    }

    override fun onRetry() {

    }

    override fun onTokenExpire() {

    }


    override fun isNetworkConnected(): Boolean {
        return NetworkHelper.isNetworkConnected(requireContext())
    }

    override fun hideKeyboard() {
        CommonUtils.hideKeyboard(requireActivity())
    }

    private fun observeData(){

    }


    public fun logout() {

        val intent = Intent(activity, AuthActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        activity?.finish()
    }

    /*  handle all type of error
    * */
    fun handleError(code: Int, msg: String): Boolean {
        var status = true
        when (code) {
            209 -> {
                status = false
                showError(getString(R.string.data_not_found))
            }
            401 -> {
                status = false
                //showError(getString(R.string.please_login_again))
            }
            422 -> {
                status = false
                showError(getString(R.string.server_error))
            }
            404 -> {
                status = false
                showNetworkError()
            }
            211 -> {
                status = false
                showError(getString(R.string.server_error))
            }
            500 -> {
                status = false
                showNetworkError()
            }
            403 -> {
                status = false
                //showError(getString(R.string.please_login_again))
            }
            300 -> {
                status = false
                showError(msg)
            }
        }
        return status
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding=null
    }

    internal interface Callback {
        fun onFragmentAttached()
        fun onFragmentDetached(tag: String?)
    }

}