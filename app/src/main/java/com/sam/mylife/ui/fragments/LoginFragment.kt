package com.sam.mylife.ui.fragments

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.sam.mylife.R
import com.sam.mylife.core.model.AppUser
import com.sam.mylife.databinding.FragmentLoginBinding
import com.sam.mylife.ui.activity.MainActivity
import com.sam.mylife.ui.base.BaseFragment


class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth


    private val auth: FirebaseAuth = FirebaseAuth.getInstance()


    private val loginResultHandler = registerForActivityResult(StartIntentSenderForResult()) { result: ActivityResult ->

        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        handleResult(task)
    }

    override fun initView() {


        FirebaseApp.initializeApp(requireContext())


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.login.setOnClickListener {
           //Toast.makeText(this, "Logging In", Toast.LENGTH_SHORT).show()
           signInGoogle()
        }
    }

    override fun onStart() {
        super.onStart()
        val acc=FirebaseAuth.getInstance()
        if(acc.currentUser!=null){
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
        }
    }
    private  fun signInGoogle(){
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }


    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
             handleResult(task)
        }
    }


    private fun handleResult(completedTask: Task<GoogleSignInAccount>){
        try {
            val account: GoogleSignInAccount? =completedTask.getResult(ApiException::class.java)
            if (account != null) {
                UpdateUI(account)
            }
        } catch (e:ApiException){
            Log.e(" TAG","  call on frag exce "+e.toString())
            Toast.makeText(requireContext(),e.toString(),Toast.LENGTH_SHORT).show()
        }
    }

    private fun UpdateUI(account: GoogleSignInAccount){
        val credential= GoogleAuthProvider.getCredential(account.idToken,null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {task->
            if(task.isSuccessful) {
                Log.e(" tag","   User id "+account.idToken.toString())
                val user=AppUser()
                user.id= account.id.toString()
                user.name=account.displayName.toString()
                user.email=account.email.toString()
                user.photo=account.photoUrl.toString()
                user.token=account.idToken.toString()
                getPrefmanager().saveUserData(user)
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()

            }
        }
    }

}