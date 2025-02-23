package com.sam.mylife.ui.activity

import android.content.Intent
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.sam.mylife.R
import com.sam.mylife.databinding.ActivityAuthBinding
import com.sam.mylife.ui.base.BaseActivity


class AuthActivity : BaseActivity<ActivityAuthBinding>() {
    private lateinit var firebaseAuth: FirebaseAuth
    override fun getViewBinding(): ActivityAuthBinding {
     return ActivityAuthBinding.inflate(layoutInflater)
    }

    override fun initView(bundle: Bundle?) {

        firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth.currentUser!=null){
            startActivity(Intent(this, MainActivity::class.java))
             finish()
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val navHostFragments = supportFragmentManager.findFragmentById(R.id.auth_frags)
        val fragment = navHostFragments?.getChildFragmentManager()?.fragments?.get(0)
       // Log.e("TAG"," current frag "+fragment+" request code "+requestCode+" result "+resultCode)
       // fragment?.onActivityResult(requestCode, resultCode, data)

    }
}