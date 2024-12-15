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
import com.example.time_fast.dao.ColaboradorDAO
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
    private var fotoPerfil: ByteArray? = null
    private var imgURI: Uri? = null

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
                Toast.makeText(this, "Campos inválidos", Toast.LENGTH_LONG).show()
            }
        }

        binding.btnCancelar.setOnClickListener {
            cancelarEdicion()
        }
    }

    override fun onStart() {
        super.onStart()
        obtenerFotoColaborador(colaborador.idColaborador)

        binding.btnCapturePhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            seleccionarFotoPerfil.launch(intent)
        }
    }

    private fun obtenerFotoColaborador(idColaborador: Int) {
        val dao = ColaboradorDAO(this)
        dao.obtenerFoto(idColaborador, { bitmap ->
            binding.ivProfileImage.setImageBitmap(bitmap)
        }, { error ->
            binding.ivProfileImage.setImageResource(R.drawable.app_logo)
        })
    }


    private val seleccionarFotoPerfil = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            imgURI = data?.data  // Asigna la URI a imgURI

            if (imgURI != null) {
                fotoPerfil = uriToByteArray(imgURI!!)
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
        if (fotoPerfil != null) {
            val dao = ColaboradorDAO(this)
            dao.subirFoto(idColaborador, fotoPerfil!!,
                { mensaje ->
                    Toast.makeText(this, "Foto actualizada correctamente", Toast.LENGTH_LONG).show()
                    obtenerFotoColaborador(idColaborador)  // Actualiza la foto en la UI
                },
                { error ->
                    Toast.makeText(this, "Error al subir la foto: $error", Toast.LENGTH_LONG).show()
                }
            )
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

        val dao = ColaboradorDAO(this)
        dao.obtenerFoto(colaborador.idColaborador,
            { bitmap ->
                binding.ivProfileImage.setImageBitmap(bitmap)
            },
            { error ->
                Toast.makeText(this, "Error al cargar la foto: $error", Toast.LENGTH_SHORT).show()
            }
        )
    }
    private fun guardarCambios() {
        colaborador.nombre = binding.etNombre.text.toString()
        colaborador.apellidoPaterno = binding.etApellidoPaterno.text.toString()
        colaborador.apellidoMaterno = binding.etApellidoMaterno.text.toString()
        colaborador.correoElectronico = binding.etCorreo.text.toString()
        colaborador.CURP = binding.etCURP.text.toString()

        val dao = ColaboradorDAO(this)

        dao.ActualizarColaborador(colaborador,
            { mensaje ->
                Toast.makeText(this, mensaje.contenido, Toast.LENGTH_LONG).show()
                if (fotoPerfil != null) {
                    subirFotoPerfil(colaborador.idColaborador)
                }
            },
            { error ->
                Toast.makeText(this, error, Toast.LENGTH_LONG).show()
            }
        )
    }


    private fun validarCampos(): Boolean {
        val nombre = binding.etNombre.text.toString()
        val apellidoPaterno = binding.etApellidoPaterno.text.toString()
        val apellidoMaterno = binding.etApellidoMaterno.text.toString()
        val correoElectronico = binding.etCorreo.text.toString()
        val CURP = binding.etCURP.text.toString()

        return nombre.isNotEmpty() && apellidoPaterno.isNotEmpty() &&
                apellidoMaterno.isNotEmpty() && correoElectronico.isNotEmpty() &&
                CURP.isNotEmpty()
    }

    private fun cancelarEdicion() {
        setResult(RESULT_CANCELED)
        finish()
    }

}