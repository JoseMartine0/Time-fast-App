package com.example.time_fast

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.time_fast.dao.LoginDAO
import com.example.time_fast.databinding.ActivityLoginColaboradorBinding

class LoginColaborador : AppCompatActivity() {

    private lateinit var binding: ActivityLoginColaboradorBinding
    private lateinit var loginDAO: LoginDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginColaboradorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginDAO = LoginDAO(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onStart() {
        super.onStart()

        binding.btnIniciarSesion.setOnClickListener {
            val numeroPersonal = binding.etNumeroPersonal.text.toString().trim()
            val password = binding.etContrasena.text.toString().trim()

            if (sonCamposValidos(numeroPersonal, password)) {
                realizarLogin(numeroPersonal, password)
            }
        }

        binding.btnIniciarSesion.setOnClickListener {
            val numeroPersonal = binding.etNumeroPersonal.text.toString().trim()
            val password = binding.etContrasena.text.toString().trim()

            if (sonCamposValidos(numeroPersonal, password)) {
                realizarLogin(numeroPersonal, password)
            }
        }
    }

    private fun sonCamposValidos(numeroPersonal: String, password: String): Boolean {
        return if (numeroPersonal.isEmpty()) {
            Toast.makeText(this, "Numero Personal obligatorio", Toast.LENGTH_SHORT).show()
            false
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Contraseña obligatoria", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }
    private fun realizarLogin(numeroPersonal: String, password: String) {
        loginDAO.loginPorNumeroPersonal(numeroPersonal, password, { colaboradorJSON ->
                val intent = Intent(this, MenuPrincipalActivity::class.java)
                intent.putExtra("colaborador", colaboradorJSON)
                startActivity(intent)
                finish()
                Toast.makeText(this, "Inicio exitoso", Toast.LENGTH_LONG).show()
            },
            { error ->
                Toast.makeText(this, "Error: $error", Toast.LENGTH_LONG).show()
            }
        )
    }
}
