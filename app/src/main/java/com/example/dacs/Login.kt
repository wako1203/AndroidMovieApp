package com.example.dacs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.dacs.databinding.ActivityLoginBinding
import com.example.dacs.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        firebaseAuth = FirebaseAuth.getInstance()

        binding.textView.setOnClickListener {
            val i = Intent(this,Register::class.java)
            startActivity(i)
        }

        binding.button.setOnClickListener{
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()){

                    firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val user = firebaseAuth.currentUser
                            val i = Intent(this,Homepage::class.java)
                            if (user != null) {
                                i.putExtra("name",user.displayName)
                                i.putExtra("id",user.uid)
                                startActivity(i)
                            }

                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }

            } else {
                Toast.makeText(this, "Các thông tin phải được nhập", Toast.LENGTH_SHORT).show()
            }
        }
    }
}