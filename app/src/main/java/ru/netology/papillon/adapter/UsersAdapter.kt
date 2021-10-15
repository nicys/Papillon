package ru.netology.papillon.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.papillon.databinding.CardUserBinding
import ru.netology.papillon.dto.User

interface OnUserInteractionListener {
//    fun onUserClick(user: User)
}

class UsersAdapter(private val onUserInteractionListener: OnUserInteractionListener) :
    ListAdapter<User, UsersViewHolder>(UserDiffItemCallback) {
    object UserDiffItemCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.idUser == newItem.idUser
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val binding =
            CardUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UsersViewHolder(binding, onUserInteractionListener)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

class UsersViewHolder(
    private val binding: CardUserBinding,
    private val onUserInteractionListener: OnUserInteractionListener
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(user: User) {
        with(binding) {
            tvUserName.text = user.name
            tvUserEnter.text = user.entered

            cvIsYou.visibility = if (user.isMe) View.VISIBLE else View.GONE
        }

//        binding.userCard.setOnClickListener {
//            onUserInteractionListener.onUserClick(user)
//        }
    }

}