package ru.netology.papillon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import ru.netology.papillon.AddEditUserFragment.Companion.textUserName
import ru.netology.papillon.SignUpFragment.Companion.textLoginName
import ru.netology.papillon.adapter.OnUserInteractionListener
import ru.netology.papillon.adapter.UsersAdapter
import ru.netology.papillon.databinding.FragmentUsersBinding
import ru.netology.papillon.dto.User
import ru.netology.papillon.viewmodel.UserViewModel

class UsersFragment : Fragment() {

    val userViewModel: UserViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentUsersBinding.inflate(inflater, container, false)

        val adapter = UsersAdapter(object : OnUserInteractionListener {
            override fun onEditUser(user: User) {
                userViewModel.editUser(user)
                findNavController().navigate(R.id.action_profileFragment_to_addEditUserFragment,
                    Bundle().apply {
                        textUserName = user.name
                        textLoginName = user.login
                    })
            }
            override fun oDeleteUser(user: User) {
                userViewModel.removedById(user.idUser)
            }
//            override fun onUserClick(user: User) {
//                TODO("Not yet implemented")
//            }
        })

        binding.rvListOfUsers.adapter = adapter

        binding.rvListOfUsers.adapter = adapter
        userViewModel.data.observe(viewLifecycleOwner, { state ->
            adapter.submitList(state.users)
            binding.tvEmptyText.isVisible = state.empty
        })

        binding.rvListOfUsers.adapter = adapter
        userViewModel.dataState.observe(viewLifecycleOwner, { state ->
            binding.pbProgress.isVisible = state.loading
            binding.swiperefresh.isRefreshing = state.refreshing
            binding.errorGroup.isVisible = state.error
            if (state.error) {
                Snackbar.make(binding.root, R.string.error_loading, Snackbar.LENGTH_LONG)
                    .setAction(R.string.retry_loading) { userViewModel.loadUsers() }
                    .show()
            }
        })

        userViewModel.networkError.observe(viewLifecycleOwner, {
            Snackbar.make(requireView(), getString(R.string.error_network), Snackbar.LENGTH_LONG)
                .show()
        })

        binding.btRetryButton.setOnClickListener {
            userViewModel.loadUsers()
        }

        binding.swiperefresh.setOnRefreshListener {
            userViewModel.refreshUsers()
        }

        binding.rvListOfUsers.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        binding.rvListOfUsers.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        return binding.root
    }
}