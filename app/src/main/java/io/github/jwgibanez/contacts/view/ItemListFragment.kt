package io.github.jwgibanez.contacts.view

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import io.github.jwgibanez.contacts.R
import io.github.jwgibanez.contacts.databinding.FragmentItemListBinding
import io.github.jwgibanez.contacts.viewmodel.ContactsViewModel
import androidx.recyclerview.widget.DividerItemDecoration

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import io.github.jwgibanez.contacts.service.model.User

class ItemListFragment : Fragment() {

    private var _binding: FragmentItemListBinding? = null
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
        _binding = FragmentItemListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        val adapter = ListAdapter(viewModel, { user -> onItemClick(user) }, ListAdapter.Diff())
        adapter.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                // Scroll to newly added item
                layoutManager.scrollToPositionWithOffset(positionStart, 0)
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                // Do nothing
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

    private fun onItemClick(user: User) {
        viewModel.user.value = user

        // Leaving this not using view binding as it relies on if the view is visible the current
        // layout configuration (layout, layout-sw600dp)
        val itemDetailFragmentContainer: View? = view?.findViewById(R.id.item_detail_nav_container)

        val bundle = Bundle()
        if (itemDetailFragmentContainer != null) {
            itemDetailFragmentContainer.findNavController()
                .navigate(R.id.fragment_item_detail, bundle)
        } else {
            findNavController(this).navigate(R.id.show_item_detail, bundle)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.add -> {
                viewModel.user.value = null
                findNavController(this).navigate(R.id.show_item_add)
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