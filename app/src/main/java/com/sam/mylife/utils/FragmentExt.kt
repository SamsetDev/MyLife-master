package com.sam.mylife.utils

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.fragment.app.Fragment
import com.sam.mylife.R

/**

 * Copyright (C) Glasswing Partner - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Created by Sanjay Singh (samset) on 19,May,2021 at 1:37 PM for Glasswing Partner.
 *
New Delhi,India
 */


fun Fragment.createDialog(): Dialog? {
    val progressDialog = Dialog(requireContext())
    progressDialog.let {
        it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        it.setContentView(R.layout.progressbar_layout)
        it.setCancelable(false)
        it.setCanceledOnTouchOutside(false)
        return it
    }
}
