package io.github.jwgibanez.contacts.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup

import androidx.core.content.ContextCompat

import com.squareup.picasso.Picasso

import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import io.github.jwgibanez.contacts.databinding.ListItemBinding
import io.github.jwgibanez.contacts.service.model.User
import io.github.jwgibanez.contacts.viewmodel.ContactsViewModel
import java.lang.Exception


class ItemViewHolder private constructor(
    private val binding: ListItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(user: User, viewModel: ContactsViewModel) {
        binding.name.text = (user.first_name ?: "") + " " + user.last_name
        binding.progressBar.visibility = View.VISIBLE
        Picasso.get().load(user.avatar).into(
            binding.imageView, object : Callback {
                override fun onSuccess() {
                    binding.progressBar.visibility = View.GONE
                }

                override fun onError(e: Exception?) {
                    binding.progressBar.visibility = View.GONE
                    binding.imageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            binding.imageView.context,
                            android.R.drawable.stat_notify_error
                        )
                    )
                }
            })
        itemView.setOnLongClickListener {
            //viewModel.delete(fact)
            true
        }
    }

    companion object {
        fun create(parent: ViewGroup): ItemViewHolder {
            return ItemViewHolder(
                ListItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }
}