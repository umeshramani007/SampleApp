package com.example.eydstest.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "gif_object")
data class GIFObject(
    @PrimaryKey val id: String,
    @Embedded var images: Images
) : Serializable {
    @Ignore
    var isFavorite: Boolean = false
}

data class Images (
    @Embedded
    val preview_webp: PreviewWebP = PreviewWebP()
)

data class PreviewWebP(
    val url: String = ""
)
