package com.sam.mylife.ui.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.sam.mylife.core.model.AppUser
import com.sam.mylife.core.preferences.PrefManager
import com.sam.mylife.databinding.ActivityMainBinding
import com.sam.mylife.ui.activity.MainActivity


typealias Inflates<T> = (LayoutInflater, ViewGroup?, Boolean) -> T
public abstract class BaseBottomSheetFragment<VBinding: ViewBinding>(private val inflate: Inflates<VBinding>) : BottomSheetDialogFragment() {
    private lateinit var baseActivity: BaseActivity<*>
    private lateinit var prefManager: PrefManager
  //  private var dialog: Dialog? = null
    private  var userid:String=""
    private  var mobileno:String=""
    private var userInfo: AppUser? = null
    private var viewBinding: VBinding? = null
    private lateinit var firebaseUser: FirebaseUser

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
        if (FirebaseAuth.getInstance()!=null && FirebaseAuth.getInstance().getCurrentUser()!=null){
            firebaseUser= FirebaseAuth.getInstance().getCurrentUser()!!
        }else{
            Log.e(" tag"," You r not login ")
        }
        if (prefManager?.userData!=null){
            userInfo= prefManager?.userData as AppUser

        }

        initView()
    }

    public fun getFirebaseUser() : FirebaseUser?{
        return firebaseUser
    }

    public fun getFirebaseUserId() : String{
        return firebaseUser.uid
    }

    public fun getUserInfo() : AppUser?{
        return userInfo
    }

    public fun getPrefmanager(): PrefManager {
        return prefManager
    }


}