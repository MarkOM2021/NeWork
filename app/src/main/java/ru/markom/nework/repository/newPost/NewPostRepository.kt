package ru.markom.nework.repository.newPost

import okhttp3.MultipartBody
import ru.markom.nework.dto.Attachment
import ru.markom.nework.dto.PostRequest
import ru.markom.nework.dto.UserRequest
import ru.markom.nework.enumeration.AttachmentType

interface NewPostRepository {
    suspend fun loadUsers(): List<UserRequest>
    suspend fun addPictureToThePost(
        attachmentType: AttachmentType,
        image: MultipartBody.Part
    ): Attachment

    suspend fun addPost(post: PostRequest)
    suspend fun getPost(id: Int): PostRequest
    suspend fun getUser(id: Int): UserRequest
}