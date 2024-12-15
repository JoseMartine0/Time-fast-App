package com.example.time_fast

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.time_fast.adapters.EnvioAdapter
import com.example.time_fast.dao.EnviosDAO
import com.example.time_fast.databinding.ActivityListaEnviosBinding
import com.example.time_fast.poko.Colaborador
import com.example.time_fast.poko.Envio
import com.example.time_fast.poko.EstadoEnvio
import com.google.gson.Gson


class ListaEnviosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListaEnviosBinding
    private val listaEnvios = ArrayList<Envio>()
    private val listaEstados = ArrayList<EstadoEnvio>()

    private lateinit var envioDao: EnviosDAO
    private lateinit var colaborador: Colaborador

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaEnviosBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        envioDao = EnviosDAO(this)
        obtenerDatosColaborador()
        cargarEnvios()

        binding.btnRegresar.setOnClickListener{
            cancelarEdicion()
        }
    }
    private fun obtenerDatosColaborador() {
        val jsonCliente = intent.getStringExtra("colaborador")
        if (jsonCliente != null) {
            val gson = Gson()
            colaborador = gson.fromJson(jsonCliente, Colaborador::class.java)
        } else {
            Toast.makeText(this, "Colaborador no encontrado. Por favor, inicie sesión nuevamente.", Toast.LENGTH_LONG).show()
            finish()
        }
    }
    private fun cargarEnvios() {
        binding.recyclerEnvios.layoutManager = LinearLayoutManager(this)
        binding.recyclerEnvios.setHasFixedSize(true)

        envioDao.obtenerEstados({ estadosObtenidos ->
            runOnUiThread {
                listaEstados.clear()
                listaEstados.addAll(estadosObtenidos)
            }
        }, { error ->
            runOnUiThread {
                Toast.makeText(this, "Error al obtener estados: $error", Toast.LENGTH_LONG).show()
            }
        })

        envioDao.obtenerEnviosAsignados(
            colaborador.numeroPersonal,
            { enviosObtenidos ->
                runOnUiThread {
                    if (enviosObtenidos.isNotEmpty()) {
                        listaEnvios.clear()
                        listaEnvios.addAll(enviosObtenidos)
                        val intent = Intent()
                        intent.putExtra("colaborador", Gson().toJson(colaborador))
                        binding.recyclerEnvios.adapter = EnvioAdapter(listaEstados, listaEnvios, this)
                    } else {
                        Toast.makeText(this, "Sin envíos disponibles", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            { error ->
                runOnUiThread {
                    Toast.makeText(this, "Error al obtener envíos: $error", Toast.LENGTH_LONG).show()
                }
            }

        )
    }

    private fun cancelarEdicion() {
        setResult(RESULT_CANCELED)
        finish()
    }



}