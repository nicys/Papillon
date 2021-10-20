package ru.netology.papillon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.papillon.AddEditUserFragment.Companion.textUserName
import ru.netology.papillon.adapter.OnUserInteractionListener
import ru.netology.papillon.adapter.UsersAdapter
import ru.netology.papillon.databinding.FragmentProfileBinding
import ru.netology.papillon.dto.User
import ru.netology.papillon.utils.StringArg
import ru.netology.papillon.utils.UserArg
import ru.netology.papillon.viewmodel.JobViewModel
import ru.netology.papillon.viewmodel.PostViewModel
import ru.netology.papillon.viewmodel.ProfileViewModel
import ru.netology.papillon.viewmodel.UserViewModel

class ProfileFragment : Fragment() {
    companion object {
        var Bundle.textUserName: String? by StringArg
        var Bundle.userData: User? by UserArg
    }

    val viewModelUser: UserViewModel by viewModels(ownerProducer = ::requireParentFragment)
    val viewModelJob: JobViewModel by viewModels(ownerProducer = ::requireParentFragment)
    val viewModelPost: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
    val viewModel: ProfileViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.bnvProfile.selectedItemId = R.id.page_3

//        val usersAdapter = UsersAdapter(object : OnUserInteractionListener {
//            override fun onEditUser(user: User) {
//                viewModel.editName(user)
//                findNavController().navigate(R.id.action_profileFragment_to_addEditUserFragment,
//                Bundle().apply {
//                    textUserName = user.name
//                })
//            }
//            override fun oDeleteUser(user: User) {
//                viewModel.removedById(user.idUser)
//            }
//        })

        val userName = binding.tvUserName.text.toString().trim()

        with(binding) {
            with(userName.isNotEmpty()) {
                if (this) { cvIsYou.isVisible }
                if (this) { profileJobId.isVisible }
                if (this) { profilePostId.isVisible }
                if (this) { btAddJob.isVisible }
                if (this) { btLike.isVisible }
                if (this) { btParticipants.isVisible }
                if (this) { ivOnline.isVisible }
                if (this) { tvOnline.isVisible }
                if (this) { btAddProfile.visibility = View.GONE }
                if (this) { tvWarning.visibility = View.GONE }
            }
        }

        binding.btAddJob.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_addEditJobFragment)
        }

        binding.btAddProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_addEditProfileFragment)
        }

        binding.bnvProfile.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.page_1 -> Toast.makeText(context, R.string.at_home, Toast.LENGTH_SHORT).show()
//                R.id.page_2 -> findNavController().navigate(R.id.action_postsFragment_to_profileFragment)
                R.id.page_3 -> findNavController().navigate(R.id.action_profileFragment_to_addEditPostFragment)
//                R.id.page_4 -> findNavController().navigate(R.id.action_postsFragment_to_profileFragment)
                else -> findNavController().navigate(R.id.action_postsFragment_to_profileFragment)
            }
            return@setOnNavigationItemSelectedListener true
        }

//        with(viewModel) {
//            arguments?.userData?.let {
//                getUserById(it.idUser).observe(viewLifecycleOwner, { user ->
//                    user ?: return@observe
//                }
//            }
//        }


        return binding.root
    }
}