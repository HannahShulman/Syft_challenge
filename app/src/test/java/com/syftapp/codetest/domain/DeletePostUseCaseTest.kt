package com.syftapp.codetest.domain

import com.syftapp.codetest.data.model.domain.Post
import com.syftapp.codetest.data.repository.BlogRepository
import com.syftapp.codetest.posts.DeletePostUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class DeletePostUseCaseTest {

    var blogRepository: BlogRepository = mockk(){
       every { deletePost(any()) } returns Single.create{ }
    }

    val useCase: DeletePostUseCase by lazy {
        DeletePostUseCase(blogRepository)
    }

    @Before
    fun setup() = MockKAnnotations.init(this)

    @Test
    fun `GIVEN post to delete WHEN DeletePostUseCase executes THEN repository calls deletePost`(){
        useCase.execute(Post(1,1,"title","body")).test()
        verify { blogRepository.deletePost(1) }
    }
}