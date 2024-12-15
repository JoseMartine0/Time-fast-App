package com.example.time_fast.poko

data class Envio(
    var idEnvio: Int,
    var idCliente: Int,
    var numeroGuia: String,
    var costo: Float,
    var descripcion: String,
    var idEstadoEnvio: Int,
    var idColaborador: Int,
    var nombreEstadoEnvio: String,
    var nombreClienteCompleto: String,
    var telefonoCliente: String,
    var correoElectronicoCliente: String,
    var calleOrigen: String,
    var numeroOrigen: String,
    var coloniaOrigen: String?,
    var codigoPostalOrigen: String,
    var municipioOrigen: String,
    var estadoOrigen: String,
    var calleDestino: String,
    var numeroDestino: String,
    var coloniaDestino: String,
    var codigoPostalDestino: String,
    var municipioDestino: String,
    var estadoDestino: String,
    var nombreColaboradorCompleto: String,
    var correoElectronicoColaborador: String
)
