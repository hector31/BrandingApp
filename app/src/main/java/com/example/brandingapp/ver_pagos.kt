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
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_ver_usuarios.*
import kotlinx.android.synthetic.main.user_row_users.view.*
import java.text.SimpleDateFormat
import java.util.*


class ver_pagos : AppCompatActivity() {

    lateinit var option: Spinner
    private val adapter= GroupAdapter<GroupieViewHolder>()
    private val rootRef = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_pagos)

        adapter.setOnItemClickListener{item,view->
            val userItem = item as UserItem
            try {
                val intent = Intent(this, info_usuario::class.java)

                intent.putExtra("Cedula",userItem.cedula)

                startActivity(intent)
            } finally {
                finish()
            }
        }

        var filtro=""
        var pos_arrglo=0;
        option = findViewById(R.id.filtro_spinner_editusers) as Spinner
        val options = arrayOf("Ultimo mes", "Enero", "Febrero", "Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre"
        ,"Noviembre","Dicembre")
        option.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options)

        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                filtro = "Ultimo mes"
                pos_arrglo=0
            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                filtro = options.get(position)
                pos_arrglo=position
            }

        }

        buscar_button_editusers.setOnClickListener {
            //Toast.makeText(this, "Filtro= " + filtro, Toast.LENGTH_SHORT).show()


            fetchUsers(pos_arrglo)

        }

    }

    private fun fetchUsers( mes: Int) {
        val mes_in=mes-1
        var i=0
        var calendarStart = Calendar.getInstance()
        calendarStart.add(Calendar.MONTH, -1)
        var calendarEnd=Calendar.getInstance()
        var pagos = rootRef.collection("users").whereGreaterThan("fecha_proximo_corte",calendarStart.time).whereLessThan("fecha_proximo_corte",calendarEnd.time)

        if(mes!=0){

            calendarStart = Calendar.getInstance()
            calendarStart.set(Calendar.MONTH,mes_in)
            calendarStart.set(Calendar.DAY_OF_MONTH,1)
            calendarStart.set(Calendar.HOUR, 0)
            calendarStart.set(Calendar.MINUTE, 0)
            calendarStart.set(Calendar.AM_PM, Calendar.AM)
            calendarEnd=Calendar.getInstance()
            calendarEnd.set(Calendar.MONTH,mes_in+1)
            calendarEnd.set(Calendar.DAY_OF_MONTH,1)
            calendarEnd.set(Calendar.HOUR, 0)
            calendarEnd.set(Calendar.MINUTE, 0)
            calendarEnd.set(Calendar.AM_PM, Calendar.AM)
            pagos = rootRef.collection("users").whereGreaterThan("fecha_proximo_corte",calendarStart.time).whereLessThan("fecha_proximo_corte",calendarEnd.time)

        }
        pagos.get()
            .addOnSuccessListener { documents->
                adapter.clear()
                for(doc in documents!!){
                    //val user= doc.getString(User_database::class.java)
                    val user=doc.data
                    val name=user.get("nombre")
                    val cedula=user.get("cedula")
                    val modificado=user.get("ultima_modificacion")
                    val fecha_modificacion=user.get("ultima_modificacion_fecha")
                    val proximo_corte=user.get("fecha_conexion")
                    val mensaje="cedula:$cedula\nmodificado: $modificado\nfecha modificacion: $fecha_modificacion\nfecha de corte: $proximo_corte"                    //doc.reference.delete()

                    //doc.reference.delete()
                    adapter.add(
                        UserItem(
                            name as String,
                            mensaje,
                            cedula as String
                        )
                    )
                    i++
                }

                Toast.makeText(this, "Pagos encontrados= " + i, Toast.LENGTH_SHORT).show()

                users_recyclerview_editusers.adapter=adapter
                users_recyclerview_editusers.addItemDecoration(
                    DividerItemDecoration(
                        users_recyclerview_editusers.getContext(),
                        DividerItemDecoration.VERTICAL
                    )
                )
            } .addOnFailureListener {
                    e -> Log.w("Error editar_usuario", "Error writing document", e)
                Toast.makeText(this, "Error al buscar, no tiene permisos administrador", Toast.LENGTH_SHORT).show()

            }

    }
}



class UserItem(val name: String, val mensaje: String, val cedula:String) : Item<GroupieViewHolder>(){

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.username_textview_editusers.text= name
        viewHolder.itemView.cedula_textView_editusers.text= mensaje
    }
    override fun getLayout(): Int {
        return R.layout.user_row_users
    }
}