package ru.netology.papillon

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import ru.netology.papillon.AddEditPostFragment.Companion.textDataContent
import ru.netology.papillon.ShowPostFragment.Companion.postData
import ru.netology.papillon.adapter.OnPostInteractionListener
import ru.netology.papillon.adapter.PostsAdapter
import ru.netology.papillon.databinding.FragmentPostsBinding
import ru.netology.papillon.dto.Post
import ru.netology.papillon.viewmodel.JobViewModel
import ru.netology.papillon.viewmodel.PostViewModel
import ru.netology.papillon.viewmodel.ProfileViewModel
import ru.netology.papillon.viewmodel.UserViewModel

class PostsFragment : Fragment() {

    val userViewModel: UserViewModel by viewModels(ownerProducer = ::requireParentFragment)
    val jobViewModel: JobViewModel by viewModels(ownerProducer = ::requireParentFragment)
    val postViewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
    val profileViewModel: ProfileViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPostsBinding.inflate(inflater, container, false)

        val adapter = PostsAdapter(object : OnPostInteractionListener {
            override fun onEditPost(post: Post) {
                postViewModel.editPost(post)
                findNavController().navigate(R.id.action_postsFragment_to_addEditPostFragment,
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
                post.videoAttach?.let {
                    val intent = Intent().apply {
                        action = Intent.ACTION_VIEW
                        Intent(Intent.ACTION_VIEW, Uri.parse("url"))
                        data = Uri.parse(post.videoAttach)
                    }
                    val videoIntent =
                        Intent.createChooser(intent, getString(R.string.chooser_video_post))
                    startActivity(videoIntent)
                }
            }

            override fun onShowPost(post: Post) {
                postViewModel.viewedById(post.id)
                findNavController().navigate(R.id.action_postsFragment_to_showPostFragment,
                    Bundle().apply { postData = post }
                )
            }
        })

        binding.rvListOfPosts.adapter = adapter
        postViewModel.data.observe(viewLifecycleOwner, { posts ->
            adapter.submitList(posts)
        })

        postViewModel.networkError.observe(viewLifecycleOwner, {
            Snackbar.make(requireView(), getString(R.string.error_network), Snackbar.LENGTH_LONG)
                .show()
        })

        binding.bnvListOfPosts.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.page_1 -> findNavController().navigate(R.id.action_postsFragment_to_profileFragment)
//                R.id.page_2 -> findNavController().navigate(R.id.action_postsFragment_to_profileFragment)
                R.id.page_3 -> findNavController().navigate(R.id.action_postsFragment_to_addEditPostFragment)
//                R.id.page_4 -> findNavController().navigate(R.id.action_postsFragment_to_profileFragment)
                else -> findNavController().navigate(R.id.action_postsFragment_to_profileFragment)
            }
            return@setOnNavigationItemSelectedListener true
        }

        binding.rvListOfPosts.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        return binding.root
    }
}