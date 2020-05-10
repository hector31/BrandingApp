package com.example.brandingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.auth.FirebaseUser
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import kotlinx.android.synthetic.main.activity_registrar_admin.*


class MainActivity : AppCompatActivity() {
    companion object{
        val TAG="MainActivity"
    }
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        btn_ver_pagos.setOnClickListener{
            Log.d(TAG, "To ver_pagos")
            val intent = Intent(this, ver_pagos::class.java)
            startActivity(intent)
        }
        btn_ver_usuarios.setOnClickListener {
            Log.d(TAG, "To ver_usuarios")
            val intent = Intent(this, ver_usuarios::class.java)
            startActivity(intent)
        }
        btn_registrar_usuario.setOnClickListener {
            Log.d(TAG, "To registrar_usuario")
            val intent = Intent(this, registrar_usuario::class.java)
            startActivity(intent)
        }
        btn_elminar_usuario.setOnClickListener{
            Log.d(TAG, "To eliminar_usuario")
            val intent = Intent(this, eliminar_usuario::class.java)
            startActivity(intent)
        }
        btn_salir.setOnClickListener{
            val intent = Intent(this, ingreso_admins::class.java)
            auth.signOut()
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }


        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // Name, email address, and profile photo Url
            val name = user.displayName
            val email = user.email
            val photoUrl = user.photoUrl

            // Check if user's email is verified
            val emailVerified = user.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            val uid = user.uid

            Log.d("MainActivity", "name: "+name+" email: "+email+" emailVeried: "+emailVerified)
        }

    }
}
