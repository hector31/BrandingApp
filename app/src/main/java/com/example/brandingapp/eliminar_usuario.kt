package com.example.brandingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_eliminar_usuario.*

class eliminar_usuario : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance()
    companion object{
        val TAG="DeleteUserAtivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eliminar_usuario)
        delete_button_deleteuser.setOnClickListener{
            Toast.makeText(this, "Eliminando usuario...",
                Toast.LENGTH_SHORT).show()
            deleteUser()
        }
    }

    private fun deleteUser(){
        val cedula=cedula_edittext_deleteuser.text.toString()
        val docRef = db.collection("users").document(cedula)

        docRef.get()
            .addOnSuccessListener {document ->
                if (document.getString("nombre") !=null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")

                    Toast.makeText(this, "Se elimino el usuario: "+document.getString("nombre") ,
                        Toast.LENGTH_SHORT).show()
                    docRef.delete()
                    cedula_edittext_deleteuser.setText("")
                } else {
                    Log.d(TAG, "No such document")
                    Toast.makeText(this, "Documento no encontrado!",
                        Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }

    }
}
