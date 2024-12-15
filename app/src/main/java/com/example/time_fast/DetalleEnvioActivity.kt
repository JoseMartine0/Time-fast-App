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

    private lateinit var envio: Envio
    private lateinit var binding: ActivityDetalleEnvioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetalleEnvioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        obtenerEnvio()
        mostrarDetalleEnvio()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detalleEnvio)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    private fun obtenerEnvio() {
        val jsonEnvio = intent.getStringExtra("envio")
        if (jsonEnvio != null) {
            val gson = Gson()
            envio = gson.fromJson(jsonEnvio, Envio::class.java)

        } else {
            Toast.makeText(this, "Error al obtener datos del envio", Toast.LENGTH_SHORT).show()
        }
    }

    private fun mostrarDetalleEnvio() {
        binding.tvEstado.text = "${envio.idEstadoEnvio}"
        binding.tvNumeroGuia.text = envio.numeroGuia
        binding.tvCosto.text = envio.costo.toString()
        binding.tvNombreCliente.text = envio.nombreClienteCompleto
        binding.tvTelefonoCliente.text = envio.telefonoCliente
        binding.tvCorreoCliente.text = envio.correoElectronicoCliente
        binding.tvDireccionOrigen.text = "${envio.calleOrigen}, ${envio.numeroOrigen}, ${envio.coloniaOrigen}, ${envio.codigoPostalOrigen}, ${envio.municipioOrigen}, ${envio.estadoOrigen} "
        binding.tvDireccionDestino.text = "${envio.calleDestino}, ${envio.numeroDestino}, ${envio.coloniaDestino}, ${envio.codigoPostalDestino}, ${envio.municipioDestino}, ${envio.estadoDestino} "

    }
}
