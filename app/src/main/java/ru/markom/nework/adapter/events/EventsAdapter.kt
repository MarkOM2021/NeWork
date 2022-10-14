package ru.markom.nework.adapter.events

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yandex.mapkit.geometry.Point
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.markom.nework.R
import ru.markom.nework.databinding.CardEventBinding
import ru.markom.nework.dto.EventResponse
import ru.markom.nework.enumeration.AttachmentType
import ru.markom.nework.enumeration.EventType.*
import ru.markom.nework.fragment.DisplayingImagesFragment.Companion.textArg
import ru.markom.nework.fragment.FeedFragment.Companion.intArg
import ru.markom.nework.fragment.MapsFragment.Companion.pointArg
import ru.markom.nework.util.GoDataTime

interface EventCallback {
    fun onLike(event: EventResponse) {}
    fun onParticipateInEvent(event: EventResponse) {}
    fun onEdit(event: EventResponse) {}
    fun onRemove(event: EventResponse) {}
    fun loadingTheListOfSpeakers(event: EventResponse) {}
    fun loadingTheListOfParticipants(event: EventResponse) {}
    fun goToPageUser(event: EventResponse) {}

}

class EventsAdapter(
    private val onInteractionListener: EventCallback,
) : PagingDataAdapter<EventResponse, EventViewHolder>(EventDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = CardEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position) ?: return
        holder.bind(event)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class EventViewHolder(
    private val binding: CardEventBinding,
    private val onInteractionListener: EventCallback
) : RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("NewApi", "SetTextI18n")
    fun bind(event: EventResponse) {
        binding.apply {
            video.visibility = View.GONE
            videoField.visibility = View.GONE

            Glide.with(itemView)
                .load(event.authorAvatar)
                .error(R.drawable.ic_avatar_loading_error_48)
                .placeholder(R.drawable.ic_baseline_cruelty_free_48)
                .timeout(10_000)
                .circleCrop()
                .into(avatar)


            if (event.attachment != null) {
                when (event.attachment.type) {
                    AttachmentType.IMAGE -> {
                        videoField.visibility = View.VISIBLE
                    }
                    AttachmentType.VIDEO -> {
                        video.visibility = View.GONE
                        videoField.visibility = View.VISIBLE
                    }
                    AttachmentType.AUDIO -> {}
                }
                Glide.with(itemView).load(event.attachment.url).timeout(10_000)
                    .into(videoField)
            }

            when (event.type) {
                OFFLINE -> type.setImageResource(R.drawable.ic_sensors_off_32)
                ONLINE -> type.setImageResource(R.drawable.ic_sensors_on_32)
            }
            author.text = event.author
            published.text = GoDataTime.convertDataTime(event.published)
            dateTime.text = GoDataTime.convertDataTime(event.datetime)
            content.text = event.content + if (event.link != null) {
                "\n" + event.link
            } else {
                ""
            }
            like.isChecked = event.likedByMe
            like.text = "${event.likeOwnerIds.size}"
            participate.isChecked = event.participatedByMe
            participate.text = "${event.participantsIds.size}"
            geo.visibility = if (event.coords != null) View.VISIBLE else View.INVISIBLE
            menu.visibility = if (event.ownerByMe) View.VISIBLE else View.INVISIBLE
            speakers.text = "${event.speakerIds.size}"

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(event)
                                true
                            }
                            R.id.edit -> {
                                onInteractionListener.onEdit(event)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }

            like.setOnClickListener {
                onInteractionListener.onLike(event)
            }
            participate.setOnClickListener {
                onInteractionListener.onParticipateInEvent(event)
            }
            participate.setOnLongClickListener {
                onInteractionListener.loadingTheListOfParticipants(event)
                true
            }

            videoField.setOnClickListener {
                it.findNavController().navigate(
                    R.id.action_eventFragment_to_displayingImagesFragment2,
                    Bundle().apply { textArg = event.attachment?.url ?: " " })
            }
            speakers.setOnClickListener {
                onInteractionListener.loadingTheListOfSpeakers(event)
            }

            author.setOnClickListener {
                onInteractionListener.goToPageUser(event)
            }

            avatar.setOnClickListener {
                onInteractionListener.goToPageUser(event)
            }
            content.setOnClickListener {
                it.findNavController().navigate(
                    R.id.action_eventFragment_to_openEventFragment,
                    Bundle().apply { intArg = event.id })
            }

            geo.setOnClickListener { view ->
                view.findNavController().navigate(
                    R.id.mapsFragment,
                    Bundle().apply {
                        Point(
                            event.coords?.lat!!.toDouble(), event.coords.long.toDouble()
                        ).also { pointArg = it }
                    })
            }

        }
    }
}


class EventDiffCallback : DiffUtil.ItemCallback<EventResponse>() {
    override fun areItemsTheSame(oldItem: EventResponse, newItem: EventResponse): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: EventResponse, newItem: EventResponse): Boolean {
        return oldItem == newItem
    }
}