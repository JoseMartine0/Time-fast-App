package com.example.time_fast.dao

import android.content.Context
import com.example.time_fast.poko.RespuestaColaborador
import com.example.time_fast.utils.Constantes
import com.google.gson.Gson
import com.koushikdutta.ion.Ion

class LoginDAO(private val context: Context) {

    fun loginPorNumeroPersonal(numeroPersonal: String, password: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        Ion.getDefault(context).conscryptMiddleware.enable(false)

        Ion.with(context)
            .load("POST", "${Constantes().URL_WS}login/colaborador")
            .setHeader("Content-Type", "application/x-www-form-urlencoded")
            .setBodyParameter("numeroPersonal", numeroPersonal)
            .setBodyParameter("password", password)
            .asString()
            .setCallback { e, result ->
                if (e == null) {
                    val gson = Gson()
                    val respuestaColaborador = gson.fromJson(result, RespuestaColaborador::class.java)

                    if (!respuestaColaborador.error) {
                        val colaboradorJSON = gson.toJson(respuestaColaborador.colaborador)
                        onSuccess(colaboradorJSON)
                    } else {
                        onError(respuestaColaborador.contenido)
                    }
                } else {
                    onError(e.message ?: "Error desconocido")
                }
            }
    }

    private fun serializarInformacion(json: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        val gson = Gson()
        val respuestaColaborador = gson.fromJson(json, RespuestaColaborador::class.java)

        if (!respuestaColaborador.error) {
            val clienteJSON = gson.toJson(respuestaColaborador.colaborador)
            onSuccess(clienteJSON)
        } else {
            onError(respuestaColaborador.contenido)
        }
    }
}
