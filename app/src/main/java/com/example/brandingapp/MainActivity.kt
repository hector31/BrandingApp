package com.example.brandingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object{
        val TAG="MainActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_ver_pagos.setOnClickListener{
            Log.d(TAG, "To ver_pagos")
            val intent = Intent(this, ver_pagos::class.java)
            startActivity(intent)
        }
        btn_registrar_pago.setOnClickListener {
            Log.d(TAG, "To registrar_pagos")
            val intent = Intent(this, registrar_pago::class.java)
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
    }
}
