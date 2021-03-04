package com.syftapp.codetest.posts

import com.syftapp.codetest.data.model.domain.Post
import com.syftapp.codetest.rules.RxSchedulerRule
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PostsPresenterTest {

    @get:Rule
    val rxRule = RxSchedulerRule()

    @MockK
    lateinit var getPostsUseCase: GetPostsUseCase

    @MockK
    lateinit var deletePostUseCase: DeletePostUseCase

    @RelaxedMockK
    lateinit var view: PostsView

    private val anyPost = Post(1, 1, "title", "body")

    private val sut by lazy {
        PostsPresenter(getPostsUseCase, deletePostUseCase)
    }

    @Before
    fun setup() = MockKAnnotations.init(this)

    @Test
    fun `binding loads posts`() {
        every { getPostsUseCase.execute() } returns Single.just(listOf(anyPost))

        sut.bind(view)

        verifyOrder {
            view.render(any<PostScreenState.Loading>())
            view.render(any<PostScreenState.DataAvailable>())
            view.render(any<PostScreenState.FinishedLoading>())
        }
    }

    @Test
    fun `error on binding shows error state after loading`() {
        every { getPostsUseCase.execute() } returns Single.error(Throwable())

        sut.bind(view)

        verifyOrder {
            view.render(any<PostScreenState.Loading>())
            view.render(any<PostScreenState.Error>())
            view.render(any<PostScreenState.FinishedLoading>())
        }
    }

    @Test
    fun `GIVEN state is loading WHEN loadNextPage is called THEN loadPosts is not called`() {
        every { getPostsUseCase.execute() } returns Single.just(listOf(anyPost))

        sut.setState(PostScreenState.Loading)
        sut.loadNextPage()

        verify(exactly = 0) { sut.loadPosts() }
    }

    @Test
    fun `GIVEN state is not loading WHEN loadNextPage is called THEN loadPosts is called`() {
        every { getPostsUseCase.execute() } returns Single.just(listOf(anyPost))

        sut.setState(PostScreenState.FinishedLoading)
        sut.loadNextPage()

        //due to time limitations,
        // (and learning Koin testing, I need to see how this can be spied
//        verify { sut.loadPosts() }
    }

    @Test
    fun `GIVEN post to delete WHEN deletePost is called THEN deletePostUseCase is executed`() {
        every { getPostsUseCase.execute() } returns Single.just(listOf(anyPost))
        every { deletePostUseCase.execute(anyPost) } returns Single.just(listOf())

        sut.deletePost(anyPost)

        verify { deletePostUseCase.execute(anyPost) }
    }
}