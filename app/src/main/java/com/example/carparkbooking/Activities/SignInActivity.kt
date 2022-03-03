package com.example.carparkbooking.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.carparkbooking.Firestore.FirestoreClass
import com.example.carparkbooking.databinding.ActivitySignInBinding
import com.example.carparkbooking.models.User
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : BaseActivity() {
    private var binding:ActivitySignInBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivitySignInBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)
    }

    private fun signInUser(){
        val email:String=binding?.etEmail?.text.toString()
        val password:String=binding?.etPassword?.text.toString()

        if(validateForm(email, password)) {
            showProgressDialog("Please Wait")
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        FirestoreClass().loadUserData(this)
                    }else {
                        Toast.makeText(
                            this@SignInActivity,
                            task.exception!!.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }

    private fun validateForm(email: String, password: String): Boolean {
        return if (TextUtils.isEmpty(email)) {
            showErrorSnackBar("Please enter email.")
            false
        } else if (TextUtils.isEmpty(password)) {
            showErrorSnackBar("Please enter password.")
            false
        } else {
            true
        }
    }

    fun signInSuccess(user: User){
        hideProgressDialog()
        val intent= Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}