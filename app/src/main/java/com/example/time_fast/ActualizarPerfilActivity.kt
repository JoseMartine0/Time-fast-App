package com.example.time_fast

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.time_fast.databinding.ActivityActualizarPerfilBinding
import com.example.time_fast.databinding.ActivityListaEnviosBinding
import com.example.time_fast.poko.Colaborador
import com.google.gson.Gson

class ActualizarPerfilActivity : AppCompatActivity() {
    private lateinit var binding: ActivityActualizarPerfilBinding
    private lateinit var colaborador: Colaborador

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityActualizarPerfilBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_actualizar_perfil)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.actualizar)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun obtenerDatosColaborador() {
        val jsonCliente = intent.getStringExtra("colaborador")
        if (jsonCliente != null) {
            val gson = Gson()
            colaborador = gson.fromJson(jsonCliente, Colaborador::class.java)
        } else {
            Toast.makeText(this, "Colaborador no encontrado. Por favor, inicie sesión nuevamente.", Toast.LENGTH_LONG).show()
            finish()
        }
    }
}