package com.deanezra.android.mycakes

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.deanezra.android.mycakes.models.Cake
import com.deanezra.android.mycakes.network.NetworkStatus
import com.deanezra.android.mycakes.network.Resource
import com.deanezra.android.mycakes.reposiitories.CakeRepository
import com.deanezra.android.mycakes.services.RetrofitService
import com.deanezra.android.mycakes.viewmodels.MainViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody
import org.junit.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import retrofit2.Response
import java.net.HttpURLConnection

class MainViewModelTest {

    // LiveData makes calls asynchronously on the main loop, but Main Looper is not available in tests,
    // so we add this rule to force androidx to run everything synchronously
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var retrofitService: RetrofitService
    lateinit var cakeRepository : CakeRepository

    lateinit var viewModel: MainViewModel

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun init(){
        Dispatchers.setMain(mainThreadSurrogate)

        retrofitService = mockk<RetrofitService>(relaxed = true)
        cakeRepository = CakeRepository(retrofitService)
        viewModel = MainViewModel(cakeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    // Birthday cake is duplicated to mimic what API will return.
    private fun listOfCakesWithDuplicates(): List<Cake> {

        return listOf(
            Cake(
                "Lemon cheesecake",
                "A cheesecake made of lemon",
                "https://s3-eu-west-1.amazonaws.com/s3.mediafileserver.co.uk/carnation/WebFiles/RecipeImages/lemoncheesecake_lg.jpg"
            ),
            Cake(
                "victoria sponge",
                "sponge with jam",
                "https://upload.wikimedia.org/wikipedia/commons/0/05/111rfyh.jpg"
            ),
            Cake(
                "Birthday cake",
                "a yearly treat",
                "https://www.frenchvillagebakery.co.uk/databaseimages/prd_8594342__painted_pink_and_gold_cake_512x640.jpg"
            ),
            Cake(
                "Carrot cake",
                "Bugs bunnys favourite",
                "https://hips.hearstapps.com/del.h-cdn.co/assets/18/08/1519321610-carrot-cake-vertical.jpg"
            ),
            Cake(
                "Birthday cake",
                "a yearly treat",
                "https://www.frenchvillagebakery.co.uk/databaseimages/prd_8594342__painted_pink_and_gold_cake_512x640.jpg"
            )
        )
    }

    @Test
    fun viewModelReturnsListOfCakesWithoutDuplicatesWhenAPISuccessfulTest() {

        //Given

        // Success route: Make our Api returns a list of fake cakes with duplicates:
        val duplicatedApiCakes = listOfCakesWithDuplicates()
        coEvery { retrofitService.getAllCakes() } returns Response.success(duplicatedApiCakes)

        //When
        viewModel.getAllCakes()

        //Then

        // Verify that view model called the repository to pull from api:
        coVerify { cakeRepository.getAllCakes() }

        // Check the returned cakes are without duplicates and sorted alphabetically (ascending):
        assertEquals(duplicatedApiCakes.distinctBy{ it.title }.sortedBy{ it.title }, viewModel.cakeList.value)
        // Finally check that state shows success:
        assertEquals(NetworkStatus.SUCCESS, viewModel.networkStatus.value)
    }

    @Test
    fun viewModelReturnsErrorWhenCakesApiFailsTest() {

        //Given
        // API returns an error (e.g. 500 internal server error)
        val errorResponseBody = ResponseBody.create(null, "500 Internal server error")

        coEvery { retrofitService.getAllCakes() } returns Response.error(
            HttpURLConnection.HTTP_INTERNAL_ERROR,
            errorResponseBody
        )

        //When
        viewModel.getAllCakes()

        //Then

        // Verify model calls the repository
        coVerify { cakeRepository.getAllCakes() }

        // Verify that models network status is error and error message isnt null
        assertEquals(NetworkStatus.ERROR, viewModel.networkStatus.value)
        assertNotNull(viewModel.errorMessage.value)
    }
}