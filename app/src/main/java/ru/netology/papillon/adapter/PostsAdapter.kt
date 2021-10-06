package ru.netology.papillon.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.netology.papillon.R
import ru.netology.papillon.databinding.CardPostBinding
import ru.netology.papillon.dto.Post
import ru.netology.papillon.utils.totalizerSmartFeed

interface OnInteractionListener {
    fun onLikePost(post: Post) {}
    fun onEditPost(post: Post) {}
    fun onRemovePost(post: Post) {}
    fun onSharePost(post: Post) {}
    fun onVideoPost(post: Post) {}
    fun onShowPost(post: Post) {}
}

class PostsAdapter(
    private val onInteractionListener: OnInteractionListener
) : androidx.recyclerview.widget.ListAdapter<Post, PostViewHolder>(PostDiffCallback) {
    object PostDiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem((position)))
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        binding.apply {
            tvUserName.text = post.author
            tvPublished.text = post.published
            tvContent.text = post.content
            ivAvatar.setImageResource(R.drawable.ic_no_avatar_user)

            btLike.isChecked = post.likedByMe
            btLike.setOnClickListener {
                onInteractionListener.onLikePost(post)
            }
            btLike.text = if (post.likedByMe) "1" else "0"

            btShare.text = totalizerSmartFeed(post.sharesCnt)
            btShare.setOnClickListener {
                onInteractionListener.onSharePost(post)
            }

            btViews.text = totalizerSmartFeed((post.viewsCnt))

            ivVideo.setOnClickListener {
                onInteractionListener.onVideoPost(post)
            }

            postCard.setOnClickListener {
                onInteractionListener.onShowPost(post)
            }

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.post_options)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.delete -> {
                                onInteractionListener.onRemovePost(post)
                                true
                            }
                            R.id.edit -> {
                                onInteractionListener.onEditPost(post)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }
        }
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}