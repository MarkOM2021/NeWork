package ru.markom.nework.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.markom.nework.R
import ru.markom.nework.databinding.CardIdsBinding
import ru.markom.nework.dto.UserRequest

interface AdapterUsersIdCallback {
    fun goToPageUser(id: Int)
}

class ListUsersIdAdapter(private val callback: AdapterUsersIdCallback) :
    ListAdapter<UserRequest, UsersIdListOfViewHolder>(UsersIdDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersIdListOfViewHolder {
        val binding = CardIdsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UsersIdListOfViewHolder(binding, callback)
    }

    override fun onBindViewHolder(holder: UsersIdListOfViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class UsersIdListOfViewHolder(
    private val binding: CardIdsBinding,
    private val callback: AdapterUsersIdCallback
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(user: UserRequest) {
        binding.apply {
            Glide.with(binding.root)
                .load(user.avatar)
                .error(R.drawable.ic_no_avatar_48)
                .placeholder(R.drawable.ic_no_user_48)
                .timeout(10_000)
                .circleCrop()
                .into(avatar)
            this.avatar.setOnClickListener {
                callback.goToPageUser(user.id)
            }
        }
    }
}

class UsersIdDiffCallback : DiffUtil.ItemCallback<UserRequest>() {
    override fun areItemsTheSame(oldItem: UserRequest, newItem: UserRequest): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UserRequest, newItem: UserRequest): Boolean {
        return oldItem == newItem
    }
}
