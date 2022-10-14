package ru.markom.nework.dto

import ru.markom.nework.enumeration.EventType

data class EventRequest(
    val id: Int,
    val content: String,
    val datetime: String?,
    val coords: Coordinates?,
    val type: EventType?,
    val attachment: Attachment?,
    val link: String?,
    val speakerIds: List<Int>?,

    )