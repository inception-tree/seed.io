package com.example.seed

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.seed.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    companion object {
        private const val GOOGLE_CLIENT_ID = "658086026451-u8c5epi2fgaeh1p81k3udgcoqtf7cm0r.apps.googleusercontent.com"
        private const val TAG = "SIGN IN"
    }

    private lateinit var binding : ActivityLoginBinding
    private lateinit var gso : GoogleSignInOptions
    private lateinit var gsc : GoogleSignInClient
    private lateinit var auth : FirebaseAuth

    private val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    { result ->
        Log.d(TAG, "${result.resultCode} ${result.data.toString()}")
        if (result.resultCode == Activity.RESULT_OK) {
            handleSuccessfulSignIn(result)
        } else {
            Toast.makeText(this, "Authentication Error", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(GOOGLE_CLIENT_ID)
            .requestEmail()
            .build()

        gsc = GoogleSignIn.getClient(this, gso)

        auth = FirebaseAuth.getInstance()

        binding.btnSignUp.setOnClickListener {
            handleGoogleSignUp()
        }

        binding.btnSignIn.setOnClickListener {
            handleGoogleSignIn()
        }
    }

    private fun handleGoogleSignIn() {
        activityResultLauncher.launch(Intent(gsc.signInIntent))
    }

    private fun handleSuccessfulSignIn(signInResult: ActivityResult) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(signInResult.data)
        val userResult = task.getResult(ApiException::class.java)
        Log.d(TAG, "${userResult.id} ${userResult.email}")
    }

    private fun handleGoogleSignUp() {

    }
}