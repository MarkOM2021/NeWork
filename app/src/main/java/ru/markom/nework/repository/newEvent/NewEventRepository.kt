package ru.markom.nework.repository.newEvent

import okhttp3.MultipartBody
import ru.markom.nework.dto.Attachment
import ru.markom.nework.dto.EventRequest
import ru.markom.nework.dto.UserRequest
import ru.markom.nework.enumeration.AttachmentType

interface NewEventRepository {
    suspend fun loadUsers(): List<UserRequest>
    suspend fun addPictureToTheEvent(
        attachmentType: AttachmentType,
        image: MultipartBody.Part
    ): Attachment

    suspend fun addEvent(event: EventRequest)
    suspend fun getEvent(id: Int): EventRequest
    suspend fun getUser(id: Int): UserRequest
}