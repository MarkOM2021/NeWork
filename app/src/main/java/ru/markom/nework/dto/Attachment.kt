package ru.markom.nework.dto

import ru.markom.nework.enumeration.AttachmentType

data class Attachment(
    val url: String,
    val type: AttachmentType
)