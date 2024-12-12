package com.example.time_fast.poko

data class RespuestaEnvio(
    var envio: Envio,
    var error : Boolean,
    var contenido : String
)
