package com.syftapp.codetest.posts

import com.syftapp.codetest.data.model.domain.Post
import com.syftapp.codetest.data.repository.BlogRepository
import io.reactivex.Single
import org.koin.core.KoinComponent

class DeletePostUseCase(private val repository: BlogRepository) : KoinComponent {

    fun execute(post: Post): Single<List<Post>> =
        repository.deletePost(post.id)
}