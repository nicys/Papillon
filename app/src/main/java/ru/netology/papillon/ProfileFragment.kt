package ru.netology.papillon

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.netology.papillon.AddEditJobFragment.Companion.textDataCompany
import ru.netology.papillon.AddEditJobFragment.Companion.textDataFinish
import ru.netology.papillon.AddEditJobFragment.Companion.textDataLink
import ru.netology.papillon.AddEditJobFragment.Companion.textDataPosition
import ru.netology.papillon.AddEditJobFragment.Companion.textDataStart
import ru.netology.papillon.AddEditPostFragment.Companion.textDataContent
import ru.netology.papillon.PhotoImageFragment.Companion.postPhoto
import ru.netology.papillon.ShowPostFragment.Companion.postData
import ru.netology.papillon.adapter.JobsAdapter
import ru.netology.papillon.adapter.OnJobInteractionListener
import ru.netology.papillon.adapter.OnPostInteractionListener
import ru.netology.papillon.adapter.PostsAdapter
import ru.netology.papillon.databinding.FragmentProfileBinding
import ru.netology.papillon.dto.Job
import ru.netology.papillon.dto.Post
import ru.netology.papillon.extensions.loadCircleCrop
import ru.netology.papillon.viewmodel.*

class ProfileFragment : Fragment() {

    val profileViewModel: ProfileViewModel by viewModels(ownerProducer = ::requireParentFragment)
    val authViewModel: AuthViewModel by viewModels(ownerProducer = ::requireParentFragment)

    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentProfileBinding.inflate(inflater, container, false)

        profileViewModel.getUserById()

        // Create postAdapter
        val postAdapter = PostsAdapter(object : OnPostInteractionListener {
            override fun onEditPost(post: Post) {
                profileViewModel.editPost(post)
                findNavController().navigate(R.id.action_profileFragment_to_addEditPostFragment,
                    Bundle().apply {
                        textDataContent = post.content
                    })
            }

            override fun onLikePost(post: Post) {
                profileViewModel.likedById(post.id)
            }

            override fun onRemovePost(post: Post) {
                profileViewModel.removedByIdPost(post.id)
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
                profileViewModel.sharedById(post.id)
            }

            override fun onShowPost(post: Post) {
                profileViewModel.viewedById(post.id)
                findNavController().navigate(R.id.action_profileFragment_to_showPostFragment2,
                    Bundle().apply { postData = post }
                )
            }

            override fun onAvatarClicked(post: Post) {
                // TODO
            }

            override fun onPhotoImage(post: Post) {
                findNavController().navigate(R.id.action_profileFragment_to_photoImageFragment,
                    Bundle().apply
                    {
                        postData = post
                        postPhoto = post.attachment?.url
                    })
            }
        })

        profileViewModel.dataPosts.observe(viewLifecycleOwner, { state ->
            postAdapter.submitList(state.posts.map { myPosts ->
                profileViewModel.myId?.let { myPosts.copy(authorId = it) }
            })
            binding.rvListOfPosts.smoothScrollToPosition(state.posts.size)
        })

        binding.rvListOfPosts.adapter = postAdapter

