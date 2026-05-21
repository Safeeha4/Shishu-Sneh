package com.shishusneh.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.shishusneh.app.R
import com.shishusneh.app.databinding.ActivityLoginBinding
import com.shishusneh.app.ui.common.BaseActivity
import com.shishusneh.app.ui.doctor.DoctorLoginActivity
import com.shishusneh.app.ui.language.LanguageSelectionActivity

class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private val signInLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                val statusCode = e.statusCode
                val message = when(statusCode) {
                    7 -> "Network Error. Check internet connection."
                    10 -> "Developer Error: Ensure SHA-1 is in Firebase Console."
                    12500 -> "Sign-in failed. Update Google Play Services."
                    12501 -> "Sign-in canceled."
                    else -> "Error $statusCode: ${e.localizedMessage}"
                }
                Log.e("AUTH_DEBUG", "Google sign in failed: $message")
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.btnGoogleLogin.setOnClickListener {
            signIn()
        }

        // DEBUG BYPASS: Long-press to skip authentication
        binding.btnGoogleLogin.setOnLongClickListener {
            Toast.makeText(this, "Debug: Bypassing Auth...", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LanguageSelectionActivity::class.java))
            finish()
            true
        }

        binding.btnDoctorLogin.setOnClickListener {
            startActivity(Intent(this, DoctorLoginActivity::class.java))
        }
    }

    private fun signIn() {
        googleSignInClient.signOut().addOnCompleteListener {
            val signInIntent = googleSignInClient.signInIntent
            signInLauncher.launch(signInIntent)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, LanguageSelectionActivity::class.java))
                    finish()
                } else {
                    val errorMsg = task.exception?.localizedMessage ?: "Unknown Error"
                    Toast.makeText(this, "Auth Failed: $errorMsg", Toast.LENGTH_LONG).show()
                }
            }
    }
}
