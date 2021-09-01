package com.example.appci

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.appci.databinding.ActivityCheckEmail2Binding
import com.example.appci.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase

class CheckEmailActivity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityCheckEmail2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCheckEmail2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        val user = auth.currentUser

        binding.veficateEmailAppCompatButton.setOnClickListener {
            val profileUpdates = userProfileChangeRequest {  }

            user!!.updateProfile(profileUpdates).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    if(user.isEmailVerified){
                        reload()
                    }else{
                        Toast.makeText(this, "Verifica tu correo",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        binding.signOutImageView.setOnClickListener {
            signOut()
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            if(currentUser.isEmailVerified){
                reload()
            }else{
                sendEmailVerfication()
            }

        }
    }

    private fun sendEmailVerfication(){
        val user = auth.currentUser
        user!!.sendEmailVerification().addOnCompleteListener(this){ task->
            if(task.isSuccessful){
                Toast.makeText(this, "Se envio un correo de verificación",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun reload (){
        val intent = Intent (this, MainActivity::class.java)
        this.startActivity(intent)
    }
    private fun signOut(){
        Firebase.auth.signOut()
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
    }
}
