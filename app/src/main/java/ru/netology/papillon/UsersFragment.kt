package ru.netology.papillon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import ru.netology.papillon.adapter.OnUserInteractionListener
import ru.netology.papillon.adapter.UsersAdapter
import ru.netology.papillon.databinding.FragmentUsersBinding
import ru.netology.papillon.viewmodel.UserViewModel

class UsersFragment : Fragment() {

    val viewModel: UserViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentUsersBinding.inflate(inflater, container, false)

        val adapter = UsersAdapter(object : OnUserInteractionListener {
//            override fun onUserClick(user: User) {
//                TODO("Not yet implemented")
//            }

        })

        binding.listOfUsers.adapter = adapter

        binding.listOfUsers.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )


        return binding.root
    }

}