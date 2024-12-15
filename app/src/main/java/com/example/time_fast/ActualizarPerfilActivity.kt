package com.example.time_fast

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.nio.charset.Charset

class ActualizarPerfilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityActualizarPerfilBinding
    private lateinit var colaborador: Colaborador
    private var fotoPerfil: ByteArray?=null

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

    override fun onStart() {
        super.onStart()
        obtenerFotoColaborador(colaborador.idColaborador)
        binding.btnCapturePhoto.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            seleccionarFotoPerfil.launch(intent)
        }
    }

    private fun obtenerFotoColaborador(idColaborador: Int){
        Ion.with(this@ActualizarPerfilActivity)
            .load("GET, ${Constantes().URL_WS}colaborador/Obtener-Foto/${idColaborador}")
            .asString()
            .setCallback{e, result ->
                if(e == null){
                    cargarFotoColaborador(result)
                }else{
                    Toast.makeText(this@ActualizarPerfilActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun cargarFotoColaborador(json: String){
        if(json.isNotEmpty()){
            val gson = Gson()
            val colaboradorFoto = gson.fromJson(json, Colaborador::class.java)
            if (colaboradorFoto.fotoBase64 != null){
                try {
                    val imgBytes = Base64.decode(colaboradorFoto.fotoBase64, Base64.DEFAULT)
                    val imgBitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.size)
                    binding.ivProfileImage.setImageBitmap(imgBitmap)
                }catch (e: Exception){
                    Toast.makeText(this@ActualizarPerfilActivity,  "Error img: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(this@ActualizarPerfilActivity, "No cuenta con una foto de perfil", Toast.LENGTH_LONG).show()

            }
        }
    }

    private val seleccionarFotoPerfil = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val imgURI = data?.data
            if (imgURI != null) {
                fotoPerfil = uriToByteArray(imgURI)
                if (fotoPerfil != null) {
                    subirFotoPerfil(colaborador.idColaborador)
                }
            }
        }
    }

    private fun uriToByteArray(uri: Uri): ByteArray? {
        return try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            byteArrayOutputStream.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun subirFotoPerfil(idColaborador: Int) {
        Ion.with(this@ActualizarPerfilActivity)
            .load("PUT", "${Constantes().URL_WS}colaborador/Subir-Foto/${idColaborador}")
            .setByteArrayBody(fotoPerfil)
            .asString()
            .setCallback { e, result ->
                if (e == null) {
                    val gson = Gson()
                    val mensaje = gson.fromJson(result, Mensaje::class.java)
                    Toast.makeText(this@ActualizarPerfilActivity, mensaje.contenido, Toast.LENGTH_LONG).show()
                    if (!mensaje.error) {
                        obtenerFotoColaborador(colaborador.idColaborador)
                    }
                } else {
                    Toast.makeText(this@ActualizarPerfilActivity, e.message, Toast.LENGTH_LONG).show()
                }
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