package com.example.googlebooksapi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.googlebooksapi.ui.theme.GoogleBooksApiTheme
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

private const val TAG = "RegisterPage"

class RegisterPage: ComponentActivity() {
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        onSignInResult(res)
    }
    private lateinit var auth: FirebaseAuth
    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            // todo navigate to SearchScreen
            Log.d(TAG, "onSignInResult: $user")
            navigateSeachScreen()
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...

            response?.let { resp->

            Log.e(TAG, "onSignInResult: ${resp.error?.errorCode}" )
                //todo show compose error
            } ?: kotlin.run {
                Log.e(TAG, "onSignInResult: user canceled" )
            }

        }
    }

    private fun navigateSeachScreen() {
        val navigate = Intent()
        navigate.setClass(this, MainActivity::class.java)
        startActivity(navigate)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent{
            RegisterApp{

            }
        }
        // create the login providers
        // trigger the implicit intent for LoginUI
        // register the Activity for Result
        // handle the 2 scenario ()

        auth = FirebaseAuth.getInstance()

        // this should be in the onStart()
        val currentUser = auth.currentUser
        if(currentUser == null){

            createFirebaseUILogon()
        } else
            navigateSeachScreen()

    }

    override fun onStop() {
        super.onStop()

    }
    private fun createFirebaseUILogon(){
        // Choose authentication providers
        val providers = arrayListOf(

            AuthUI.IdpConfig.GoogleBuilder().build()
        )


            // Create and launch sign-in intent
            val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)


    }

}

@Composable
fun RegisterApp(content: @Composable ()-> Unit) {
    GoogleBooksApiTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colors.background
        ) {

            content()
        }
    }
}