package com.syftapp.codetest.data.repository

import com.syftapp.codetest.data.api.BlogApi
import com.syftapp.codetest.data.dao.CommentDao
import com.syftapp.codetest.data.dao.PostDao
import com.syftapp.codetest.data.dao.UserDao
import com.syftapp.codetest.data.model.domain.Comment
import com.syftapp.codetest.data.model.domain.Pageable
import com.syftapp.codetest.data.model.domain.Post
import com.syftapp.codetest.data.model.domain.User
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import org.koin.core.KoinComponent

class BlogRepository(
    private val postDao: PostDao,
    private val commentDao: CommentDao,
    private val userDao: UserDao,
    private val blogApi: BlogApi
) : KoinComponent, BlogDataProvider {


    override var fetchedAllPosts = false //flag if loading all data has completed.

    override fun getUsers(): Single<List<User>> {
        return fetchData(
            local = { userDao.getAll() },
            remote = { blogApi.getUsers() },
            insert = { value -> userDao.insertAll(*value.toTypedArray()) }
        )
    }

    override fun getComments(): Single<List<Comment>> {
        return fetchData(
            local = { commentDao.getAll() },
            remote = { blogApi.getComments() },
            insert = { value -> commentDao.insertAll(*value.toTypedArray()) }
        )
    }

    override fun deletePost(postId: Int): Single<List<Post>> {
        return postDao.getAllNotDeleted(postId)
        //if data would return:
        //- new list of posts, I would use a transaction, and update the db
        //-just a response (Successful/error) would update the db accordingly, and fetch.
        //Now is a temp solution

        //fetchData(
//            local = { postDao.getAllNotDeleted(postId) },
//            remote = { blogApi.deletePost(postId) },
//            insert = { _ -> postDao.deletePost(postId) }
//        )
    }

    override fun getPosts(page: Int?): Single<List<Post>> {
        return fetchPagedData(
            local = { postDao.getAll() },
            remote = { blogApi.getPosts(it) },
            insert = { value, currentPage ->
                value.map { it.page = currentPage }
                postDao.insertAll(*value.toTypedArray())
            }
        )
    }

    fun getPost(postId: Int): Maybe<Post> {
        return postDao.get(postId)
    }

    private fun <T> fetchData(
        local: () -> Single<List<T>>,
        remote: () -> Single<List<T>>,
        insert: (insertValue: List<T>) -> Completable
    ): Single<List<T>> {

        return local()
            .flatMap {
                if (it.isNotEmpty()) {
                    Single.just(it)
                } else {
                    remote()
                        .map { value ->
                            insert.invoke(value).subscribe();
                            value
                        }
                }
            }
    }


    //implement logic for data that can be paged.
    //data is stored in the db, with the page, so have reference in the future
    // what was the last fetched page, and then fetch the following one.
    //This repository has a flag, if reached end of posts list on server (dislike this implementation)
    //This method will be called from an upper layer (presenter/vm),
    // which invokes this by the users
    //action of scrolling to end of list,
    //The repository is also responsible for calculating which page to fetch remotely by fetching the
    //max page from db +1.


    //Issues with pagination, no guarantee that data has not changed.
    //Can call page = 2 get a list of 3 then call again and get either a different list or the data may have been removed
    //Can guarantee data consistancy.
    //In fact this is assuming that backed supports out of index paged (if it doesn't
    // the developers must be fired or taught :) :) )

    private fun <T : Pageable> fetchPagedData(
        local: () -> Single<List<T>>,
        remote: (page: Int) -> Single<List<T>>,
        insert: (insertValue: List<T>, page: Int) -> Completable
    ): Single<List<T>> {
        return local()
            .flatMap { list ->
                val last = list.maxBy { (it as? Pageable)?.page ?: 0 }?.page ?: 0
                if (fetchedAllPosts) {
                    Single.just(list)
                } else {
                    val currentPage = last + 1
                    remote(currentPage)
                        .map { value ->
                            fetchedAllPosts = value.isEmpty()
                            insert(value, currentPage).subscribe()
                            list + value
                        }
                }
            }
    }
}
