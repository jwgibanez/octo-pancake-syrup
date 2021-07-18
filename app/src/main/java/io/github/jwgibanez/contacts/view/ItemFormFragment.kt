package io.github.jwgibanez.contacts.view

import android.os.Bundle
import android.view.*
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import io.github.jwgibanez.contacts.R
import io.github.jwgibanez.contacts.databinding.FragmentItemFormBinding
import io.github.jwgibanez.contacts.utils.loadImage
import io.github.jwgibanez.contacts.viewmodel.ContactsViewModel

class ItemFormFragment : Fragment() {

    private var _binding: FragmentItemFormBinding? = null
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
        _binding = FragmentItemFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.user.observe(viewLifecycleOwner) { user ->
            user?.apply {
                avatar?.let {
                    loadImage(it, binding.avatar, null)
                }
                binding.firstName.setText(first_name ?: "")
                binding.lastName.setText(last_name ?: "")
                binding.mobileLayout.visibility = VISIBLE
                binding.mobile.setText(id.toString()) // ID in place of mobile number
                binding.email.setText(email ?: "")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_form, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.cancel -> {
                NavHostFragment.findNavController(this).navigate(
                    if (viewModel.user.value != null) R.id.edit_cancel else R.id.add_cancel)
                true
            }
            R.id.save -> {
                NavHostFragment.findNavController(this).navigate(
                    if (viewModel.user.value != null) R.id.edit_save else R.id.add_save)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}