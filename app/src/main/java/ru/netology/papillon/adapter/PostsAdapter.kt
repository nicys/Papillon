package ru.netology.papillon.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.papillon.BuildConfig
import ru.netology.papillon.R
import ru.netology.papillon.databinding.CardPostBinding
import ru.netology.papillon.dto.AttachmentType
import ru.netology.papillon.dto.Post
import ru.netology.papillon.extensions.loadCircleCrop
import ru.netology.papillon.extensions.loadImage
import ru.netology.papillon.utils.sumTotalFeed

interface OnPostInteractionListener {
    fun onLikePost(post: Post) {}
    fun onEditPost(post: Post) {}
    fun onRemovePost(post: Post) {}
    fun onSharePost(post: Post) {}
    fun onVideoPost(post: Post) {}
    fun onShowPost(post: Post) {}
    fun onAvatarClicked(post: Post) {}
}

class PostsAdapter(
    private val onPostInteractionListener: OnPostInteractionListener
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onPostInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onPostInteractionListener: OnPostInteractionListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        binding.apply {
            tvUserName.text = post.author
            tvPublished.text = post.published
            tvContent.text = post.content

            post.authorAvatar?.let {
                ivAvatar.loadCircleCrop("${BuildConfig.BASE_URL}/avatars/${post.authorAvatar}")
            } ?: ivAvatar.setImageResource(R.drawable.ic_no_avatar_user)

            btLike.isChecked = post.likedByMe
            btLike.setOnClickListener {
                onPostInteractionListener.onLikePost(post)
            }
            btLike.text = if (post.likedByMe) "1" else "0"

            btShare.text = sumTotalFeed(post.sharesCnt)
            btShare.setOnClickListener {
                onPostInteractionListener.onSharePost(post)
            }

            btViews.text = sumTotalFeed((post.viewsCnt))

            ivVideo.setOnClickListener {
                onPostInteractionListener.onVideoPost(post)
            }

            tvContent.setOnClickListener {
                onPostInteractionListener.onShowPost(post)
            }

            if (!post.ownedByMe) menu.visibility = GONE
            else {
                menu.visibility = VISIBLE
                menu.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.post_options_menu)
                        menu.setGroupVisible(R.id.owned, post.ownedByMe)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.delete -> {
                                    onPostInteractionListener.onRemovePost(post)
                                    true
                                }
                                R.id.edit -> {
                                    onPostInteractionListener.onEditPost(post)
                                    true
                                }
                                else -> false
                            }
                        }
                    }.show()
                }
            }

            if (post.attachment == null) {
                imageAttachment.visibility = GONE
                videoContainer.visibility = GONE
            } else {
                when (post.attachment!!.type) {
                    AttachmentType.IMAGE -> {
                        imageAttachment.visibility = VISIBLE
                        videoContainer.visibility = GONE
                        imageAttachment.loadImage("${BuildConfig.BASE_URL}/media/${post.attachment!!.url}")
                    }
                    AttachmentType.VIDEO -> {
                        imageAttachment.visibility = GONE
                        videoContainer.visibility = VISIBLE
                        Glide.with(binding.root).load(post.attachment!!.url).into(ivVideo)
                    }
                    AttachmentType.AUDIO -> {
                        imageAttachment.visibility = GONE
                        videoContainer.visibility = VISIBLE
                        ivVideo.setImageDrawable(
                            AppCompatResources.getDrawable(
                                itemView.context,
                                R.drawable.ic_audio_24
                            )
                        )
                    }
                }
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