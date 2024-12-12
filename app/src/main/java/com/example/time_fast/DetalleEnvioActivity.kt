package com.example.time_fast

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.time_fast.databinding.ActivityDetalleEnvioBinding
import com.example.time_fast.databinding.ActivityListaEnviosBinding
import com.example.time_fast.poko.Colaborador
import com.example.time_fast.poko.Envio
import com.google.gson.Gson

class DetalleEnvioActivity : AppCompatActivity() {

    private lateinit var envio : Envio
    private lateinit var binding: ActivityDetalleEnvioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetalleEnvioBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_detalle_envio)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detalleEnvio)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        obtenerEnvio()
        println(envio)
        if (::envio.isInitialized) {
            println(envio)
            mostrarDetallesEnvio()
        } else {
            Toast.makeText(this, "No se recibieron datos del envío", Toast.LENGTH_SHORT).show()
        }    }

    private fun obtenerEnvio() {
        val jsonCliente = intent.getStringExtra("envio")
        if (jsonCliente != null) {
            envio = Gson().fromJson(jsonCliente, Envio::class.java)
        } else {
            Toast.makeText(this, "Error al obtener datos del envio", Toast.LENGTH_SHORT).show()
        }
    }
    private fun mostrarDetallesEnvio() {
            binding.tvEstado.text = "Estado: chimichanga"
            binding.tvNumeroGuia.text = "Número de Guía: ${envio.numeroGuia}"
            binding.tvCosto.text = "Costo: $${envio.costo}"
            binding.tvNombreCliente.text = "Cliente: ${envio.nombreClienteCompleto}"
            binding.tvTelefonoCliente.text = "Teléfono: ${envio.telefonoCliente}"
            binding.tvCorreoCliente.text = "Correo: ${envio.correoElectronicoCliente}"
            binding.tvDireccionOrigen.text = "Origen: ${envio.calleOrigen}, ${envio.numeroOrigen}, ${envio.coloniaOrigen}, ${envio.codigoPostalOrigen}, ${envio.ciudadOrigen}, ${envio.estadoOrigen} "
            binding.tvDireccionDestino.text = "Destino: ${envio.calleDestino}, ${envio.numeroDestino}, ${envio.coloniaDestino}, ${envio.codigoPostalDestino}, ${envio.ciudadDestino}, ${envio.estadoDestino} "

    }
}