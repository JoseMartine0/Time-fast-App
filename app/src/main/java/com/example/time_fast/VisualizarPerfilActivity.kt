package com.example.time_fast

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.time_fast.databinding.ActivityMenuPrincipalBinding
import com.example.time_fast.databinding.ActivityVizualizarPerfilBinding
import com.example.time_fast.poko.Colaborador
import com.google.gson.Gson

class VisualizarPerfilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVizualizarPerfilBinding
    private lateinit var colaborador: Colaborador

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVizualizarPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        obtenerDatos()
        cargarDatos()
    }



    private fun obtenerDatos() {
        val jsonColaborador = intent.getStringExtra("colaborador")
        if (jsonColaborador != null){
            val gson = Gson()
            colaborador = gson.fromJson(jsonColaborador, Colaborador::class.java)
        }
    }

    private fun cargarDatos() {
        binding.tvIdColaborador.setText(colaborador.idColaborador.toString())
        binding.tvNombre.setText(colaborador.nombre)
        binding.tvApellidoPaterno.setText(colaborador.apellidoPaterno)
        binding.tvApellidoMaterno.setText(colaborador.apellidoMaterno)
        binding.tvCorreoElectronico.setText(colaborador.correoElectronico)
        binding.tvCURP.setText(colaborador.CURP)
        binding.tvNumeroPersonal.setText(colaborador.numeroPersonal)
        binding.tvidRol.setText(colaborador.idRol.toString())
        binding.tvRol.setText(colaborador.rol.toString())
    }
}