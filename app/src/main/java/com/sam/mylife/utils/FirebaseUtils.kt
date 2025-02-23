package com.sam.mylife.utils

import android.annotation.SuppressLint
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sam.mylife.utils.AppConstants.COLLECTION_USERS

object FirebaseUtils {
    val user= FirebaseAuth.getInstance().getCurrentUser()
    @SuppressLint("StaticFieldLeak")
    val fireStoreDatabase = FirebaseFirestore.getInstance()
    val uid= if (user!=null) user.uid else "00"
    val fireStoreDatabaseUsers= fireStoreDatabase.collection(uid)
    val fireStoreDb= fireStoreDatabase.collection(uid).document(COLLECTION_USERS)
    val fireStoreDb2= fireStoreDatabase.collection(uid)
}