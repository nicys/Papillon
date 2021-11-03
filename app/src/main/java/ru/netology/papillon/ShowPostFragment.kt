package ru.netology.papillon

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.papillon.AddEditPostFragment.Companion.textDataContent
import ru.netology.papillon.databinding.FragmentShowPostBinding
import ru.netology.papillon.dto.Post
import ru.netology.papillon.utils.PostArg
import ru.netology.papillon.utils.sumTotalFeed
import ru.netology.papillon.viewmodel.PostViewModel

class ShowPostFragment : Fragment() {

    companion object {
        var Bundle.postData: Post? by PostArg
    }

    private val postViewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentShowPostBinding.inflate(inflater, container, false)

        with(postViewModel) {
            arguments?.postData?.let { showPost ->

                getPostById(showPost.id).observe(viewLifecycleOwner, {
                    post ?: return@observe

                    binding.apply {
                        tvUserName.text = post.author
                        tvPublished.text = post.published
                        tvContent.text = post.content
                        ivAvatar.setImageResource(R.drawable.ic_no_avatar_user)

                        btLike.isChecked = post.likedByMe
                        btLike.setOnClickListener {
                            likedById(post.id)
                        }
                        btLike.text = if (post.likedByMe) "1" else "0"

                        btShare.text = sumTotalFeed(post.sharesCnt)
                        btShare.setOnClickListener {
                            val intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, post.published)
                                type = "text/plain"
                            }
                            val shareIntent =
                                Intent.createChooser(intent, getString(R.string.chooser_share_post))
                            startActivity(shareIntent)
                            sharedById(post.id)
                        }

                        btViews.text = sumTotalFeed(post.viewsCnt)

                        ivVideo.setOnClickListener {
                            post.videoAttach?.let {
                                val intent = Intent().apply {
                                    action = Intent.ACTION_VIEW
                                    Intent(Intent.ACTION_VIEW, Uri.parse("url"))
                                    data = Uri.parse(post.videoAttach)
                                }
                                val videoIntent =
                                    Intent.createChooser(
                                        intent,
                                        getString(R.string.chooser_video_post)
                                    )
                                startActivity(videoIntent)
                            }
                        }

                        menu.setOnClickListener {
                            PopupMenu(it.context, it).apply {
                                inflate(R.menu.post_options_menu)
                                setOnMenuItemClickListener { item ->
                                    when (item.itemId) {
                                        R.id.delete -> {
                                            removedById(post.id)
                                            findNavController().navigate(R.id.action_showPostFragment_to_postsFragment)
                                            true
                                        }
                                        R.id.edit -> {
                                            findNavController().navigate(R.id.action_showPostFragment_to_addEditPostFragment,
                                                Bundle().apply
                                                {
                                                    textDataContent = post.content
                                                })
                                            true
                                        }
                                        else -> false
                                    }
                                }
                            }.show()
                        }
                    }
                })
            }
        }

        return binding.root
    }
}