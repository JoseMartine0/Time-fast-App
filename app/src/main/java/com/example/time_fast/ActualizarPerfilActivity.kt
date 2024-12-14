package com.example.time_fast

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.time_fast.databinding.ActivityActualizarPerfilBinding
import com.example.time_fast.databinding.ActivityListaEnviosBinding
import com.example.time_fast.poko.Colaborador
import com.example.time_fast.poko.Mensaje
import com.example.time_fast.utils.Constantes
import com.google.gson.Gson
import com.koushikdutta.ion.Ion
import java.nio.charset.Charset

class ActualizarPerfilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityActualizarPerfilBinding
    private lateinit var colaborador: Colaborador

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActualizarPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        obtenerDatosColaborador()
        cargarDatosColaborador()

        binding.btnGuardar.setOnClickListener {
            if (validarCampos()) {
                guardarCambios()

            } else {
                Toast.makeText(this, "Campos invalidos", Toast.LENGTH_LONG).show()
            }
        }
        binding.btnCancelar.setOnClickListener{
            cancelarEdicion()
        }

    }




    private fun obtenerDatosColaborador() {
        val jsonColaborador = intent.getStringExtra("colaborador")
        if (jsonColaborador != null) {
            val gson = Gson()
            colaborador = gson.fromJson(jsonColaborador, Colaborador::class.java)
        } else {
            Toast.makeText(this, "Colaborador no encontrado. Por favor, inicie sesión nuevamente.", Toast.LENGTH_LONG).show()
        }
    }

    private fun cargarDatosColaborador() {
        binding.etNombre.setText(colaborador.nombre)
        binding.etApellidoPaterno.setText(colaborador.apellidoPaterno)
        binding.etApellidoMaterno.setText(colaborador.apellidoMaterno)
        binding.etCorreo.setText(colaborador.correoElectronico)
        binding.etCURP.setText(colaborador.CURP)
    }

    private fun guardarCambios() {
        colaborador.nombre = binding.etNombre.text.toString()
        colaborador.apellidoPaterno = binding.etApellidoPaterno.text.toString()
        colaborador.apellidoMaterno = binding.etApellidoMaterno.text.toString()
        colaborador.correoElectronico = binding.etCorreo.text.toString()
        colaborador.CURP = binding.etCURP.text.toString()

        Ion.with(this)
            .load("PUT", "${Constantes().URL_WS}colaboradores/editar")
            .setJsonPojoBody(colaborador)
            .asString(Charsets.UTF_8)
            .setCallback{e,resutl ->
                if (e == null){
                    val gson = Gson()
                    val mensaje = gson.fromJson(resutl, Mensaje::class.java)
                    Toast.makeText(this, mensaje.contenido, Toast.LENGTH_LONG).show()

                    if (!mensaje.error){
                        val intent = Intent()
                        intent.putExtra("Colaborador actualizado", Gson().toJson(colaborador))
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                }else{
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun validarCampos(): Boolean {
        val nombre = binding.etNombre.text.toString()
        val apellidoPaterno = binding.etApellidoPaterno.text.toString()
        val apellidoMaterno = binding.etApellidoMaterno.text.toString()
        val correoEllectronico = binding.etCorreo.text.toString()
        val CURP = binding.etCURP.text.toString()
        return nombre.isNotEmpty() && apellidoPaterno.isNotEmpty() && apellidoMaterno.isNotEmpty() && correoEllectronico.isNotEmpty() && CURP.isNotEmpty()

    }

    private fun cancelarEdicion() {
        setResult(RESULT_CANCELED)
        finish()
    }


}