package com.sam.mylife.core.model;

import com.google.firebase.firestore.PropertyName;

import java.util.List;

public class HomeResult {
    @PropertyName("monthItem")
    private List<MonthItemModel> monthItem;
    
    public List<MonthItemModel> getMonthItem() {
        return monthItem;
    }

    public void setMonthItem(List<MonthItemModel> monthItem) {
        this.monthItem = monthItem;
    }


}
