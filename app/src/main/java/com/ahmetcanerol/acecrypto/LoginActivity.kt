package com.ahmetcanerol.acecrypto

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.ahmetcanerol.acecrypto.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUserMetadata
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var mAut: FirebaseAuth
    private lateinit var networkLive: ConnectionLive

    companion object{
        private const val RC_SING_IN=120
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkNetworkConnection()
        mAut= FirebaseAuth.getInstance()

        val ConnectionManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = ConnectionManager.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected) {
            Toast.makeText(this@LoginActivity, "Connection is available", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this@LoginActivity, "Connection is not available", Toast.LENGTH_LONG).show()
            showAlertDialog()
        }
        val loginbtn:Button=findViewById(R.id.loginBtn)
        val gso= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail().build()
        googleSignInClient=GoogleSignIn.getClient(this,gso)

        val currentUser = mAut.currentUser
        if(currentUser != null){
            var intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
         binding.progressBar2.visibility=View.GONE
         loginbtn.setOnClickListener {
            binding.progressBar2.visibility=View.VISIBLE
            signIn()
        }
        binding.accchange.setOnClickListener {
            accChange()
        }
    }

    private fun checkNetworkConnection(){
        networkLive= ConnectionLive(application)
        networkLive.observe(this, Observer { isConneted->
            if(isConneted){
                binding.textView.visibility= View.VISIBLE
                binding.loginBtn.visibility= View.VISIBLE
            }else{
                binding.loginBtn.visibility= View.GONE
                binding.textView.visibility= View.GONE
                showAlertDialog()
            }
        })
    }
    private fun signIn(){
        val signIn=googleSignInClient.signInIntent
        startActivityForResult(signIn,RC_SING_IN)


    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== RC_SING_IN){
            val task= GoogleSignIn.getSignedInAccountFromIntent(data)
            val ex=task.exception
            if(task.isSuccessful){
                try {
                    val  account=task.getResult(ApiException::class.java)
                    Log.d(ContentValues.TAG,"firebaseAuthGoogle"+account.id)
                    account.idToken?.let {
                        firebaseAuthWithGoogle(it)
                    }

                    binding.progressBar2.visibility=View.GONE
                }catch (e:Exception){
                    e.localizedMessage
                    println(e.message)
                    Log.d(ContentValues.TAG,"Google Sign In Failed"+ e.toString())
                }
            }
            else {
                Log.d(ContentValues.TAG,"firebaseAuthGoogle"+ex.toString())
            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken:String){
        val credential= GoogleAuthProvider.getCredential(idToken,null)
        mAut.signInWithCredential(credential)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    Log.d("SignIn","signInCredential::success")
                    val newuser: Boolean = it.result.additionalUserInfo?.isNewUser == false
                    val db = Firebase.firestore
                    var user=mAut.currentUser

                    if(newuser){  //for new user
                        val data: MutableMap<String, Any> = HashMap()
                        data["balance"] = 5000.01
                        data["email"]=user?.email.toString()
                        val userRef=db.collection("USER").document(user!!.uid)
                        userRef.set(data)

                        val intent=Intent(this,MainActivity::class.java)
                        startActivity(intent)
                    }else{
                        val intent=Intent(this,MainActivity::class.java)
                        startActivity(intent)
                    }
                }else{
                    Log.d("SignIn","signInCredential::Failed",it.exception)
                }
            }
    }
    @SuppressLint("MissingInflatedId")
    private fun showAlertDialog(){
        val builder = AlertDialog.Builder(this,R.style.Theme_ACECrypto)
            .create()
        val view = layoutInflater.inflate(R.layout.custom_alert,null)
        val  button = view.findViewById<Button>(R.id.dialogDismiss_button)
        builder.setView(view)
        button.setOnClickListener {
            builder.dismiss()
        }
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }
   private fun accChange(){
       mAut.signOut()
       googleSignInClient.signOut()
       signIn()
   }

}