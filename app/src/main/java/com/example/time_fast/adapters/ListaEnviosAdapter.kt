package com.example.time_fast.adapters

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.time_fast.R
import com.example.time_fast.poko.Envio
import com.example.time_fast.DetalleEnvioActivity
import com.example.time_fast.MenuPrincipalActivity
import com.example.time_fast.dao.EnviosDAO
import com.example.time_fast.poko.Colaborador
import com.example.time_fast.poko.EstadoEnvio
import com.google.gson.Gson

class EnvioAdapter(private val estados: List<EstadoEnvio>, private val envios: List<Envio>, private val context: android.content.Context) : RecyclerView.Adapter<EnvioAdapter.ViewHolderEnvio>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderEnvio {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_envios, parent, false)
        return ViewHolderEnvio(view)
    }

    override fun getItemCount(): Int = envios.size

    override fun onBindViewHolder(holder: ViewHolderEnvio, position: Int) {
        val envio = envios[position]
        holder.bind(envio)

    }

    inner class ViewHolderEnvio(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNumeroGuia: TextView = itemView.findViewById(R.id.tvNumeroGuia)
        private val tvNombreCompletoCliente: TextView = itemView.findViewById(R.id.tvNombreCompletoCliente)
        private val tvCorreoCliente: TextView = itemView.findViewById(R.id.tvCorreoCliente)
        private val tvDescripcion : TextView = itemView.findViewById(R.id.tvDescripcion)
        private val tvEstado : TextView = itemView.findViewById(R.id.tvEstado)
        private val tvCalleDestino: TextView = itemView.findViewById(R.id.tvCalleDestino)
        private val tvNumeroDestino: TextView = itemView.findViewById(R.id.tvNumeroDestino)
        private val tvColoniaDestino: TextView = itemView.findViewById(R.id.tvColoniaDestino)
        private val tvCodigoPostalDestino: TextView = itemView.findViewById(R.id.tvCodigoPostalDestino)
        private val tvCiudadDestino: TextView = itemView.findViewById(R.id.tvCiudadDestino)
        private val tvEstadoDestino: TextView = itemView.findViewById(R.id.tvEstadoDestino)

        private val btnVerDetalles: Button = itemView.findViewById(R.id.btnVerDetalles)
        private val btnCambiarEstado: Button = itemView.findViewById(R.id.btnCambiarEstado)

        fun bind(envio: Envio) {
            if (envio.idEstado == 5 || envio.idEstado == 3){
                tvDescripcion.text = "Descripcion: ${envio.descripcion}"
            }
            tvNumeroGuia.text = "Número de guía: ${envio.numeroGuia}"
            tvNombreCompletoCliente.text = "Cliente: ${envio.nombreClienteCompleto}"
            tvCorreoCliente.text = "Correo: ${envio.correoElectronicoCliente}"
            tvEstado.text = "Estado: ${envio.nombreEstado}"
            tvCalleDestino.text = "Calle: ${envio.calleDestino}"
            tvNumeroDestino.text = "Número: ${envio.numeroDestino}"
            tvColoniaDestino.text = "Colonia: ${envio.coloniaDestino}"
            tvCodigoPostalDestino.text = "Código Postal: ${envio.codigoPostalDestino}"
            tvCiudadDestino.text = "Ciudad: ${envio.municipioDestino}"
            tvEstadoDestino.text = "Estado: ${envio.estadoDestino}"

            btnVerDetalles.setOnClickListener {
                val intent = Intent(itemView.context, DetalleEnvioActivity::class.java)
                intent.putExtra("envio", Gson().toJson(envio))
                itemView.context.startActivity(intent)
            }

            btnCambiarEstado.setOnClickListener {
                    mostrarDialogoEstado(envio)
            }
        }

        fun mostrarDialogoEstado(envio: Envio) {
            val envioDao = EnviosDAO(context)
            val dialog = Dialog(itemView.context)
            dialog.setContentView(R.layout.dialog_estado)

            val spinner = dialog.findViewById<Spinner>(R.id.sp_estado)
            val editTextDescripcion = dialog.findViewById<EditText>(R.id.et_descripcion)
            val nombresEstados = estados.map { it.nombre}
            val adapter = ArrayAdapter(itemView.context, android.R.layout.simple_spinner_item, nombresEstados)
            spinner.adapter = adapter

            editTextDescripcion.visibility = View.GONE
            spinner.setSelection(envio.idEstado -1 )

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    val estadoSeleccionado = estados[position]
                    if (estadoSeleccionado.nombre == "cancelado" || estadoSeleccionado.nombre == "detenido") {
                        editTextDescripcion.visibility = View.VISIBLE
                    } else {
                        editTextDescripcion.visibility = View.GONE
                        editTextDescripcion.text.clear()
                    }
                    // Guarda la posición del elemento seleccionado
                    val idEstado = estadoSeleccionado.idEstadoEnvio
                    println(idEstado)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
            val estadosMap = mapOf(
                "pendiente" to 1,
                "en transito" to 2,
                "detenido" to 3,
                "entregado" to 4,
                "cancelado" to 5
            )
            val btnGuardar = dialog.findViewById<Button>(R.id.btn_guardar_estado)
            btnGuardar.setOnClickListener {
                if (spinner.selectedItem != null) {
                    val estadoSeleccionado = estados[spinner.selectedItemPosition]
                    val id = estadosMap[estadoSeleccionado.nombre] ?: 0
                    val descripcion = editTextDescripcion.text.toString()

                    envioDao.actualizarEstadoEnvio(envio.idEnvio, id, descripcion,
                        { respuesta ->
                            if (!respuesta.error) {
                                Toast.makeText(itemView.context, respuesta.contenido, Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(itemView.context, "Error: ${respuesta.contenido}", Toast.LENGTH_LONG).show()
                            }
                        },
                        { error ->
                            Toast.makeText(itemView.context, "Error: $error", Toast.LENGTH_LONG).show()
                        }
                    )

                    val intent = Intent()
                    (context as Activity).setResult(Activity.RESULT_OK, intent)
                    (context as Activity).finish()
                    dialog.dismiss()
                } else {
                    Toast.makeText(itemView.context, "Por favor selecciona un estado", Toast.LENGTH_SHORT).show()
                }
            }
            dialog.show()
        }


    }
}