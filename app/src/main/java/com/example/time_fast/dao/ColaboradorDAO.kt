package com.example.time_fast.dao

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import com.example.time_fast.poko.Colaborador
import com.example.time_fast.poko.Envio
import com.example.time_fast.poko.Mensaje
import com.example.time_fast.utils.Constantes
import com.google.gson.Gson
import com.koushikdutta.ion.Ion

class ColaboradorDAO (private val context: Context){

    fun ActualizarColaborador(colaborador: Colaborador, onSuccess: (Mensaje) -> Unit, onError: (String) -> Unit) {
        Ion.with(context)
            .load("PUT", "${Constantes().URL_WS}colaboradores/editar")
            .asString()
            .setCallback { e, result ->
                if (e == null && result != null) {
                    val gson = Gson()
                    val mensaje = gson.fromJson(result, Mensaje::class.java)
                    onSuccess(mensaje)
                } else {
                    onError(e?.message ?: "No se pudo obtener la información")
                }
            }
    }
}