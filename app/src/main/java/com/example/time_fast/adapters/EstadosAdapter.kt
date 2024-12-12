package com.example.time_fast.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class EstadoAdapter(private val context: Context, private val estados: List<String>) :
    ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, estados) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        (view as TextView).text = estados[position]
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        (view as TextView).text = estados[position]
        return view
    }
}