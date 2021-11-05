package ru.netology.papillon

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.papillon.AddEditJobFragment.Companion.textDataCompany
import ru.netology.papillon.AddEditJobFragment.Companion.textDataFinish
import ru.netology.papillon.AddEditJobFragment.Companion.textDataLink
import ru.netology.papillon.AddEditJobFragment.Companion.textDataPosition
import ru.netology.papillon.AddEditJobFragment.Companion.textDataStart
import ru.netology.papillon.AddEditPostFragment.Companion.textDataContent
import ru.netology.papillon.ShowPostFragment.Companion.postData
import ru.netology.papillon.adapter.JobsAdapter
import ru.netology.papillon.adapter.OnJobInteractionListener
import ru.netology.papillon.adapter.OnPostInteractionListener
import ru.netology.papillon.adapter.PostsAdapter
import ru.netology.papillon.databinding.FragmentProfileBinding
import ru.netology.papillon.dto.Job
import ru.netology.papillon.dto.Post
import ru.netology.papillon.viewmodel.JobViewModel
import ru.netology.papillon.viewmodel.PostViewModel
import ru.netology.papillon.viewmodel.ProfileViewModel
import ru.netology.papillon.viewmodel.UserViewModel

class ProfileFragment : Fragment() {

    val userViewModel: UserViewModel by viewModels(ownerProducer = ::requireParentFragment)
    val jobViewModel: JobViewModel by viewModels(ownerProducer = ::requireParentFragment)
    val postViewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
    private val profileViewModel: ProfileViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.bnvProfile.selectedItemId = R.id.page_3

//        val profileAccess = profileViewModel.dataUser.observe(viewLifecycleOwner) { users->
//            users.find { user->
//                    user.idUser == idUser
//                }
//                binding.tvUserName.text = user.name
//            }


        val postAdapter = PostsAdapter(object : OnPostInteractionListener {
            override fun onEditPost(post: Post) {
                postViewModel.editPost(post)
                findNavController().navigate(R.id.action_profileFragment_to_addEditPostFragment,
                    Bundle().apply {
                        textDataContent = post.content
                    })
            }

            override fun onLikePost(post: Post) {
                postViewModel.likedById(post.id)
            }

            override fun onRemovePost(post: Post) {
                postViewModel.removedById(post.id)
            }

            override fun onSharePost(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }
                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(shareIntent)
                postViewModel.sharedById(post.id)
            }

            override fun onVideoPost(post: Post) {
//                post.videoAttach?.let {
//                    val intent = Intent().apply {
//                        action = Intent.ACTION_VIEW
//                        Intent(Intent.ACTION_VIEW, Uri.parse("url"))
//                        data = Uri.parse(post.videoAttach)
//                    }
//                    val videoIntent =
//                        Intent.createChooser(intent, getString(R.string.chooser_video_post))
//                    startActivity(videoIntent)
//                }
            }

            override fun onShowPost(post: Post) {
                postViewModel.viewedById(post.id)
                findNavController().navigate(R.id.action_profileFragment_to_showPostFragment,
                    Bundle().apply { postData = post }
                )
            }
        })

        binding.rvListOfPosts.adapter = postAdapter
        postViewModel.data.observe(viewLifecycleOwner, { state ->
            postAdapter.submitList(state.posts)
//            binding.tvEmptyText.isVisible = state.empty
        })

        val jobAdapter = JobsAdapter(object : OnJobInteractionListener {
            override fun onDeleteJob(job: Job) {
                jobViewModel.removedById(job.id)
            }

            override fun onEdinJob(job: Job) {
                jobViewModel.editJob(job)
                findNavController().navigate(R.id.action_profileFragment_to_addEditJobFragment,
                    Bundle().apply {
                        textDataCompany = job.company
                        textDataPosition = job.position
                        textDataStart = job.start
                        textDataFinish = job.finish
                        textDataLink = job.link
                    })
            }
        })

        binding.rvListOfJobs.adapter = jobAdapter
        jobViewModel.data.observe(viewLifecycleOwner, { state ->
            jobAdapter.submitList(state.jobs)
//            binding.tvEmptyText.isVisible = state.empty
        })

        val userName = binding.tvUserName.text.toString().trim()

        binding.btAddJob.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_addEditJobFragment)
        }

        binding.btAddProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_addEditUserFragment)
        }

        binding.bnvProfile.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.page_1 -> Toast.makeText(context, R.string.at_home, Toast.LENGTH_SHORT).show()
//                R.id.page_2 -> findNavController().navigate(R.id.action_postsFragment_to_profileFragment)
                R.id.page_3 -> findNavController().navigate(R.id.action_profileFragment_to_addEditPostFragment)
//                R.id.page_4 -> findNavController().navigate(R.id.action_postsFragment_to_profileFragment)
                else -> findNavController().navigate(R.id.action_postsFragment_to_profileFragment)
            }
            return@setOnNavigationItemSelectedListener true
        }

        with(binding) {
            with(userName.toString().isNotBlank() && userName.toString().isNotEmpty()) {
                if (this) {
                    cvIsYou.visibility = View.VISIBLE
                }
                if (this) {
                    profileJobId.visibility = View.VISIBLE
                }
                if (this) {
                    profileJob.visibility = View.VISIBLE
                }
                if (this) {
                    profilePostId.visibility = View.VISIBLE
                }
                if (this) {
                    btAddJob.visibility = View.VISIBLE
                }
                if (this) {
                    btLike.visibility = View.VISIBLE
                }
                if (this) {
                    btParticipants.visibility = View.VISIBLE
                }
                if (this) {
                    ivOnline.visibility = View.VISIBLE
                }
                if (this) {
                    tvOnline.visibility = View.VISIBLE
                }
                if (this) {
                    btAddProfile.visibility = View.GONE
                }
                if (this) {
                    tvWarning.visibility = View.GONE
                }
            }
        }

        return binding.root
    }
}