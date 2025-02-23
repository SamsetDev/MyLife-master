package com.sam.mylife.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.sam.mylife.R
import com.sam.mylife.databinding.ActivityMainBinding
import com.sam.mylife.ui.base.BaseActivity
import com.sam.mylife.ui.fragments.HomeFragment
import com.sam.mylife.utils.ext.currentNavigationFragment
import java.util.Calendar

class MainActivity : BaseActivity<ActivityMainBinding>() {
    private lateinit var navController: NavController
    private lateinit var firebaseAuth: FirebaseAuth
    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initView(bundle: Bundle?) {
        navController = findNavController(R.id.home_frags)



        binding.fabAdd.setOnClickListener {
           // navController.navigate(R.id.addRecordFragment)
            showBottomSheetDialogFragment(null)
        }
        binding.mytool.ivFilter.setOnClickListener {
           val currentFrag=supportFragmentManager.currentNavigationFragment as HomeFragment
            //currentFrag.filterAction()

        }
        binding.mytool.tvClear.setOnClickListener {
            //val currentFrag=supportFragmentManager.currentNavigationFragment as HomeFragment
           // currentFrag.clearFilter()
            firebaseAuth = FirebaseAuth.getInstance()
            if (firebaseAuth.currentUser!=null){
                firebaseAuth.signOut()
                startActivity(Intent(this,AuthActivity::class.java))
            }


        }

        navController.addOnDestinationChangedListener(object : NavController.OnDestinationChangedListener{
            override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
                binding.mytool.tvTitle.text= destination.label

            }

        })

        setupYearSpinner()

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupYearSpinner(){
        val yearList = mutableListOf<String>()
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        for (i in currentYear downTo currentYear - 10) yearList.add(i.toString())

        binding.mytool.spinYear.setOnTouchListener { v, event ->
            val rotate = AnimationUtils.loadAnimation(this, R.anim.rotate)
            v.startAnimation(rotate)
            false
        }

        val adapter = ArrayAdapter(this, R.layout.year_spinner_item, yearList)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)

        binding.mytool.spinYear.adapter=adapter
        binding.mytool.spinYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parentView?.getItemAtPosition(position).toString()
                setYear(selectedItem)

            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // No item selected, display default text

            }
        }
    }

}