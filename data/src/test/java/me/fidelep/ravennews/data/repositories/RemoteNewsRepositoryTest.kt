package me.fidelep.ravennews.data.repositories

import kotlinx.coroutines.test.runTest
import me.fidelep.ravennews.data.api.NewsApi
import me.fidelep.ravennews.data.api.models.GetNewsResponse
import me.fidelep.ravennews.data.db.model.toModel
import me.fidelep.ravennews.data.getMockedApiResponse
import me.fidelep.ravennews.data.getMockedStoryEntities
import me.fidelep.ravennews.data.getMockedSuccess
import me.fidelep.ravennews.domain.interfaces.INewsPreferences
import me.fidelep.ravennews.domain.interfaces.INewsRepository
import me.fidelep.ravennews.domain.models.NewsStoryWrapper
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class RemoteNewsRepositoryTest {
    @Mock
    private val api: NewsApi = mock()

    @Mock
    private val preferences: INewsPreferences = mock()

    @Spy
    private lateinit var repository: INewsRepository

    @Before
    fun setup() {
        repository = RemoteNewsRepository(api, preferences)
    }

    @Test
    fun `Api calls getNews from LocalNewsRepository returns NewsStoryWrapper success`() =
        runTest {
            val expected = NewsStoryWrapper.Success(getMockedStoryEntities().map { it.toModel() })
            whenever(api.getNews(Mockito.anyString())).thenReturn(getMockedApiResponse())

            val result = repository.getNews()

            assert(result is NewsStoryWrapper.Success)
            assertEquals(expected, result)
        }

    @Test
    fun `Api returns empty when calls getNews from LocalNewsRepository returns NewsStoryWrapper success with empty list`() =
        runTest {
            val expected = NewsStoryWrapper.Success(listOf())
            whenever(api.getNews(Mockito.anyString())).thenReturn(GetNewsResponse(listOf()))

            val result = repository.getNews()

            assert(result is NewsStoryWrapper.Success)
            assert((result as NewsStoryWrapper.Success).stories.isEmpty())
            assertEquals(expected, result)
        }

    @Test
    fun `Stories are excluded when calls getNews from LocalNewsRepository returns NewsStoryWrapper success without excluded stories`() =
        runTest {
            val fullStories = getMockedSuccess()
            whenever(api.getNews(Mockito.anyString())).thenReturn(getMockedApiResponse())
            whenever(preferences.getDeletedNews()).thenReturn(setOf("1", "2"))

            val result = repository.getNews()

            assert(result is NewsStoryWrapper.Success)
            assertNotEquals(fullStories, result)
            assert((result as NewsStoryWrapper.Success).stories.size < fullStories.stories.size)
            assert(!result.stories.contains(fullStories.stories[0]))
            assert(!result.stories.contains(fullStories.stories[1]))
        }

    @Test
    fun `No stories are excluded when calls getNews from LocalNewsRepository returns NewsStoryWrapper success full stories`() =
        runTest {
            val expected = NewsStoryWrapper.Success(getMockedStoryEntities().map { it.toModel() })
            whenever(api.getNews(Mockito.anyString())).thenReturn(getMockedApiResponse())

            val result = repository.getNews()

            assert(result is NewsStoryWrapper.Success)
            assertEquals(expected, result)
            assertEquals(expected.stories.size, (result as NewsStoryWrapper.Success).stories.size)
        }

    @Test
    fun `Unknown exception requesting Api when calls getNews from LocalNewsRepository returns NewsStoryWrapper GenericError`() =
        runTest {
            whenever(api.getNews(Mockito.anyString())).thenThrow()

            val result = repository.getNews()

            assert(result is NewsStoryWrapper.GenericError)
        }

    @Test
    fun `IOException requesting api when calls getNews from LocalNewsRepository returns NewsStoryWrapper NetworkError`() =
        runTest {
            val messageError = "Error writing/reading from api"
            whenever(api.getNews(Mockito.anyString())).thenAnswer { throw IOException() }

            val result = repository.getNews()

            assert(result is NewsStoryWrapper.NetworkError)
        }

    @Test
    fun `No internet connection when calls getNews from LocalNewsRepository returns NewsStoryWrapper NetworkError`() =
        runTest {
            whenever(api.getNews(Mockito.anyString())).thenAnswer { throw IOException() }

            val result = repository.getNews()

            assert(result is NewsStoryWrapper.NetworkError)
        }

    @Test
    fun `Api HttPException when calls getNews from LocalNewsRepository returns NewsStoryWrapper custom Error`() =
        runTest {
            val body: ResponseBody = mock()
            val httpError = Response.error<GetNewsResponse>(404, body)
            httpError.errorBody()

            whenever(api.getNews(Mockito.anyString())).thenAnswer { throw HttpException(httpError) }

            val result = repository.getNews()

            assert(result is NewsStoryWrapper.Error)
            assertEquals(404, (result as NewsStoryWrapper.Error).code)
        }
}
