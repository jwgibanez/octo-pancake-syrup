package io.github.jwgibanez.contacts.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import io.github.jwgibanez.contacts.R
import io.github.jwgibanez.contacts.databinding.FragmentItemDetailBinding
import io.github.jwgibanez.contacts.service.model.User
import io.github.jwgibanez.contacts.utils.formFullName
import io.github.jwgibanez.contacts.utils.loadImage
import io.github.jwgibanez.contacts.viewmodel.ContactsViewModel
import android.view.*
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment

class ItemDetailFragment : Fragment() {

    private var _binding: FragmentItemDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ContactsViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.user.value != null) {
            viewModel.user.observe(viewLifecycleOwner) { updateView(it) }
        } else {
            viewModel.error.value = getString(R.string.generic_error_message)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_details, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.edit -> {
                NavHostFragment.findNavController(this).navigate(R.id.show_item_edit)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateView(user:User?) {
        user?.let {
            loadImage(user.avatar, binding.avatar!!, null)
            binding.name!!.text = formFullName(user)
            binding.icEmail!!.visibility = if (!user.email.isNullOrEmpty()) VISIBLE else GONE

            val pairs = ArrayList<InfoAdapter.ValuePair>()
            // ID in place of mobile number
            pairs.add(InfoAdapter.ValuePair("mobile", user.id.toString()))
            if (user.email?.isBlank() == false) {
                pairs.add(InfoAdapter.ValuePair("email", user.email))
            }

            val ad = InfoAdapter()
            binding.itemList?.apply {
                adapter = ad
                addItemDecoration(
                    DividerItemDecoration(
                        context,
                        DividerItemDecoration.VERTICAL
                    )
                )
            }
            ad.set(pairs)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}