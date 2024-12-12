package com.example.time_fast.poko

data class RespuestaEnvios(
    val error: Boolean,
    val contenido: String,
    val envios: List<Envio>
)
