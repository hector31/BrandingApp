package com.example.brandingapp

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_registrar_usuario.*
import java.text.Normalizer
import java.util.*

class registrar_usuario : AppCompatActivity() {
    lateinit var option: Spinner
    var deco_selec=""
    companion object{
        val TAG="registrar_usuario"
    }
    val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_usuario)

        val c= Calendar.getInstance()
        val year=c.get(Calendar.YEAR)
        val month=c.get(Calendar.MONTH)
        val day=c.get(Calendar.DAY_OF_MONTH)
        fecha_conexion_button_adduser.setOnClickListener{
            val dpd=
                DatePickerDialog(this, DatePickerDialog.OnDateSetListener{ view, mYear, mMonth, mDay->
                    fecha_conexion_textview_adduser.setText(""+mDay+"/"+(mMonth+1)+"/"+mYear)
                },year,month,day)

            dpd.show()
        }

        spinner_deco()

        adduser_button_adduser.setOnClickListener{
            performRegister()
        }
    }

    private fun performRegister() {

        var name= Normalizer.normalize(nameuser_edittext_adduser.text.toString().toUpperCase(),
            Normalizer.Form.NFD)
        name=Regex("\\p{InCombiningDiacriticalMarks}+").replace(name, "")
        val cedula= cedula_edittext_adduser.text.toString()
        val celular= celular_edittext_adduser.text.toString()
        val correo= correo_edittext_adduser.text.toString()
        var barrio =
            Normalizer.normalize(barrio_edittext_adduser.text.toString().toUpperCase(), Normalizer.Form.NFD)
        barrio=Regex("\\p{InCombiningDiacriticalMarks}+").replace(barrio, "")
        val direccion= direccion_edittext_adduser.text.toString()
        val fecha_conexion=fecha_conexion_textview_adduser.text.toString()
        val valor=valor_plan_editText_add_user.text.toString()


        Log.d(TAG,"name is = "+name)
        Log.d(TAG,"cedula= $cedula")
        Log.d(TAG,"celular= $celular")
        Log.d(TAG,"correo= $correo")
        Log.d(TAG,"barrio= $barrio")
        Log.d(TAG,"direccion= $direccion")
        Log.d(TAG,"fecha_conexion= $fecha_conexion")
        Log.d(TAG,"decos= $deco_selec")

        if(barrio.isEmpty()||valor.isEmpty()||name.isEmpty()||cedula.isEmpty() || celular.isEmpty()||direccion.isEmpty()||fecha_conexion.isEmpty()||deco_selec.isEmpty()) // this is if passwor or email is empty
        {
            Toast.makeText(this,"por favor ingrese todos los campos", Toast.LENGTH_SHORT).show()
            return
        }
        val user= User_database(
            name,
            cedula,
            correo,
            barrio,
            celular,
            direccion,
            fecha_conexion,
            deco_selec,
            valor
        )
        db.collection("users").document(cedula)
            .get().addOnSuccessListener {document->
                if(document.getString("nombre")!=null){
                    Toast.makeText(this,"La cedula ingresada ya existe, verifique la informacion", Toast.LENGTH_SHORT).show()
                }else{
                    db.collection("users").document(cedula)
                        .set(user)
                        .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!")
                            Toast.makeText(this,"Se ingreso exitosamente un nuevo usuario", Toast.LENGTH_SHORT).show()
                            nameuser_edittext_adduser.setText("")
                            cedula_edittext_adduser.setText("")
                            celular_edittext_adduser.setText("")
                            correo_edittext_adduser.setText("")
                            barrio_edittext_adduser.setText("")
                            direccion_edittext_adduser.setText("")
                            valor_plan_editText_add_user.setText("")

                        }
                        .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
                }

            }



    }

    private fun spinner_deco() {
        option = findViewById(R.id.decos_spinner_adduser) as Spinner
        val options = arrayOf("1", "2", "3", "4","5")
        option.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options)
        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                deco_selec = "1"
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                deco_selec = options.get(position)
            }
        }

    }
}

class User_database(val nombre: String, val cedula: String, val correo: String, val barrio: String, val celular: String, val direccion: String,
                    val fecha_conexion: String,val decos:String,val valor_plan:String){
    constructor(): this ("","","","","","","","","")
}