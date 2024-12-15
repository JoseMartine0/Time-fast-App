package com.example.time_fast.dao

import android.R.id
import android.content.Context
import com.example.time_fast.poko.Envio
import com.example.time_fast.poko.EstadoEnvio
import com.example.time_fast.poko.Mensaje
import com.example.time_fast.poko.RespuestaEnvios
import com.example.time_fast.utils.Constantes
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.koushikdutta.ion.Ion

class EnviosDAO (private val context: Context){

    fun obtenerEnviosAsignados(numeroPersonal: String, onSuccess: (List<Envio>) -> Unit, onError: (String) -> Unit) {
        Ion.with(context)
            .load("GET", "${Constantes().URL_WS}envios/obtenerEnviosAsignados/$numeroPersonal")
            .asString()
            .setCallback { e, result ->
                if (e == null && result != null) {
                    val envios = serializarEnvios(result)
                    if (envios != null) {
                        onSuccess(envios)
                    } else {
                        onError("Error al procesar la información de envíos")
                    }
                } else {
                    onError(e?.message ?: "No se pudo obtener la información")
                }
            }
    }
    fun obtenerEstados(onSuccess: (List<EstadoEnvio>) -> Unit, onError: (String) -> Unit) {
        Ion.with(context)
            .load("GET", "${Constantes().URL_WS}estados/todo")
            .asString()
            .setCallback { e, result ->
                if (e == null && result != null) {
                    val gson = Gson()
                    val estadosType = object : TypeToken<List<EstadoEnvio>>() {}.type
                    val estados: List<EstadoEnvio> = gson.fromJson(result, estadosType)

                    if (estados.isNotEmpty()) {
                        onSuccess(estados)
                    } else {
                        onError("No se encontraron estados de envío")
                    }
                } else {
                    onError(e?.message ?: "No se pudo obtener la información")
                }
            }
    }

    fun actualizarEstadoEnvio(idEnvio: Int, idEstado: Int, descripcion: String?, onSuccess: (Mensaje) -> Unit, onError: (String) -> Unit) {
        if(idEstado == 3 || idEstado == 5){
            if(descripcion == null){
                return
            }
        }
        Ion.with(context)
            .load("PUT", "${Constantes().URL_WS}envios/actualizarEstado")
            .setHeader("Content-Type", "application/x-www-form-urlencoded")
            .setBodyParameter("idEnvio", idEnvio.toString())
            .setBodyParameter("idEstado", idEstado.toString())
            .setBodyParameter("descripcion", descripcion ?: "")
            .asString()
            .setCallback { e, result ->
                if (e == null && result != null) {
                    try {
                        println(result)
                        val gson = Gson()
                        val respuesta = gson.fromJson(result, Mensaje::class.java)
                    } catch (e: JsonSyntaxException) {
                        onError("La respuesta del servidor no es un objeto JSON válido")
                    }
                } else {
                    onError(e?.message ?: "Error al actualizar el estado del envío")
                }
            }
    }



    private fun serializarEnvios(json: String): List<Envio>? {
        return try {
            val gson = Gson()
            val respuestaEnvios = gson.fromJson(json, RespuestaEnvios::class.java)

            if (!respuestaEnvios.error) {
                respuestaEnvios.envios
            } else {
                null
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }

}