        binding.rvListOfPosts.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        // Create jobAdapter
        val jobAdapter = JobsAdapter(object : OnJobInteractionListener {
            override fun onDeleteJob(job: Job) {
                profileViewModel.removedByIdJob(job.id)
            }
            override fun onEdinJob(job: Job) {
                profileViewModel.editJob(job)
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

        profileViewModel.dataJobs.observe(viewLifecycleOwner, { state ->
            jobAdapter.submitList(state.jobs.map { myJobs ->
                profileViewModel.myId?.let { myJobs.copy(jobId = it) }
            })
            binding.rvListOfPosts.smoothScrollToPosition(state.jobs.size)
        })

        binding.rvListOfJobs.adapter = jobAdapter

        profileViewModel.currentUser.observe(viewLifecycleOwner) { user ->
            user.avatar?.let {
                binding.ivAvatar.loadCircleCrop(it)
            } ?: binding.ivAvatar.setImageDrawable(
                AppCompatResources.getDrawable(requireContext(), R.drawable.ic_no_avatar_user)
            )
            binding.tvUserName.text = user.name
        }



        return binding.root
    }

}


//class ProfileFragment : Fragment() {
//
//    val userViewModel: UserViewModel by viewModels(ownerProducer = ::requireParentFragment)
//    val jobViewModel: JobViewModel by viewModels(ownerProducer = ::requireParentFragment)
//    val postViewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
//    private val profileViewModel: ProfileViewModel by viewModels(ownerProducer = ::requireParentFragment)
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val binding = FragmentProfileBinding.inflate(inflater, container, false)
//        binding.bnvProfile.selectedItemId = R.id.page_3
//
////        val profileAccess = profileViewModel.dataUser.observe(viewLifecycleOwner) { users->
////            users.find { user->
////                    user.idUser == idUser
////                }
////                binding.tvUserName.text = user.name
////            }
//
//
//        val postAdapter = PostsAdapter(object : OnPostInteractionListener {
//            override fun onEditPost(post: Post) {
//                postViewModel.editPost(post)
//                findNavController().navigate(R.id.action_profileFragment_to_addEditPostFragment,
//                    Bundle().apply {
//                        textDataContent = post.content
//                    })
//            }
//
//            override fun onLikePost(post: Post) {
//                postViewModel.likedById(post.id)
//            }
//
//            override fun onRemovePost(post: Post) {
//                postViewModel.removedById(post.id)
//            }
//
//            override fun onSharePost(post: Post) {
//                val intent = Intent().apply {
//                    action = Intent.ACTION_SEND
//                    putExtra(Intent.EXTRA_TEXT, post.content)
//                    type = "text/plain"
//                }
//                val shareIntent =
//                    Intent.createChooser(intent, getString(R.string.chooser_share_post))
//                startActivity(shareIntent)
//                postViewModel.sharedById(post.id)
//            }
//
//            override fun onShowPost(post: Post) {
//                postViewModel.viewedById(post.id)
//                findNavController().navigate(R.id.action_profileFragment_to_showPostFragment,
//                    Bundle().apply { postData = post }
//                )
//            }
//        })
//
//        binding.rvListOfPosts.adapter = postAdapter
//        postViewModel.data.observe(viewLifecycleOwner, { state ->
//            postAdapter.submitList(state.posts)
////            binding.tvEmptyText.isVisible = state.empty
//        })
//
//        val jobAdapter = JobsAdapter(object : OnJobInteractionListener {
//            override fun onDeleteJob(job: Job) {
//                jobViewModel.removedById(job.id)
//            }
//
//            override fun onEdinJob(job: Job) {
//                jobViewModel.editJob(job)
//                findNavController().navigate(R.id.action_profileFragment_to_addEditJobFragment,
//                    Bundle().apply {
//                        textDataCompany = job.company
//                        textDataPosition = job.position
//                        textDataStart = job.start
//                        textDataFinish = job.finish
//                        textDataLink = job.link
//                    })
//            }
//        })
//
//        binding.rvListOfJobs.adapter = jobAdapter
//        jobViewModel.data.observe(viewLifecycleOwner, { state ->
//            jobAdapter.submitList(state.jobs)
////            binding.tvEmptyText.isVisible = state.empty
//        })
//
//        val userName = binding.tvUserName.text.toString().trim()
//
//        binding.btAddJob.setOnClickListener {
//            findNavController().navigate(R.id.action_profileFragment_to_addEditJobFragment)
//        }
//
//        binding.btAddProfile.setOnClickListener {
//            findNavController().navigate(R.id.action_profileFragment_to_addEditUserFragment)
//        }
//
//        binding.bnvProfile.setOnNavigationItemSelectedListener {
//            when (it.itemId) {
//                R.id.page_1 -> Toast.makeText(context, R.string.at_home, Toast.LENGTH_SHORT).show()
////                R.id.page_2 -> findNavController().navigate(R.id.action_postsFragment_to_profileFragment)
//                R.id.page_3 -> findNavController().navigate(R.id.action_profileFragment_to_addEditPostFragment)
////                R.id.page_4 -> findNavController().navigate(R.id.action_postsFragment_to_profileFragment)
//                else -> findNavController().navigate(R.id.action_postsFragment_to_profileFragment)
//            }
//            return@setOnNavigationItemSelectedListener true
//        }
//
//        with(binding) {
//            with(userName.toString().isNotBlank() && userName.toString().isNotEmpty()) {
//                if (this) {
//                    cvIsYou.visibility = VISIBLE
//                    profileJobId.visibility = VISIBLE
//                    profileJob.visibility = VISIBLE
//                    profilePostId.visibility = VISIBLE
//                    btAddJob.visibility = VISIBLE
//                    btLike.visibility = VISIBLE
//                    btParticipants.visibility = VISIBLE
//                    ivOnline.visibility = VISIBLE
//                    tvOnline.visibility = VISIBLE
//                    btAddProfile.visibility = GONE
//                    tvWarning.visibility = GONE
//                }
//            }
//        }
//
//        return binding.root
//    }
//}