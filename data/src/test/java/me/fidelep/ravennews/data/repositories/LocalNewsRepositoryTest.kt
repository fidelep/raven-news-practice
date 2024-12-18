package me.fidelep.ravennews.data.repositories

import kotlinx.coroutines.test.runTest
import me.fidelep.ravennews.data.db.dao.NewsStoryDao
import me.fidelep.ravennews.data.getMockedStoryEntities
import me.fidelep.ravennews.data.getMockedSuccess
import me.fidelep.ravennews.domain.interfaces.ILocalNewsRepository
import me.fidelep.ravennews.domain.interfaces.INewsPreferences
import me.fidelep.ravennews.domain.models.NewsStoryWrapper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class LocalNewsRepositoryTest {
    @Mock
    private val dao: NewsStoryDao = mock()

    @Mock
    private val preferences: INewsPreferences = mock()

    @Spy
    private lateinit var repository: ILocalNewsRepository

    @Before
    fun setup() {
        repository = LocalNewsRepository(dao, preferences)
    }

    @Test
    fun `Db is empty when calls getNews from LocalNewsRepository returns NewsStoryWrapper success empty list`() =
        runTest {
            val expected = NewsStoryWrapper.Success(listOf())
            whenever(dao.getAll()).thenReturn(listOf())

            val result = repository.getNews()

            assert(result is NewsStoryWrapper.Success)
            assert((result as NewsStoryWrapper.Success).stories.isEmpty())
            assertEquals(expected, result)
        }

    @Test
    fun `Db is populated when calls getNews from LocalNewsRepository returns NewsStoryWrapper success with stories`() =
        runTest {
            val expected = getMockedSuccess()
            whenever(dao.getAll()).thenReturn(getMockedStoryEntities())

            val result = repository.getNews()

            assert(result is NewsStoryWrapper.Success)
            assertEquals(expected, result)
        }

    @Test
    fun `Stories are excluded when calls getNews from LocalNewsRepository returns NewsStoryWrapper success without excluded stories`() =
        runTest {
            val fullStories =
                getMockedSuccess()
            whenever(dao.getAll()).thenReturn(getMockedStoryEntities())
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
            val expected =
                getMockedSuccess()
            whenever(dao.getAll()).thenReturn(getMockedStoryEntities())

            val result = repository.getNews()

            assert(result is NewsStoryWrapper.Success)
            assertEquals(expected, result)
            assertEquals(expected.stories.size, (result as NewsStoryWrapper.Success).stories.size)
        }

    @Test
    fun `Error querying Db when calls getNews from LocalNewsRepository returns NewsStoryWrapper GenericError`() =
        runTest {
            whenever(dao.getAll()).thenThrow()

            val result = repository.getNews()

            assert(result is NewsStoryWrapper.GenericError)
        }

    @Test
    fun `IOException querying Db when calls getNews from LocalNewsRepository returns NewsStoryWrapper Error 0x04 with message`() =
        runTest {
            val messageError = "Error reading from DB"
            whenever(dao.getAll()).thenAnswer { throw IOException() }

            val result = repository.getNews()

            assert(result is NewsStoryWrapper.Error)
            assertEquals((result as NewsStoryWrapper.Error).code, 0x04)
            assertEquals(messageError, result.message)
        }
}
