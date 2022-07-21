package com.example.quackiechattie

import android.content.Intent
import android.content.IntentSender
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.MutableState
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.internal.service.Common
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

fun AppCompatActivity.oneTapGoogleSignIn(
    oneTapClient: SignInClient,
    signInRequest: BeginSignInRequest,
    signUpRequest: BeginSignInRequest,
    firebaseAuth: FirebaseAuth
) {
    oneTapClient.beginSignIn(signInRequest)
        .addOnSuccessListener(this) { result ->
            performAuthentication(oneTapClient, result, firebaseAuth)
        }.addOnFailureListener(this) {
            oneTapSignUp(oneTapClient, signUpRequest, firebaseAuth)
            Log.e("Auth", "Unable to login ${it.localizedMessage}")
        }
}

private fun AppCompatActivity.oneTapSignUp(
    oneTapClient: SignInClient,
    signUpRequest: BeginSignInRequest,
    firebaseAuth: FirebaseAuth,
) {
    oneTapClient.beginSignIn(signUpRequest)
        .addOnSuccessListener(this) { result ->
            performAuthentication(oneTapClient, result, firebaseAuth)
        }.addOnFailureListener(this) {
            Log.e("Auth", "Error while creating account ${it.localizedMessage}")
        }
}

private fun AppCompatActivity.performAuthentication(
    oneTapClient: SignInClient,
    result: BeginSignInResult,
    firebaseAuth: FirebaseAuth
) {
    try {
        val startForResult =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {activityResult: ActivityResult ->
            try {
                val credentials =
                    oneTapClient.getSignInCredentialFromIntent(activityResult.data)
                val idToken = credentials.googleIdToken
                loginWithFirebase(idToken, firebaseAuth)
            } catch (apiException: ApiException) {
                when (apiException.statusCode) {
                    CommonStatusCodes.CANCELED -> {
                        Log.d("Auth", "One-tap dialog was closed.")
                    }

                    CommonStatusCodes.NETWORK_ERROR -> {
                        Toast.makeText(
                            this@performAuthentication,
                            "Check your god damn network",
                            Toast.LENGTH_SHORT
                        ).show()
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            startActivity(Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY))
                        }
                    }

                    else -> {
                        Toast.makeText(
                            this@performAuthentication,
                            "Error: ${apiException.statusCode} and ${apiException.status}",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("Auth", "Login failed")
                    }
                }
            }
        }
        startForResult.launch(IntentSenderRequest.Builder(result.pendingIntent.intentSender).build())
    } catch (e: IntentSender.SendIntentException) {
        Log.e("Auth", "One Tap UI failed: ${e.localizedMessage}")
    }
}

private fun loginWithFirebase(
    idToken: String?,
    firebaseAuth: FirebaseAuth,
) {
    val credentials = GoogleAuthProvider.getCredential(idToken, null)
    firebaseAuth.signInWithCredential(credentials)
        .addOnCompleteListener {
            if (it.isSuccessful) {
                Log.e("Auth", "Successful")
            } else {
                Log.e("Auth", "Error ${it.exception}")
            }
        }
}