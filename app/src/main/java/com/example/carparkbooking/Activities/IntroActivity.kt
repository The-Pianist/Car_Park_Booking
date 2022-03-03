package com.example.carparkbooking.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.carparkbooking.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {
    private var binding:ActivityIntroBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityIntroBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)

        binding?.btnSignUp?.setOnClickListener {
            val intent= Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding?.btnSignIn?.setOnClickListener {
            val intent=Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }
}