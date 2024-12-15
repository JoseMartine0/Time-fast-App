package com.example.time_fast

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.time_fast.databinding.ActivityMenuPrincipalBinding
import com.example.time_fast.poko.Colaborador
import com.google.gson.Gson

class MenuPrincipalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuPrincipalBinding
    private lateinit var colaborador: Colaborador

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        obtenerDatosColaborador()

        binding.btnListaEnvios.setOnClickListener {
            Log.d("MenuPrincipal", "Botón Lista Envíos presionado")
            val intent = Intent(this, ListaEnviosActivity::class.java).apply {
                putExtra("colaborador", Gson().toJson(colaborador))
            }
            startActivity(intent)
        }


        binding.btnVizualizarPerfil.setOnClickListener {
            val intent = Intent(this, VisualizarPerfilActivity::class.java).apply {
                putExtra("colaborador", Gson().toJson(colaborador))
            }
            startActivity(intent)
        }

        binding.btnCerrarSesion.setOnClickListener {
            val intent = Intent(this, LoginColaborador::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun obtenerDatosColaborador() {
        val jsonCliente = intent.getStringExtra("colaborador")
        if (jsonCliente != null) {
            colaborador = Gson().fromJson(jsonCliente, Colaborador::class.java)
        } else {
            Toast.makeText(this, "Error al obtener datos del colaborador", Toast.LENGTH_SHORT).show()
        }
    }
}
