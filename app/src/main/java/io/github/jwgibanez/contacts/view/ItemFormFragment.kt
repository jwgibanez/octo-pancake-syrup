package io.github.jwgibanez.contacts.view

import android.os.Bundle
import android.view.*
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import io.github.jwgibanez.contacts.R
import io.github.jwgibanez.contacts.databinding.FragmentItemFormBinding
import io.github.jwgibanez.contacts.service.request.UserRequest
import io.github.jwgibanez.contacts.utils.loadImage
import io.github.jwgibanez.contacts.utils.showDialog
import io.github.jwgibanez.contacts.utils.toast
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
                binding.addPhoto.setOnClickListener { toast(requireContext(), "Add photo clicked.") }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_form, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home, R.id.cancel -> {
                showDialog(
                    requireContext(),
                    "Warning",
                    "Data will not be saved.", {
                        NavHostFragment.findNavController(this).navigate(
                            if (viewModel.user.value != null) R.id.edit_cancel else R.id.add_cancel)
                    }, {
                        // Dismiss
                    }
                )
                true
            }
            R.id.save -> {
                save()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun save() {
        val user = UserRequest()

        if (binding.firstName.text?.isNotEmpty() == true) {
            user.first_name = binding.firstName.text?.trim().toString()
        } else {
            viewModel.error.value = "First name must not be empty."
            return
        }

        if (binding.lastName.text?.isNotEmpty() == true) {
            user.last_name = binding.lastName.text?.trim().toString()
        } else {
            viewModel.error.value = "Last name must not be empty."
            return
        }

        if (binding.email.text?.isNotEmpty() == true) {
            user.email = binding.email.text?.trim().toString()
        } else {
            viewModel.error.value = "Last name must not be empty."
            return
        }

        val userId = viewModel.user.value?.id
        if (userId != null) {
            viewModel.updateUser(requireActivity(), userId, user) {
                showDialog(
                    requireContext(),
                    "Existing Contact",
                    "Contact details updated.") {
                    NavHostFragment.findNavController(this).navigate(R.id.edit_save)
                }
            }
        } else {
            viewModel.addUser(requireActivity(), user) {
                showDialog(
                    requireContext(),
                    "New Contact",
                    "Contact details added.") {
                    NavHostFragment.findNavController(this).navigate(R.id.add_save)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}