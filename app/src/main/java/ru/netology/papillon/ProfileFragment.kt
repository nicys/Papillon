package ru.netology.papillon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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

    val userViewModel: UserViewModel by viewModels(ownerProducer = ::requireParentFragment)
    val jobViewModel: JobViewModel by viewModels(ownerProducer = ::requireParentFragment)
    val postViewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
    val profileViewModel: ProfileViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.bnvProfile.selectedItemId = R.id.page_3

        val profileAccess = profileViewModel.data.observe(viewLifecycleOwner) {
            it.map { user ->
                binding.tvUserName.text = user.name
            }
        }

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
            with(profileAccess.toString().isNotBlank() || profileAccess.toString().isNotEmpty()) {
                if (this) { cvIsYou.visibility = View.VISIBLE }
                if (this) { profileJobId.visibility = View.VISIBLE  }
                if (this) { profileJob.visibility = View.VISIBLE  }
                if (this) { profilePostId.visibility = View.VISIBLE  }
                if (this) { btAddJob.visibility = View.VISIBLE  }
                if (this) { btLike.visibility = View.VISIBLE  }
                if (this) { btParticipants.visibility = View.VISIBLE  }
                if (this) { ivOnline.visibility = View.VISIBLE  }
                if (this) { tvOnline.visibility = View.VISIBLE  }
                if (this) { btAddProfile.visibility = View.GONE }
                if (this) { tvWarning.visibility = View.GONE }
            }
        }

        binding.btAddJob.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_addEditJobFragment)
        }

        binding.btAddProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_addEditUserFragment)
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

        return binding.root
    }
}