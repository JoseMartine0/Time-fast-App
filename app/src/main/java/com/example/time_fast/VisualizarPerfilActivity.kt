package com.example.time_fast

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.time_fast.databinding.ActivityVizualizarPerfilBinding
import com.example.time_fast.poko.Colaborador
import com.google.gson.Gson
import com.example.time_fast.dao.ColaboradorDAO

class VisualizarPerfilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVizualizarPerfilBinding
    private lateinit var colaborador: Colaborador

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVizualizarPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegresarMenu.setOnClickListener{
            cancelarEdicion()
        }

        obtenerDatos()
        cargarDatos()

        binding.ivActualizarPerfil.setOnClickListener{
            val intent = Intent(this, ActualizarPerfilActivity::class.java)
            intent.putExtra("colaborador", Gson().toJson(colaborador))
            startActivityForResult(intent, REQUEST_ACTUALIZAR_COLABORADOR)
        }

    }

    private fun obtenerDatos() {
        val jsonColaborador = intent.getStringExtra("colaborador")
        if (jsonColaborador != null){
            val gson = Gson()
            colaborador = gson.fromJson(jsonColaborador, Colaborador::class.java)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ACTUALIZAR_COLABORADOR && resultCode == Activity.RESULT_OK) {
            val jsonColaboradorActualizado = data?.getStringExtra("colaboradorActualizado")
            if (jsonColaboradorActualizado != null) {
                colaborador = Gson().fromJson(jsonColaboradorActualizado, Colaborador::class.java)
                cargarDatos()
            }
        }
    }

    private fun cargarDatos() {
        binding.tvNombre.setText(colaborador.nombre + " "+  colaborador.apellidoPaterno +" "+ colaborador.apellidoMaterno)
        binding.tvCorreoElectronico.setText(colaborador.correoElectronico)
        binding.tvCURP.setText(colaborador.CURP)
        binding.tvNumeroPersonal.setText(colaborador.numeroPersonal)
        binding.tvNumeroLicencia.setText(colaborador.numeroLicencia)
        val dao = ColaboradorDAO(this)
        dao.obtenerFoto(colaborador.idColaborador, { bitmap ->
            binding.ivProfileImage.setImageBitmap(bitmap)
        }, { error ->
            binding.ivProfileImage.setImageResource(R.drawable.app_logo)
        })
    }

    companion object{
        private const val REQUEST_ACTUALIZAR_COLABORADOR = 1
    }

    private fun cancelarEdicion() {
        setResult(RESULT_CANCELED)
        finish()
    }

}