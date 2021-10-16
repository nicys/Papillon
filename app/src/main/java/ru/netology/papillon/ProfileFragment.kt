package ru.netology.papillon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import ru.netology.papillon.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.bnvProfile.selectedItemId = R.id.page_3

        val userName = binding.tvUserName.text.toString().trim()

        with(binding) {
            with(userName.isNotEmpty()) {
                if (this) { cvIsYou.isVisible }
                if (this) { profileJobId.isVisible }
                if (this) { profilePostId.isVisible }
                if (this) { btAddProfile.visibility = View.GONE }
                if (this) { tvWarning.visibility = View.GONE }
            }
        }

        binding.btAddJob.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_addEditJobFragment)
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