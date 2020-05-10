package com.example.brandingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_editar_usuario.*
import java.text.Normalizer

class editar_usuario : AppCompatActivity() {


    lateinit var option: Spinner
    var deco_selec=""
    var deco_selec_aux=""
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_usuario)

        val nombre=intent.getStringExtra("nombre")
        val cedula=intent.getStringExtra("cedula")
        val celular=intent.getStringExtra("celular")
        val correo=intent.getStringExtra("correo")
        val direccion=intent.getStringExtra("direccion")
        val barrio=intent.getStringExtra("barrio")
        val valor_plan=intent.getStringExtra("valor_plan")
        deco_selec_aux=intent.getStringExtra("decos")

        nameuser_edittext_edituserinfo.setText(nombre)
        cedula_edittext_edituserinfo.setText(cedula)
        celular_edittext_edituserinfo.setText(celular)
        correo_edittext_edituserinfo.setText(correo)
        barrio_edittext_edituserinfo.setText(barrio)
        direccion_edittext_edituserinfo.setText(direccion)
        valor_plan_editText_edit_user.setText(valor_plan)
        spinner_deco()

        setuser_button_edituserinfo.setOnClickListener{
            upDateInfoUser(cedula)

        }
    }



    private fun spinner_deco() {
        option = findViewById(R.id.nodo_spinner_edituserinfo) as Spinner
        val options = arrayOf("Actual:"+deco_selec_aux,"1", "2", "3", "4","5")
        option.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options)
        option.prompt = deco_selec_aux
        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                deco_selec = deco_selec_aux

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                if(options.get(position)=="Actual:"+deco_selec_aux){
                    deco_selec=deco_selec_aux
                }else{
                    deco_selec = options.get(position)}
            }
        }
    }

    private fun upDateInfoUser(cedula: String) {
        var name= Normalizer.normalize(nameuser_edittext_edituserinfo.text.toString().toUpperCase(),
            Normalizer.Form.NFD)
        name=Regex("\\p{InCombiningDiacriticalMarks}+").replace(name, "")
        var barrio = Normalizer.normalize(barrio_edittext_edituserinfo.text.toString().toUpperCase(),
            Normalizer.Form.NFD)
        barrio=Regex("\\p{InCombiningDiacriticalMarks}+").replace(barrio, "")

        db.collection("users").document(cedula)
            .update(
                "barrio", barrio,
                "cedula",cedula_edittext_edituserinfo.text.toString(),
                "celular",celular_edittext_edituserinfo.text.toString(),
                "correo",correo_edittext_edituserinfo.text.toString(),
                "direccion",direccion_edittext_edituserinfo.text.toString(),
                "decos", deco_selec,
                "nombre",name,
                "valor_plan",valor_plan_editText_edit_user.text.toString()
            )
            .addOnSuccessListener { Log.d("Exito editar_usuario", "DocumentSnapshot successfully written!")
                Toast.makeText(this,"Se actualizo la informacion del usuario", Toast.LENGTH_SHORT).show()
                val intent= Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e -> Log.w("Error editar_usuario", "Error writing document", e)
            }
    }
}
