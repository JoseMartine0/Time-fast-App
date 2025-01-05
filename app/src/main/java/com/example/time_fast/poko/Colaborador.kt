package com.example.time_fast.poko

data class Colaborador(
    var idColaborador: Int,
    var nombre: String,
    var apellidoPaterno: String,
    var apellidoMaterno: String,
    var numeroPersonal: String,
    var correoElectronico: String,
    var password: String,
   var CURP: String,
    var idRol: Int,
    var rol: String,
    var fotoBase64: String?,
    var numeroLicencia: String,
    var activo: Boolean
)
