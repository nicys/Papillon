package ru.netology.papillon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.netology.papillon.databinding.FragmentPhotoImageBinding
import ru.netology.papillon.dto.Post
import ru.netology.papillon.extensions.load
import ru.netology.papillon.utils.PostArg
import ru.netology.papillon.utils.StringArg
import ru.netology.papillon.viewmodel.PostViewModel

class PhotoImageFragment : Fragment() {

    companion object {
        var Bundle.postPhoto: String? by StringArg
        var Bundle.postData: Post? by PostArg
    }

    private val postViewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPhotoImageBinding.inflate(inflater, container, false)

        with(postViewModel) {
            arguments?.postData?.let { post ->
                val postWithPhoto = post
                getPostById(postWithPhoto.id).observe(viewLifecycleOwner, { post ->
                    post ?: return@observe
                })
                arguments?.postPhoto?.let {
                    binding.fullScreenPhoto.load(post.attachment?.url.orEmpty())
                }
            }
            return binding.root
        }
    }
}