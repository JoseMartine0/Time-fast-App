package com.example.time_fast

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.time_fast.databinding.ActivityMenuPrincipalBinding
import com.example.time_fast.databinding.ActivityVizualizarPerfilBinding
import com.example.time_fast.poko.Colaborador
import com.example.time_fast.utils.Constantes
import com.google.gson.Gson
import com.koushikdutta.ion.Ion
import android.util.Base64
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import com.example.time_fast.poko.Mensaje
import java.io.ByteArrayOutputStream
import java.io.InputStream

class VisualizarPerfilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVizualizarPerfilBinding
    private lateinit var colaborador: Colaborador
    private var fotoPerfil: ByteArray?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVizualizarPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnRegresarMenu = findViewById<Button>(R.id.btnRegresarMenu)
        btnRegresarMenu.setOnClickListener{
            val intent = Intent(this, MenuPrincipalActivity::class.java)
            startActivity(intent)
            finish()
        }

        obtenerDatos()
        cargarDatos()

        binding.ivActualizarPerfil.setOnClickListener{
            val intent = Intent(this, ActualizarPerfilActivity::class.java)
            intent.putExtra("colaborador", Gson().toJson(colaborador))
            startActivityForResult(intent, REQUEST_ACTUALIZAR_COLABORADOR)
        }




    }

    override fun onStart() {
        super.onStart()
        obtenerFotoColaborador(colaborador.idColaborador)
        binding.ivProfileImage.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            seleccionarFotoPerfil.launch(intent)
        }
    }

    private fun obtenerFotoColaborador(idColaborador: Int){
        Ion.with(this@VisualizarPerfilActivity)
            .load("GET, ${Constantes().URL_WS}colaborador/Obtener-Foto/${idColaborador}")
            .asString()
            .setCallback{e, result ->
                if(e == null){
                    cargarFotoColaborador(result)
                }else{
                    Toast.makeText(this@VisualizarPerfilActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
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
                    Toast.makeText(this@VisualizarPerfilActivity,  "Error img: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(this@VisualizarPerfilActivity, "No cuenta con una foto de perfil", Toast.LENGTH_LONG).show()
                        
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
        Ion.with(this@VisualizarPerfilActivity)
            .load("PUT", "${Constantes().URL_WS}colaborador/Subir-Foto/${idColaborador}")
            .setByteArrayBody(fotoPerfil)
            .asString()
            .setCallback { e, result ->
                if (e == null) {
                    val gson = Gson()
                    val mensaje = gson.fromJson(result, Mensaje::class.java)
                    Toast.makeText(this@VisualizarPerfilActivity, mensaje.contenido, Toast.LENGTH_LONG).show()
                    if (!mensaje.error) {
                        obtenerFotoColaborador(colaborador.idColaborador)
                    }
                } else {
                    Toast.makeText(this@VisualizarPerfilActivity, e.message, Toast.LENGTH_LONG).show()
                }
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

    companion object{
        private const val REQUEST_ACTUALIZAR_COLABORADOR = 1
    }

    private fun cancelarEdicion() {
        setResult(RESULT_CANCELED)
        finish()
    }
}