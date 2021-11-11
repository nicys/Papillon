package ru.netology.papillon.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.netology.papillon.dto.AttachmentType
import ru.netology.papillon.entity.PostEntity

@Dao
interface PostDao {
    @Query("SELECT * FROM PostEntity ORDER BY id")
    fun getAll(): Flow<List<PostEntity>>

    @Query("SELECT * FROM PostEntity ORDER BY id")
    fun getAllPosts(): LiveData<List<PostEntity>>

    @Query(
        """
            UPDATE PostEntity SET
            likesCnt = likesCnt + CASE WHEN likedByMe THEN -1 ELSE 1 END,
            likedByMe = CASE WHEN likedByME THEN 0 ELSE -1 END
            WHERE id = :id
            """
    )
    suspend fun likedById(id: Long)

    @Query(
        """
            UPDATE PostEntity SET
            sharesCnt = sharesCnt + 1
            WHERE id = :id
            """
    )
    suspend fun sharedById(id: Long)

    @Query(
        """
            UPDATE PostEntity SET
            viewsCnt = viewsCnt + 1
            WHERE id = :id
            """
    )
    suspend fun viewedById(id: Long)

    @Query("DELETE FROM PostEntity WHERE id = :id")
    fun removedById(id: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: PostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(posts: List<PostEntity>)

    @Query("SELECT * FROM PostEntity WHERE id = :id ")
    suspend fun getPostById(id: Long) : PostEntity
}

class Converters {
    @TypeConverter
    fun toAttachmentType(value: String) = enumValueOf<AttachmentType>(value)
    @TypeConverter
    fun fromAttachmentType(value: AttachmentType) = value.name
}