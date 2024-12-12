package com.example.time_fast.poko

data class Envio(
    val idEnvio: Int,
    val idCliente: Int,
    val numeroGuia: String,
    val costo: Float,
    val descripcion: String,
    val idDireccionOrigen: Int,
    val idDireccionDestino: Int,
    var idEstado: Int,
    val idColaborador: Int,
    val nombreEstado: String,

    val nombreClienteCompleto: String,
    val telefonoCliente: String,
    val correoElectronicoCliente: String,

    val calleOrigen: String,
    val numeroOrigen: String,
    val coloniaOrigen: String,
    val codigoPostalOrigen: String,
    val ciudadOrigen: String,
    val estadoOrigen: String,

    val calleDestino: String,
    val numeroDestino: String,
    val coloniaDestino: String,
    val codigoPostalDestino: String,
    val ciudadDestino: String,
    val estadoDestino: String,
)