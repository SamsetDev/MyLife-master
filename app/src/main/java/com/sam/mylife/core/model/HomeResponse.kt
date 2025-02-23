package com.sam.mylife.core.model

import com.google.firebase.firestore.PropertyName

data class HomeResponse(val month:String, var type:Int=1, var isExpanded:Boolean=false, @PropertyName("monthItem") var list:List<MonthItemModel>){
    constructor() : this("",0,false, ArrayList<MonthItemModel>())
}