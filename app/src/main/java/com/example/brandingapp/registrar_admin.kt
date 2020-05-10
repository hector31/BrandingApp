package com.example.brandingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_ingreso_admins.*
import kotlinx.android.synthetic.main.activity_registrar_admin.*
import com.google.android.gms.tasks.Task
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.Uri
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class registrar_admin : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_admin)
        auth = FirebaseAuth.getInstance()
        btn_registrar_admin.setOnClickListener {
            val nombre=nombre_edittext_registrar.text.toString()
            val correo=correo_edittext_registrar.text.toString()
            val contraseña=contraseña_edittext_registrar.text.toString()
            val recontraseña=recontraseña_edittext_registrar.text.toString()


            if(!nombre.isEmpty()&&!correo.isEmpty()&&!contraseña.isEmpty()&&!recontraseña.isEmpty()){
                if(contraseña==recontraseña){

                    auth.createUserWithEmailAndPassword(correo, contraseña)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("Registar_admin", "createUserWithEmail:success")

                                 val user = auth.currentUser
                                user?.sendEmailVerification()?.addOnCompleteListener{ task->
                                    if(task.isSuccessful){

                                        val profileUpdates = UserProfileChangeRequest.Builder()
                                            .setDisplayName(nombre)
                                            .build()

                                        user.updateProfile(profileUpdates)
                                            .addOnCompleteListener { task ->
                                                if (task.isSuccessful) {
                                                    Log.d("UpdateName", "User profile updated.")
                                                }
                                            }
                                        FirebaseAuth.getInstance().signOut()
                                        updateUI(user)
                                    } else{
                                        // If sign in fails, display a message to the user.
                                        Log.w("Registrar_admin", "createUserWithEmail:failure", task.exception)
                                        Toast.makeText(baseContext, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show()
                                        updateUI(null)
                                        startActivity(Intent(this,ingreso_admins::class.java))
                                    }
                                }

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("Registrar_admin", "createUserWithEmail:failure", task.exception)
                                Toast.makeText(baseContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()
                                updateUI(null)
                                startActivity(Intent(this,ingreso_admins::class.java))
                            }

                            // ...
                        }
                }else{
                    Toast.makeText(baseContext, "Las contraseñas no coinciden",
                        Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(baseContext, "Faltan campos por llenar.",
                    Toast.LENGTH_SHORT).show()
            }


        }
    }
    private fun updateUI(user: FirebaseUser?) {
        if(user!=null){
            startActivity(Intent(this,ingreso_admins::class.java))
            Toast.makeText(this, "Se envio un correo de verficacion, por favor confirmar para poder ingresar",
                Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(baseContext, "Fallo en el ingreso, verifique los datos.",
                Toast.LENGTH_SHORT).show()
        }
    }
}

