/*
    For usernames and rooms to enter
 */

package com.example.quackiechattie


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_menu.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    val gson: Gson = Gson()
    lateinit var mSock: Socket;
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = Firebase.auth
        loginButton.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.loginButton -> signIn()      }
    }

    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }
    // [END auth_with_google]

    // [START signin]
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    // [END signin]

    private fun updateUI(user: FirebaseUser?) {
        SocketHandler.setSocket()
        SocketHandler.establishConnection()
        mSock = SocketHandler.getSocket()
        val success = User.setFireBaseUser(user)
        val displayname = user?.displayName
        if (success) {
            if (displayname != null) {
                Log.d("USER", displayname)
                val uName = User.getUsername()
                val uid = User.getUserID()
                val data = initData(uName, uid)
                val jData = gson.toJson(data)
                mSock.emit("checkUsername", jData)
                mSock.on("NEW_USER", userFailedLogin)
                mSock.on("USER_EXISTS", userLoginSuccess)
            } else {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }
        } else {
            Log.d("FirebaseUser", "Method: setFireBaseUser() failed.")
        }
    }

    private var userFailedLogin = Emitter.Listener {
        closeListeners()
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private var userLoginSuccess = Emitter.Listener {
        closeListeners()
        val intent = Intent(this, ChatRoomsActivity::class.java)
        startActivity(intent)
    }

    private fun closeListeners() {
        mSock.off("NEW_USER")
        mSock.off("USER_EXISTS")
    }

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }
}