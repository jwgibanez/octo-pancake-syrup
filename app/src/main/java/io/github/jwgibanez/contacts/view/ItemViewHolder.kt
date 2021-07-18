package io.github.jwgibanez.contacts.view

import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import io.github.jwgibanez.contacts.databinding.ListItemBinding
import io.github.jwgibanez.contacts.service.model.User
import io.github.jwgibanez.contacts.utils.formFullName
import io.github.jwgibanez.contacts.utils.loadImage
import io.github.jwgibanez.contacts.viewmodel.ContactsViewModel

class ItemViewHolder private constructor(
    private val binding: ListItemBinding,
    private val onItemClick: (User) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(user: User, viewModel: ContactsViewModel) {
        binding.name.text = formFullName(user)
        binding.progressBar.visibility = View.VISIBLE
        loadImage(user.avatar, binding.imageView, binding.progressBar)
        itemView.setOnClickListener { onItemClick(user) }
    }

    companion object {
        fun create(parent: ViewGroup, onItemClick: (User) -> Unit): ItemViewHolder {
            return ItemViewHolder(
                ListItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ),
                onItemClick
            )
        }
    }
}