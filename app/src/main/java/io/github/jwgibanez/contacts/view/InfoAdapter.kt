package io.github.jwgibanez.contacts.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.github.jwgibanez.contacts.databinding.ListItemValuePairBinding

class InfoAdapter(
) : RecyclerView.Adapter<InfoAdapter.ItemViewHolder>() {

    private val values = ArrayList<ValuePair>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            ListItemValuePairBinding.inflate(
                inflater,
                parent,
                false
            )
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = values[position]
        holder.name.text = item.name ?: ""
        holder.value.text = item.value ?: ""
    }

    fun set(newValues: ArrayList<ValuePair>) {
        values.apply {
            clear()
            addAll(newValues)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount() = values.size

    inner class ItemViewHolder(binding: ListItemValuePairBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.name
        val value: TextView = binding.value
    }

    data class ValuePair(
        var name: String?,
        var value: String?
    )
}