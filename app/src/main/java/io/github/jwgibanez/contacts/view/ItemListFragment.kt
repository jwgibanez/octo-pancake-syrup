package io.github.jwgibanez.contacts.view

import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import io.github.jwgibanez.contacts.R
import io.github.jwgibanez.contacts.placeholder.PlaceholderContent
import io.github.jwgibanez.contacts.databinding.FragmentItemListBinding
import io.github.jwgibanez.contacts.databinding.ItemListContentBinding
import io.github.jwgibanez.contacts.viewmodel.ContactsViewModel
import androidx.recyclerview.widget.DividerItemDecoration

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver


class ItemListFragment : Fragment() {

    private var _binding: FragmentItemListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ContactsViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        val adapter = ListAdapter(viewModel, ListAdapter.Diff())
        adapter.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                // Scroll to newly added item
                layoutManager.scrollToPositionWithOffset(positionStart, 0)
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                Toast.makeText(context, "Fact deleted.", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.users.observe(viewLifecycleOwner) { facts ->
            //binding.emptyMessage.setVisibility(if (facts.size() > 0) GONE else VISIBLE)
            adapter.submitList(facts)
        }

        binding.itemList.adapter = adapter
        binding.itemList.layoutManager = layoutManager

        val decoration = DividerItemDecoration(binding.itemList.context, DividerItemDecoration.VERTICAL)
        binding.itemList.addItemDecoration(decoration)

        viewModel.fetchUsers(requireActivity())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}