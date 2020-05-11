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
import com.google.firebase.firestore.Query
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_ver_usuarios.*
import kotlinx.android.synthetic.main.user_row_users.view.*
import java.util.*

class ver_usuarios : AppCompatActivity() {
    lateinit var option: Spinner
    private val adapter= GroupAdapter<GroupieViewHolder>()
    private val rootRef = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_usuarios)
        supportActionBar?.title = "Usuarios"//change name to action bar

        adapter.setOnItemClickListener{ item, _ ->
            val userItem = item as UserItem2
            try {
                val intent = Intent(this, info_usuario::class.java)

                intent.putExtra("Cedula",userItem.cedula)

                startActivity(intent)
            } finally {
                finish()
            }
        }

        var filtro=""
        option = findViewById(R.id.filtro_spinner_editusers) as Spinner
        val options = arrayOf("Cedula", "Nombre", "Decos", "Barrio","Correo")
        option.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options)


        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                filtro = "cedula"
            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                filtro = options.get(position)
            }

        }

        buscar_button_editusers.setOnClickListener {
            //Toast.makeText(this, "Filtro= " + filtro, Toast.LENGTH_SHORT).show()
            val busqueda=buscar_editText_editusers.text.toString()
            if(busqueda.isEmpty()){
                Toast.makeText(this, "Por favor ingrese su palabra de busqueda ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            fetchUsers(filtro.toLowerCase(Locale.ENGLISH),busqueda)

        }


    }


    private fun fetchUsers(filtro :String,keyword: String) {
        var i=0
        var query = rootRef.collection("users").whereEqualTo(filtro, keyword)
        if(filtro!="cedula"&&filtro!="nombre"){
            query = rootRef.collection("users").whereEqualTo(filtro, keyword)
                .orderBy("nombre", Query.Direction.ASCENDING)}
        //val   query = rootRef.collection("users") // ACTIVAR ESTA PARTE SI SE QUIERE BORRAR USUARIOS DE BASE DE DATOS
        //    .orderBy("nombre", Query.Direction.ASCENDING)
        query.get()
            .addOnSuccessListener{ snapshots->

                adapter.clear()
                for(doc in snapshots!!){
                    //val user= doc.getString(User_database::class.java)
                    val user=doc.data
                    val name=user.get("nombre")
                    val cedula=user.get("cedula")
                    adapter.add(
                        UserItem2(
                            name as String,
                            cedula as String
                        )
                    )
                    i++
                }
                if(filtro=="barrio"||filtro=="decos")
                    Toast.makeText(this, "Usuarios encontrados= " + i, Toast.LENGTH_SHORT).show()
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

class UserItem2(val name: String, val cedula:String) : Item<GroupieViewHolder>(){

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.username_textview_editusers.text= name
        viewHolder.itemView.cedula_textView_editusers.text= cedula
    }
    override fun getLayout(): Int {
        return R.layout.user_row_users
    }
}