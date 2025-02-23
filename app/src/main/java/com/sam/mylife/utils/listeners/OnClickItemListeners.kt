package com.sam.mylife.utils.listeners

import android.view.View

interface OnClickItemListeners {
    fun onItemClick(data:Any)
    fun onItemClick(data:Any,docId:String,v: View)
}