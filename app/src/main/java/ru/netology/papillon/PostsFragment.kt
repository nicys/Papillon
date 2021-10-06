package ru.netology.papillon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
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
            override fun onLikePost(post: Post) {}
            override fun onEditPost(post: Post) {}
            override fun onRemovePost(post: Post) {}
            override fun onSharePost(post: Post) {}
            override fun onVideoPost(post: Post) {}
            override fun onShowPost(post: Post) {}
        })

        binding.rvListOfPosts.adapter = adapter

        return binding.root
    }
}