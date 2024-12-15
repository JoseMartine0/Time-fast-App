package com.example.time_fast.dao

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import com.example.time_fast.poko.Colaborador
import com.example.time_fast.poko.Mensaje
import com.example.time_fast.utils.Constantes
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.koushikdutta.ion.Ion
import org.json.JSONObject


class ColaboradorDAO (private val context: Context){
    fun ActualizarColaborador(colaborador: Colaborador, onSuccess: (Mensaje) -> Unit, onError: (String) -> Unit) {
        Ion.with(context)
            .load("PUT", "${Constantes().URL_WS}colaboradores/editar")
            .setJsonPojoBody(colaborador)
            .asString(Charsets.UTF_8)
            .setCallback { e, result ->
                if (e == null) {
                    try {
                        val gson = Gson()
                        val mensaje = gson.fromJson(result, Mensaje::class.java)
                        if (!mensaje.error) {
                            onSuccess(mensaje)
                        } else {
                            onError("Error al actualizar el perfil: ${mensaje.contenido}")
                        }
                    } catch (ex: JsonSyntaxException) {
                        onError("Error al procesar la respuesta del servidor")
                    }
                } else {
                    onError("Error al actualizar: ${e.message}")
                }
            }
    }

    fun subirFoto(idColaborador: Int, foto: ByteArray, onSuccess: (Mensaje) -> Unit, onError: (String) -> Unit) {
        Ion.with(context)
            .load("PUT", "${Constantes().URL_WS}colaboradores/subir-foto/$idColaborador")
            .setHeader("Content-Type", "image/png")
            .setByteArrayBody(foto)
            .asString()
            .setCallback { e, result ->
                if (e == null && result != null) {
                    try {
                        val gson = Gson()
                        val mensaje = gson.fromJson(result, Mensaje::class.java)
                        onSuccess(mensaje)
                    } catch (e: JsonSyntaxException) {
                        onError("Error al procesar la respuesta del servidor")
                    }
                } else {
                    onError(e?.message ?: "Error al subir la foto")
                }
            }
    }

    fun obtenerFoto(idColaborador: Int, onSuccess: (Bitmap) -> Unit, onError: (String) -> Unit) {
            Ion.with(context)
                .load("GET", "${Constantes().URL_WS}colaboradores/obtener-foto/$idColaborador")
                .asString()
                .setCallback { e, result ->
                    if (e == null && result != null) {
                        try {
                            val json = JSONObject(result)
                            val base64String = json.getString("fotoBase64")

                            val bitmapBytes = android.util.Base64.decode(
                                base64String,
                                android.util.Base64.DEFAULT
                            )
                            val bitmap =
                                BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.size)

                            if (bitmap != null) {
                                onSuccess(bitmap)
                            } else {
                                onError("No se pudo decodificar la imagen")
                            }
                        } catch (ex: Exception) {
                            onError("Error al obtener la foto")
                        }
                    } else {
                        onError(e?.message ?: "Respuesta vacía")
                    }
                }
    }

}