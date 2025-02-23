package com.sam.mylife.core.model

import com.google.gson.annotations.SerializedName

 class MonthItemModel(@SerializedName("id")val id:String,
                      @SerializedName("date")val date:String,
                      @SerializedName("item")val item:String,
                      @SerializedName("month")val month:String,
                      @SerializedName("usertoken")val token:String){
     constructor() : this("","","","","")
 }