package ru.netology.papillon.model

import android.net.Uri
import ru.netology.papillon.enumeration.AttachmentType
import java.io.File

data class MediaModel(
    val uri: Uri? = null,
    val file: File? = null,
    val type: AttachmentType? = null
)