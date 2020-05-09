package com.example.brandingapp

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_info_usuario.*
import java.io.Serializable
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class info_usuario : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance()
    var user = User_Info()
    val temporal_admin="Hector"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_usuario)

        supportActionBar?.title = "Informacion Usuario"//change name to action bar
        // var textView = findViewById(R.id.infouser_textView_userinfo)
        val cedula_user=intent.getStringExtra("Cedula")
        show_infoUser(cedula_user)
        val c= Calendar.getInstance()
        val year=c.get(Calendar.YEAR)
        val month=c.get(Calendar.MONTH)
        val day=c.get(Calendar.DAY_OF_MONTH)
        val cedula=intent.getStringExtra("Cedula")
        val query = db.collection("users").document(cedula!!)
        query.get()
            .addOnSuccessListener {doc->
                fecha_prox_textView_user_info.setText(doc.getString("fecha_conexion"))
            }

        fecha_prox_calendario_button_user_info.setOnClickListener{
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = sdf.format(Date())

            val dpd= DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener{ view, mYear, mMonth, mDay->
                    fecha_prox_textView_user_info.setText(""+mDay+"/"+(mMonth+1)+"/"+mYear)

                },year,month,day)
            actualizar_fecha_button_user_info.setOnClickListener{
                Toast.makeText(this,"Actualizando fecha...", Toast.LENGTH_SHORT).show()
                db.collection("users").document(cedula!!)
                query.update(
                    "fecha_conexion",fecha_prox_textView_user_info.text.toString(),
                    "ultima_modificacion",temporal_admin,
                    "ultima_modificacion_fecha",currentDate
                )
                Toast.makeText(this,"Fecha actualizada con exito.", Toast.LENGTH_LONG).show()
                show_infoUser(cedula_user)
            }

            dpd.show()
        }
        edituser_button_userinfo.setOnClickListener{
            val intent= Intent(this, editar_usuario::class.java)
            intent.putExtra("nombre",user.nombre)
            intent.putExtra("cedula",user.cedula)
            intent.putExtra("correo",user.correo)
            intent.putExtra("celular",user.celular)
            intent.putExtra("barrio",user.barrio)
            intent.putExtra("direccion",user.direccion)
            intent.putExtra("fecha_conexion",user.fecha_conexion)
            intent.putExtra("decos",user.decos)
            intent.putExtra("valor_plan",user.valor_plan)

            startActivity(intent)
        }



    }


    private fun show_infoUser(cedulaUser: String){
        val docRef = db.collection("users").document(cedulaUser)
        docRef.get()
            .addOnSuccessListener {document ->
                Log.d("UserInfoActivity", "DocumentSnapshot data: ${document.data}")
                val nombre= document.getString("nombre")!!
                val cedula=document.getString("cedula")!!
                val correo=document.getString("correo")!!
                val celular=document.getString("celular")!!
                val barrio=document.getString("barrio")!!
                val direccion=document.getString("direccion")!!
                val fecha_conexion=document.getString("fecha_conexion")!!
                val decos=document.getString("decos")!!
                val ultima_modificacion=document.getString("ultima_modificacion")!!
                val ultima_modificacion_fecha=document.getString("ultima_modificacion_fecha")!!
                val valor=document.getString("valor_plan")!!
                user= User_Info(
                    nombre,
                    cedula,
                    correo,
                    barrio,
                    celular,
                    direccion,
                    fecha_conexion,
                    ultima_modificacion,
                    ultima_modificacion_fecha,
                    decos,
                    valor
                )

                infouser_textView_userinfo.text= "Nombre= ".plus(nombre).plus("\n").plus("Cedula= ").plus(cedula).plus("\n")
                    .plus("Celular= ").plus(celular).plus("\n").plus("Correo= ").plus(correo).plus("\n")
                    .plus("Barrio= ").plus(barrio).plus("\n").plus("Direccion= ").plus(direccion).plus("\n")
                    .plus("Ultima modificacion= ").plus(ultima_modificacion).plus("\n").plus("Fecha ultima modificacion= ").plus(ultima_modificacion_fecha).plus("\n")
                    .plus("Nodo= ").plus(decos).plus("\n")
                    .plus("Fecha limite de pago= ").plus(fecha_conexion).plus("\n").plus("Valor plan= ").plus(valor)

            }
            .addOnFailureListener { exception ->
                Log.d("UserInfoActivity", "get failed with ", exception)
            }
    }


}


class User_Info(val nombre: String, val cedula: String, val correo: String, val barrio: String, val celular: String, val direccion: String,
                val fecha_conexion: String, val ultima_modificacion: String,val ultima_modificacion_fecha:String,val decos:String,val valor_plan:String):
    Serializable {
    constructor(): this ("","","","","","","","","","","")
}