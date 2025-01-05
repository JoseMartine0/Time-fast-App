package com.example.time_fast

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.time_fast.databinding.ActivityDetalleEnvioBinding
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
        binding.btnRegresar.setOnClickListener {
            finish() 
        }    }

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
        binding.tvEstado.text = "Estado : "+envio.nombreEstadoEnvio
        binding.tvNumeroGuia.text = "Numero de Guia : " + envio.numeroGuia
        binding.tvCosto.text = "Costo : $"+ envio.costo.toString()
        binding.tvDescripcion.text = "Descripcion :"+envio.descripcion
        binding.tvNombreCliente.text = "Nombre : "+envio.nombreClienteCompleto
        binding.tvTelefonoCliente.text = "Telefono : "+envio.telefonoCliente
        binding.tvCorreoCliente.text = "Correo Electronico : "+envio.correoElectronicoCliente
        binding.tvDireccionOrigen.text = "${envio.calleOrigen}, ${envio.numeroOrigen}, ${envio.coloniaOrigen}, ${envio.codigoPostalOrigen}, ${envio.municipioOrigen}, ${envio.estadoOrigen} "
        binding.tvDireccionDestino.text = "${envio.calleDestino}, ${envio.numeroDestino}, ${envio.coloniaDestino}, ${envio.codigoPostalDestino}, ${envio.municipioDestino}, ${envio.estadoDestino} "

    }
}
