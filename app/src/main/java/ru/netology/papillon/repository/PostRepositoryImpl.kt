package ru.netology.papillon.repository

import androidx.lifecycle.Transformations
import ru.netology.papillon.dao.PostDao
import ru.netology.papillon.dto.Post
import ru.netology.papillon.extensions.toEntityPost

class PostRepositoryImpl(
    private val postDao: PostDao
) : PostRepository {
    override fun getAll() = Transformations.map(postDao.getAll()) {
        it.map {
            Post(
                it.id, it.authorId, it.author, it.authorAvatar, it.published,
                it.content, it.likedByMe, it.likesCnt, it.shares, it.sharesCnt,
                it.views, it.viewsCnt, it.videoAttach, it.audioAttach, it.imageAttach
            )
        }
    }

    override fun likedById(id: Long) {
        postDao.likedById(id)
    }

    override fun sharedById(id: Long) {
        postDao.sharedById(id)
    }

    override fun removedById(id: Long) {
        postDao.removedById(id)
    }

    override fun savePost(post: Post) {
        postDao.savePost(post.toEntityPost())
    }
}