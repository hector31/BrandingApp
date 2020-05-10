package com.example.brandingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_ingreso_admins.*
import kotlinx.android.synthetic.main.activity_registrar_admin.*


class ingreso_admins : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingreso_admins)
        auth = FirebaseAuth.getInstance()

        login_button_login.setOnClickListener{
            val usuario=nameuser_edittext_login.text.toString()
            val contraseña=password_edittext_login.text.toString()
            if(!usuario.isEmpty()&&!contraseña.isEmpty()){
                doLogin(usuario,contraseña)
            }

            else{
                Toast.makeText(this, "Por favor completar todos los campos usuario y contraseña",
                    Toast.LENGTH_SHORT).show()
            }
        }

        registrar_button_login.setOnClickListener {
            Log.d(MainActivity.TAG, "To registrar_admin")
            val intent = Intent(this, registrar_admin::class.java)
            startActivity(intent)
        }
    }
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI2(currentUser)
    }
    private fun doLogin(usuario: String, contraseña: String) {
        auth.signInWithEmailAndPassword(usuario, contraseña)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Ingreso_admins", "signInWithEmail:success")
                    val user = auth.currentUser
                    val emailVerified = user?.isEmailVerified
                    updateUI(user)
                    if(emailVerified==true){
                        updateUI(user)
                    }
                    else{
                        Toast.makeText(baseContext, "Aun no ha verificado su correo, por favor validar correo.",
                            Toast.LENGTH_SHORT).show()
                        FirebaseAuth.getInstance().signOut()
                    }

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Ingreso_admins", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                    // ...
                }

                // ...
            }
    }


    private fun updateUI(user: FirebaseUser?) {
        if(user!=null){
            startActivity(Intent(this,MainActivity::class.java))
            Toast.makeText(baseContext, "Ingreso exitoso",
                Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(baseContext, "Fallo en el ingreso, verifique los datos.",
                Toast.LENGTH_SHORT).show()
        }
    }
    private fun updateUI2(user: FirebaseUser?) {
        if(user!=null){
            startActivity(Intent(this,MainActivity::class.java))
            Toast.makeText(baseContext, "Ingreso exitoso",
                Toast.LENGTH_SHORT).show()
        }
    }

}
