package com.syftapp.codetest.data.repository

import com.syftapp.codetest.data.model.domain.Comment
import com.syftapp.codetest.data.model.domain.Post
import com.syftapp.codetest.data.model.domain.User
import io.reactivex.Single

interface BlogDataProvider {

    var fetchedAllPosts: Boolean

    fun getUsers(): Single<List<User>>

    fun getComments(): Single<List<Comment>>

    fun getPosts(page: Int? = null): Single<List<Post>>

    fun deletePost(postId: Int):  Single<List<Post>>
}