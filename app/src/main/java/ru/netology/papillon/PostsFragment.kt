package ru.netology.papillon

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.papillon.AddEditPostFragment.Companion.textDataContent
import ru.netology.papillon.AddEditPostFragment.Companion.textDataVideo
import ru.netology.papillon.adapter.OnPostInteractionListener
import ru.netology.papillon.adapter.PostsAdapter
import ru.netology.papillon.databinding.FragmentPostsBinding
import ru.netology.papillon.dto.Post
import ru.netology.papillon.viewmodel.PostViewModel

class PostsFragment : Fragment() {

    val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPostsBinding.inflate(inflater, container, false)

        val adapter = PostsAdapter(object : OnPostInteractionListener {
            override fun onEditPost(post: Post) {
                viewModel.editPost(post)
                findNavController().navigate(R.id.action_postsFragment_to_addEditPostFragment,
                Bundle().apply{
                    textDataContent = post.content
                    textDataVideo = post.videoAttach })
            }

            override fun onLikePost(post: Post) { viewModel.likedById(post.id) }
            override fun onRemovePost(post: Post) { viewModel.removedById(post.id) }
            override fun onSharePost(post: Post) { viewModel.sharedById(post.id) }
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
            override fun onShowPost(post: Post) {}
        })

        binding.rvListOfPosts.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner, { posts ->
            adapter.submitList(posts)
        })

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_postsFragment_to_addEditPostFragment)
        }

        binding.bnvListOfPosts.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.page_1 -> findNavController().navigate(R.id.action_postsFragment_to_profileFragment)
//                R.id.page_2 -> findNavController().navigate(R.id.action_postsFragment_to_profileFragment)
//                R.id.page_3 -> findNavController().navigate(R.id.action_postsFragment_to_profileFragment)
//                R.id.page_4 -> findNavController().navigate(R.id.action_postsFragment_to_profileFragment)
                else -> findNavController().navigate(R.id.action_postsFragment_to_profileFragment)
            }
            return@setOnNavigationItemSelectedListener true
        }


        return binding.root
    }
}