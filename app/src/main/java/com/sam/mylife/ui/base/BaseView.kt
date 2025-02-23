package com.sam.mylife.ui.base

import androidx.annotation.StringRes

/**
 * Copyright (C) Glasswing Partner - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * <p>
 * Created by Sanjay Singh (samset) on 16,May,2021 at 7:05 PM for Glasswing Partner.
 * <p>
 * New Delhi,India
 */


public interface BaseView{


    fun showLoading()

    fun onTokenExpire()

    fun showError(@StringRes resId: Int)

    fun showError(message: String)

    fun onShowContent()

    fun showServerError(msg:String)

    fun showNetworkError()

    fun showNoData()

    fun onRetry()

    fun isNetworkConnected(): Boolean

    fun hideKeyboard()

}
