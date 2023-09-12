package dev.thunderbolt.dogbreeds.domain.usecase

import app.cash.turbine.test
import dev.thunderbolt.dogbreeds.domain.entity.DogBreed
import dev.thunderbolt.dogbreeds.domain.entity.Response
import dev.thunderbolt.dogbreeds.domain.repository.DogBreedRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetBreedListTest {

    private lateinit var getBreedList: GetBreedList

    @MockK
    lateinit var repository: DogBreedRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getBreedList = GetBreedList(repository)
    }

    @Test
    fun `getBreedList() - success`() = runTest {
        val result = listOf(
            DogBreed("Poodle"),
            DogBreed("Labrador"),
            DogBreed("Pug"),
        )
        every { repository.getBreedList() } returns flow {
            emit(result)
        }

        getBreedList().test {
            assertEquals(Response.Loading<List<DogBreed>>(), awaitItem())
            assertEquals(Response.Success(result), awaitItem())
            awaitComplete()
            ensureAllEventsConsumed()
        }

        verify { repository.getBreedList() }
    }

    @Test
    fun `getBreedList() - error`() = runTest {
        val exception = Exception("Unexpected error!")
        every { repository.getBreedList() } returns flow {
            throw exception
        }

        getBreedList().test {
            assertEquals(Response.Loading<List<DogBreed>>(), awaitItem())
            assertEquals(
                Response.Error<List<DogBreed>>(exception.message.orEmpty()),
                awaitItem()
            )
            awaitComplete()
            ensureAllEventsConsumed()
        }

        verify { repository.getBreedList() }
    }
}
