package com.example.time_fast

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.time_fast.databinding.ActivityMenuPrincipalBinding

class MenuPrincipalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuPrincipalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuPrincipalBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_menu_principal)

        // Navegación a Lista de Envíos
        val btnListaEnvios: Button = findViewById(R.id.btnListaEnvios)
        btnListaEnvios.setOnClickListener {
            val intent = Intent(this, ListaEnviosActivity::class.java)
            startActivity(intent)
        }

        // Navegación a Detalle de Envío
        binding.btnDetalleEnvio.setOnClickListener {
            startActivity(Intent(this, DetalleEnvioActivity::class.java))
        }

        // Navegación a Actualizar Estado
        binding.btnActualizarEstado.setOnClickListener {
            startActivity(Intent(this, ActualizarPerfilActivity::class.java))
        }

        // Navegación a Actualizar Perfil
        binding.btnActualizarPerfil.setOnClickListener {
            startActivity(Intent(this, ActualizarPerfilActivity::class.java))
        }

        // Cerrar Sesión
        binding.btnCerrarSesion.setOnClickListener {
            startActivity(Intent(this, LoginColaborador::class.java))
            finish() // Termina la actividad actual
        }
    }
}
