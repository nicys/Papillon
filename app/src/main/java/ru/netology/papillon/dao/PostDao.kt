package ru.netology.papillon.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.netology.papillon.dto.Post
import ru.netology.papillon.entity.PostEntity

@Dao
interface PostDao {
    @Query("SELECT * FROM PostEntity ORDER BY id")
    fun getAll(): LiveData<List<PostEntity>>

    @Query(
        """
            UPDATE PostEntity SET
            likesCnt = likesCnt + CASE WHEN likedByMe THEN -1 ELSE 1 END,
            likedByMe = CASE WHEN likedByME THEN 0 ELSE -1 END
            WHERE id = :id
            """
    )
    fun likedById(id: Long)

    @Query(
        """
            UPDATE PostEntity SET
            sharesCnt = sharesCnt + 1
            WHERE id = :id
            """
    )
    fun sharedById(id: Long)

    @Query(
        """
            UPDATE PostEntity SET
            viewsCnt = viewsCnt + 1
            WHERE id = :id
            """
    )
    fun viewedById(id: Long)

    @Query("DELETE FROM PostEntity WHERE id = :id")
    fun removedById(id: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePost(post: PostEntity)
}